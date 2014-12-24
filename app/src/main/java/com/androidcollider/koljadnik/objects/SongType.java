package com.androidcollider.koljadnik.objects;

/**
 * Created by pseverin on 24.12.14.
 */
public class SongType {

    private int id;
    private String name;
    private int quantity;

    public SongType(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
