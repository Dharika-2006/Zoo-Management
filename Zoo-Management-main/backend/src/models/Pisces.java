package models;

public class Pisces extends Animal {
    public Pisces(String name, int age) {
        super(name, age);
    }

    @Override
    public void medicalCheckup() {
        System.out.println("Performing a checkup for Picses: checking skin, scales, and temperature regulation.");
        getHealthRecord().addRecord("Routine Picses checkup performed.");
    }

    @Override
    public void feedingRoutine() {
        System.out.println("Feeding routine for " + getName() + ": Providing insects and food.");
    }
}