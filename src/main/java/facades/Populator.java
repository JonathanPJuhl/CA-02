/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.*;

import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate(){
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(emf);

        CityInfo ci = new CityInfo(2830, "Virum");
        Address ad = new Address("Street", "Additional", ci);
        List<Phone> phones = new ArrayList<Phone>();
        Phone phone = new Phone(2134566, "home");
        phones.add(phone);
        Hobby hobby = new Hobby("Fodbold", "spark til bolden og fake skader");
        List<Hobby> hobbies = new ArrayList<>();
        hobbies.add(hobby);

        fe.create(new PersonDTO(new Person("mail@mail.dk", "Jens", "Brønd"), ad, phones, hobbies));

        CityInfo ci2 = new CityInfo(2800, "Lyngby");
        Address ad2 = new Address("Street", "Additional", ci2);
        List<Phone> phones2 = new ArrayList<Phone>();
        Phone phone2 = new Phone(2134566232, "home");
        phones2.add(phone2);
        Hobby hobby2 = new Hobby("Håndbold", "spark til bolden og få rødt kort");
        List<Hobby> hobbies2 = new ArrayList<>();
        hobbies2.add(hobby2);

        fe.create(new PersonDTO(new Person("mail@mail.dk", "Jens2", "Brønd2"), ad2, phones2, hobbies2));

        CityInfo ci3 = new CityInfo(2970, "Hørsholm");
        Address ad3= new Address("Street", "Additional", ci3);
        List<Phone> phones3 = new ArrayList<Phone>();
        Phone phone3 = new Phone(2134566232, "home");
        phones3.add(phone3);
        Hobby hobby3 = new Hobby("Amerikansk fodbold", "spark til bolden og få rigtige skader");
        List<Hobby> hobbies3 = new ArrayList<>();
        hobbies3.add(hobby3);

        fe.create(new PersonDTO(new Person("mail@mail.dk", "Jens3", "Brønd3"), ad3, phones3, hobbies3));
       /* fe.create(new RenameMeDTO(new RenameMe("First 1", "Last 1")));
        fe.create(new RenameMeDTO(new RenameMe("First 2", "Last 2")));
        fe.create(new RenameMeDTO(new RenameMe("First 3", "Last 3")));*/
        
    }
    
    public static void main(String[] args) {
        populate();
    }
}
