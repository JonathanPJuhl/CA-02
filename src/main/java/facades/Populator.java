/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.*;
import entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import utils.EMF_Creator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class Populator {

    public static void populate() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(emf);
        AddressAndCityInfoFacade acF = AddressAndCityInfoFacade.getPersonFacade(emf);
        PhoneFacade pF = PhoneFacade.getPersonFacade(emf);
        HobbyFacade hF = HobbyFacade.getHobbyFacade(emf);

        CityInfoDTO ci = new CityInfoDTO(new CityInfo(7777777, "Virum"));
        CityInfoDTO ci2 = new CityInfoDTO(new CityInfo(8888888, "Lyngby"));
        CityInfoDTO ci3 = new CityInfoDTO(new CityInfo(9999999, "Helvete"));
        AddressDTO ad = new AddressDTO( new Address("Street", "Additional"));
        AddressDTO ad2 = new AddressDTO( new Address("Street2", "In addition"));
        AddressDTO ad3 = new AddressDTO( new Address("Street3", "Also this"));
        ad.setCityInfoDto(ci);
        ad2.setCityInfoDto(ci2);
        ad3.setCityInfoDto(ci3);
        //acF.createCityInfo(ci);

        //AddressDTO address = acF.getAddressFromDB(ad);

        List<PhoneDTO> phones = new ArrayList<>();
        List<PhoneDTO> phones2 = new ArrayList<>();
        List<PhoneDTO> phones3 = new ArrayList<>();
        PhoneDTO phone = new PhoneDTO(new Phone(2134566, "home"));
        PhoneDTO phone2 = new PhoneDTO(new Phone(483927398, "home"));
        PhoneDTO phone3 = new PhoneDTO(new Phone(12928382, "home"));
        //pF.createPhone(phone);
        //PhoneDTO phoneFromDB = pF.getPhoneFromDB(phone);
        phones.add(phone);
        phones2.add(phone2);
        phones3.add(phone3);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Fodbold", "spark til bolden og fake skader","boldspill", "teamsport"));
        HobbyDTO hobby2 = new HobbyDTO(new Hobby("Håndbold", "kast bolden og rigtige skader","boldspill", "teamsport"));
        HobbyDTO hobby3 = new HobbyDTO(new Hobby("Tennis", "sving til bolden og ingen skader","boldspill", "teamsport"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        List<HobbyDTO> hobbies2 = new ArrayList<>();
        List<HobbyDTO> hobbies3 = new ArrayList<>();

        //hF.createHobby(hobby);
        //HobbyDTO hobbyFromDB = hF.getHobbyFromDB(hobby);
        hobbies.add(hobby);
        hobbies2.add(hobby2);
        hobbies3.add(hobby3);
        PersonDTO pDTO = new PersonDTO(new Person("mail@mail.dk", "Jens", "Brønd"), ad, phones, hobbies);
        PersonDTO pDTO2 = new PersonDTO(new Person("nice@mailsnice.org", "Peter", "Ballerupsen"), ad, phones2, hobbies2);
        PersonDTO pDTO3 = new PersonDTO(new Person("icecool@nice.com", "Juno", "Børslev"), ad, phones3, hobbies3);

        pDTO.setAddress(ad);
        pDTO2.setAddress(ad2);
        pDTO3.setAddress(ad3);
        pDTO.setPhones(phones);
        pDTO2.setPhones(phones2);
        pDTO3.setPhones(phones3);
        pDTO.setHobbies(hobbies);
        pDTO2.setHobbies(hobbies2);
        pDTO3.setHobbies(hobbies3);
        fe.create(pDTO);
        fe.create(pDTO2);
        fe.create(pDTO3);

        /*CityInfo ci2 = new CityInfo(2800, "Lyngby");
        Address ad2 = new Address("Street", "Additional", ci2);
        List<Phone> phones2 = new ArrayList<Phone>();
        Phone phone2 = new Phone(2134566232, "home");
        phones2.add(phone2);
        Hobby hobby2 = new Hobby("Håndbold", "spark til bolden og få rødt kort");
        List<Hobby> hobbies2 = new ArrayList<>();
        hobbies2.add(hobby2);

        fe.create(new PersonDTO(new Person("mail@mail.dk", "Jens2", "Brønd2"), ad2, phones2, hobbies2));

        CityInfo ci3 = new CityInfo(2970, "Hørsholm");
        Address ad3 = new Address("Street", "Additional", ci3);
        List<Phone> phones3 = new ArrayList<Phone>();
        Phone phone3 = new Phone(2134566232, "home");
        phones3.add(phone3);
        Hobby hobby3 = new Hobby("Amerikansk fodbold", "spark til bolden og få rigtige skader");
        List<Hobby> hobbies3 = new ArrayList<>();
        hobbies3.add(hobby3);

        fe.create(new PersonDTO(new Person("mail@mail.dk", "Jens3", "Brønd3"), ad3, phones3, hobbies3));*/
        /* fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));*/

    }

    public static void main(String[] args) {
        populate();
    }
}
