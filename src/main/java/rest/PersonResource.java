package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.PersonDTO;
import entities.Person;
import errorhandling.ArgumentNullException;
import utils.EMF_Creator;
import facades.PersonFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
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
    public String getAllPersonsByGivenHobby(@PathParam("hobby") String hobbyGiven) {
        return GSON.toJson(FACADE.getAllPersonsByGivenHobby(hobbyGiven));
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
    public String getNumberOfPersonsByHobby(@PathParam("hobby") String hobbyGiven) {
        long count = FACADE.getNumberOfPersonsByHobby(hobbyGiven);
        return "{\"count\":" + count + "}";
    }

    @Path("id/{id}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByID(@PathParam("id") int id) {
        return GSON.toJson(FACADE.getbyID(id));
    }

    @Path("delete/{id}")
    @DELETE
    public int deletePersonById(@PathParam("id") int id) {
        FACADE.deletePersonById(id);
        return id;
    }
//    @Produces({MediaType.APPLICATION_JSON})
//    @Consumes({MediaType.APPLICATION_JSON})

//    @Path("editPerson/{newPersonData}")
//    @PUT
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces({MediaType.APPLICATION_JSON})
    @Path("{id}")
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String editPersonByIDAndPersonInfo(@PathParam("id") int iDToEdit, String persInfoForUpdating) {
        String jsonPersonDTO;
        try {
            PersonDTO persDTOEditTo = GSON.fromJson(persInfoForUpdating, PersonDTO.class);
            jsonPersonDTO = GSON.toJson(FACADE.updatePerson(persDTOEditTo, iDToEdit));
        } catch (ArgumentNullException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
            jsonPersonDTO = ex.getMessage();
        } catch (NullPointerException e) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, e);
            jsonPersonDTO = e.getMessage();
        }
        return jsonPersonDTO;
    }

    @Path("addHobbyToPerson/{id}")
    @PUT
    public void addHobby(@PathParam("id") int idOfPerson, String hobbyToBeAdded) throws Exception {
        FACADE.addHobbyToPerson(idOfPerson, hobbyToBeAdded);
    }

    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addPerson(String json) {
        Person persDTO = GSON.fromJson(json, Person.class);
        PersonDTO persistedPers = FACADE.create(new PersonDTO(persDTO));
        return GSON.toJson(persistedPers);
    }

    @Path("removeHobbyFromPerson/{id}")
    @DELETE
    public void removeHobby(@PathParam("id") int idOfPerson, String hobbyToBeRemoved) throws Exception {
        FACADE.removeHobby(idOfPerson, hobbyToBeRemoved);
    }

    @Path("addPhoneToPerson/{id}")
    @PUT
    public void addPhone(@PathParam("id") int idOfPerson, String numbr) throws Exception {
        FACADE.addPhoneToPerson(idOfPerson, Integer.parseInt(numbr));
    }

    @Path("removePhoneFromPerson/{id}")
    @DELETE
    public void removePhone(@PathParam("id") int idOfPerson, String numbr) throws Exception {
        FACADE.removePhone(idOfPerson, Integer.parseInt(numbr));
    }

    @Path("phone/{phoneNr}")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonByPhone(@PathParam("phoneNr") int phoneNr) {

        return GSON.toJson(FACADE.getByPhone(phoneNr));
    }

}
