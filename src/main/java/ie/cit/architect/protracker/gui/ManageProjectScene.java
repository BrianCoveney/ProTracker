package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.Project;
import ie.cit.architect.protracker.persistors.MongoDBPersistor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by brian on 27/02/17.
 */
public class ManageProjectScene {


    private String projectName;
    private static final String CURR_DIR = "src/main/resources";
    private static final String TXT_FILE_DESC = "txt files (*.txt)";
    private static final String PDF_FILE_DESC = "PdfInvoice files (*.PdfInvoice)";
    private static final String TXT_FILE_EXT = "*.txt";
    private static final String PDF_FILE_EXTENSION = "*.PdfInvoice";
    private VBox vBoxMiddlePane;
    private ObservableList<CheckBox> checkBoxList;
    private ObservableList<Label> labelList;
    private Button buttonDelete, buttonRename;
    private Label labelDate;
    private String editDialogInput;
    private HBox hBoxProject;


    private Mediator mediator;

    public ManageProjectScene(Mediator mediator) {
        this.mediator = mediator;
        checkBoxList = FXCollections.observableArrayList();
        labelList = FXCollections.observableArrayList();
    }


    public void start(Stage stage) {

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(createLeftPane());
        borderPane.setCenter(createMiddlePane());
        borderPane.setRight(createRightPane());
        borderPane.setBottom(createBottomPane());

        int sceneWidth = 1000;
        Scene scene = new Scene(
                borderPane,
                sceneWidth, Consts.APP_HEIGHT);

        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + " Manage project");
        stage.show();
    }


    private VBox createRightPane() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_left");
        vBox.setMinWidth(Consts.PANE_WIDTH);

        Button buttonOpen = new Button("Open");
        buttonOpen.setOnAction(event -> openDocument());
        Button buttonViewStage = new Button("View Stage");
        buttonViewStage.setOnAction(event -> mediator.changeToViewTimelineScene());


        buttonRename = new Button("Rename");
        buttonRename.setOnAction(event -> updateNameDialog());
        buttonRename.setDisable(true);

        buttonDelete = new Button("Delete");
        buttonDelete.setOnAction(event -> deleteProject());

        ObservableList<Button> buttonList =
                FXCollections.observableArrayList(buttonOpen, buttonViewStage, buttonRename, buttonDelete);

        for (Button button : buttonList) {
            button.setFocusTraversable(false);
            button.setMinWidth(150);
            VBox.setMargin(button, new Insets(0, 37.5, 50, 37.5));
        }

        Label labelOperations = new Label("Operations:");

        VBox.setMargin(labelOperations, new Insets(30, 0, 50, 10));

        // add controls to VBox
        vBox.getChildren().addAll(labelOperations, buttonOpen, buttonViewStage, buttonRename, buttonDelete);

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
     * @see MongoDBPersistor#readProjects()
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

        getProjectName();

    }



    private Project getProjectName() {
        for(CheckBox checkBox : checkBoxList) {
            checkBox.setOnAction(event -> {
                buttonRename.setDisable(false);
                projectName =  checkBox.getText();
            });

            removeControlsFromScrollPane();
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
            System.out.println(editDialogInput);
        }

        editProjectName();


        vBoxMiddlePane.getChildren().clear();

        addProjectsToMiddlePane();
    }


    // Update
    private void editProjectName() { DBController.getInstance().updateProjectName(projectName, editDialogInput); }


    // Delete
    // 'deleteButton' listener which calls the UserController to remove the selected project from the database
    private void deleteProject() {
        DBController.getInstance().deleteProject(getProjectName());
    }




    private VBox createLeftPane() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_right");
        vBox.setMinWidth(225);

        Label label = new Label("Search Projects:");
        VBox.setMargin(label, new Insets(30, 0, 20, 10));


        TextField textField = new TextField();

        VBox.setMargin(textField, new Insets(0, 37.5, 0, 37.5));


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


    private void openDocument() {

        File file;

        FileChooser fileChooser = new FileChooser();

//        FileChooser.ExtensionFilter extensionFilterText =
//                new FileChooser.ExtensionFilter(TXT_FILE_DESC, TXT_FILE_EXT);

        FileChooser.ExtensionFilter extensionFilterText =
                new FileChooser.ExtensionFilter(PDF_FILE_DESC, PDF_FILE_EXTENSION);


        fileChooser.getExtensionFilters().addAll(extensionFilterText);

        // open my resource directory, which contains the text files.
        fileChooser.setInitialDirectory(
                new java.io.File(CURR_DIR));

        file = fileChooser.showOpenDialog(null);

        if (file != null) {
            openFile(file);
        }
    }


    // solves JavaFX Freezing on Desktop.open(file) Ref: http://stackoverflow.com/a/34429067/5942254
    private void openFile(File file) {

        if (Desktop.isDesktopSupported()) {

            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();

        }

    }
}
