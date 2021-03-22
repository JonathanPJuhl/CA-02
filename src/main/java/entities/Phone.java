
package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author MariHaugen
 */
@Entity
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
    
    

    public int getId() {
        return id;
    }

    
    
}
