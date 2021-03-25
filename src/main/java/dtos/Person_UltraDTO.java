package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class Person_UltraDTO {

    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private int phoneNumber;
    private String typeOfNumber;
    private String hobbyName;
    private String description;

    public Person_UltraDTO(int id, String email, String firstName, String lastName, int phoneNumber, String typeOfNumber, String hobbyName, String description) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.typeOfNumber = typeOfNumber;
        this.hobbyName = hobbyName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTypeOfNumber() {
        return typeOfNumber;
    }

    public void setTypeOfNumber(String typeOfNumber) {
        this.typeOfNumber = typeOfNumber;
    }

    public String getHobbyName() {
        return hobbyName;
    }

    public void setHobbyName(String hobbyName) {
        this.hobbyName = hobbyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
