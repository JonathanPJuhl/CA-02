
package dtos;

import entities.Address;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;


public class PhoneDTO {
    
    private int phoneNumber;
    private String typeOfNumber;


    public PhoneDTO(Phone phone) {
        this.phoneNumber = phone.getPhoneNumber();
        this.typeOfNumber = phone.getTypeOfNumber();
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

    
    
}
