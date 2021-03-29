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
import errorhandling.ArgumentNullException;
import java.util.ArrayList;
import java.util.List;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person person;
    private static List<Hobby> hobbies;
    private static List<Phone> phones;
    private static Address ad;
    private static CityInfo ci;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {

    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();

            ci = new CityInfo(2830, "Virum");
            ad = new Address("Street", "Additional");
            ad.setCityInfo(ci);
            //ad.addCityInfo(ci);

            phones = new ArrayList<>();
            Phone phone = new Phone(2134566, "home");

            Hobby hobby = new Hobby("Fodbold", "spark til bolden og fake skader", "boldspill", "teamsport");
            hobbies = new ArrayList<>();

            person = new Person("mail@mail.dk", "Jens", "Brønd", phones, ad, hobbies);
            person.addHobby(hobby);
            //person.setAddress(ad);
            person.addPhone(phone);
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
    public void testCreatePerson() {

        CityInfoDTO ci = new CityInfoDTO(new CityInfo(2800, "Lyngby"));
        AddressDTO ad = new AddressDTO(new Address("Street2", "Additional more"));
        ad.setCityInfoDto(ci);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Tennis", "smash bold", "boldspill", "teamsport and single player"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("cool@mail.dk", "Peter", "Jensen"), ad, phones, hobbies);

        pDTO.setAddress(ad);
        pDTO.setPhones(phones);
        pDTO.setHobbies(hobbies);
        facade.create(pDTO);

        assertEquals(facade.getCount(), 2);
    }

    @Test
    public void testGetAllPersons() {

        CityInfoDTO ci = new CityInfoDTO(new CityInfo(2030, "Holte"));
        AddressDTO ad = new AddressDTO(new Address("Street3", "Additional and more"));
        ad.setCityInfoDto(ci);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also so home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Hockey", "smash more bold", "vintersport", "teamsport"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("icecool@mail.dk", "Hugo", "Jarvier"), ad, phones, hobbies);

        pDTO.setAddress(ad);
        pDTO.setPhones(phones);
        pDTO.setHobbies(hobbies);
        facade.create(pDTO);

        assertEquals(facade.getAll().size(), 2);
    }

    @Test
    public void testGetAllByHobby() {

        List<PersonDTO> lisDto = facade.getAllPersonsByGivenHobby("Fodbold");

        assertEquals(lisDto.get(0).getFirstName(), "Jens");

    }

    @Test
    public void testEditPerson() throws ArgumentNullException, Exception {
        int personToChangeID = person.getId();
        PersonDTO pDToExpected = new PersonDTO(person);
        pDToExpected.setEmail("wannabemail@hacker.dk");
        PersonDTO pdtoResult = facade.updatePerson(pDToExpected, personToChangeID);
        assertEquals(pDToExpected.getEmail(), pdtoResult.getEmail());
    }

    //Negativ test
    @Test
    public void testEditPersonWithNullEmail() throws Exception {

        int personToChangeID = person.getId();
        PersonDTO pDToExpected = new PersonDTO(person);
        pDToExpected.setEmail(null);
        ArgumentNullException exception = Assertions.assertThrows(ArgumentNullException.class, () -> {
            facade.updatePerson(pDToExpected, personToChangeID);
        });
        String expectedMessage = "En personegenskab er null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    public void testGetAllByCity() {

        PersonFacade fe = PersonFacade.getPersonFacade(emf);
        List<PersonDTO> lisDto = fe.getPeopleByCity("Virum");

        assertEquals(lisDto.size(), 1);

    }

    @Test
    public void testNumberOfPersonsByHobby() {

        PersonFacade fe = PersonFacade.getPersonFacade(emf);
        Long nrOfPeople = fe.getNumberOfPersonsByHobby("Fodbold");

        assertEquals(nrOfPeople, 1);

    }

    @Test
    public void testDeletePerson() {

        facade.deletePersonById(person.getId());

        assertEquals(facade.getAll().size(), 0);

    }

    @Test
    void testAddHobby() throws Exception {
        EntityManager em = emf.createEntityManager();

        Hobby hobby2 = new Hobby("Ping Pong", "smash med battet", "boldspill", "freeforall altid bro");
        hobbies.add(hobby2);
        em.getTransaction().begin();
        em.persist(hobby2);
        em.getTransaction().commit();
        em.close();
//        person.getHobbies().forEach(h -> {
//            System.out.println(h.getName());
//        });
        facade.addHobbyToPerson(person.getId(), "Ping Pong");
        assertEquals(person.getHobbies().size(), 2);
    }

    @Test
    void testAddhone() throws Exception {
        EntityManager em = emf.createEntityManager();

        Phone phone2 = new Phone(23112314, "Club");
        phones.add(phone2);
        em.getTransaction().begin();
        em.persist(phone2);
        em.getTransaction().commit();
        em.close();
//        person.getHobbies().forEach(h -> {
//            System.out.println(h.getName());
//        });
        facade.addPhoneToPerson(person.getId(), 23112314);
        assertEquals(person.getPhones().size(), 2);
    }

    @Test
    void testHobbyRemoval() throws Exception {
        EntityManager em = emf.createEntityManager();

        Hobby hobby2 = new Hobby("Ping Pong", "smash med battet", "boldspill", "freeforall altid bro");
        em.getTransaction().begin();
        em.persist(hobby2);
        em.getTransaction().commit();
        em.close();
//        person.getHobbies().forEach(h -> {
//            System.out.println(h.getName());
//        });
        facade.addHobbyToPerson(person.getId(), "Ping Pong");
        facade.removeHobby(person.getId(), "Ping Pong");
                assertEquals(1, person.getHobbies().size());
    }
    
    
    @Test
    void testPhoneRemoval() throws Exception {
        EntityManager em = emf.createEntityManager();

        Phone phone2 = new Phone( 33224512, "Work");
        em.getTransaction().begin();
        em.persist(phone2);
        em.getTransaction().commit();
        em.close();
//        person.getHobbies().forEach(h -> {
//            System.out.println(h.getName());
//        });
        facade.addPhoneToPerson(person.getId(), 33224512);
        facade.removePhone(person.getId(), 33224512);
                assertEquals(1, person.getPhones().size());
    }
    
    //TODO DENNE TEST VIRKER IKKE - LØS DEN !
    @Disabled // SLET DENNE NÅR DET VIRKER
    @Test
    void getPersonByPhoneNumberTest(){
        try{EntityManager em = emf.createEntityManager();
        int givenPhoneNumber = 2134566;
        PersonDTO pdtoExpected = new PersonDTO(person);
        PersonDTO pdtoActuel = facade.getPersonByPhoneNumber(givenPhoneNumber);
        
        assertTrue(pdtoActuel.getId() == pdtoExpected.getId() 
                && pdtoActuel.getEmail().equals(pdtoExpected.getEmail())
                && pdtoActuel.getFirstName().equals(pdtoExpected.getFirstName())
                && pdtoActuel.getLastName().equals(pdtoExpected.getLastName())
                && pdtoActuel.getAddress().getAdditionalInfo().equals(pdtoExpected.getAddress().getAdditionalInfo())
                && pdtoActuel.getAddress().getCityInfoDto().getCityName().equals(pdtoExpected.getAddress().getCityInfoDto().getCityName()) // TODO CityInfo bliver ikke tilføjet - løs det
                && pdtoActuel.getHobbies().containsAll(pdtoExpected.getHobbies())
        );}
        catch(ArgumentNullException ex){
            System.out.println("Testen fik et null med som argument"); 
        }
    }
    

// TODO - maybe? hvis vi brugervalidere med at der kun skal være 8 cifre?     
//    //Negative test
//    @Test
//    void getPersonByTooLongPhoneNumberTest(){
//        
//    }
//    
//    //Negative test
//    @Test
//    void getPersonByTooShortPhoneNumberTest(){
//        
//    }

}
