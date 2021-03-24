package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class HobbyDTO {

    private String hobbyName;
    private String description;
    

    public HobbyDTO(Hobby hobby) {
        this.hobbyName = hobby.getHobbyName();
        this.description = hobby.getDescription();
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
