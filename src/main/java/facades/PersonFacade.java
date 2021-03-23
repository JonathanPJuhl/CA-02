package facades;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.PhoneDTO;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
            pers.addHobby(new Hobby(h.getHobbyName(), h.getDescription()));
        }

        pers.addAddress(new Address(pDTO.getAddress().getStreet(),
                pDTO.getAddress().getAdditionalInfo(),
                new CityInfo(pDTO.getAddress().getCityInfoDto().getZip(),
                        pDTO.getAddress().getCityInfoDto().getCityName())));

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
    public long getRenameMeCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }

    }
    /*
    public List<RenameMeDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<RenameMe> query = em.createQuery("SELECT r FROM RenameMe r", RenameMe.class);
        List<RenameMe> rms = query.getResultList();
        return RenameMeDTO.getDtos(rms);
    }*/

 /*
    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = getPersonFacade(emf);
        fe.getAll().forEach(dto->System.out.println(dto));
    }
     */
}
