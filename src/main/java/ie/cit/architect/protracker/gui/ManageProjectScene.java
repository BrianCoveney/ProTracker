package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.Controller;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.Project;
import ie.cit.architect.protracker.persistors.MongoDBPersistor;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by brian on 27/02/17.
 */
public class ManageProjectScene {



    private String projectName, selectedProjectName, selectedProjectClient;
    private double selectedProjectFee, doubleDialogInput;
    private static final String FILE_SEP = File.separator;
    private static final String DOUBLE_FILE_SEP = FILE_SEP + FILE_SEP;
    private static final String PATH_TO_DESKTOP = System.getProperty("user.home") + FILE_SEP + "Desktop" + FILE_SEP;
    private VBox vBoxMiddlePane;
    private ObservableList<CheckBox> checkBoxList;
    private ObservableList<Label> labelList;
    private Button buttonDelete, buttonRename, buttonDesign;
    private Label labelDate;
    private String editDialogInput;
    private HBox hBoxProject;
    private Stage window;
    private static String selectedProject;


    private Mediator mediator;

    public ManageProjectScene(Mediator mediator) {
        this.mediator = mediator;
        checkBoxList = FXCollections.observableArrayList();
        labelList = FXCollections.observableArrayList();
    }


    public ManageProjectScene() { }


    public void start(Stage stage) {

        window = stage;

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(createLeftPane());
        borderPane.setCenter(createMiddlePane());
        borderPane.setRight(createRightPane());
        borderPane.setBottom(createBottomPane());

        Scene scene = new Scene(
                borderPane,
                Consts.APP_WIDTH + 50, Consts.APP_HEIGHT);

        scene.getStylesheets().add("/stylesheet.css");
        window.setScene(scene);
        window.setTitle(Consts.APPLICATION_TITLE + " Manage project");
        window.show();
    }




    private VBox createRightPane() {
        VBox vBox = new VBox();


        Button changeFee = new Button("Change Fee");
        changeFee.setOnAction(event -> {
            selectedProject();
            editBilling();
        });;


        Button buttonViewStage = new Button("View Stage");
        buttonViewStage.setOnAction(event -> {
            selectedProject = projectName;
            Platform.runLater(() ->{
                    mediator.changeToViewProjectTimeline();

            });
         });


        buttonRename = new Button("Rename");
        buttonRename.setOnAction(event -> updateNameDialog());
        buttonRename.setDisable(true);

        buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> {
            deleteProject();
            removeControlsFromScrollPane();
        });

        ObservableList<Button> buttonList =
                FXCollections.observableArrayList(changeFee, buttonViewStage, buttonRename, buttonDelete);

        for (Button button : buttonList) {
            button.setFocusTraversable(false);
            button.setMinWidth(150);
            VBox.setMargin(button, new Insets(0, 37.5, 50, 37.5));
        }

        Label labelOperations = new Label("Operations:");

        VBox.setMargin(labelOperations, new Insets(10, 0, 50, 10));

