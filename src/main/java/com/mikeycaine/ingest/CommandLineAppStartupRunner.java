package com.mikeycaine.ingest;

import com.mikeycaine.ingest.adminboundaries.AdministrativeUnitRepository;
import com.mikeycaine.ingest.hillcsv.HillRepository;
import com.mikeycaine.ingest.terrain50.Terrain50GridRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final Terrain50GridRepository terrain50GridRepository;
    private final AdministrativeUnitRepository administrativeUnitRepository;

    private final HillRepository hillRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Application started with command-line arguments: {}", Arrays.toString(args));

        log.info("Found " + terrain50GridRepository.count() + " terrain tiles");
        if (terrain50GridRepository.count() == 2858) { // a complete set
            Stream.of(
                "NN17",     // Ben Nevis
                "NY20",            // Scafell Pike
                "SH65"             // Yr Wyddfa
            ).forEach(tileId -> logHighestPointInTile(tileId));
        }

        log.info("Found " + administrativeUnitRepository.count() + " Administrative Units (11743?)");

        log.info("Found " + hillRepository.count() + " Hills (21291?)");
    }

    void logHighestPointInTile(String tileId) {
        terrain50GridRepository.findById(tileId).ifPresent(tile ->
            tile.maxHeightOpt().ifPresent(maxHeight -> {
                log.info("Max height for tile " + tile.getGridSquare() + ": " + maxHeight);
            }));
    }
}
