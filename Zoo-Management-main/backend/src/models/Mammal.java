package models;

public class Mammal extends Animal {
    public Mammal(String name, int age) {
        super(name, age);
    }

    @Override
    public void medicalCheckup() {
        System.out.println("Performing a checkup for Mammal: checking fur and body temperature.");
        getHealthRecord().addRecord("Routine mammal checkup performed.");
    }

    @Override
    public void feedingRoutine() {
        System.out.println("Feeding routine for " + getName() + ": Providing grains and fresh greens.");
    }
}