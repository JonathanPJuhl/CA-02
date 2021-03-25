package facades;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.Person_UltraDTO;
import dtos.PhoneDTO;
import entities.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.PathParam;

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
    public PersonDTO getbyID(int id){
    EntityManager em = emf.createEntityManager();
    PersonDTO pDTO = new PersonDTO(em.find(Person.class, id));
        return pDTO;
}

    //TODO Remove/Change this before use
    public int updatePerson(Person newData) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Person pers = em.merge(newData);
            em.remove(getbyID(pers.getId()));
            em.persist(newData);

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
        } finally {
            em.close();
        }
        return 1;
    }

    public int getPersonIDByNameAndNumber(PersonDTO personDTO){
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
        TypedQuery<Person> count = em.createQuery("SELECT COUNT(p) FROM Person p JOIN p.hobbies ph WHERE ph.name =:hobby", Person.class);
        count.setParameter("hobby", hobbyGiven);
        List<Person> people = count.getResultList();
        long howMany = people.size();
        return howMany;
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
