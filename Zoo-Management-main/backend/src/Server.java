import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

import models.*;
import services.*;

public class Server {
    public static void main(String[] args) throws IOException {
        // ✅ Initialize backend data
        Main.initializeData();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // ========== GET /animals ==========
        server.createContext("/animals", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Animal> animals = Main.getAnimalService().getAllAnimals();
                StringBuilder response = new StringBuilder();
                for (Animal a : animals) {
                    response.append(a.getId())
                            .append(" - ")
                            .append(a.getName())
                            .append(" (")
                            .append(a.getAge())
                            .append(")\n");
                }
                sendResponse(exchange, 200, response.toString());
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== GET /enclosures ==========
        server.createContext("/enclosures", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                StringBuilder response = new StringBuilder();
                for (Enclosure e : Main.getEnclosureService().getAllEnclosures()) {
                    response.append("ID: ").append(e.getId())
                            .append(" | Type: ").append(e.getType()).append("\n");

                    // Animals inside this enclosure
                    response.append("Animals: ");
                    if (e.getAnimals().isEmpty()) {
                        response.append("None\n");
                    } else {
                        for (Animal a : e.getAnimals()) {
                            response.append(a.getName())
                                    .append(" (ID: ").append(a.getId()).append("), ");
                        }
                        response.setLength(response.length() - 2); // remove last comma
                        response.append("\n");
                    }

                    // Staff assigned
                    response.append("Staff: ");
                    if (e.getAssignedStaff().isEmpty()) {
                        response.append("None\n");
                    } else {
                        for (Staff s : e.getAssignedStaff()) {
                            response.append(s.getName())
                                    .append(" (").append(s.getRole())
                                    .append(" ID: ").append(s.getStaffId()).append("), ");
                        }
                        response.setLength(response.length() - 2);
                        response.append("\n");
                    }

                    response.append("\n");
                }

                sendResponse(exchange, 200, response.toString());
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /addAnimal ==========
        server.createContext("/addAnimal", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    String[] parts = body.split(",");
                    if (parts.length < 3) {
                        sendResponse(exchange, 400, "Invalid format. Expected: type,name,age");
                        return;
                    }
                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());

                    Animal newAnimal = Main.getAnimalService().createAnimal(type, name, age);
                    sendResponse(exchange, 200, "✅ Added " + newAnimal.getName() + " successfully!");
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Server error: " + e.getMessage());
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /removeAnimal ==========
        server.createContext("/removeAnimal", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    int id = Integer.parseInt(body);
                    Animal target = Main.getAnimalService().search(id);
                    Main.getAnimalService().removeAnimal(target);
                    sendResponse(exchange, 200, "🗑️ Removed animal ID " + id);
                } catch (AnimalNotFoundException e) {
                    sendResponse(exchange, 404, "❌ Animal not found");
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Server error: " + e.getMessage());
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /addEnclosure ==========
        server.createContext("/addEnclosure", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    Enclosure e = Main.getEnclosureService().createEnclosure(body);
                    sendResponse(exchange, 200, "🏗️ Enclosure '" + e.getType() + "' added!");
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Error adding enclosure");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /assignAnimal ==========
        server.createContext("/assignAnimal", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    String[] parts = body.split(",");
                    int animalId = Integer.parseInt(parts[0]);
                    int enclosureId = Integer.parseInt(parts[1]);

                    Animal a = Main.getAnimalService().search(animalId);
                    Enclosure e = Main.getEnclosureService().findEnclosureById(enclosureId);
                    if (a != null && e != null) {
                        e.addAnimal(a);
                        sendResponse(exchange, 200, "🐾 Assigned " + a.getName() + " to enclosure " + e.getType());
                    } else {
                        sendResponse(exchange, 404, "Animal or enclosure not found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Error assigning animal");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /doctorCheckup ==========
        server.createContext("/doctorCheckup", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    int animalId = Integer.parseInt(body);
                    Animal a = Main.getAnimalService().search(animalId);
                    if (a != null) {
                        a.getHealthRecord().addRecord("Doctor checkup completed at " + java.time.LocalDateTime.now());
                        sendResponse(exchange, 200, "🩺 Checkup done for " + a.getName());
                    } else {
                        sendResponse(exchange, 404, "Animal not found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Error performing checkup");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

        // ========== POST /feedAnimal ==========
        server.createContext("/feedAnimal", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    int animalId = Integer.parseInt(body);
                    Animal a = Main.getAnimalService().search(animalId);
                    if (a != null) {
                        sendResponse(exchange, 200, "🍽️ " + a.getName() + " has been fed!");
                    } else {
                        sendResponse(exchange, 404, "Animal not found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendResponse(exchange, 500, "Error feeding animal");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });
        // ========== GET /staff ==========
        server.createContext("/staff", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                List<Staff> staffList = Main.getStaffService().getAllStaff();
                StringBuilder res = new StringBuilder();
                for (Staff s : staffList) {
                    res.append("ID: ").append(s.getStaffId())
                            .append(" | Name: ").append(s.getName())
                            .append(" | Role: ").append(s.getRole()).append("\n");
                }
                sendResponse(exchange, 200, res.toString());
            } else sendResponse(exchange, 405, "Method not allowed");
        });

// ========== POST /addStaff ==========
        server.createContext("/addStaff", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    String[] parts = body.split(",");
                    if (parts.length < 4) {
                        sendResponse(exchange, 400, "Invalid format. Expected: role,name,username,password");
                        return;
                    }

                    String role = parts[0].trim();
                    String name = parts[1].trim();
                    String username = parts[2].trim();
                    String password = parts[3].trim();

                    Staff newStaff = Main.getStaffService().createStaff(role, name, username, password);
                    if (newStaff != null)
                        sendResponse(exchange, 200, "✅ Added " + name + " as " + role);
                    else
                        sendResponse(exchange, 400, "❌ Invalid role");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

// ========== POST /removeStaff ==========
        server.createContext("/removeStaff", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    int staffId = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                    Staff staff = Main.getStaffService().findStaffById(staffId);
                    if (staff != null) {
                        Main.getStaffService().removeStaff(staffId);
                        sendResponse(exchange, 200, "🗑️ Removed staff ID " + staffId);
                    } else sendResponse(exchange, 404, "❌ Staff not found");
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });

// ========== POST /assignStaff ==========
        server.createContext("/assignStaff", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                try (InputStream is = exchange.getRequestBody()) {
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                    String[] parts = body.split(",");
                    if (parts.length < 2) {
                        sendResponse(exchange, 400, "Expected format: staffId,enclosureId");
                        return;
                    }

                    int staffId = Integer.parseInt(parts[0].trim());
                    int enclosureId = Integer.parseInt(parts[1].trim());

                    Staff s = Main.getStaffService().findStaffById(staffId);
                    Enclosure e = Main.getEnclosureService().findEnclosureById(enclosureId);

                    if (s != null && e != null) {
                        e.assignStaff(s);
                        sendResponse(exchange, 200, "👷 Assigned " + s.getName() + " to " + e.getType());
                    } else {
                        sendResponse(exchange, 404, "Staff or Enclosure not found");
                    }
                }
            } else sendResponse(exchange, 405, "Method not allowed");
        });


        System.out.println("✅ Zoo API running at http://localhost:8080");
        server.start();
    }

    private static void sendResponse(HttpExchange exchange, int code, String message) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
        exchange.close();
    }
}
