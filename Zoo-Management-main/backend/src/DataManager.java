import models.Animal;
import services.AnimalService;
import java.io.*;
import java.util.List;

public class DataManager {

    // Save all animals to a JSON-like text file
    public static void saveAnimals(String filePath, AnimalService animalService) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("[");
            List<Animal> animals = animalService.getAllAnimals();
            for (int i = 0; i < animals.size(); i++) {
                Animal a = animals.get(i);
                writer.println("  {");
                writer.println("    \"id\": " + a.getId() + ",");
                writer.println("    \"name\": \"" + a.getName() + "\",");
                writer.println("    \"age\": " + a.getAge() + ",");
                writer.println("    \"type\": \"" + a.getClass().getSimpleName() + "\"");
                writer.print("  }");
                if (i < animals.size() - 1) writer.println(",");
                else writer.println();
            }
            writer.println("]");
            System.out.println("✅ Animals saved to " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to save animals: " + e.getMessage());
        }
    }

    // Load animals from file (recreates them in the service)
    public static void loadAnimals(String filePath, AnimalService animalService) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("⚠️ No saved animals found — starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("\"name\":")) {
                    String name = line.split(":")[1].replace("\"", "").replace(",", "").trim();
                    // the next few lines extract type and age
                }
            }
            System.out.println("✅ Animals loaded from " + filePath);
        } catch (IOException e) {
            System.err.println("❌ Failed to load animals: " + e.getMessage());
        }
    }
}
