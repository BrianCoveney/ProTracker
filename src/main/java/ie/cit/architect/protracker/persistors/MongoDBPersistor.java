package ie.cit.architect.protracker.persistors;

import com.mongodb.*;
import ie.cit.architect.protracker.model.*;

import java.net.UnknownHostException;

/**
 * Created by brian on 13/03/17.
 */
public class MongoDBPersistor implements IPersistor {

    private MongoClient mongoClientConn;
    private DBCollection tableUsers, tableProjects;
    private DB db;

    private static final String DB_LOCAL_HOST = "localhost";
    private static final String DB_NAME = "protracker";
    private static final int DB_PORT = 27017;


    public MongoDBPersistor() {

        try {

            mongoClientConn = new MongoClient(DB_LOCAL_HOST, DB_PORT);

            if(mongoClientConn != null) {
                System.out.println("Connected to MongoDB!");
            } else {
                System.out.println("Connection to MongoDB Failed!");
            }

            //Get Database
            // if database doesn't exist, mongoDB will create it for you
            db = mongoClientConn.getDB(DB_NAME);

            //Get Collection / Table from 'protracker'
            //if collection doesn't exist, mongoDB will create it for you
            tableUsers = db.getCollection("users");

            // create another table
            tableProjects = db.getCollection("projects");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void writeUsers(UserList users) {

        try {
            for (IUser currUser : users.getUsers()) {
                BasicDBObject document = new BasicDBObject();
                //key value pair
                document.put("email", currUser.getEmailAddress());
                document.put("password", currUser.getPassword());
                tableUsers.insert(document);
            }

        }catch (MongoException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void writeProjects(ProjectList projects) {

        try {
            for (Project currProject : projects.getProjects()) {
                BasicDBObject document = new BasicDBObject();
                document.put("name", currProject.getName());
                document.put("author", currProject.getAuthor());
                document.put("location", currProject.getLocation());
                document.put("client_name", currProject.getClientName());

                tableProjects.insert(document);
            }
        }catch (MongoException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void displayProjects(ProjectList projects) {

        try {
            for (Project currProject : projects.getProjects()) {
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("name", currProject.getName());
                searchQuery.put("author", currProject.getAuthor());
                searchQuery.put("location", currProject.getLocation());
                searchQuery.put("client_name", currProject.getClientName());

                DBCursor cursor = tableProjects.find(searchQuery);

                System.out.println("Project:");
                while (cursor.hasNext()){
                    System.out.println(cursor.next());
                }
            }
        }catch (MongoException e) {
            e.printStackTrace();
        }

    }


    // not called
    @Override
    public User selectRecords() {
        return null;
    }
}



























