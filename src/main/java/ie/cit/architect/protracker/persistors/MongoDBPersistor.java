package ie.cit.architect.protracker.persistors;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ie.cit.architect.protracker.model.*;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;


/**
 * Author: Brian Coveney
 * Date:   13/03/17.
 */
public class MongoDBPersistor implements IPersistor {

    private MongoCollection collectionEmployees, collectionClients, collectionProjects, collectionMessages;
    private MongoDatabase database;

    private ArrayList<Project> orderedArrayList;
    private String DB_NAME = "protracker";


    public MongoDBPersistor() {

        try {

            // Comment / Un-comment either of the below Objects for localhost / AWS MongoDB

            // local database
            MongoClient mongoClientConn = MongoLocalConnector.databaseConnectionLocal();

            // remote database
//            MongoClient mongoClientConn = MongoRemoteConnector.databaseConnectionRemote()


            //Get Database
            // if database doesn't exist, mongoDB will create it for you
            database = mongoClientConn.getDatabase(DB_NAME);


            //Get Collection / Table from 'protracker'
            //if collection doesn't exist, mongoDB will create it for you
            collectionEmployees = database.getCollection("employees");

            collectionClients = database.getCollection("clients");


            // create another table
            collectionProjects = database.getCollection("projects");

            // create another table
            collectionMessages = database.getCollection("messages");



        } catch (MongoException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void writeMessages(ChatMessage chatMessages) {
        Document document = new Document();
        try {

            document.put("message", chatMessages.getMessage());

            collectionMessages.insertOne(document);

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ChatMessage readMessage() {

        ChatMessage chatMessage = null;
        FindIterable<Document> databaseRecords = database.getCollection("messages").find();
        MongoCursor<Document> cursor = databaseRecords.iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                String mMessage = document.getString("message");
                chatMessage = new ChatMessage(mMessage);
            }
        } finally {
            cursor.close();
        }
        return chatMessage;
    }


    @Override
    public Project readProject(MongoCollection collection) {

        Project project = new Project();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                String projectName = doc.getString("name");
                project.setName(projectName);

            }
        } finally {
            cursor.close();
        };
        return project;
    }




    @Override
    public void writeEmployeeUsers(EmployeeList employeeUsers) {
        Document document = new Document();
        try {
            for (IUser currUser : employeeUsers.getEmployeeUsers()) {
                document.put("email", currUser.getEmailAddress());
                document.put("password", currUser.getPassword());
            }
            collectionEmployees.insertOne(document);

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeClientUsers(ClientList clientUsers) {
        Document document = new Document();
        try {
            for (IUser currUser : clientUsers.getClientUsers()) {
                document.put("email", currUser.getEmailAddress());
                document.put("password", currUser.getPassword());
            }

            collectionClients.insertOne(document);

        } catch (MongoException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void writeProjects(ProjectList projects) {
        try {
            Document document = new Document();

            for (IProject currProject : projects.getProjects()) {

                document.put("project_id", currProject.getProjectId());
                document.put("name", currProject.getName());
                document.put("author", currProject.getAuthor());
                document.put("location", currProject.getLocation());
                document.put("client_name", currProject.getClientName());
                document.put("create_date", currProject.getDate());

            }

            collectionProjects.insertOne(document);

        }catch (MongoException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void deleteProject(Project projectName) {
        collectionProjects.deleteOne(eq("name", projectName.getName()));
    }


    @Override
    public void deleteUser(IUser user) {
        collectionEmployees.deleteOne(eq("email", user.getEmailAddress()));
    }


    // for JUnit tests
    public IUser getDbUser(EmployeeUser user) {
        return user;
    }


















    @Override
    public Project updateProject(String currentName, String newName) {

        Project project = new Project(currentName);

        collectionProjects.updateOne(eq("name", currentName),
                new Document("$set", new Document("name", newName)));


        project.setName(newName);

        return project;
    }



















    @Override
    public ArrayList<Project> readProjects() {

        // HashSet so we do not store duplicate values
        Set<Project> projectNameHashSet = new HashSet<>();
        ArrayList<Project> projectArrayList;

        FindIterable<Document> databaseRecords = database.getCollection("projects").find();
        MongoCursor<Document> cursor = databaseRecords.iterator();

        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                int projectId = document.getInteger("project_id");
                String projectName = document.getString("name");
                Date projectDate = document.getDate("create_date");

                // create project with values set from DB
                Project project = new Project(projectId, projectName, projectDate);

                // add the Project to our HashSet
                projectNameHashSet.add(project);
            }
        } finally {
            cursor.close();
        }

        // adding our HashSet to an ArrayList for sorting
        projectArrayList = sortedProjects(projectNameHashSet);

        return projectArrayList;

    }


    private ArrayList<Project> sortedProjects(Set<Project> projectNameHashSet) {

        orderedArrayList = new ArrayList();

        orderedArrayList.addAll(projectNameHashSet);


        /**
         * Using our Comparator method to sort the list how we choose.
         * Other sorting method options below
         * @see #sortProjectsDateAscending(ArrayList)
         * @see #sortProjectsAlphabetically(ArrayList)
         * @see #sortProjectsById(ArrayList)
         */
        sortProjectsDateDescending(orderedArrayList);


        return orderedArrayList;

    }


    private void sortProjectsDateDescending(ArrayList<Project> list) {
        list.sort((p1, p2) -> {

            if (p1.getDate().before(p2.getDate())) {
                return 1;
            } else if (p1.getDate().after(p2.getDate())) {
                return -1;
            }
            return 0;
        });
    }


    //other sorting options

    private void sortProjectsDateAscending(ArrayList<Project> list) {
        Collections.sort(list, Comparator.comparing(Project::getDate));
    }


    private void sortProjectsAlphabetically(ArrayList<Project> list) {
        Collections.sort(list, new Comparator<Project>() {
            @Override
            public int compare(Project o1, Project o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    private void sortProjectsById(ArrayList<Project> list) {
        Collections.sort(list, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {

                if (p1.getProjectId() > p2.getProjectId()) {
                    return 1;
                } else if (p1.getProjectId() < p2.getProjectId()) {
                    return -1;
                }

                return 0;
            }
        });


    }




    public Project getDbProject(Project project) {
        return project;
    }

}



















