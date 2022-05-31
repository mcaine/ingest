package com.mikeycaine.ingest.terrain50;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Terrain50Controller {
    private final Terrain50GridService gridService;

    @GetMapping("/maxHeights")
    public final List<HeightAtPosition> maxHeights() {
        return gridService.maxHeights();
    }
}
