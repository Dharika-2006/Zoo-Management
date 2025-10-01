package models;

public class Doctor extends Staff {
    public Doctor(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public String getRole() {
        return "Doctor";
    }

    public void performCheckup(Animal animal) {
        System.out.println("Doctor " + getName() + " is performing a checkup on " + animal.getName() + ".");
        animal.medicalCheckup(); // Polymorphism
    }
}