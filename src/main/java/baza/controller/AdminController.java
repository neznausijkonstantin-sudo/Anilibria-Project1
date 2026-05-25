package baza.controller;

import baza.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import baza.dto.UserDto;
import baza.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/*
 REST-контроллер админской части.
 Он принимает HTTP-запросы с frontend-страницы admin.html/adminScript.js
 и возвращает JSON или HTTP-статус. Доступ к этим URL разрешен только
 пользователям с ролью ADMIN в WebSecurityConfig.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // GET /admin/table - вернуть всех пользователей для таблицы админки.
    @GetMapping("/table")
    public ResponseEntity<List<UserDto>> viewUsers() {
        // Entity User преобразуем в UserDto, чтобы не отдавать пароль на frontend.
        List<UserDto> userList = userService.getAllUsers().stream()
                .map(UserDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    // PATCH /admin/{id}/edit - обновить пользователя с указанным id.
    @PatchMapping("/{id}/edit")
    public ResponseEntity<HttpStatus> newEdit(@PathVariable("id") long id, @RequestBody User user) {
        // id берем из URL, чтобы обновлялся именно тот пользователь, на которого пришел запрос.
        user.setId(id);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // DELETE /admin/{id}/delete - удалить пользователя.
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HttpStatus> newDelete(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // POST /admin/create - создать пользователя из JSON-тела запроса.
    @PostMapping("/create")
    public ResponseEntity<HttpStatus> newNewUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // GET /admin/{id} - получить одного пользователя для edit/delete modal.
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> showUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new UserDto(userService.showUser(id)), HttpStatus.OK);
    }

    // GET /admin/auth - вернуть текущего авторизованного пользователя.
    @GetMapping("/auth")
    public ResponseEntity<UserDto> getApiAuthUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(new UserDto(user), HttpStatus.OK);
    }
}
