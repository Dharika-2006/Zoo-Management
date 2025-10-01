package models;

import java.util.ArrayList;
import java.util.List;

public class Enclosure {
    private static int idCounter = 1;
    private int id;
    private String type;
    
    // Composition: Enclosure owns its list of animals
    private List<Animal> animals; 
    
    // Aggregation: Enclosure references Staff, but doesn't own them
    private List<Staff> assignedStaff;

    public Enclosure(String type) {
        this.id = idCounter++;
        this.type = type;
        this.animals = new ArrayList<>();
        this.assignedStaff = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public List<Animal> getAnimals() { return animals; }
    public List<Staff> getAssignedStaff() { return assignedStaff; }

    public void addAnimal(Animal animal) {
        this.animals.add(animal);
    }
    
    public void removeAnimal(Animal animal) {
        this.animals.remove(animal);
    }

    public void assignStaff(Staff staff) {
        this.assignedStaff.add(staff);
    }

    public void removeStaff(Staff staff) {
        this.assignedStaff.remove(staff);
    }
}