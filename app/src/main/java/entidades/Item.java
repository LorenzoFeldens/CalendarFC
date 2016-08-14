package entidades;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item implements Serializable {
    private int id;
    private ArrayList<Game> games;
    private String name;

    Item() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
