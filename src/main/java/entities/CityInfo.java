
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "CityInfo.deleteAllRows", query = "DELETE FROM CityInfo")
public class CityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String zip;
    private String cityName;
    
    @OneToMany(cascade =CascadeType.PERSIST, mappedBy="cityInfo")
    private List<Address> address;

    public CityInfo() {
    }

    public CityInfo(String zip, String cityName) {
        this.zip = zip;
        this.cityName = cityName;

    }

    public void addAddress(Address address){
        if (address != null){
            this.address.add(address);
            address.setCityInfo(this);
        }
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCityName() {
        return cityName;
    }



    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

      public List<Address> getAddress() {
        return address;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
    
}
