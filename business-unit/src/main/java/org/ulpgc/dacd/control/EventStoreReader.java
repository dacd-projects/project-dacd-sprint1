package org.ulpgc.dacd.control;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class EventStoreReader {

    private final String eventStorePath;
    private final DatamartUpdater datamartUpdater;

    public EventStoreReader(String eventStorePath, DatamartUpdater datamartUpdater) {
        this.eventStorePath = eventStorePath;
        this.datamartUpdater = datamartUpdater;
    }

    public void loadHistory() {
        try (Stream<Path> files = Files.walk(Paths.get(eventStorePath))) {
            files.filter(p -> p.toString().endsWith(".events"))
                    .sorted()
                    .forEach(this::processFile);
        } catch (IOException e) {
            System.out.println("No se encontró el event store en: " + eventStorePath);
        }
    }

    private void processFile(Path path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    datamartUpdater.process(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo fichero: " + path);
        }
    }
}