        // add controls to VBox
        vBox.getChildren().addAll(labelOperations, changeFee, buttonViewStage, buttonRename, buttonDelete);
        return vBox;
    }



    private ScrollPane createMiddlePane() {

        vBoxMiddlePane = new VBox();
        vBoxMiddlePane.getStyleClass().add("hbox_middle");
        int paneWidth = 300;
        vBoxMiddlePane.setMinWidth(paneWidth);

        Label labelName = new Label("Name");
        Label labelDateModified = new Label("Date Modified");
        HBox.setMargin(labelName, new Insets(10, 0, 0, 35));
        HBox.setMargin(labelDateModified, new Insets(10, 0, 0, 135));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(labelName, labelDateModified);
        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox, vBoxMiddlePane);

        addProjectsToMiddlePane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);

        return scrollPane;
    }




    /**
     * CheckBoxes populated with the project 'name' field from MongoDB
     * @see DBController#selectRecords()
     * @see MongoDBPersistor#createProjectList()
     */
    private void addProjectsToMiddlePane() {
        ArrayList<Project> projectArrayList = DBController.getInstance().selectRecords();

        for(Project project : projectArrayList) {

            CheckBox checkBox = new CheckBox(project.getName());
            checkBoxList.add(checkBox);

            labelDate = new Label(project.getFormattedDate());
            labelList.add(labelDate);

            hBoxProject = new HBox();
            labelDate.getStyleClass().add("label_padding");
            checkBox.getStyleClass().add("checkbox_padding");
            hBoxProject.getChildren().addAll(checkBox, labelDate);
            vBoxMiddlePane.getChildren().add(hBoxProject);
        }
        getProjectByName();
    }


    public String getProjectName() {
            for (CheckBox checkBox : checkBoxList) {
                checkBox.setOnAction(event -> {
                    projectName = checkBox.getText();
                });
            }
        return projectName;
    }


    // to be used in ViewProjectTimeline scene
    public String getSelectedProject() {
        return selectedProject;
    }


    public Project selectedProject() {

        String name = getProjectName();

        // read the checkbox selected project from the db
        Project project = DBController.getInstance().readProjectDetails(name);

        selectedProjectName = project.getName();
        selectedProjectClient = project.getClientName();
        selectedProjectFee = project.getFee();

        return project;
    }



    private void editBilling() {

        double newFee =  updateFeeDialog();

        Controller.getInstance().createInvoice(selectedProjectName, selectedProjectClient, selectedProjectFee);

        Controller.getInstance().editBilling(selectedProjectName, selectedProjectClient, newFee);

        DBController.getInstance().updateProjectFee(selectedProjectFee, newFee);

    }


    public Double updateFeeDialog() {
        javafx.scene.control.Dialog dialog = new TextInputDialog();
        dialog.setTitle("Project Fee");
        dialog.setHeaderText("Enter the new project fee");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            doubleDialogInput = Double.valueOf(result.get());
        }
        return doubleDialogInput;
    }



    private Project getProjectByName() {
        for(CheckBox checkBox : checkBoxList) {
            checkBox.setOnAction(event -> {
                projectName =  checkBox.getText();
                buttonRename.setDisable(false);

            });
        }
        Project project = new Project();
        project.setName(projectName);

        return project;
    }


    private void removeControlsFromScrollPane() {

        for (int i = 0; i < checkBoxList.size(); i++) {
            if(checkBoxList.get(i).isSelected()) {
                checkBoxList.get(i).setVisible(false);
                checkBoxList.get(i).setManaged(false);
                labelList.get(i).setVisible(false);
                labelList.get(i).setManaged(false);
            }
        }
    }


    private void updateNameDialog() {
        Dialog dialog = new TextInputDialog();
        dialog.setTitle("Edit Project Name");
        dialog.setHeaderText("Enter the new project name");

        Optional<String> result = dialog.showAndWait();

        if(result.isPresent()) {
            editDialogInput = result.get();
        }

        editProjectName();

        vBoxMiddlePane.getChildren().clear();
        addProjectsToMiddlePane();
    }


    // Update
    private void editProjectName() { DBController.getInstance().updateProjectName(projectName, editDialogInput); }




    // Delete
    // 'deleteButton' listener which calls the Controller to remove the selected project from the database
    private void deleteProject() {
        DBController.getInstance().deleteProject(getProjectByName());

        File file = new File(PATH_TO_DESKTOP + FILE_SEP + projectName);
        deleteDir(file);
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }



    private VBox createLeftPane() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_right");


        Label label = new Label("Search Projects:");
        VBox.setMargin(label, new Insets(10, 0, 20, 10));

        TextField textField = new TextField();

        VBox.setMargin(textField, new Insets(10, 10, 0, 10));

        vBox.getChildren().addAll(label, textField);

        return vBox;
    }


    private AnchorPane createBottomPane() {

        Button buttonContinue = new Button("Continue");
        buttonContinue.setOnAction(event -> {
            mediator.changeToArchitectMenuScene();
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> mediator.changeToArchitectMenuScene());

        // layout
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("anchorpane_color");
        AnchorPane.setTopAnchor(buttonCancel, 10.0);
        AnchorPane.setBottomAnchor(buttonCancel, 10.0);
        AnchorPane.setRightAnchor(buttonCancel, 150.0);
        AnchorPane.setBottomAnchor(buttonContinue, 10.0);
        AnchorPane.setRightAnchor(buttonContinue, 10.0);

        anchorPane.getChildren().addAll(buttonCancel, buttonContinue);

        return anchorPane;
    }




}