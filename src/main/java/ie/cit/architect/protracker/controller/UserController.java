package ie.cit.architect.protracker.controller;

import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.*;

/**
 * Created by brian on 24/03/17.
 */
public class UserController {

    private static UserController instance;
    private IProject project;



    private UserController() {}


    // singleton
    public static UserController getInstance() {
        if(instance == null) {
            instance = new UserController();
        }
        return instance;
    }


    public Project createProject(String name, String author, String location, String client) {

        return new Project(name, author, location, client);

    }


    public IUser createClientUser(String emailAddress, String password) {
        return new ClientUser(emailAddress, password);
    }


    public IUser createEmployeeUser(String emailAddress, String password) {
        return new EmployeeUser(emailAddress, password);
    }

    public boolean isEmployeeUserEmailValid(String emailAddress) {
        if(emailAddress.equals(Consts.MANAGER_EMAIL_1) || emailAddress.equals(Consts.MANAGER_EMAIL_2)) {
            return true;
        }
        return false;
    }

    public boolean isClientUserEmailValid(String emailAddress) {
        if(emailAddress.equals(Consts.CLIENT_EMAIL_1)
                || emailAddress.equals(Consts.CLIENT_EMAIL_2)
                || emailAddress.equals(Consts.CLIENT_EMAIL_3)) {
            return true;
        }
        return false;
    }

    public boolean isUserPasswordValid(String password) {
        if (password.equals(Consts.USER_PASS)) {
            return true;
        }
        return false;
    }


    public String getProject() {
        return project.getLocation();
    }



}
