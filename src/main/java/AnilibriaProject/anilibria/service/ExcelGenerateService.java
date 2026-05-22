package AnilibriaProject.anilibria.service;


import AnilibriaProject.anilibria.dto.FranchiseDto;

import java.util.List;

public interface ExcelGenerateService {

    byte[] generateFranchiseExcel(List<FranchiseDto> franchiseDtoList);
}
