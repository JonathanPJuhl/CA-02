/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dtos.HobbyDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author MariHaugen
 */
@Entity
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy= "person")
    private List<Phone> phones = new ArrayList<>();
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Address address;
    
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy= "persons")
    private List<Hobby> hobbies;

    public Person() {
    }

    public Person(String email, String firstName, String lastName, List<Phone> phones, Address address, List<Hobby> hobbies) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phones = phones;
        this.address = address;
        this.hobbies = hobbies;
    }

    public Person(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addAddress(Address address){
        if (address != null){
           this.setAddress(address);
           //address.addPerson(this);
          }
    }
    
    
    public void addHobby(Hobby hobby){
        if (hobby != null){
            Hobby hobby2 = new Hobby(hobby.getHobbyName(), hobby.getDescription());
            this.hobbies.add(hobby2);
            hobby2.getPersons().add(this);
        }
    }
    
    public void addPhone(Phone phone){
        if (phone != null){
            this.phones.add(phone);
            phone.setPerson(this);
        }
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

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Hobby> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Hobby> hobbies) {
        this.hobbies = hobbies;
    }
    
    
}
