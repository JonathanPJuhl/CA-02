
package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;


@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String street;
    private String additionalInfo;
    @ManyToOne
    private CityInfo cityInfo;
    
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy= "address")
    private Person person;

    public Address() {
    }
  
    public int getId() {
        return id;
    }

    
    
}
