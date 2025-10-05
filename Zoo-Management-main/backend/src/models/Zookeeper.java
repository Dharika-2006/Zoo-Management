package models;

public class Zookeeper extends Staff {
    public Zookeeper(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public String getRole() {
        return "Zookeeper";
    }

    public void manageFeeding(Animal animal) {
        System.out.println("Zookeeper " + getName() + " is managing the feeding for " + animal.getName() + ".");
        animal.feedingRoutine(); // Polymorphism
    }
}