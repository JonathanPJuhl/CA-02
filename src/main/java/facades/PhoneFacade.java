package facades;

import dtos.AddressDTO;
import dtos.PhoneDTO;
import entities.Phone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PhoneFacade {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PhoneFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PhoneFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public PhoneDTO createPhone(PhoneDTO phone) {

        Phone phone1 = new Phone(phone.getPhoneNumber(), phone.getTypeOfNumber());

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(phone1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PhoneDTO(phone1);
    }

    public PhoneDTO getPhoneFromDB(PhoneDTO phone){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Phone> phoneFromDB = em.createQuery("SELECT p FROM Phone p WHERE p.phoneNumber = :a1", Phone.class);
        phoneFromDB.setParameter("a1", phone.getPhoneNumber());
        return new PhoneDTO(phoneFromDB.getSingleResult());
    }



    /* public RenameMeDTO getById(long id){
        EntityManager em = emf.createEntityManager();
        return new RenameMeDTO(em.find(RenameMe.class, id));
    }*/
    //TODO Remove/Change this before use
    public long getRenameMeCount() {
        EntityManager em = emf.createEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(a) FROM Address a").getSingleResult();
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
