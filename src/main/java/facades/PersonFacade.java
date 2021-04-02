package facades;

import dtos.AddressDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.Person_UltraDTO;
import dtos.PhoneDTO;
import entities.*;
import errorhandling.ArgumentNullException;
import errorhandling.ExceptionDTO;
import errorhandling.IllegalPhoneException;

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

        Person pers = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName());

        for (PhoneDTO p : pDTO.getPhones()) {
            pers.addPhone(new Phone(p.getPhoneNumber(), p.getTypeOfNumber()));
        }

        for (HobbyDTO h : pDTO.getHobbies()) {

            Hobby hobby = new Hobby(h.getName(), h.getWikiLink(), h.getCategory(), h.getType());

            pers.addHobby(hobby);

        }

        Address address = new Address(pDTO.getAddress().getStreet(),
                pDTO.getAddress().getAdditionalInfo());
        address.addCityInfo(new CityInfo(pDTO.getAddress().getCityInfoDto().getZip(), pDTO.getAddress().getCityInfoDto().getCityName()));
        pers.addAddress(address);

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pers);
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

    public PersonDTO validatePersonDTO(PersonDTO pdto) throws ArgumentNullException { //???
        if (pdto.getEmail() == null
                || pdto.getFirstName() == null
                || pdto.getEmail() == null
                || pdto.getAddress() == null
                || pdto.getHobbies().contains(null)
                || pdto.getPhones().contains(null)
                || pdto.getAddress().getCityInfoDto() == null) {
            throw new ArgumentNullException("En personegenskab er null", 400);
        }
        return pdto;
    }

    public Person findPersonByID(int personDTOID, EntityManager em) {
        Query query = em.createQuery("SELECT p FROM Person p WHERE p.id =:id", Person.class);
        query.setParameter("id", personDTOID);

        return (Person) query.getSingleResult();
    }

    public Address findAdressByAddressDTO(AddressDTO addressdto, EntityManager em) {
        TypedQuery<Address> addressQuery = em.createQuery("SELECT a FROM Adress a WHERE a.street =:street AND a.additionalInfo = :additionalInfo", Address.class);
        addressQuery.setParameter("street", addressdto.getStreet());
        addressQuery.setParameter("additionalInfo", addressdto.getAdditionalInfo());

        return addressQuery.getSingleResult();
    }

    private boolean validatePhone(int phoneNumber) throws IllegalPhoneException {
        boolean allGood = true;
        if (String.valueOf(phoneNumber).length() != 8) {
            allGood = false;
        }
        return allGood;
    }

    public PersonDTO updatePerson(PersonDTO newPersonDTO, int oldPersonID) throws ArgumentNullException {
        Address newAddress;
        EntityManager em = emf.createEntityManager();
        Person personFromDB;
        Person newPersonToUpdate = new Person();
        Person personUpdated = new Person();
        List<Phone> newListOfPhones = new ArrayList<>();
        List<Hobby> newListOfHobbies = new ArrayList<>();
        Phone oldPhone;
        Phone oldPhoneByDBPerson;
        //List<Boolean> phonesAreGood = ArrayList<>(); 

        try {
            em.getTransaction().begin();
            validatePersonDTO(newPersonDTO);

            /*  newPersonDTO.getPhones().forEach(phone ->{ 
                validatePhone(phone.getPhoneNumber());
            throw new IllegalPhoneException(406, "Phone number contains 8 digits");
            });*/
            personFromDB = findPersonByID(oldPersonID, em);

            personFromDB.setFirstName(newPersonDTO.getFirstName());
            personFromDB.setLastName(newPersonDTO.getLastName());
            personFromDB.setEmail(newPersonDTO.getEmail());

            //Ny metode? Husk at teste
            for (int i = 0; i < newPersonDTO.getPhones().size()-1; i++) {
                for (int j = 0; j < personFromDB.getPhones().size()-1; j++) {
                    if (newPersonDTO.getPhones().get(j).getPhoneNumber() == personFromDB.getPhones().get(i).getPhoneNumber()
                            && newPersonDTO.getPhones().get(j).getTypeOfNumber().equals(personFromDB.getPhones().get(i).getTypeOfNumber())) {
                        break;
                    } else {
                        personFromDB.getPhones().get(i).setPhoneNumber(newPersonDTO.getPhones().get(j).getPhoneNumber());
                        personFromDB.getPhones().get(i).setTypeOfNumber(newPersonDTO.getPhones().get(j).getTypeOfNumber());
                    }
                }
            }
            //   personFromDB.setPhones(newListOfPhones);

            newAddress = new Address(newPersonDTO.getAddress().getStreet(), newPersonDTO.getAddress().getAdditionalInfo());
            personFromDB.getAddress().setStreet(newAddress.getStreet());
            personFromDB.getAddress().setAdditionalInfo(newAddress.getAdditionalInfo());
            CityInfo newCityInfo = new CityInfo(newPersonDTO.getAddress().getCityInfoDto().getZip(), newPersonDTO.getAddress().getCityInfoDto().getCityName());
            personFromDB.getAddress().getCityInfo().setZip(newCityInfo.getZip());
            personFromDB.getAddress().getCityInfo().setCityName(newCityInfo.getCityName());

            //Ny metode? Husk at teste
            for (int i = 0; i < newPersonDTO.getHobbies().size()-1; i++) {
                for (int j = 0; j < personFromDB.getHobbies().size()-1; j++) {
                    if (newPersonDTO.getHobbies().get(j).getName().equals(personFromDB.getHobbies().get(i).getName())) {
                        break;
                    } else {
                        Hobby newhobby = em.find(Hobby.class, newPersonDTO.getHobbies().get(j).getName());
                        personFromDB.getHobbies().get(i).setName(newhobby.getName());
                        personFromDB.getHobbies().get(i).setCategory(newhobby.getCategory());
                        personFromDB.getHobbies().get(i).setWikiLink(newhobby.getWikiLink());
                        personFromDB.getHobbies().get(i).setType(newhobby.getType());
                    }
                }
            }

            //personFromDB.setHobbies(newListOfHobbies);
            em.merge(personFromDB);
            personUpdated = findPersonByID(oldPersonID, em);
            em.getTransaction().commit();

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

    public void addHobbyToPerson(int id, HobbyDTO hobbyDTO) throws Exception {
        EntityManager em = emf.createEntityManager();
        Hobby hobbyToBeAdded;
        Person personToBeEdited;

        try {

            em.getTransaction().begin();
            hobbyToBeAdded = em.find(Hobby.class, hobbyDTO.getName());
            if (hobbyToBeAdded == null) {
                throw new Exception("The hobby refered by the given hobby name does not currently exist within our databases");
            }

            personToBeEdited = em.find(Person.class, id);
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
