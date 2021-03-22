/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
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
    private List<Phone> phones;
    
    @ManyToOne
    private Address address;
    
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy= "persons")
    private List<Hobby> hobbies;

    public Person() {
    }
    
    

    public int getId() {
        return id;
    }

    
    
}
