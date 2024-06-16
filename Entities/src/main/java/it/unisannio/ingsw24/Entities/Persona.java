package it.unisannio.ingsw24.Entities;

import java.util.Date;
import java.util.Objects;

public abstract class Persona {

    private String name;
    private String surname;
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date bDate;
    private String email;
    private String gender;
    private String role;
    private String password;

    public Persona(){}

    public Persona(String name, String surname, Date bDate, String mail, String gender, String role, String password) {
        this.name = name;
        this.surname = surname;
        this.bDate = bDate;
        this.email = mail;
        this.gender = gender;
        this.role = role;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getbDate() {
        return bDate;
    }

    public void setbDate(Date bDate) {
        this.bDate = bDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(name, persona.name) && Objects.equals(surname, persona.surname) && Objects.equals(bDate, persona.bDate) && Objects.equals(email, persona.email) && Objects.equals(gender, persona.gender) && Objects.equals(role, persona.role) && Objects.equals(password, persona.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, bDate, email, gender, role, password);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", bDate=" + bDate +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

