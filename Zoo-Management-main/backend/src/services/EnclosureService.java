package services;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

public class EnclosureService {
    private List<Enclosure> allEnclosures = new ArrayList<>();

     public EnclosureService() {
        System.out.println("Initializing EnclosureService with default enclosures...");
        allEnclosures.add(new Enclosure("Savannah")); // Will get ID 1
        allEnclosures.add(new Enclosure("Reptile House"));      // Will get ID 2
    }

    public Enclosure createEnclosure(String type) {
        Enclosure enclosure = new Enclosure(type);
        allEnclosures.add(enclosure);
        return enclosure;
    }
    
    public Enclosure findEnclosureById(int id) {
        for (Enclosure enc : allEnclosures) {
            if (enc.getId() == id) {
                return enc;
            }
        }
        return null;
    }

    public List<Enclosure> getAllEnclosures() {
        return allEnclosures;
    }
    
     public void displayEnclosureStatus() {
        System.out.println("\n--- Enclosure Status ---");
        if (allEnclosures.isEmpty()) {
            System.out.println("No enclosures in the zoo.");
            return;
        }
        for (Enclosure e : allEnclosures) {
            // Animal counting logic (no change)
            Map<String, Integer> animalCounts = new HashMap<>();
            for (Animal a : e.getAnimals()) {
                animalCounts.put(a.getName(), animalCounts.getOrDefault(a.getName(), 0) + 1);
            }
            StringBuilder animalDetails = new StringBuilder();
            if (animalCounts.isEmpty()) {
                animalDetails.append("Empty");
            } else {
                animalDetails.append(
                    animalCounts.entrySet().stream()
                        .map(entry -> entry.getKey() + " (x" + entry.getValue() + ")")
                        .collect(Collectors.joining(", "))
                );
            }

            // New logic to display staff
            String staffDetails;
            if (e.getAssignedStaff().isEmpty()) {
                staffDetails = "None";
            } else {
                staffDetails = e.getAssignedStaff().stream()
                                  .map(Staff::getName)
                                  .collect(Collectors.joining(", "));
            }

            System.out.println("Enclosure ID " + e.getId() + " [" + e.getType() + "]");
            System.out.println("  -> Animals: " + animalDetails);
            System.out.println("  -> Staff: " + staffDetails);
        }
    }
}
