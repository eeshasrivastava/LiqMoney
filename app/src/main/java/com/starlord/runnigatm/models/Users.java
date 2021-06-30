package com.starlord.runnigatm.models;

public class Users {
    private String firstName;
    private String lastName;
    private String email;
    private String pin;

    public Users(String firstName, String lastName, String email, String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
    }

    public Users() {

    }

    public String getPin() { return pin; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setPin(String pin) { this.pin = pin; }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
