package ie.cit.architect.protracker.App;


import com.sun.javafx.application.LauncherImpl;
import ie.cit.architect.protracker.Preloader.preloader;
import ie.cit.architect.protracker.controller.Controller;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.gui.*;
import ie.cit.architect.protracker.model.Project;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Mediator extends Application {

    private HomeScene homeMenuScene;
    private ArchitectMenuScene architectMenuScene;
    private ClientMenuScene clientMenuScene;
    private CreateNewProjectScene createNewProjectScene;
    private ViewMessagesScene viewMessagesScene;
    private ManageProjectScene manageProjectScene;
    private NavigationPane navigationPane;
    private CustomArchitectDialog customArchitectDialog;
    private CustomClientDialog customClientDialog;
    private ViewProjectTimeline viewProjectTimeline;
    private Controller controller;
    private ClientViewProjectTimeline clientViewProjectTimeline;
    private ClientMessages clientMessages;
    private ClientProjectStage clientProjectStage;
    private ClientBilling clientBilling;
    private ClientContact clientContact;
    private Stage primaryStage;

    // Preloader
    private static final int COUNT_LIMIT = 25000;
    private static int stepCount = 1;
    public static String STEP() {
        return stepCount++ + ". ";
    }

    public static void main(String[] args) {
        LauncherImpl.launchApplication(Mediator.class, preloader.class, args);
    }

    @Override
    public void init() throws Exception {
        System.out.println(Mediator.STEP() + "MyApplication#init (doing some heavy lifting), thread: " + Thread.currentThread().getName());

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 0; i < COUNT_LIMIT; i++)
        {
            double progress = (100 * i) / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
    }// end Preloader


    //  Swapping scenes. Ref: http://stackoverflow.com/a/14168529/5942254
    public Mediator(){
        homeMenuScene = new HomeScene(this);
        architectMenuScene = new ArchitectMenuScene(this);
        clientMenuScene = new ClientMenuScene(this);
        createNewProjectScene = new CreateNewProjectScene(this);
        viewMessagesScene = new ViewMessagesScene(this);
        manageProjectScene = new ManageProjectScene(this);
        navigationPane = new NavigationPane(this);
        customArchitectDialog = new CustomArchitectDialog(this);
        customClientDialog = new CustomClientDialog(this);
        viewProjectTimeline = new ViewProjectTimeline(this);
        clientViewProjectTimeline = new ClientViewProjectTimeline(this);
        clientMessages = new ClientMessages(this);
        clientProjectStage = new ClientProjectStage(this);
        clientBilling = new ClientBilling(this);
        clientContact = new ClientContact (this);

        // TODO:
        // - add new scenes
        // - add methods below for those scenes to be opened
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.getIcons().add(new Image(this.getClass().getResource("/icon.png").toString()));
        homeMenuScene.start(primaryStage); // default

        // Database type (MongoDB / MySQL) selected in the controller
        Platform.runLater(() -> DBController.getInstance());
    }


    // methods to change scene
    public void changeToArchitectMenuScene(){
        architectMenuScene.start(primaryStage);
    }

    public void changeToClientMenuScene(){
        clientMenuScene.start(primaryStage);
    }

    public void changeToCreateProjectScene() { createNewProjectScene.start(primaryStage); }

    public void changeToViewMessagesScene() { viewMessagesScene.start(primaryStage); }

    public void changeToManageProjectScene() { manageProjectScene.start(primaryStage); }

    public void changeToArchitectCustomDialog() { customArchitectDialog.signInArchitectDialog(); }

    public void changeToClientCustomDialog() { customClientDialog.signInClientDialog(); }

    public void changeToViewProjectTimeline() { viewProjectTimeline.start(primaryStage); }

    public void changeToHomeScene()  {
        try {
            homeMenuScene.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void changeToClientTimeline() throws Exception { clientViewProjectTimeline.start(primaryStage); }

    public void changeToClientMessages() throws Exception { clientMessages.start(primaryStage);}

    public void changeToClientStage() throws Exception {clientProjectStage.start(primaryStage);}

    public void changeToClientBilling() throws Exception {clientBilling.start(primaryStage);}

    public void changeToClientContact() throws Exception {clientContact.start(primaryStage);}



    // passing values between scenes

    public void passProjectName(String projectName) {
        viewProjectTimeline.setProjectName(projectName);
    }

    public void passProjectNameToClientBilling(String projectName) {
        clientBilling.setProjectName(projectName);
    }

}


















