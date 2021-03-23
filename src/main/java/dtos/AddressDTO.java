package dtos;

import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;

import java.util.ArrayList;
import java.util.List;

public class AddressDTO {

    private String street;
    private String additionalInfo;
    private CityInfoDTO cityInfoDto;

    public AddressDTO(String street, String additionalInfo, CityInfoDTO cityInfoDto) {
        this.street = street;
        this.additionalInfo = additionalInfo;
        this.cityInfoDto = cityInfoDto;
    }

    public AddressDTO(entities.Address address) {
        this.street = address.getStreet();
        this.additionalInfo = address.getAdditionalInfo();
        this.cityInfoDto = new CityInfoDTO(address.getCityInfo());
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
