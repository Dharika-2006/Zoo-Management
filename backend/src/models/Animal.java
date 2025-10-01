package models;
public abstract class Animal implements FeedingRoutine {
    private static int idCounter = 1;
    private int id;
    private String name;
    private int age;
    private String healthStatus;
    private HealthRecord healthRecord; // Composition

    public Animal(String name, int age) {
        this.id = idCounter++;
        this.name = name;
        this.age = age;
        this.healthStatus = "Healthy";
        this.healthRecord = new HealthRecord(this.id); // Composition
    }

    // Abstract method for polymorphism
    public abstract void medicalCheckup();

    // Overloaded search methods will be in AnimalService
    
    public int getId() { return id; }
    public String getName() { return name; }
    public HealthRecord getHealthRecord() { return healthRecord; }
    public void setHealthStatus(String status) { this.healthStatus = status; }
    public int getAge() {
        return age;
    }
    public String getHealthStatus() {
        return healthStatus;
    }
}