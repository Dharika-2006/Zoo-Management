package models;

public class Reptile extends Animal {
    public Reptile(String name, int age) {
        super(name, age);
    }

    @Override
    public void medicalCheckup() {
        System.out.println("Performing a checkup for Reptile: checking skin, scales, and temperature regulation.");
        getHealthRecord().addRecord("Routine reptile checkup performed.");
    }

    @Override
    public void feedingRoutine() {
        System.out.println("Feeding routine for " + getName() + ": Providing insects and vegetables.");
    }
}