package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.*;
import entities.*;
import errorhandling.ArgumentNullException;
import errorhandling.ExceptionDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.PathParam;
import rest.PersonResource;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PersonDTO create(PersonDTO pDTO) {
        EntityManager em = emf.createEntityManager();
        Person pers = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName());






        try {
            em.getTransaction().begin();
            em.persist(pers);
            em.getTransaction().commit();

            for (PhoneDTO p : pDTO.getPhones()) {
                pers.addPhone(new Phone(p.getPhoneNumber(), p.getTypeOfNumber()));
            }

            for (HobbyDTO h : pDTO.getHobbies()) {

                Hobby hobby = new Hobby(h.getName(), h.getWikiLink(), h.getCategory(), h.getType());

                pers.addHobby(hobby);

            }
            Address address = new Address(pDTO.getAddress().getStreet(),
                    pDTO.getAddress().getAdditionalInfo());
            CityInfo cityInfo = getCityInfo(pDTO.getCityInfoDTO());
            //address.addCityInfo(new CityInfo(pDTO.getAddress().getCityInfoDto().getZip(), pDTO.getAddress().getCityInfoDto().getCityName()));
            address.addCityInfo(cityInfo);
            pers.addAddress(address);
            //pers.getAddress().setCityInfo(getCityInfo(new CityInfoDTO(pDTO.getCityInfoDTO().getZip(), pDTO.getCityInfoDTO().getCityName())));



            em.getTransaction().begin();
            em.merge(pers);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(pers);
    }

    public PersonDTO getbyID(int id) {
        EntityManager em = emf.createEntityManager();
        PersonDTO pDTO = new PersonDTO(em.find(Person.class, id));
        return pDTO;

    }
    public CityInfo getCityInfo(CityInfoDTO cdto){
        EntityManager em = emf.createEntityManager();
        TypedQuery<CityInfo> query = em.createQuery("SELECT c FROM CityInfo c WHERE c.zip =:zip", CityInfo.class);
        query.setParameter("zip", cdto.getZip());

        return query.getSingleResult();
    }

    public PersonDTO validatePersonDTO(PersonDTO pdto) throws ArgumentNullException { //???
        if (pdto.getEmail() == null
                || pdto.getFirstName() == null
                || pdto.getEmail() == null) {
            throw new ArgumentNullException("En personegenskab er null", 400); //?????? - TODO Tilpas til bedre exception
        }
        return pdto;
    }

    public Person findPersonByID(int personDTOID, EntityManager em) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.id =:id", Person.class);
        query.setParameter("id", personDTOID);

        return (Person) query.getSingleResult();
    }

    public PersonDTO updatePerson(PersonDTO newPersonDTO, int oldPersonID) throws ArgumentNullException, NullPointerException { //TODO overveje måske et objekt istedet for int ?

        EntityManager em = emf.createEntityManager();
        PersonDTO updatedPersonDTO;
        Person personFromDB;
        Person personUpdated = null;
        try {
            em.getTransaction().begin();


            /*if (newData.getFirstName() != null) { */
            validatePersonDTO(newPersonDTO);
            personFromDB = findPersonByID(oldPersonID, em);
            if (personFromDB == null) {
                throw new NullPointerException("Person to update doesn't exist");
            }
            personFromDB.setEmail(newPersonDTO.getEmail());
            personFromDB.setFirstName(newPersonDTO.getFirstName());
            personFromDB.setLastName(newPersonDTO.getLastName());
            em.merge(personFromDB);
            personUpdated = findPersonByID(personFromDB.getId(), em);

            /*if (newData.getFirstName() != null) {

            Query query = em.createQuery("UPDATE Person p SET p.firstName =:newFirstName WHERE p.id =:id");
            query.setParameter("newFirstName", newData.getFirstName());
            query.setParameter("id", newData.getId());
            query.executeUpdate();
        }
        if (newData.getLastName() != null) {
            Query query2 = em.createQuery("UPDATE Person p SET p.lastName =:newLastName WHERE p.id =:id");
            query2.setParameter("newLastName", newData.getLastName());
            query2.setParameter("id", newData.getId());
            query2.executeUpdate();
        }
        if (newData.getAddress() != null) {
            Query query3 = em.createQuery("UPDATE Person p SET p.address =:newAddress WHERE p.id =:id");
            query3.setParameter("newAddress", newData.getAddress());
            query3.setParameter("id", newData.getId());
            query3.executeUpdate();
        }
        if (newData.getEmail() != null) {
            Query query4 = em.createQuery("UPDATE Person p SET p.email =:newEmail WHERE p.id =:id");
            query4.setParameter("newEmail", newData.getEmail());
            query4.setParameter("id", newData.getId());
            query4.executeUpdate();
        }

        if (newData.getHobbies() != null) {
           // TypedQuery<Person_UltraDTO> query7777 = em.createQuery("UPDATE new dtos.Person_UltraDTO (p.hobbyName, p.description) FROM Person p JOIN p.hobbies", Person_UltraDTO.class);
           // TypedQuery<Person_UltraDTO> query5 = em.createQuery("UPDATE Hobby h SET (h.description =:description, h.hobbyName =:hobbyName) WHERE t.id in (select t1.id from Team t1  LEFT JOIN t1.members m WHERE t1.current = :current_true AND m.account = :account)", Person_UltraDTO.class);
            TypedQuery<Person_UltraDTO> query6 = em.createQuery("UPDATE Hobby h SET h.description =:description, h.hobbyName =:hobbyName WHERE h.id in (select h1.id from Hobby h1  LEFT JOIN h1.persons p WHERE h1.id = :hobbyID AND p.id = :personID)", Person_UltraDTO.class);
            TypedQuery<Person_UltraDTO> query7 = em.createQuery("UPDATE Person p SET p.hobbies =: hobby WHERE p.id in (select p1.id from Person p1  LEFT JOIN p1.hobbies h WHERE h.hobbyName = :hobbyName AND p.id = :personID)", Person_UltraDTO.class);
            query7.setParameter("hobby", newData.getHobbies());
            query7.setParameter("hobbyName", newData.getHobbies().get(0).getHobbyName());
            query7.setParameter("personID", newData.getId());



            
            
            //TypedQuery<PersonStyleDTO> q4 = em.createQuery("SELECT new dto.PersonStyleDTO(p.name, p.year, s.styleName) FROM Person p JOIN p.styles s", dto.PersonStyleDTO.class);

            
            
//            Query query5 = em.createQuery("UPDATE Person SET Person =:newEmail WHERE person.id =:id");


//            
//            query5.setParameter("newEmail", newData.getEmail());
//            query5.setParameter("id", newData.getId());
//            query5.executeUpdate();
        }*/
            em.getTransaction().commit();
        } catch (ArgumentNullException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            ex.getErrorCode();
            throw ex;
        } finally {
            em.close();
        }
        return new PersonDTO(personUpdated);
    }

    public int getPersonIDByNameAndNumber(PersonDTO personDTO) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> pers = em.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class);
        pers.setParameter("email", personDTO.getEmail());

        Person p = pers.getSingleResult();
        int id = p.getId();
        /*Person persFromDB = pers.getSingleResult();
        int id = persFromDB.getId();*/
        return id;
    }

    public long getCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long personCount = (long) em.createQuery("SELECT COUNT(r) FROM Person r").getSingleResult();
            return personCount;
        } finally {
            em.close();
        }

    }

    public void addHobbyToPerson(int id, String hobbyAdded) throws Exception {
        EntityManager em = emf.createEntityManager();
        Hobby hobbyToBeAdded;

        try {

            em.getTransaction().begin();
            Query aPerson = em.createQuery("SELECT p FROM Person p WHERE p.id=:id");
            aPerson.setParameter("id", id);
            Person personToBeEdited = (Person) aPerson.getSingleResult();

            if (personToBeEdited == null) {
                throw new Exception("Person does not exist in DataBase");
            }

            Query aHobby = em.createQuery("SELECT h FROM Hobby h WHERE h.name=:name");
            aHobby.setParameter("name", hobbyAdded);
            hobbyToBeAdded = (Hobby) aHobby.getSingleResult();

            if (hobbyToBeAdded == null) {
                throw new Exception("The hobby refered by the given hobby name does not currently exist within our databases");
            }

            personToBeEdited.addHobby(hobbyToBeAdded);

            em.merge(personToBeEdited);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public void addPhoneToPerson(int id, int number) throws Exception {
        EntityManager em = emf.createEntityManager();
        Phone phoneToBeAdded;

        try {
            em.getTransaction().begin();
            Query aPerson = em.createQuery("SELECT p FROM Person p WHERE p.id=:id");
            aPerson.setParameter("id", id);
            Person personToBeEdited = (Person) aPerson.getSingleResult();

            if (personToBeEdited == null) {
                throw new Exception("Person does not exist in DataBase");
            }

            Query aPhone = em.createQuery("SELECT p FROM Phone p WHERE p.phoneNumber=:numbr");
            aPhone.setParameter("numbr", number);
            phoneToBeAdded = (Phone) aPhone.getSingleResult();

            if (phoneToBeAdded == null) {
                throw new Exception("The given number, does not correlate to an existing phone in the current DataBase");
            }
            personToBeEdited.addPhone(phoneToBeAdded);

            em.merge(personToBeEdited);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public void removeHobby(int id, String hobbyRemove) throws Exception {
        EntityManager em = emf.createEntityManager();
        Hobby hobbyToBeRemoved;

        try {

            Query aPerson = em.createQuery("SELECT p FROM Person p WHERE p.id=:id");
            aPerson.setParameter("id", id);
            Person personToBeEdited = (Person) aPerson.getSingleResult();

            if (personToBeEdited == null) {
                throw new Exception("Person does not exist in DataBase");
            }

            Query aHobby = em.createQuery("SELECT h FROM Hobby h WHERE h.name=:name");
            aHobby.setParameter("name", hobbyRemove);
            hobbyToBeRemoved = (Hobby) aHobby.getSingleResult();

            if (hobbyToBeRemoved == null) {
                throw new Exception("The hobby refered by the given hobby name does not currently exist within our databases");
            }

            personToBeEdited.removeHobbie(hobbyToBeRemoved);

            em.getTransaction().begin();
            em.merge(personToBeEdited);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public void removePhone(int id, int phoneRemove) throws Exception {
        EntityManager em = emf.createEntityManager();
        Phone phoneToBeRemoved;

        try {

            Query aPerson = em.createQuery("SELECT p FROM Person p WHERE p.id=:id");
            aPerson.setParameter("id", id);
            Person personToBeEdited = (Person) aPerson.getSingleResult();

            if (personToBeEdited == null) {
                throw new Exception("Person does not exist in DataBase");
            }

            Query aPhone = em.createQuery("SELECT p FROM Phone p WHERE p.phoneNumber=:numbr");
            aPhone.setParameter("numbr", phoneRemove);
            phoneToBeRemoved = (Phone) aPhone.getSingleResult();

            if (phoneToBeRemoved == null) {
                throw new Exception("The hobby refered by the given hobby name does not currently exist within our databases");
            }

            personToBeEdited.removePhone(phoneToBeRemoved);

            em.getTransaction().begin();
            em.merge(personToBeEdited);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public List<PersonDTO> getAll() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> pdto = query.getResultList();
        List<PersonDTO> pdtos = PersonDTO.getDtos(pdto);
        return pdtos;
    }

    public List<PersonDTO> getAllPersonsByGivenHobby(String hobbyGiven) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies hob WHERE hob.name =:var1", Person.class);
        query.setParameter("var1", hobbyGiven);
        List<Person> pdto = query.getResultList();
        List<PersonDTO> pdtos = PersonDTO.getDtos(pdto);
        return pdtos;
    }

    public List<PersonDTO> getPeopleByCity(String city) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address.cityInfo cit WHERE cit.cityName =:cityname", Person.class);
        query.setParameter("cityname", city);
        List<Person> pdto = query.getResultList();
        List<PersonDTO> pdtos = PersonDTO.getDtos(pdto);
        return pdtos;
    }

    //Ugly but works
    public long getNumberOfPersonsByHobby(String hobbyGiven) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> count = em.createQuery("SELECT p FROM Person p JOIN p.hobbies ph WHERE ph.name =:hobby", Person.class);
        count.setParameter("hobby", hobbyGiven);
        List<Person> people = count.getResultList();
        long howMany = people.size();
        return howMany;
    }

    public void deletePersonById(int id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            TypedQuery<Person> aPerson = em.createQuery("SELECT p FROM Person p WHERE p.id=:id", Person.class);
            aPerson.setParameter("id", id);
            Person pToBeDeleted = aPerson.getSingleResult();
            em.remove(pToBeDeleted);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public PersonDTO getByPhone(int phoneNr) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            //TypedQuery<Person> aPerson = em.createQuery("SELECT p FROM Person p JOIN p.phones pp WHERE pp.phoneNumber = :phone", Person.class);
            TypedQuery<Phone> aPerson = em.createQuery("SELECT p FROM Phone p WHERE p.phoneNumber = :phone", Phone.class);
            TypedQuery<Address> cityInfo = em.createQuery("SELECT a FROM Address a WHERE a.id = :aID", Address.class);
            aPerson.setParameter("phone", phoneNr);

            Phone phone = aPerson.getSingleResult();

            Person p = phone.getPerson();
            cityInfo.setParameter("aID", p.getAddress().getId());
            CityInfoDTO ciDTO = new CityInfoDTO(cityInfo.getSingleResult().getCityInfo());
            em.getTransaction().commit();
            List<HobbyDTO> hDTO = new ArrayList<>();
            for(Hobby h: p.getHobbies()){
                hDTO.add(new HobbyDTO(h));
            }
            List<PhoneDTO> pDTO = new ArrayList<>();
            for(Phone po: p.getPhones()){
                pDTO.add(new PhoneDTO(po));
            }
            return new PersonDTO(p, new AddressDTO(p.getAddress(), ciDTO),pDTO, hDTO);
        } finally {
            em.close();
        }
    }

//    public long getNumberOfPersonsByHobby(String hobbyGiven) {
//        EntityManager em = emf.createEntityManager();
//        TypedQuery<long> count = em.createQuery("SELECT COUNT(p) FROM Person p JOIN p.hobbies sw WHERE sw.hobbyName =:var1", long.class);
//        count.setParameter("var1", hobbyGiven);
//        List<PersonDTO> howManyInHobby = count.getResultList();
//        
//        return howManyInHobby;
//    }
    /*
    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = getPersonFacade(emf);
        fe.getAll().forEach(dto->System.out.println(dto));
    }
     */
}
