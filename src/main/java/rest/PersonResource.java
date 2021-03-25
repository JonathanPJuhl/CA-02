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
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
    public String getAllPersons() {
        List<PersonDTO> ListOfPeople = FACADE.getAll();
        return GSON.toJson(ListOfPeople);

    }

    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPeopleCount() {
        long count = FACADE.getCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":" + count + "}";  //Done manually so no need for a DTO
    }



    @Path("hobby/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<PersonDTO> getAllPersonsByGivenHobby(@PathParam("hobby") String hobbyGiven) {
        return FACADE.getAllPersonsByGivenHobby(hobbyGiven);
    }

    @Path("city/{cityname}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersonsByGivenCity(@PathParam("cityname") String cityname) {
        return GSON.toJson(FACADE.getPeopleByCity(cityname));
    }
    
    @Path("count/{hobby}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getNumberOfPersonsByHobby(@PathParam("hobby") String hobbyGiven){
        long count = FACADE.getNumberOfPersonsByHobby(hobbyGiven);
        return "{\"count\":" + count + "}";
    }
    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByID(@PathParam("id") int id){
        return  GSON.toJson(FACADE.getbyID(id));
    }



//    @Path("editPerson/{newPersonData}")
//    @PUT
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
    
    
}
