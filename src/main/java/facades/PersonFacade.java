package facades;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

    /* public RenameMeDTO getById(long id){
        EntityManager em = emf.createEntityManager();
        return new RenameMeDTO(em.find(RenameMe.class, id));
    }*/
    //TODO Remove/Change this before use
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
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.hobbies hob WHERE hob.hobbyName =:var1", Person.class);
        query.setParameter("var1", hobbyGiven);
        List<Person> pdto = query.getResultList();
        List<PersonDTO> pdtos = PersonDTO.getDtos(pdto);
        return pdtos;
    }

    public List<PersonDTO> getPeopleByCity(int cityZip) {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p JOIN p.address.cityInfo cit WHERE cit.zip =:var1", Person.class);
        query.setParameter("var1", cityZip);
        List<Person> pdto = query.getResultList();
        List<PersonDTO> pdtos = PersonDTO.getDtos(pdto);
        return pdtos;
    }

    //Ugly but works
    public long getNumberOfPersonsByHobby(String hobbyGiven) {
        EntityManager em = emf.createEntityManager();
        Query count = em.createQuery("SELECT COUNT(p) FROM Person p JOIN p.hobbies sw WHERE sw.hobbyName ='" + hobbyGiven + "'");
        long howMany = (long) count.getSingleResult();
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
