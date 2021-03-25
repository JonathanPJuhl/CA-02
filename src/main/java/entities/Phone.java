
package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author MariHaugen
 */
@Entity
@NamedQuery(name = "Phone.deleteAllRows", query = "DELETE FROM Phone")
public class Phone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int phoneNumber;
    private String typeOfNumber;
    
    @ManyToOne
    Person person;

    public Phone() {
    }

    public Phone(int phoneNumber, String typeOfNumber) {
        this.phoneNumber = phoneNumber;
        this.typeOfNumber = typeOfNumber;
    }

    public int getId() {
        return id;
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

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    
    
}
