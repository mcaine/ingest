package com.mikeycaine.ingest.terrain50;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Terrain50GridService {

    private final Terrain50GridRepository repo;

    public List<String> gridSquareNames() {
        return repo.findAll().stream()
            .map(grid -> grid.getGridSquare())
            .collect(Collectors.toList());
    }

    public List<HeightAtPosition> maxHeights() {
        return repo.findAll().stream()
            .map(grid -> grid.maxHeightOpt().orElse(null))
            .sorted()
            .collect(Collectors.toList());
    }
}
