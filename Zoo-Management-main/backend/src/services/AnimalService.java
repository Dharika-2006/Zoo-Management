package services;

import models.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalService {
    private List<Animal> allAnimals = new ArrayList<>();

    public AnimalService() {
        System.out.println("Initializing AnimalService with default animals...");
        allAnimals.add(new Mammal("Lion", 5));  
        allAnimals.add(new Reptile("Snake", 3));
        allAnimals.add(new Mammal("Zebra", 4));   
    }
    // Overloading: Search by ID
    public Animal search(int id) throws AnimalNotFoundException {
        for (Animal animal : allAnimals) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        throw new AnimalNotFoundException("Animal with ID " + id + " not found.");
    }

    // Overloading: Search by name (returns the first match)
    public Animal search(String name) throws AnimalNotFoundException {
        for (Animal animal : allAnimals) {
            if (animal.getName().equalsIgnoreCase(name)) {
                return animal;
            }
        }
        throw new AnimalNotFoundException("Animal with name '" + name + "' not found.");
    }
    
    public Animal createAnimal(String type, String name, int age) {
        Animal animal = null;
        if ("Mammal".equalsIgnoreCase(type)) {
            animal = new Mammal(name, age);
        } else if ("Reptile".equalsIgnoreCase(type)) {
            animal = new Reptile(name, age);
        }
        
        if (animal != null) {
            allAnimals.add(animal);
        }
        return animal;
    }

    public List<Animal> getAllAnimals() {
        return allAnimals;
    }

    public void removeAnimal(Animal animal) {
        allAnimals.remove(animal);
    }
}