package facades;

import dtos.HobbyDTO;
import entities.Hobby;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private HobbyFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static HobbyFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public HobbyDTO createHobby(HobbyDTO hobby) {

        Hobby hobby1 = new Hobby(hobby.getHobbyName(), hobby.getDescription());

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(hobby1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(hobby1);
    }

    public HobbyDTO getHobbyFromDB(HobbyDTO hobby){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Hobby> hobbyFromDB = em.createQuery("SELECT h FROM Hobby h WHERE h.hobbyName = :a1", Hobby.class);
        hobbyFromDB.setParameter("a1", hobby.getHobbyName());
        return new HobbyDTO(hobbyFromDB.getSingleResult());
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
