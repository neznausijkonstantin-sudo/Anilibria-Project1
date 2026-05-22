package AnilibriaProject.anilibria.service;

import AnilibriaProject.anilibria.dto.FranchiseDto;

import java.util.List;
import java.util.Optional;

public interface AnilibriaClientService {
    Optional<List<FranchiseDto>> getFranchiseFromApi();
}
