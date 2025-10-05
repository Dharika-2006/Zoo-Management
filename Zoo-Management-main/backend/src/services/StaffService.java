package services;

import models.Doctor;
import models.Staff;
import models.Zookeeper;
import java.util.ArrayList;
import java.util.List;

public class StaffService {
    private List<Staff> allStaff = new ArrayList<>();

    public StaffService() {
            }

    public Staff createStaff(String role, String name, String username, String password) {
        Staff staff = null;
        if ("Doctor".equalsIgnoreCase(role)) {
            staff = new Doctor(name, username, password);
        } else if ("Zookeeper".equalsIgnoreCase(role)) {
            staff = new Zookeeper(name, username, password);
        }

        if (staff != null) {
            allStaff.add(staff);
        }
        return staff;
    }

    public Staff findStaffById(int id) {
        for (Staff staff : allStaff) {
            if (staff.getStaffId() == id) {
                return staff;
            }
        }
        return null;
    }
    
    public boolean removeStaff(int id) {
        Staff staffToRemove = findStaffById(id);
        if (staffToRemove != null) {
            allStaff.remove(staffToRemove);
            return true;
        }
        return false;
    }

    public List<Staff> getAllStaff() {
        return allStaff;
    }
    public Staff validateLogin(String username, String password) {
    for (Staff staffMember : allStaff) {
        if (staffMember.getUsername().equals(username) && staffMember.getPassword().equals(password)) {
            return staffMember; // Return the logged-in staff member object
        }
    }
    return null; // Return null if no match is found
}
}