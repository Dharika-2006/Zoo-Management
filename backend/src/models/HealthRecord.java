package models;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HealthRecord {
    private int animalId;
    private List<String> records;

    public HealthRecord(int animalId) {
        this.animalId = animalId;
        this.records = new ArrayList<>();
        addRecord("Health record created.");
    }

    public void addRecord(String entry) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.records.add("[" + timestamp + "] " + entry);
    }

    public void viewRecords() {
        System.out.println("--- Health Records for Animal ID: " + animalId + " ---");
        for (String record : records) {
            System.out.println(record);
        }
    }
    public List<String> getRecords() {
        return records;
    }
}