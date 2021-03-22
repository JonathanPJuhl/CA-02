
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity
public class Hobby implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String hobbyName;
    private String description;
    
    @ManyToMany
    @JoinColumn(name = "hobbies")
    private List<Person> persons;

    public Hobby() {
    }
    
    

    public int getId() {
        return id;
    }

    
}
