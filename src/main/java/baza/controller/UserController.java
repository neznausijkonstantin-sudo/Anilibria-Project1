package baza.controller;

import AnilibriaProject.anilibria.service.AnilibriaService;
import baza.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import baza.dto.UserDto;

/*
 * REST-контроллер пользовательской части.
 *
 * Здесь лежат API, доступные обычному пользователю и админу:
 * получение текущего пользователя и скачивание Excel-отчета.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final AnilibriaService anilibriaService;

    public UserController(AnilibriaService anilibriaService) {
        this.anilibriaService = anilibriaService;
    }

    // GET /user/auth - вернуть данные текущего авторизованного пользователя.
    @GetMapping("/auth")
    public ResponseEntity<UserDto> getApiAuthUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }

    /*
     * GET /user/franchises?year=2026
     *
     * Возвращает Excel-файл как массив байтов. Заголовок CONTENT_DISPOSITION
     * говорит браузеру скачать файл, а contentType сообщает, что это .xlsx.
     */
    @GetMapping("/franchises")
    public ResponseEntity<byte[]> getFranchisesByYear(@RequestParam Integer year) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=franchises.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(anilibriaService.getFranchise(year));
    }
}
