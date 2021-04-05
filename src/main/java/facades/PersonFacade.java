package facades;


import dtos.AddressDTO;
import dtos.CityInfoDTO;
import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.Person_UltraDTO;
import dtos.PhoneDTO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.*;

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
        EntityManager em = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();
        Person pers = new Person(pDTO.getEmail(), pDTO.getFirstName(), pDTO.getLastName());

        try {
            em.getTransaction().begin();
            em.persist(pers);
            em.getTransaction().commit();

            for (PhoneDTO p : pDTO.getPhones()) {
                pers.addPhone(new Phone(p.getPhoneNumber(), p.getTypeOfNumber()));
            }


            for (HobbyDTO h : pDTO.getHobbies()) {
                HobbyFacade hf = HobbyFacade.getHobbyFacade(emf);
                Hobby hobby = hf.findSingleHobbyByName(h.getName());

                pers.addHobby(hobby);
                hobby.addPerson(pers);
                /*em.getTransaction().begin();
                em.merge(hobby);
                em.getTransaction().commit();*/
            }
            Address address = new Address(pDTO.getAddress().getStreet(),
                    pDTO.getAddress().getAdditionalInfo());
            CityInfo cityInfo = getCityInfo(pDTO.getCityInfoDTO());
            //address.addCityInfo(new CityInfo(pDTO.getAddress().getCityInfoDto().getZip(), pDTO.getAddress().getCityInfoDto().getCityName()));
            address.addCityInfo(cityInfo);
            pers.addAddress(address);
            //pers.getAddress().setCityInfo(getCityInfo(new CityInfoDTO(pDTO.getCityInfoDTO().getZip(), pDTO.getCityInfoDTO().getCityName())));



            em2.getTransaction().begin();

            em2.merge(pers);
            em2.getTransaction().commit();
        } finally {
            em.close();
            em2.close();
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
                || pdto.getEmail() == null
                || pdto.getAddress() == null
                || pdto.getHobbies().contains(null)
                || pdto.getPhones().contains(null)
                || pdto.getCityInfoDTO() == null) {
            throw new ArgumentNullException("En personegenskab er null", 400);
        }
        return pdto;
    }

    public Person findPersonByID(int phoneNumber, EntityManager em) {
        TypedQuery<Phone> query = em.createQuery("SELECT ph FROM Phone ph WHERE ph.phoneNumber =:number", Phone.class);
        query.setParameter("number", phoneNumber);
        Phone phone = query.getSingleResult();
        return phone.getPerson();
    }

    public Address findAdressByAddressDTO(AddressDTO addressdto, EntityManager em) {
        TypedQuery<Address> addressQuery = em.createQuery("SELECT a FROM Address a WHERE a.street =:street AND a.additionalInfo = :additionalInfo", Address.class);
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

    public PersonDTO updatePerson(PersonDTO newPersonDTO, int oldPersonPhoneNumber) throws ArgumentNullException {
        Address newAddress;
        EntityManager em = emf.createEntityManager();
        EntityManager emPhone = emf.createEntityManager();
        Person personFromDB;
        Person newPersonToUpdate = new Person();
        Person personUpdated = new Person();
        List<Phone> newListOfPhones = new ArrayList<>();
        List<PhoneDTO> extraPhonesDTOs = new ArrayList<>();
        List<Hobby> emptyOfHobbies = new ArrayList<>();
        Phone phone;
        PhoneDTO phoneNew;
        Phone phoneToDelete;

        validatePersonDTO(newPersonDTO);

        /*  newPersonDTO.getPhones().forEach(phone ->{ 
                validatePhone(phone.getPhoneNumber());
            throw new IllegalPhoneException(406, "Phone number contains 8 digits");
            });*/
        personFromDB = findPersonByID(oldPersonPhoneNumber, em);

        personFromDB.setFirstName(newPersonDTO.getFirstName());
        personFromDB.setLastName(newPersonDTO.getLastName());
        personFromDB.setEmail(newPersonDTO.getEmail());

        //Phones
        newPersonDTO.getPhones().forEach(phoneDTO -> {
            extraPhonesDTOs.add(phoneDTO);
        });

        //Makes sure that personFromDB's list of phones isn't larger than the new list og phonesDTOs
        while (newPersonDTO.getPhones().size() < personFromDB.getPhones().size()) {
            personFromDB.getPhones().remove(newPersonDTO.getPhones().size() - 1);
        }

        //Sets new phones instead of old once but keeps id
        for (int i = 0; i < newPersonDTO.getPhones().size(); i++) {
            try {
                phone = personFromDB.getPhones().get(i);
                phoneNew = newPersonDTO.getPhones().get(i);
                phone.setPhoneNumber(phoneNew.getPhoneNumber());
                phone.setTypeOfNumber(phoneNew.getTypeOfNumber());
                extraPhonesDTOs.remove(phoneNew);
            } catch (Exception e) {
                break;

            }
        }

        //Persist new phones if there is more phones i the new list og phones
        if (!(extraPhonesDTOs.isEmpty())) {
            try {
                emPhone.getTransaction().begin();
                for (PhoneDTO pdto : extraPhonesDTOs) {
                    Phone phoneToAdd = new Phone(pdto.getPhoneNumber(), pdto.getTypeOfNumber());
                    emPhone.persist(phoneToAdd);
                    TypedQuery<Phone> pQuery = emPhone.createQuery("SELECT p FROM Phone p WHERE p.phoneNumber =:number", Phone.class);
                    pQuery.setParameter("number", pdto.getPhoneNumber());
                    personFromDB.addPhone(pQuery.getSingleResult());

                }
                emPhone.getTransaction().commit();
            } finally {
                emPhone.close();
            }
        }

        //Adresses and City information
        newAddress = new Address(newPersonDTO.getAddress().getStreet(), newPersonDTO.getAddress().getAdditionalInfo());
        personFromDB.getAddress().setStreet(newAddress.getStreet());
        personFromDB.getAddress().setAdditionalInfo(newAddress.getAdditionalInfo());
        CityInfo newCityInfo = new CityInfo(newPersonDTO.getCityInfoDTO().getZip(), newPersonDTO.getCityInfoDTO().getCityName());
        personFromDB.getAddress().getCityInfo().setZip(newCityInfo.getZip());
        personFromDB.getAddress().getCityInfo().setCityName(newCityInfo.getCityName());

        //Hobbies - resets old list and adds the new hobbies
        newPersonDTO.getHobbies().forEach((hDTO) -> {
            HobbyFacade hf = HobbyFacade.getHobbyFacade(emf);
            Hobby hobby = hf.findSingleHobbyByName(hDTO.getName());
            personFromDB.setHobbies(emptyOfHobbies);
            personFromDB.addHobby(hobby);
        });

        try {
            em.getTransaction().begin();
            em.merge(personFromDB);
            personUpdated = em.find(Person.class, personFromDB.getId());
            em.getTransaction().commit();

        } finally {
            em.close();
        }
        return new PersonDTO(personUpdated, true);
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
            TypedQuery<Hobby> hobbyQ = em.createQuery("SELECT h FROM Hobby h WHERE h.name =:name", Hobby.class);
            hobbyQ.setParameter("name", hobbyDTO.getName());
            hobbyToBeAdded = hobbyQ.getSingleResult();

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

    public PersonDTO addPhoneToPerson(int id, int number) throws Exception {
        EntityManager em = emf.createEntityManager();
        Phone phoneToBeAdded;
        Person personResult = null;

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
            personResult = em.find(Person.class, personToBeEdited.getId());

        } finally {
            em.close();
        }
        return new PersonDTO(personResult, true);
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
