import models.*;
import services.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Services to manage logic
    private static final AnimalService animalService = new AnimalService();
    private static final EnclosureService enclosureService = new EnclosureService();
    private static final StaffService staffService = new StaffService();
    
    // User object for login validation
    private static final Admin admin = new Admin("admin", "123");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Welcome to Zoo Management System ===");
        
        try {
            System.out.println("Performing initial animal-to-enclosure assignments...");
            Enclosure savannah = enclosureService.findEnclosureById(1);
            Enclosure reptileHouse = enclosureService.findEnclosureById(2);
            Animal Lion = animalService.search(1);
            Animal Snake = animalService.search(2);
            Animal Zebra = animalService.search(3);
            
            if (savannah != null && Lion != null) savannah.addAnimal(Lion);
            if (reptileHouse != null && Snake != null) reptileHouse.addAnimal(Snake);
            if (savannah != null && Zebra != null) savannah.addAnimal(Zebra);
            
        } catch (AnimalNotFoundException e) {
            System.err.println("Error during initial assignment: " + e.getMessage());
        }
        while (true) {
            System.out.println("\nSelect your role: (1) Admin (2) Doctor (3) Zookeeper (4) Exit");
            int roleChoice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch(roleChoice) {
                case 1:
                    System.out.print("Enter admin username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter admin password: ");
                    String password = sc.nextLine();
                    if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                         System.out.println("Admin login successful!");
                         runAdminMenu(sc);
                    } else {
                        System.out.println("Invalid admin credentials.");
                    }
                    break;
                
                case 2:
                    System.out.print("Enter doctor username: ");
                    String docUsername = sc.nextLine();
                    System.out.print("Enter doctor password: ");
                    String docPassword = sc.nextLine();
                    
                    Staff loggedInStaffDoc = staffService.validateLogin(docUsername, docPassword);
                    
                    if (loggedInStaffDoc != null && loggedInStaffDoc instanceof Doctor) {
                        System.out.println("Doctor login successful!");
                        runDoctorMenu(sc, (Doctor) loggedInStaffDoc);
                    } else {
                        System.out.println("Invalid doctor credentials.");
                    }
                    break;

                case 3:
                    System.out.print("Enter zookeeper username: ");
                    String zooUsername = sc.nextLine();
                    System.out.print("Enter zookeeper password: ");
                    String zooPassword = sc.nextLine();

                    Staff loggedInStaffZoo = staffService.validateLogin(zooUsername, zooPassword);

                    if (loggedInStaffZoo != null && loggedInStaffZoo instanceof Zookeeper) {
                        System.out.println("Zookeeper login successful!");
                        runZookeeperMenu(sc, (Zookeeper) loggedInStaffZoo);
                    } else {
                        System.out.println("Invalid zookeeper credentials.");
                    }
                    break;

                case 4:
                    saveAnimalReport(); // Save the text file report on exit
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void runAdminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Portal ---");
            System.out.println("Select area: (1) Animal Management (2) Staff Management (3) Back to Role Selection");
            int areaChoice = sc.nextInt();
            sc.nextLine();

            if (areaChoice == 1) {
                runAnimalManagementMenu(sc);
            } else if (areaChoice == 2) {
                runStaffManagementMenu(sc);
            } else if (areaChoice == 3) {
                return; 
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void runAnimalManagementMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Animal Management ---");
            System.out.println("1. Add Animal");
            System.out.println("2. Remove Animal");
            System.out.println("3. Assign Animal to Enclosure");
            System.out.println("4. Add Enclosure");
            System.out.println("5. View Animals");
            System.out.println("6. View Enclosures");
            System.out.println("7. Back to Admin Portal");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                 switch (choice) {
                    case 1: // Add animal
                        System.out.print("Enter species name (e.g., Lion): ");
                        String name = sc.nextLine();
                        
                        // ADDED BACK: Prompt for the animal type
                        System.out.print("Enter animal type (Mammal/Reptile): ");
                        String type = sc.nextLine();

                        System.out.print("Enter age: ");
                        int age = sc.nextInt();
                        sc.nextLine();
                        
                        // MODIFIED: Calling createAnimal with three arguments
                        Animal newAnimal = animalService.createAnimal(type, name, age);

                        if (newAnimal != null) {
                            System.out.println("Success! " + name + " (ID: " + newAnimal.getId() + ") was added to the zoo.");
                        } else {
                            System.out.println("Failed to create animal of type " + type);
                        }
                        break;
                    
                    case 2: // Remove animal
    System.out.print("Enter ID of animal to remove: ");
    int idToRemove = sc.nextInt();
    sc.nextLine();
    Animal animalToRemove = animalService.search(idToRemove);
    
    // This loop now works correctly because Enclosure.removeAnimal() is fixed
    for (Enclosure enc : enclosureService.getAllEnclosures()) {
        if (enc.getAnimals().contains(animalToRemove)) {
            enc.removeAnimal(animalToRemove);
            System.out.println(animalToRemove.getName() + " has been removed from enclosure " + enc.getType() + ".");
            break; // Exit the loop once the animal is found and removed
        }
    }
    
    animalService.removeAnimal(animalToRemove);
    System.out.println("Animal " + animalToRemove.getName() + " permanently removed from the zoo.");
    break;

                    case 3: // Assign animal
                        System.out.print("Enter Animal ID to assign: ");
                        int animalId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Enclosure ID: ");
                        int enclosureId = sc.nextInt();
                        sc.nextLine();

                        Animal animalToAssign = animalService.search(animalId);
                        Enclosure enclosure = enclosureService.findEnclosureById(enclosureId);
                        
                        if (animalToAssign != null && enclosure != null) {
                            enclosure.addAnimal(animalToAssign);
                            System.out.println(animalToAssign.getName() + " assigned to Enclosure " + enclosure.getId());
                        } else {
                            System.out.println("Animal or Enclosure not found.");
                        }
                        break;

                    case 4: // Add Enclosure
                        System.out.print("Enter enclosure type (e.g., Savannah, Jungle): ");
                        String encType = sc.nextLine();
                        Enclosure newEnc = enclosureService.createEnclosure(encType);
                        System.out.println("Enclosure " + newEnc.getType() + " (ID: " + newEnc.getId() + ") created.");
                        break;

                    case 5: // View Animals
                        System.out.println("\n--- All Animals in Zoo ---");
                        for (Animal a : animalService.getAllAnimals()) {
                            System.out.println("ID: " + a.getId() + " | Name: " + a.getName() + " | Age: " + a.getAge() + " | Status: " + a.getHealthStatus());
                        }
                        break;
                        
                    case 6: // View Enclosures
                        enclosureService.displayEnclosureStatus();
                        break;

                    case 7:
                        return; // Go back
                    default:
                        System.out.println("Invalid choice.");
                 }
            } catch (AnimalNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private static void runStaffManagementMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Staff Management ---");
            System.out.println("1. Add Staff");
            System.out.println("2. Remove Staff");
            System.out.println("3. Assign Staff to Enclosure");
            System.out.println("4. View All Staff");
            System.out.println("5. Back to Admin Portal");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: // Add Staff
                    System.out.print("Enter role (Doctor/Zookeeper): ");
                    String role = sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    Staff newStaff = staffService.createStaff(role, name, username, password);
                    if (newStaff != null) {
                        System.out.println(role + " " + name + " (ID: " + newStaff.getStaffId() + ") created.");
                    } else {
                        System.out.println("Invalid role specified.");
                    }
                    break;
                case 2: // Remove Staff
                    System.out.print("Enter Staff ID to remove: ");
                    int idToRemove = sc.nextInt();
                    sc.nextLine();
                    Staff staffToRemove = staffService.findStaffById(idToRemove);
                    if (staffToRemove != null) {
                        for (Enclosure enc : enclosureService.getAllEnclosures()) {
                            enc.removeStaff(staffToRemove);
                        }
                        staffService.removeStaff(idToRemove);
                        System.out.println("Staff member " + staffToRemove.getName() + " removed.");
                    } else {
                        System.out.println("Staff member not found.");
                    }
                    break;
                case 3: // Assign Staff to Enclosure
                    System.out.print("Enter Staff ID: ");
                    int staffId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Enclosure ID: ");
                    int enclosureId = sc.nextInt();
                    sc.nextLine();
                    
                    Staff staffToAssign = staffService.findStaffById(staffId);
                    Enclosure enclosureToAssign = enclosureService.findEnclosureById(enclosureId);
                    
                    if (staffToAssign != null && enclosureToAssign != null) {
                        enclosureToAssign.assignStaff(staffToAssign);
                        System.out.println(staffToAssign.getName() + " assigned to Enclosure " + enclosureToAssign.getId());
                    } else {
                        System.out.println("Staff or Enclosure not found.");
                    }
                    break;
                case 4: // View All Staff
                    System.out.println("\n--- All Staff ---");
                    for (Staff s : staffService.getAllStaff()) {
                        System.out.println("ID: " + s.getStaffId() + " | Name: " + s.getName() + " | Role: " + s.getRole());
                    }
                    break;
                case 5:
                    return; // Go back
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void runDoctorMenu(Scanner sc, Doctor doctor) {
        if (doctor == null) { 
            System.out.println("Doctor not found or not logged in."); 
            return; 
        }
        System.out.println("\n--- Doctor Portal (Welcome " + doctor.getName() + ") ---");
        System.out.print("Enter Animal ID to perform checkup: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            Animal animal = animalService.search(id);
            doctor.performCheckup(animal);
            animal.getHealthRecord().viewRecords();
        } catch (AnimalNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void runZookeeperMenu(Scanner sc, Zookeeper zookeeper) {
        if (zookeeper == null) { 
            System.out.println("Zookeeper not found or not logged in."); 
            return; 
        }
        System.out.println("\n--- Zookeeper Portal (Welcome " + zookeeper.getName() + ") ---");
        System.out.print("Enter Animal ID to manage feeding: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            Animal animal = animalService.search(id);
            zookeeper.manageFeeding(animal);
        } catch (AnimalNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void saveAnimalReport() {
        try (PrintWriter writer = new PrintWriter("zoo_report.txt", "UTF-8")) {
            System.out.println("Saving animal report to zoo_report.txt...");
            writer.println("======= ZOO ANIMAL HEALTH REPORT =======");
            writer.println("Generated on: " + java.time.LocalDateTime.now());
            writer.println();
            List<Animal> animals = animalService.getAllAnimals();
            if (animals.isEmpty()) {
                writer.println("No animals in the zoo.");
            } else {
                for (Animal animal : animals) {
                    writer.println("----------------------------------------");
                    writer.println("ANIMAL ID:   " + animal.getId());
                    writer.println("Name:        " + animal.getName());
                    writer.println("Age:         " + animal.getAge());
                    writer.println("Status:      " + animal.getHealthStatus());
                    
                    HealthRecord record = animal.getHealthRecord();
                    if (record != null && record.getRecords() != null) {
                        writer.println("\n  --- Health History ---");
                        for (String entry : record.getRecords()) {
                            writer.println("  " + entry);
                        }
                    }
                    writer.println("----------------------------------------");
                    writer.println();
                }
            }
            System.out.println("Report saved successfully!");
        } catch (IOException e) {
            System.err.println("An error occurred while writing the report file: " + e.getMessage());
        }
    }
}