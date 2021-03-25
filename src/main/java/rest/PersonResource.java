package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import entities.Person;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Todo Remove or change relevant parts before ACTUAL use
@Path("persons")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPeopleCount() {
        long count = FACADE.getCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }

    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PersonDTO> getAllPersons() {
        List<PersonDTO> ListOfPeople = FACADE.getAll();
        return ListOfPeople;

    }

    @Path("peopByHobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PersonDTO> getAllPersonsByGivenHobby(@PathParam("hobby") String hobbyGiven) {
        return FACADE.getAllPersonsByGivenHobby(hobbyGiven);
    }

    @Path("peopleByZip/{zip}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PersonDTO> getAllPersonsByGivenCity(@PathParam("zip") int zipper) {
        return FACADE.getPeopleByCity(zipper);
    }
    
    @Path("peopleNumberByHobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public long getNumberOfPersonsByHobby(@PathParam("hobby") String hobbyGiven){
        return FACADE.getNumberOfPersonsByHobby(hobbyGiven);
    }
    
}
