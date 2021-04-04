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
            em.persist(new CityInfo("2800", "Lyngby"));
            em.getTransaction().commit();
            CityInfo ci = facade.getCityInfo(new CityInfoDTO("2800", "Lyngby"));
            Address ad = new Address("Street", "Additional");
            ad.addCityInfo(ci);
            em.getTransaction().begin();
            phones = new ArrayList<>();
            Phone phone = new Phone(2134566, "home");

            Hobby hobby = new Hobby("Fodbold", "spark til bolden og fake skader", "boldspill", "teamsport");
            hobbies = new ArrayList<>();

            person = new Person("mail@mail.dk", "Jens", "Brønd");
            person.addHobby(hobby);

            person.addPhone(phone);
            person.addAddress(ad);
            em.merge(person);

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
        CityInfo cityInfo = new CityInfo("2800", "Lyngby");
        Address address = new Address("Street2", "Additional more");
        address.addCityInfo(cityInfo);
        AddressDTO ad = new AddressDTO(address);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Tennis", "smash bold", "boldspill", "teamsport and single player"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("cool@mail.dk", "Peter", "Jensen"), ad, phones, hobbies, new CityInfoDTO(cityInfo));

        pDTO.setAddress(ad);
        pDTO.setPhones(phones);
        pDTO.setHobbies(hobbies);
        facade.create(pDTO);

        assertEquals(facade.getCount(), 2);
    }

    @Test
    public void testGetAllPersons() {
        CityInfo cityInfo = new CityInfo("2030", "Holte");
        Address address = new Address("Street3", "Additional and more");
        address.setCityInfo(cityInfo);
        AddressDTO ad = new AddressDTO(address);
        List<PhoneDTO> phones = new ArrayList<PhoneDTO>();
        PhoneDTO phone = new PhoneDTO(new Phone(73829374, "also so home"));
        phones.add(phone);
        HobbyDTO hobby = new HobbyDTO(new Hobby("Hockey", "smash more bold", "vintersport", "teamsport"));
        List<HobbyDTO> hobbies = new ArrayList<>();
        hobbies.add(hobby);
        PersonDTO pDTO = new PersonDTO(new Person("icecool@mail.dk", "Hugo", "Jarvier"), ad, phones, hobbies, new CityInfoDTO(cityInfo));

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
    public void testEditPersonSetEmail() throws ArgumentNullException, Exception {
        int personToChangeID = person.getId();
        PersonDTO pDToExpected = new PersonDTO(person);
       pDToExpected.setEmail("wannabemail@hacker.dk");
        
        PersonDTO pdtoResult = facade.updatePerson(pDToExpected, personToChangeID);
        assertEquals(pDToExpected.getEmail(), pdtoResult.getEmail());
    }
    
    @Test
    public void testEditPersonSetNewListManyPhones() throws ArgumentNullException, Exception {
        EntityManager em = emf.createEntityManager();
        List<Phone> emptyListOfPhones = new ArrayList<>();
        Phone phone1 = new Phone(12345678, "Home");
        Phone phone2 = new Phone(87654321, "Home");
        Phone phone3 = new Phone(29292929, "Work");
        Person pers = person;
        pers.setPhones(emptyListOfPhones);
        pers.addPhone(phone1);
        pers.addPhone(phone2);
        pers.addPhone(phone3);
        try{
        em.getTransaction().begin();
        em.merge(pers);
        em.getTransaction().commit();
        }finally{
        em.close();
        }
        PersonDTO pDTOToEditedPerson = new PersonDTO(pers);
        
        Phone phoneNew1 = new Phone(33333333, "Work");
        pDTOToEditedPerson.getPhones().get(1).setPhoneNumber(phoneNew1.getPhoneNumber());
        pDTOToEditedPerson.getPhones().get(1).setTypeOfNumber(phoneNew1.getTypeOfNumber());
        Phone phoneNew2 = new Phone(99999999, "Private");
        pDTOToEditedPerson.getPhones().add(new PhoneDTO(phoneNew2));
        PersonDTO acctualPersonDTO = facade.updatePerson(pDTOToEditedPerson,pers.getId());
      
        assertTrue(phoneNew1.getPhoneNumber() == acctualPersonDTO.getPhones().get(1).getPhoneNumber() 
                && phoneNew1.getTypeOfNumber().equals(acctualPersonDTO.getPhones().get(1).getTypeOfNumber())
                && phoneNew2.getPhoneNumber() == acctualPersonDTO.getPhones().get(pDTOToEditedPerson.getPhones().size()-1).getPhoneNumber()
                && phoneNew2.getTypeOfNumber().equals(acctualPersonDTO.getPhones().get(pDTOToEditedPerson.getPhones().size()-1).getTypeOfNumber()));
    }
    
    @Test
    public void testEditPersonSetNewListLessPhones() throws ArgumentNullException, Exception {
        EntityManager em = emf.createEntityManager();
        List<Phone> emptyListOfPhones = new ArrayList<>();
        Phone phone1 = new Phone(12345678, "Home");
        Phone phone2 = new Phone(87654321, "Home");
        Phone phone3 = new Phone(29292929, "Work");
        Person pers = person;
        pers.setPhones(emptyListOfPhones);
        pers.addPhone(phone1);
        pers.addPhone(phone2);
        pers.addPhone(phone3);
        try{
        em.getTransaction().begin();
        em.merge(pers);
        em.getTransaction().commit();
        }finally{
        em.close();
        }
        PersonDTO pDTOToEditedPerson = new PersonDTO(pers);
        
        Phone phoneNew1 = new Phone(33333333, "Work");
        pDTOToEditedPerson.getPhones().get(1).setPhoneNumber(phoneNew1.getPhoneNumber());
        pDTOToEditedPerson.getPhones().get(1).setTypeOfNumber(phoneNew1.getTypeOfNumber());
        
        pDTOToEditedPerson.getPhones().remove(0);
        
        PersonDTO acctualPersonDTO = facade.updatePerson(pDTOToEditedPerson,pers.getId());
      
        assertTrue(phoneNew1.getPhoneNumber() == acctualPersonDTO.getPhones().get(0).getPhoneNumber() 
                && phoneNew1.getTypeOfNumber().equals(acctualPersonDTO.getPhones().get(0).getTypeOfNumber())
        && acctualPersonDTO.getPhones().size() == 2);
    }
    
    
    @Test
    public void testEditPersonSetNewListHobbies() throws ArgumentNullException, Exception {
        EntityManager em = emf.createEntityManager();
        List<Hobby> emptyListOfHobbies = new ArrayList<>();
        Hobby hobby1 = new Hobby("Håndhold", "Kast med bolden", "boldspil", "teamsport");
        Hobby hobby2 = new Hobby("Badminton", "Ram en fjerbold", "ketcherspil", "en-mod-en");
        Hobby hobby3 = new Hobby("PokémonGO", "Fang og træn pokémons", "app spil", "enemandsspil");
        Hobby hobby4 = new Hobby("Tennis", "Ram en bold", "ketcherspil", "en-mod-en");
        Person pers = person;
        pers.setHobbies(emptyListOfHobbies);
        pers.addHobby(hobby1);
        pers.addHobby(hobby2);
        pers.addHobby(hobby3);
        pers.addHobby(hobby4);
        try{
        em.getTransaction().begin();
        em.merge(pers);
        em.getTransaction().commit();
        }finally{
        em.close();
        }
        PersonDTO pDTOToEditedPerson = new PersonDTO(pers);
        
        Hobby newHobby = new Hobby("Volleyball", "Kast med bolden over hegn", "boldspil", "teamsport");
        pDTOToEditedPerson.getHobbies().get(0).setName(newHobby.getName());
        pDTOToEditedPerson.getHobbies().get(0).setWikiLink(newHobby.getWikiLink());
        pDTOToEditedPerson.getHobbies().get(0).setCategory(newHobby.getCategory());
        pDTOToEditedPerson.getHobbies().get(0).setType(newHobby.getType());
     
        PersonDTO acctualPersonDTO = facade.updatePerson(pDTOToEditedPerson,pers.getId());
      
        assertTrue(newHobby.getName().equals(acctualPersonDTO.getHobbies().get(0).getName())&&
        newHobby.getWikiLink().equals(acctualPersonDTO.getHobbies().get(0).getWikiLink())&&
        newHobby.getCategory().equals(acctualPersonDTO.getHobbies().get(0).getCategory())&&
        newHobby.getType().equals(acctualPersonDTO.getHobbies().get(0).getType()));
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

        assertTrue(actualMessage.equals(expectedMessage));
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
        try{em.getTransaction().begin();
        em.persist(hobby2);
        em.getTransaction().commit();
        facade.addHobbyToPerson(person.getId(), new HobbyDTO(hobby2));
        person = em.find(Person.class, person.getId());
        }finally{
            em.close();
        }
        assertEquals(2, person.getHobbies().size());
    }

    @Test
    void testAddphone() throws Exception {
        EntityManager em = emf.createEntityManager();

        Phone phone2 = new Phone(23112314, "Club");
        phones.add(phone2);
        em.getTransaction().begin();
        em.persist(phone2);
        em.getTransaction().commit();
        em.close();

        PersonDTO personDTOExpected = facade.addPhoneToPerson(person.getId(), 23112314);
        assertEquals( 2,personDTOExpected.getPhones().size());
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
        facade.addHobbyToPerson(person.getId(), new HobbyDTO(hobby2));
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

}
