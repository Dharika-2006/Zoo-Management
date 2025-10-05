package models;
public class Amphibian extends Animal {
    public Amphibian(String name, int age) {
        super(name, age);
    }

    @Override
    public void medicalCheckup() {
        System.out.println("Performing a checkup for Amphibian: checking skin, scales, and temperature regulation.");
        getHealthRecord().addRecord("Routine Amphibian checkup performed.");
    }

    @Override
    public void feedingRoutine() {
        System.out.println("Feeding routine for " + getName() + ": Providing insects.");
    }
}