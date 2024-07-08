package it.unisannio.ingsw24.Entities.Owner;

import it.unisannio.ingsw24.Entities.Persona;
import it.unisannio.ingsw24.Entities.user.AppUser;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Owner extends Persona {

    private String id_owner;
    private List<String> parks;

    public Owner(){}

    public Owner(String id, String name, String surname, LocalDate bDate, String mail, String gender, String role, String password, List<String> parks) {
        super(name, surname, bDate, mail, gender, role, password);
        this.parks = parks;
        this.id_owner = id;
    }

    public Owner(String id, String name, String surname, LocalDate bDate, String mail, String gender, String role, String password) {
        super(name, surname, bDate, mail, gender, role, password);
        this.id_owner = id;
    }

    public String getId_owner() {
        return id_owner;
    }

    public void setId_owner(String id_owner) {
        this.id_owner = id_owner;
    }

    public List<String> getParks() {
        return parks;
    }

    public void setParks(ArrayList<String> parks) {
        this.parks = parks;
    }
}