
package rest;

import dtos.PersonDTO;
import entities.*;
import facades.PersonFacade;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class RenameMeResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person person;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        CityInfo ci = new CityInfo(2830, "Virum");
        Address ad = new Address("Street", "Additional");
        ad.addCityInfo(ci);

        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone(2134566, "home");
        phones.add(phone);


        Hobby hobby = new Hobby("Fodbold", "spark til bolden og fake skader", "boldsport", "teamsport");
        List<Hobby> hobbies = new ArrayList<>();
        hobbies.add(hobby);

        person = new Person("mail@mail.dk", "Jens", "Brønd", phones, ad, hobbies);
        person.addHobby(hobby);
        person.addAddress(ad);
        person.addPhone(phone);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(person);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This test assumes the database contains two rows
    @Test
    public void testFindAllPersons() throws Exception {
        given()
                .contentType("application/json")
                .get("/persons").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("", hasSize(1));
    }

    @Test
    public void testCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/persons/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(1));
    }
    @Test
    public void testPeopleByCity(){
        given()
                .contentType("application/json")
                .get("/persons/city/"+person.getAddress().getCityInfo().getCityName())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("", hasSize(1));
    }
    @Test
    @Disabled
    public void testCountByHobby(){
        given()
                .contentType("application/json")
                .get("/persons/count/"+"Fodbold")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(1));
    }
    @Test
    public void testPeopleByID(){
        PersonFacade pf = PersonFacade.getPersonFacade(emf);
        int id = pf.getPersonIDByNameAndNumber(new PersonDTO(person));
        given()
                .contentType("application/json")
                .get("/persons/id/"+id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Jens"));
    }
}
