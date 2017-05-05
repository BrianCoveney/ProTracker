package ie.cit.architect.protracker.controller;

import ie.cit.architect.protracker.gui.CreateNewProjectScene;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.helpers.PdfInvoice;
import ie.cit.architect.protracker.model.*;

import java.io.IOException;

/**
 * Created by brian on 24/03/17.
 */
public class Controller {

    private static Controller instance;
    private IProject project;
    private CreateNewProjectScene createNewProjectScene;


    private PdfInvoice invoice;



    private Controller() {}


    // singleton
    public static Controller getInstance() {
        if(instance == null) {
            instance = new Controller();
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


    public void createInvoice(String projectName, String projectClient, double fee) {
        try {
            PdfInvoice.getInstance().createPdfDocument(projectName, projectClient,  fee);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void editBilling(String projectName, String projectClient, double fee) {

        PdfInvoice.getInstance().editPdf(projectName, projectClient, fee);

    }


}
