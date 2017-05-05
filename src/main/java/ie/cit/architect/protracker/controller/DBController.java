package ie.cit.architect.protracker.controller;

import ie.cit.architect.protracker.model.*;
import ie.cit.architect.protracker.persistors.IPersistor;
import ie.cit.architect.protracker.persistors.MongoDBPersistor;

import java.util.ArrayList;

import static ie.cit.architect.protracker.controller.PersistenceMode.MONGODB;
import static ie.cit.architect.protracker.controller.PersistenceMode.MYSQL;

/**
 * Created by brian on 3/3/2017.
 */
public class DBController {

    private static DBController instance;
    private PersistenceMode persistenceMode;
    private IPersistor persistor;
    private EmployeeList employeeList;
    private ClientList clientList;
    private ProjectList projectList;
    private Project project;
    private ChatMessage chatMessage;


    private DBController() {
        this.employeeList = new EmployeeList();
        this.clientList = new ClientList();
        this.projectList = new ProjectList();
        this.project = new Project();
        this.chatMessage = new ChatMessage();
    }


    public static DBController getInstance() {
        if (instance == null) {
            instance = new DBController();
        }
        return instance;
    }

    public void setPersistenceMode(PersistenceMode mode) {
        if(mode.equals(MYSQL)) {
//            this.persistor = new MySQLPersistor();
        }
        else if(mode.equals(MONGODB)) {
            this.persistor = new MongoDBPersistor();
        }
    }

    public void setPersistor(IPersistor persistor) {
        this.persistor = persistor;
    }



    public void addUser(String email, String password) {

        IUser employeeUser =
                UserController.getInstance().createEmployeeUser(email, password);

        this.employeeList.add(employeeUser);
    }


    public ClientUser addUser(ClientUser user) {
        this.clientList.add(user);
        return user;
    }

    public void saveEmployeeUser() {
        this.persistor.writeEmployeeUsers(this.employeeList);
    }

    public void saveClientUser() {
        this.persistor.writeClientUsers(this.clientList);
    }

    // projects
    public void addProject(Project project) {
        this.projectList.add(project);
    }

    public void saveProject() {
        this.persistor.writeProjects(this.projectList);
    }


    public ArrayList<Project> selectRecords() {
        return this.persistor.readProjects();
    }


    public void deleteProject(Project project) {
        this.persistor.deleteProject(project);
    }


    public Project updateProjectName(String currentProjectName, String upDatedProjectName) {

        for(IProject project : this.projectList.getProjects()) {
            if(project.getName().equals(currentProjectName)) {
                project.setName(upDatedProjectName);
            }
        }

        return this.persistor.updateProject(currentProjectName,  upDatedProjectName);
    }

    public void saveMessage(ChatMessage chatMessage) {
        this.persistor.writeMessages(chatMessage);
    }

    public ChatMessage readMessage() {
        ChatMessage chatM = this.persistor.readMessage();
        return chatM;
    }

}























