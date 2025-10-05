package models;

public class Bird extends Animal {
    public Bird(String name, int age) {
        super(name, age);
    }

    @Override
    public void medicalCheckup() {
        System.out.println("Performing a checkup for Bird: checking wings and heart rate.");
        getHealthRecord().addRecord("Routine Bird checkup performed.");
    }

    @Override
    public void feedingRoutine() {
        System.out.println("Feeding routine for " + getName() + ": Providing insects and fruits.");
    }
}