package models;

public abstract class Staff extends User {
    private static int idCounter = 101;
    private int staffId;
    private String name;

    public Staff(String name, String username, String password) {
        super(username, password);
        this.staffId = idCounter++;
        this.name = name;
    }

    public int getStaffId() {
        return staffId;
    }

    public String getName() {
        return name;
    }
}