package AnilibriaProject.anilibria.service;

import AnilibriaProject.anilibria.dto.FranchiseDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
 Главный сервис для сценария "скачать Excel с франшизами".
 Он связывает два шага:
 1. получить данные из внешнего API;
 2. отфильтровать их и передать в генератор Excel.
 */
@Service
public class AnilinbriaServiceImpl implements AnilibriaService {

    private final AnilibriaClientService anilibriaClientService;
    private final ExcelGenerateService excelGenerateService;

    public AnilinbriaServiceImpl(AnilibriaClientService anilibriaClientService, ExcelGenerateService excelGenerateService) {
        this.anilibriaClientService = anilibriaClientService;
        this.excelGenerateService = excelGenerateService;
    }

    @Override
    public byte[] getFranchise(int year) {
        // Сначала получаем весь список франшиз из внешнего API.
        Optional<List<FranchiseDto>> franchiseFromApi = anilibriaClientService.getFranchiseFromApi();
        if (franchiseFromApi.isEmpty()) {
            throw new IllegalStateException("Failed to load franchises from Anilibria API");
        }
        // После фильтрации по году возвращаем готовый Excel-файл как byte[].
        return excelGenerateService.generateFranchiseExcel(filterFranchiseListByYear(franchiseFromApi.get(), year));
    }

    // Оставляем только те франшизы, у которых first_year равен выбранному году.
    private List<FranchiseDto> filterFranchiseListByYear(List<FranchiseDto> franchiseDtoList, int year) {
        return franchiseDtoList.stream()
                .filter(franchiseDto -> String.valueOf(year).equals(franchiseDto.getFirst_year()))
                .collect(Collectors.toList());
    }
}