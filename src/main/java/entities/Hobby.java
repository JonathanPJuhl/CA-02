
package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

    public Hobby(String hobbyName, String description) {
        this.hobbyName = hobbyName;
        this.description = description;
    }

    public void addPerson(Person person){
        if (person != null){
            this.persons.add(person);
            //person.getHobbies().add(this);
        }
    }
    
    public int getId() {
        return id;
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hobby)) return false;
        Hobby hobby = (Hobby) o;
        return getId() == hobby.getId() && getHobbyName().equals(hobby.getHobbyName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHobbyName());
    }
}
