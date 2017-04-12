package ie.cit.architect.protracker.persistors;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import ie.cit.architect.protracker.helpers.Credentials;
import ie.cit.architect.protracker.model.*;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;


/**
 * Created by brian on 13/03/17.
 */
public class MongoDBPersistor implements IPersistor {

    private MongoCollection collectionUsers, collectionProjects;
    private MongoDatabase database;

    private ArrayList<Project> orderedArrayList;
    private String DB_NAME = "protracker";




    public MongoDBPersistor() {

        try {

            //local database
//            MongoClient mongoClientConn = new MongoClient("localhost", 27017);

            // remote database
            String mongoURI = "mongodb://" + Credentials.DB_MONGO_USER + ":" + Credentials.DB_MONGO_PASS + "@" +
                    Credentials.DB_MONGO_IP +"/" + Credentials.DB_NAME;

            MongoClient mongoClientConn = new MongoClient( new MongoClientURI(mongoURI));


            if(mongoClientConn != null) System.out.println("Connected to MongoDB!");
            else System.out.println("Connection to MongoDB Failed!");


            //Get Database
            // if database doesn't exist, mongoDB will create it for you
            database = mongoClientConn.getDatabase(DB_NAME);


            //Get Collection / Table from 'protracker'
            //if collection doesn't exist, mongoDB will create it for you
            collectionUsers = database.getCollection("users");

            // create another table
            collectionProjects = database.getCollection("projects");


        } catch (MongoException e) {
            e.printStackTrace();
        }
    }


    // for JUnit tests
    public User getDbUser(User user) {
        return user;
    }

    public Project getDbProject(Project project) {
        return project;
    }


    @Override
    public void writeUsers(UserList users) {
        Document document = new Document();
        try {
            for (IUser currUser : users.getUsers()) {
                document.put("email", currUser.getEmailAddress());
                document.put("password", currUser.getPassword());
            }

//            runLater(() -> collectionUsers.insertOne(document));
            collectionUsers.insertOne(document);

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


                //A unique index ensures that the indexed fields do not store duplicate values
                // https://docs.mongodb.com/v3.2/core/index-unique/
//                Document indexOption = new Document();
//                indexOption.put("unique", true);
            }

            collectionProjects.insertOne(document);

        }catch (MongoException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void deleteProject(String projectName) {
        collectionProjects.deleteOne(eq("name", projectName));
    }


    @Override
    public void updateProject(String currentProjectName, String upDatedProjectName) {
        collectionProjects.updateOne(eq("name", currentProjectName),
                new Document("$set", new Document("name", upDatedProjectName)));
    }


    @Override
    public ArrayList<Project> getProjectNameList() {

        Set<Project> projectNameHashSet = new HashSet<>();
        ArrayList<Project> projects;

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

        projects = sortedProjects(projectNameHashSet);

        return projects;

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

}



















