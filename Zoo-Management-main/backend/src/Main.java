import models.*;
import services.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {
    // === Services (made public for Server.java access) ===
    public static final AnimalService animalService = new AnimalService();
    public static final EnclosureService enclosureService = new EnclosureService();
    public static final StaffService staffService = new StaffService();

    // === Getter Methods for Server.java ===
    public static AnimalService getAnimalService() {
        return animalService;
    }
    public static EnclosureService getEnclosureService() {
        return enclosureService;
    }
    public static StaffService getStaffService() {
        return staffService;
    }

    // === User object for admin login ===
    private static final Admin admin = new Admin("admin", "123");

    // === Data initialization ===
    public static void initializeData() {
        try {
            // === Default Enclosures ===
            if (enclosureService.getAllEnclosures().isEmpty()) {
                enclosureService.createEnclosure("Savannah");
                enclosureService.createEnclosure("Reptile House");
            }

            // === Default Animals ===
            if (animalService.getAllAnimals().isEmpty()) {
                animalService.createAnimal("Mammal", "Lion", 5);
                animalService.createAnimal("Reptile", "Snake", 2);
                animalService.createAnimal("Mammal", "Zebra", 4);
            }

            // === Default Staff ===
            if (staffService.getAllStaff().isEmpty()) {
                Staff doctor = staffService.createStaff("Doctor", "Ram", "ram", "123");
                Staff zookeeper = staffService.createStaff("Zookeeper", "Ashwath", "ashwath", "123");

                // Assign Ashwath (Zookeeper) to the Savannah enclosure
                Enclosure savannah = enclosureService.findEnclosureById(1);
                if (savannah != null && zookeeper != null) {
                    savannah.assignStaff(zookeeper);
                }
            }

            // === Assign animals to enclosures ===
            Enclosure savannah = enclosureService.findEnclosureById(1);
            Enclosure reptileHouse = enclosureService.findEnclosureById(2);
            Animal lion = animalService.search(1);
            Animal snake = animalService.search(2);
            Animal zebra = animalService.search(3);

            if (savannah != null && lion != null) savannah.addAnimal(lion);
            if (reptileHouse != null && snake != null) reptileHouse.addAnimal(snake);
            if (savannah != null && zebra != null) savannah.addAnimal(zebra);

        } catch (AnimalNotFoundException e) {
            System.err.println("Error during initial assignment: " + e.getMessage());
        }
    }


    // === Main ===
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Welcome to Zoo Management System ===");
        DataManager.loadAnimals("animals.json", animalService);
        initializeData();

        while (true) {
            System.out.println("\nSelect your role: (1) Admin (2) Doctor (3) Zookeeper (4) Exit");
            int roleChoice = sc.nextInt();
            sc.nextLine();

            switch (roleChoice) {
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
                    if (loggedInStaffDoc instanceof Doctor) {
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
                    if (loggedInStaffZoo instanceof Zookeeper) {
                        System.out.println("Zookeeper login successful!");
                        runZookeeperMenu(sc, (Zookeeper) loggedInStaffZoo);
                    } else {
                        System.out.println("Invalid zookeeper credentials.");
                    }
                    break;

                case 4:
                    saveAnimalReport();
                    DataManager.saveAnimals("animals.json", animalService);
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // === Admin Menu ===
    private static void runAdminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Portal ---");
            System.out.println("Select area: (1) Animal Management (2) Staff Management (3) Back to Role Selection");
            int areaChoice = sc.nextInt();
            sc.nextLine();

            if (areaChoice == 1) runAnimalManagementMenu(sc);
            else if (areaChoice == 2) runStaffManagementMenu(sc);
            else if (areaChoice == 3) return;
            else System.out.println("Invalid choice.");
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
                    case 1:
                        System.out.print("Enter species name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter type (Mammal/Reptile): ");
                        String type = sc.nextLine();
                        System.out.print("Enter age: ");
                        int age = sc.nextInt();
                        sc.nextLine();
                        Animal newAnimal = animalService.createAnimal(type, name, age);
                        System.out.println("✅ " + name + " added (ID: " + newAnimal.getId() + ")");
                        break;

                    case 2:
                        System.out.print("Enter animal ID to remove: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        Animal a = animalService.search(id);
                        animalService.removeAnimal(a);
                        System.out.println("❌ " + a.getName() + " removed.");
                        break;

                    case 3:
                        System.out.print("Enter Animal ID: ");
                        int animalId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Enclosure ID: ");
                        int encId = sc.nextInt();
                        sc.nextLine();
                        Enclosure enc = enclosureService.findEnclosureById(encId);
                        Animal an = animalService.search(animalId);
                        if (an != null && enc != null) enc.addAnimal(an);
                        System.out.println("✅ Animal assigned.");
                        break;

                    case 4:
                        System.out.print("Enter enclosure type: ");
                        String encType = sc.nextLine();
                        Enclosure newEnc = enclosureService.createEnclosure(encType);
                        System.out.println("✅ Enclosure created: " + newEnc.getType());
                        break;

                    case 5:
                        System.out.println("\n--- All Animals ---");
                        for (Animal ani : animalService.getAllAnimals()) {
                            System.out.println(ani.getId() + " - " + ani.getName() + " (" + ani.getAge() + ")");
                        }
                        break;

                    case 6:
                        enclosureService.displayEnclosureStatus();
                        break;

                    case 7:
                        return;
                }
            } catch (Exception e) {
                System.out.println("⚠️ " + e.getMessage());
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
            System.out.println("5. Back");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter role (Doctor/Zookeeper): ");
                    String role = sc.nextLine();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter username: ");
                    String uname = sc.nextLine();
                    System.out.print("Enter password: ");
                    String pass = sc.nextLine();
                    Staff s = staffService.createStaff(role, name, uname, pass);
                    System.out.println("✅ Added " + s.getName() + " (" + s.getRole() + ")");
                    break;

                case 2:
                    System.out.print("Enter Staff ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    staffService.removeStaff(id);
                    System.out.println("❌ Staff removed.");
                    break;

                case 3:
                    System.out.print("Enter Staff ID: ");
                    int sid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Enclosure ID: ");
                    int eid = sc.nextInt();
                    sc.nextLine();
                    Enclosure e = enclosureService.findEnclosureById(eid);
                    Staff st = staffService.findStaffById(sid);
                    if (e != null && st != null) e.assignStaff(st);
                    System.out.println("✅ Staff assigned.");
                    break;

                case 4:
                    for (Staff staff : staffService.getAllStaff())
                        System.out.println(staff.getStaffId() + " - " + staff.getName() + " (" + staff.getRole() + ")");
                    break;

                case 5:
                    return;
            }
        }
    }

    private static void runDoctorMenu(Scanner sc, Doctor doctor) {
        System.out.print("Enter Animal ID to perform checkup: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            Animal a = animalService.search(id);
            doctor.performCheckup(a);
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void runZookeeperMenu(Scanner sc, Zookeeper zoo) {
        System.out.print("Enter Animal ID to feed: ");
        int id = sc.nextInt();
        sc.nextLine();
        try {
            Animal a = animalService.search(id);
            zoo.manageFeeding(a);
        } catch (Exception e) {
            System.out.println("⚠️ " + e.getMessage());
        }
    }

    private static void saveAnimalReport() {
        try (PrintWriter writer = new PrintWriter("zoo_report.txt", "UTF-8")) {
            System.out.println("Saving animal report...");
            writer.println("=== ZOO ANIMAL HEALTH REPORT ===");
            for (Animal a : animalService.getAllAnimals()) {
                writer.println("ID: " + a.getId() + " | " + a.getName() + " | " + a.getHealthStatus());
            }
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
}
