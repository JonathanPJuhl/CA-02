package facades;

import dtos.AddressDTO;
import dtos.CityInfoDTO;
import entities.Address;
import entities.CityInfo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class AddressAndCityInfoFacade {

    private static AddressAndCityInfoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AddressAndCityInfoFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AddressAndCityInfoFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressAndCityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public AddressDTO createAddress(AddressDTO address) {
       // CityInfo ci2 = new CityInfo(ci.getZip(), ci.getCityName());
        Address address1 = new Address(address.getStreet(), address.getAdditionalInfo());

       /* for (HobbyDTO h : pDTO.getHobbies()) {
            pers.addHobby(new Hobby(h.getHobbyName(), h.getDescription()));
        }*/





        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(address1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AddressDTO(address1);
    }
    public CityInfoDTO createCityInfo(CityInfoDTO cityInfo) {

        CityInfo ci = new CityInfo(cityInfo.getZip(), cityInfo.getCityName());

        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(ci);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CityInfoDTO(ci);
    }

    public AddressDTO getAddressFromDB(AddressDTO address){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Address> addressFromDB = em.createQuery("SELECT a FROM Address a WHERE a.street = :a1 AND a.additionalInfo = :a2", Address.class);
        addressFromDB.setParameter("a1", address.getStreet());
        addressFromDB.setParameter("a2", address.getAdditionalInfo());
        return new AddressDTO(addressFromDB.getSingleResult());
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
