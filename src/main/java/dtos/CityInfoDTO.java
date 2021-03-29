package dtos;


import entities.Address;
import entities.CityInfo;


import java.util.ArrayList;
import java.util.List;

public class CityInfoDTO {

    private int zip;
    private String cityName;
    private List<AddressDTO> addressesDTOs = new ArrayList<>();

    public CityInfoDTO(CityInfo ci) {
        this.zip = ci.getZip();
        this.cityName = ci.getCityName();
    }
    
    
  public List<AddressDTO> getAddressesDTOs(List<Address> adresses){
        adresses.forEach(adrs->this.addressesDTOs.add(new AddressDTO(adrs)));
        return addressesDTOs;
    }
    
    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AddressDTO> getAddressesDTOs() {
        return addressesDTOs;
    }

    public void setAddressesDTOs(List<AddressDTO> addressesDTOs) {
        this.addressesDTOs = addressesDTOs;
    }
    
    
}
