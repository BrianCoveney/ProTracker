package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.controller.PersistenceMode;
import ie.cit.architect.protracker.helpers.Consts;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class HomeScene {

    private Mediator mediator;
    private GridPane view;


    // Composition - passing a reference of Mediator to HomeScene's constructor. Now HomeScene 'has-a' Mediator
    public HomeScene(Mediator mediator) {
        this.mediator = mediator;
    }


    public void start(Stage stage) throws Exception {

        DBController.getInstance().setPersistenceMode(PersistenceMode.MONGODB);

        Scene scene = new Scene(createHomeMenu(), Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE);
        stage.show();
    }




    private void closePreviousStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stagePrev = (Stage) source.getScene().getWindow();
        stagePrev.close();
    }


    private GridPane createHomeMenu() {
        Button buttonSignInClient = new Button("Sign in Client");
        Button buttonSignInArchitect = new Button("Sign in Architect");
        buttonSignInClient.setOnAction(event -> {
            mediator.changeToClientCustomDialog();
//            closePreviousStage(event);
        });
        buttonSignInArchitect.setOnAction(event -> {
            mediator.changeToArchitectCustomDialog();
//            closePreviousStage(event);
        });

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 0, 20, 20));
        gridPane.setVgap(20);

        Image logo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
        ImageView iview1 = new ImageView(logo);

        Label labelSubTitle = new Label("Welcome");
        //labelSubTitle.setId("Welcome");
        labelSubTitle.getStyleClass().add("label_subtitle");

        buttonSignInClient.setMinWidth(150);
        buttonSignInArchitect.setMinWidth(150);

        gridPane.add(iview1, 0, 1);
        GridPane.setHalignment(iview1, HPos.CENTER);

        gridPane.add(labelSubTitle, 0, 2);
        GridPane.setHalignment(labelSubTitle, HPos.CENTER);

        gridPane.add(buttonSignInClient, 0, 5);
        GridPane.setHalignment(buttonSignInClient, HPos.CENTER);

        gridPane.add(buttonSignInArchitect, 0, 6);
        GridPane.setHalignment(buttonSignInArchitect, HPos.CENTER);

        return gridPane;
    }

}
