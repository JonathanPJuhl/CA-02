package facades;

import dtos.PersonDTO;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Address;
import entities.Phone;
import java.util.ArrayList;
import java.util.List;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
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

            em.persist(new PersonDTO(new Person("mail@mail.dk", "Jens", "Brønd"), ad, phones, hobbies));

            CityInfo ci2 = new CityInfo(2800, "Lyngby");
            Address ad2 = new Address("Street", "Additional", ci2);
            List<Phone> phones2 = new ArrayList<Phone>();
            Phone phone2 = new Phone(2134566232, "home");
            phones2.add(phone2);
            Hobby hobby2 = new Hobby("Håndbold", "spark til bolden og få rødt kort");
            List<Hobby> hobbies2 = new ArrayList<>();
            hobbies2.add(hobby2);

            em.persist(new PersonDTO(new Person("mail@mail.dk", "Jens2", "Brønd2"), ad2, phones2, hobbies2));

            CityInfo ci3 = new CityInfo(2970, "Hørsholm");
            Address ad3 = new Address("Street", "Additional", ci3);
            List<Phone> phones3 = new ArrayList<Phone>();
            Phone phone3 = new Phone(2134566232, "home");
            phones3.add(phone3);
            Hobby hobby3 = new Hobby("Amerikansk fodbold", "spark til bolden og få rigtige skader");
            List<Hobby> hobbies3 = new ArrayList<>();
            hobbies3.add(hobby3);

            em.persist(new PersonDTO(new Person("mail@mail.dk", "Jens3", "Brønd3"), ad3, phones3, hobbies3));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    
  
    
    // TODO: Delete or change this method 


}
