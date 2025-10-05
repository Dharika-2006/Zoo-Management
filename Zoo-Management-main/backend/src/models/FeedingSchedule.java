package models;

public class FeedingSchedule {
    private String time;
    private String food;

    public FeedingSchedule(String time, String food) {
        this.time = time;
        this.food = food;
    }

    public void showSchedule() {
        System.out.println("Time: " + time + ", Food: " + food);
    }
}
