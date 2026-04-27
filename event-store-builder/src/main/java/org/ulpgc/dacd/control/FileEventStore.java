package org.ulpgc.dacd.control;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileEventStore {

    private static final String BASE_PATH = "eventstore";
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd").withZone(ZoneOffset.UTC);

    public void save(String topic, String json) {
        try {
            JsonObject event = JsonParser.parseString(json).getAsJsonObject();

            long ts = event.get("ts").getAsLong();
            String ss = event.get("ss").getAsString();
            String date = DATE_FORMAT.format(Instant.ofEpochMilli(ts));

            Path dir = Paths.get(BASE_PATH, topic, ss);
            Files.createDirectories(dir);

            Path file = dir.resolve(date + ".events");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
                writer.write(json);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}