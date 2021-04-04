package dtos;


import entities.Address;
import entities.CityInfo;


import java.util.ArrayList;
import java.util.List;

public class CityInfoDTO {

    private String zip;
    private String cityName;
    private List<Address> adresses;

    public CityInfoDTO(CityInfo ci) {
        this.zip = ci.getZip();
        this.cityName = ci.getCityName();
    }
    public CityInfoDTO(String zip, String cityName) {
        this.zip = zip;
        this.cityName = cityName;
    }
/*    public CityInfoDTO(int zip, String cityName) {
        this.zip = zip;
        this.cityName = cityName;
    }*/

    
    
  public static List<AddressDTO> getDtos(List<Address> adresses){
        List<AddressDTO> addressDtos = new ArrayList();
        adresses.forEach(adrs->addressDtos.add(new AddressDTO(adrs)));
        return addressDtos;
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

    public List<Address> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<Address> adresses) {
        this.adresses = adresses;
    }

    
    
}
