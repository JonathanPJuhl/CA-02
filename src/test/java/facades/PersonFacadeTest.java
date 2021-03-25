package facades;

import dtos.AddressDTO;
import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import sun.font.TrueTypeFont;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person person;

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
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();

            EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();

            CityInfo ci = new CityInfo(2830, "Virum");
            Address ad = new Address("Street", "Additional");
            ad.addCityInfo(ci);
            //acF.createCityInfo(ci);

            //AddressDTO address = acF.getAddressFromDB(ad);
            List<Phone> phones = new ArrayList<>();
            Phone phone = new Phone(2134566, "home");
            phones.add(phone);
            //pF.createPhone(phone);
            //PhoneDTO phoneFromDB = pF.getPhoneFromDB(phone);

            Hobby hobby = new Hobby("Fodbold", "spark til bolden og fake skader");
            List<Hobby> hobbies = new ArrayList<>();
            hobbies.add(hobby);

            //hF.createHobby(hobby);
            //HobbyDTO hobbyFromDB = hF.getHobbyFromDB(hobby);
            person = new Person("mail@mail.dk", "Jens", "Brønd", phones, ad, hobbies);
            em.persist(person);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    @Disabled
    public void testCreatePerson() {

        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(emf);

        AddressAndCityInfoFacade acF = AddressAndCityInfoFacade.getPersonFacade(emf);
        PhoneFacade pF = PhoneFacade.getPersonFacade(emf);
        HobbyFacade hF = HobbyFacade.getPersonFacade(emf);

        CityInfoDTO ci = new CityInfoDTO(new CityInfo(2800, "Lyngby"));
        AddressDTO ad = new AddressDTO(new Address("Street2", "Additional more"));
        ad.setCityInfoDto(ci);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Tennis", "smash bold"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("cool@mail.dk", "Peter", "Jensen"), ad, phones, hobbies);

        pDTO.setAddress(ad);
        pDTO.setPhones(phones);
        pDTO.setHobbies(hobbies);
        fe.create(pDTO);

        assertEquals(facade.getCount(), 2);
    }

    @Test
    public void testGetAllPersons() {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(emf);

        AddressAndCityInfoFacade acF = AddressAndCityInfoFacade.getPersonFacade(emf);
        PhoneFacade pF = PhoneFacade.getPersonFacade(emf);
        HobbyFacade hF = HobbyFacade.getPersonFacade(emf);

        CityInfoDTO ci = new CityInfoDTO(new CityInfo(2030, "Holte"));
        AddressDTO ad = new AddressDTO(new Address("Street3", "Additional and more"));
        ad.setCityInfoDto(ci);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also so home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Hockey", "smash more bold"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("icecool@mail.dk", "Hugo", "Jarvier"), ad, phones, hobbies);

        pDTO.setAddress(ad);
        pDTO.setPhones(phones);
        pDTO.setHobbies(hobbies);
        fe.create(pDTO);

        assertEquals(facade.getAll().size(), 2);
    }

    @Test
    public void testGetAllByHobby() {

        EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = PersonFacade.getPersonFacade(EMF);
        List<PersonDTO> lisDto = fe.getAllPersonsByGivenHobby("Tennis");
        assertEquals(lisDto.get(0).getFirstName(), "Jens");

    }

}
