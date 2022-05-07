package com.mikeycaine.ingest;

import com.mikeycaine.ingest.terrain50.Terrain50GridRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final Terrain50GridRepository repo;

    @Override
    public void run(String...args) throws Exception {
        log.info("Application started with command-line arguments: {}", Arrays.toString(args));

        log.info("Found " + repo.count() + " terrain tiles");
        if (repo.count() == 2858) { // a complete set
            Stream.of("NN17", "NY20", "SH65").forEach(tileId -> logHighestPointInTile(tileId));
        }
    }

    void logHighestPointInTile(String tileId) {
        repo.findById(tileId).ifPresent(tile -> {
            OptionalDouble maxHeightOpt = tile.heights().mapToDouble(h -> h.height).max();
            maxHeightOpt.ifPresent(maxHeight-> {
                log.info("Max height for tile " + tile.getGridSquare() + ": " + String.format("%.1f", maxHeight) +"m");
            });
        });
    }
}
