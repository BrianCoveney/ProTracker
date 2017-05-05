package ie.cit.architect.protracker.persistors;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import ie.cit.architect.protracker.model.*;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import static com.mongodb.client.model.Filters.eq;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;


/**
 * Created by brian on 11/04/17.
 */
public class MongoDBPersistorTest {

    private MongoCollection collectionEmployees, collectionClientsTest, collectionProjects;
    private MongoDatabase database;

    private static String DB_MONGO_PASS = "topdog12";
    private static String DB_MONGO_USER = "developmentuser";
    private static String DB_MONGO_IP = "ec2-54-202-69-181.us-west-2.compute.amazonaws.com";
    private static String DB_NAME = "protrackerdev";


    private MongoDBPersistor mongoDBPersistor;
//    private IUser user;


    @Before
    public void setUp() throws Exception {

        mongoDBPersistor = new MongoDBPersistor();
        try {


            // local database
            MongoClient mongoClientConn = new MongoClient("localhost", 27017);
            database = mongoClientConn.getDatabase("mongotest");


            // remote database
//            String mongoURI = "mongodb://" + DB_MONGO_USER + ":" + DB_MONGO_PASS + "@" +
//                    DB_MONGO_IP + "/" + DB_NAME;
//
//            MongoClient mongoClientConn = new MongoClient(new MongoClientURI(mongoURI));

            database = mongoClientConn.getDatabase(DB_NAME);

            collectionEmployees = database.getCollection("employees");

            collectionClientsTest = database.getCollection("clients");

            collectionProjects = database.getCollection("projects");

        } catch (MongoException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void writeOneUser() throws Exception {

        // Given
        IUser employee = new EmployeeUser("jason@email.com", "passwd");
        Document document = new Document();
        document.put("email", employee.getEmailAddress());
        document.put("password", employee.getPassword());

        // When
        collectionEmployees.insertOne(document);

        // Then
        assertThat(collectionEmployees.count(), is(1L));

        collectionEmployees.deleteOne(eq("email", "jason@email.com"));


    }


    @Test
    public void writeManyUsers() throws Exception {

        // Given
        EmployeeUser user1 = new EmployeeUser("fake@email.com", "passwd");
        // ... add more users
        EmployeeList employeeList = new EmployeeList();
        employeeList.add(user1);

        // When
        mongoDBPersistor.writeEmployeeUsers(employeeList);


        IUser user = mongoDBPersistor.getDbUser(user1);

        // Then
        assertTrue("Expected not null ", user != null);
        assertEquals("fake@email.com", user.getEmailAddress());
        assertEquals("passwd", user.getPassword());

        // clean up
        mongoDBPersistor.deleteUser(user);

    }


    @Test
    public void writeClientUser(){

        ClientUser clientUser1 = new ClientUser("davey@email.com", "passwd");
        ClientList clientList = new ClientList();
        clientList.add(clientUser1);


        // This is the same as MongoDBPersistor#writeClientUsers(ClientList clientUsers)
        // If we called the method from MongoDBPersistor, then we would be writing to the production database.
        // Here we are writing to our test database.
        Document document = new Document();
        try {
            for (IUser currClientUser : clientList.getClientUsers()) {
                document.put("email", currClientUser.getEmailAddress());
                document.put("password", currClientUser.getPassword());
            }

            collectionClientsTest.insertOne(document);

        } catch (MongoException e) {
            e.printStackTrace();
        }

        // verifies count of ClientUser Collection is 1
        assertThat(collectionClientsTest.count(), is(1L));

        // clean up
        collectionClientsTest.deleteOne(eq("email", "davey@email.com"));

    }


    @Test
    public void writeEmployeeUser(){

        EmployeeUser employeeUser = new EmployeeUser("mona@email.com", "passwd");
        EmployeeList employeeList = new EmployeeList();
        employeeList.add(employeeUser);


        // This is the same as MongoDBPersistor#writeClientUsers(ClientList clientUsers)
        // If we called the method from MongoDBPersistor, then we would be writing to the production database.
        // Here we are writing to our test database.
        Document document = new Document();
        try {
            for (IUser currEmployeeUser : employeeList.getEmployeeUsers()) {
                document.put("email", currEmployeeUser.getEmailAddress());
                document.put("password", currEmployeeUser.getPassword());
            }

            collectionEmployees.insertOne(document);

        } catch (MongoException e) {
            e.printStackTrace();
        }

        // verifies count of ClientUser Collection is 1
        assertThat(collectionEmployees.count(), is(1L));

        // clean up
        collectionEmployees.deleteOne(eq("email", "mona@email.com"));

    }


    @Test
    public void updateProject() throws Exception {

        /** Given **/
        String originalName = "Eastside";
        String updatedName = "Westside";

        // construct a Project object with its initial name value set to "House"
        Project project = new Project(originalName, "Brian", "Cork", "Jack");


        // Here we are mocking steps taken in MongoDBPersistor#writeProjects(ProjectList project)
        // Calling that method would write to the main database, this writes to our test database.
        ProjectList projectList = new ProjectList();
        projectList.add(project);

        Document document = new Document();
        for (IProject currProject : projectList.getProjects()) {
            document.put("project_id", currProject.getProjectId());
            document.put("name", currProject.getName());
            document.put("author", currProject.getAuthor());
            document.put("location", currProject.getLocation());
            document.put("client_name", currProject.getClientName());
            document.put("create_date", currProject.getDate());
        }

        collectionProjects.insertOne(document);

        // verifies collection contains a record
        assertThat(collectionProjects.count(), is(1L));


        Project project2 = mongoDBPersistor.readProject(collectionProjects);
        assertThat(project2.getName(), is(originalName));




        /** When **/
        // update the the Projects name in the database, the same as the method in MongoDBPersistor
        collectionProjects.updateOne(eq("name", originalName),
                new Document("$set", new Document("name", updatedName)));


        // this is the method in MongoDBPersistor we are testing, it returns
        // a Project object with the updated name
        project = mongoDBPersistor.updateProject(originalName, updatedName );


        /** Then **/
        assertThat(project.getName(), is(updatedName));
        assertThat(project.getName(), is(not(originalName)));



        // cleanup the db by removing the updated entry
        collectionProjects.deleteOne(eq("name", updatedName));

    }


}



























