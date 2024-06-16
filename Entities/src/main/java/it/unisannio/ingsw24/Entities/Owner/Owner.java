package it.unisannio.ingsw24.Entities.Owner;

import it.unisannio.ingsw24.Entities.Persona;

import java.util.ArrayList;
import java.util.Date;

public class Owner extends Persona {

    private String id_owner;
    private ArrayList<String> parks;

    public Owner(String name, String surname, Date bDate, String mail, String gender, ArrayList<String> parks, String role, String password) {
        super(name, surname, bDate, mail, gender, role, password);
        this.parks = new ArrayList<>();
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public ArrayList<String> getParks() {
        return parks;
    }

    public void setParks(ArrayList<String> parks) {
        this.parks = parks;
    }
}

