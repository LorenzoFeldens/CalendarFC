package entidades;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Item implements Serializable {
    private int id;
    private ArrayList<Game> games;
    private String name;

    public Item() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
