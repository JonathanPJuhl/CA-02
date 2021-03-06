package dtos;

import entities.*;

import java.util.ArrayList;
import java.util.List;

public class AddressDTO {

    private String street;
    private String additionalInfo;
    private CityInfoDTO cityInfoDto;
    String zip;

    
    public AddressDTO(Address address, String zip) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.zip = zip;
    }

    public AddressDTO(Address address, CityInfoDTO c) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.cityInfoDto = c;
    }


    public AddressDTO(Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
       // this.cityInfoDto = new CityInfoDTO(address.getCityInfo());
    }

    public String getZip() {
        return zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public CityInfoDTO getCityInfoDto() {
        return cityInfoDto;
    }

    public void setCityInfoDto(CityInfoDTO cityInfoDto) {
        this.cityInfoDto = cityInfoDto;
    }

}
