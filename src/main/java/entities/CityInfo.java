
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private int zip;
    private String cityName;
    
    @OneToMany(cascade =CascadeType.PERSIST, mappedBy="cityInfo")
    private List<Address> address;

    public CityInfo() {
    }
   
    
    public int getZip() {
        return zip;
    }

   
    
}
