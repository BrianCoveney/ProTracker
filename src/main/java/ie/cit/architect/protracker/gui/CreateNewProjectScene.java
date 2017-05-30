package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.Controller;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.Project;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by brian on 10/02/17.
 */
public class CreateNewProjectScene {

    private static final String FILE_SEP = File.separator;
    private static final String DOUBLE_FILE_SEP = FILE_SEP + FILE_SEP;
    private static final String PATH_TO_DESKTOP = System.getProperty("user.home") + FILE_SEP + "Desktop" + FILE_SEP;
    private String subDirectory;

    private Double editDialogInput;
    private ArrayList<String> directoryArrayList = new ArrayList<>();
    private CheckBox[] checkboxList = new CheckBox[10];
    private TextField tfProjectName = new TextField();
    private TextField tfProjectAuthor = new TextField();
    private TextField tfProjectClient = new TextField();
    private TextField tfProjectLocation;
    private TextField tfProjectFee;
    private String projectName, projectDate, projectAuthor, projectLocation, projectClient;
    private double projectFee;
    private Project project;
    private Mediator mediator;


    public CreateNewProjectScene(Mediator mediator) {
        this.mediator = mediator;
    }

    public CreateNewProjectScene(){}


    public void start(Stage stage) {

        createCheckboxArray();

        Scene scene = new Scene(
                createContainer(createLeftPane(), createMiddlePane(), createRightPane(), createBottomPane()),
                Consts.APP_WIDTH, Consts.APP_HEIGHT);

        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + " Create New");
        stage.show();
    }

    private void getUserInput() {
        projectName = tfProjectName.getText();
        projectDate = String.valueOf(LocalDate.now());
        projectAuthor = tfProjectAuthor.getText();
        projectLocation = tfProjectLocation.getText();
        projectClient = tfProjectClient.getText();
        projectFee = Double.parseDouble(tfProjectFee.getText());
    }


    private static BorderPane createContainer(VBox vb1, VBox vb2, VBox vb3, AnchorPane ap) {
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(vb1);
        borderPane.setCenter(vb2);
        borderPane.setRight(vb3);
        borderPane.setBottom(ap);
        return borderPane;
    }

    private VBox createLeftPane() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_left");
        vBox.setMinWidth(Consts.PANE_WIDTH);

        tfProjectLocation = new TextField();
        tfProjectFee = new TextField();

        ObservableList<TextField> textFieldList =
                FXCollections.observableArrayList(tfProjectName, tfProjectAuthor, tfProjectClient, tfProjectLocation, tfProjectFee);
        List<String> text = Arrays.asList("Project Name", "Name of Author", "Name of ClientUser", "Project Location", "Project Fee");

        for (int i = 0; i < textFieldList.size(); i++) {
            textFieldList.get(i).setPromptText(text.get(i));
        }


        for (TextField textField : textFieldList) {
            textField.setFocusTraversable(false);
            VBox.setMargin(textField, new Insets(0, 37.5, 0, 37.5));
        }


        // Labels
        Label lbProjectName = new Label("Name of project");
        Label lbProjectAuthor = new Label("Name of author");
        Label lbProjectClient = new Label("Name of client");
        Label lbProjectLocation = new Label("Location");
        Label lbProjectFee = new Label("Project Fee");

        List<Label> labelList = Arrays.asList(lbProjectName, lbProjectAuthor, lbProjectClient, lbProjectLocation, lbProjectFee);

        for (Label label : labelList) {
            label.getStyleClass().add("label_padding");
        }

        VBox.setMargin(lbProjectName, new Insets(10, 0, 0, 0));

        Button buttonCreate = new Button("Create Project");
        buttonCreate.getStyleClass().add("label_padding");
        buttonCreate.setOnAction(event -> {
                    createProject();
                    createDirectories();
        });

        VBox.setMargin(buttonCreate, new Insets(10, 37.5, 0, 80));


        // add controls to VBox
        vBox.getChildren().addAll(lbProjectName, tfProjectName, lbProjectAuthor, tfProjectAuthor,
                lbProjectClient, tfProjectClient, lbProjectLocation, tfProjectLocation, lbProjectFee, tfProjectFee, buttonCreate);

        return vBox;
    }


    private VBox createMiddlePane() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_right");
        vBox.setMinWidth(Consts.PANE_WIDTH);
        Label label = new Label("Select folders to create:");
        vBox.getChildren().add(label);

        for (CheckBox checkBox : checkboxList) {
            checkBox.setOnAction(event -> removeDigits(event));
            checkBox.getStyleClass().add("checkbox_padding");
            vBox.getChildren().add(checkBox);
        }
        VBox.setMargin(label, new Insets(20, 0, 0, 0));

        return vBox;
    }


    private void createDirectories() {
        try {
//            String projectName = tfProjectName.getText();
            Path path1 = Paths.get(PATH_TO_DESKTOP + projectName + DOUBLE_FILE_SEP);
            Files.createDirectories(path1);

            if (!projectName.isEmpty()) {
                for (String dir : directoryArrayList) {
                    subDirectory = dir;
                    Path path2 = Paths.get(PATH_TO_DESKTOP + projectName + DOUBLE_FILE_SEP + subDirectory);
                    Files.createDirectories(path2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void removeDigits(ActionEvent event) {
        for (CheckBox checkBox : checkboxList) {
            if (event.getSource().equals(checkBox) && checkBox.isSelected()) {
                String checkboxText = checkBox.getText();
                String numberRemoved = checkboxText.replaceAll("\\d", "");

                directoryArrayList.add(numberRemoved);
            }
        }
    }


    // @link { Ref: http://stackoverflow.com/a/23512831/5942254 }
    private void createCheckboxArray() {
        List<String> text = Arrays.asList(
                "SiteMaps", "ProposedDrawings", "StructuralDrawings", "SupplierDetails",
                "FireDrawings", "Images", "Exports", "Imports", "Documents", "Invoice");

        for (int i = 0; i < checkboxList.length; i++) {
            checkboxList[i] = new CheckBox((i + 1) + " " + text.get(i));
        }
    }


    private VBox createRightPane() {
        Button buttonOpen = new Button("Open");
        buttonOpen.setMinWidth(150);
        buttonOpen.setOnAction(event -> openDocument());

        Button buttonInvoice = new Button("Create Invoice");
        buttonInvoice.setMinWidth(150);
        buttonInvoice.setOnAction(event -> createInvoice());

        VBox vBox = new VBox();
        vBox.getStyleClass().add("hbox_middle");
        vBox.setMinWidth(Consts.PANE_WIDTH);
        vBox.setPadding(new Insets(1));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(100);

        vBox.getChildren().addAll(buttonOpen, buttonInvoice);

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


    /**
     * Button 'Continue' method listener
     * that uses the Controller class create and populate the Project object
     * @see CreateNewProjectScene#createBottomPane
     */
    private void createProject() {

        Date date = new Date();
        date.getTime();

        // values from TextFields stored as strings
        getUserInput();

        project = Controller.getInstance().createProject(
                projectName, projectAuthor, projectLocation, projectClient, projectFee);

        addProjectToDB();

    }


    private void addProjectToDB() {


        try {
            Platform.runLater(() -> {

                if (project != null) {
                    DBController.getInstance().addProject(project);
                }

                DBController.getInstance().saveProject();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void createInvoice() {
        Controller.getInstance().createInvoice(projectName, projectClient, projectFee);
    }




    private void openDocument() {
        File file;
        FileChooser fileChooser = new FileChooser();

        // open created project directory
        fileChooser.setInitialDirectory(
                new java.io.File(PATH_TO_DESKTOP + projectName + DOUBLE_FILE_SEP));

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