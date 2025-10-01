package models;

import java.util.ArrayList;
import java.util.List;

public class AnimalList<T extends Animal> {
    private List<T> list = new ArrayList<>();

    public void add(T animal) {
        list.add(animal);
    }

    public void showAll() {
        for (T a : list) {
            System.out.println(a.getName());
        }
    }
}
