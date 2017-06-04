package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.ChatMessage;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * Created by brian on 27/02/17.
 */
public class ViewMessagesScene {


    /**
     * Redesign of scene swapping. I had been using the Mediator, but this
     * requires the start(...) method to be in each class.
     * I've refactored the design so that each individual component are
     * not application sub classes, but just plain old regular classes.
     * @link { https://stackoverflow.com/a/32465949/5942254 }
     */
    private BorderPane view;

    public ViewMessagesScene() {
        view = new BorderPane();
        view.setTop(homeButtonContainer());
        view.setCenter(MessagesPane());
        view.setBottom(createBottomPane());
    }

    public Parent getView() {
        return view;
    }


    private Pane MessagesPane() {
        BorderPane pane = new BorderPane();
        pane.setRight(createRightPane());
        pane.setLeft(createLeftPane());
        return pane;
    }



    private VBox createLeftPane() {
        VBox vBox = new VBox();
        vBox.setMinWidth(Consts.PANE_WIDTH);

        Label label = new Label("Incoming Chat Message:");

        TextArea textArea = new TextArea();
        textArea.setMaxWidth(200);
        textArea.setPrefHeight(200);
        textArea.setWrapText(true);

        // read chat message from the message saved in Client Scene
        // and have the ChatMessage object reference it
        ChatMessage chatMessage = DBController.getInstance().readMessage();

        // set the TextArea with this reference
        textArea.setText(chatMessage.getMessage());

        VBox.setMargin(label, new Insets(30, 0, 0, 40));
        VBox.setMargin(textArea, new Insets(0, 0, 0, 40));

        vBox.getChildren().addAll(label, textArea);

        return vBox;
    }

    private VBox createRightPane() {
        VBox vBox = new VBox();
        vBox.setMinWidth(Consts.PANE_WIDTH);

        Label label = new Label("Compose Chat Message:");

        TextArea textArea = new TextArea();
        textArea.setMaxWidth(200);
        textArea.setPrefHeight(200);
        textArea.setWrapText(true);

        Button sendButton = new Button("Send");


        sendButton.setOnAction(event -> {

            String input = textArea.getText();

            // create ChatMessage object with TextArea input as its message
            ChatMessage message = new ChatMessage(input);

            // save the message in MongoDB
            DBController.getInstance().saveMessage(message);

        });


        VBox.setMargin(label, new Insets(30, 0, 0, 40));
        VBox.setMargin(textArea, new Insets(0, 0, 0, 40));
        VBox.setMargin(sendButton, new Insets(0, 0, 0, 40));


        vBox.getChildren().addAll(label, textArea, sendButton);

        return vBox;
    }

    public HBox homeButtonContainer() {
        Button buttonHome = new Button("Home");
        Image logo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
        ImageView iview1 = new ImageView(logo);
        iview1.setFitWidth(236.25);
        iview1.setFitHeight(62.5);

        buttonHome.setOnAction(event -> {
            try {

                changeToArchitectMenu(buttonHome);

                closePreviousStage(event);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        HBox hb = new HBox(buttonHome);
        hb.setSpacing(100);
        hb.setPadding(new Insets(10));
        hb.setAlignment(Pos.TOP_LEFT);

        HBox hb1 = new HBox(iview1);
        hb1.setSpacing(100);
        hb1.setPadding(new Insets(10));
        hb1.setAlignment(Pos.TOP_RIGHT);

        HBox hb2 = new HBox(hb, hb1);
        hb2.setSpacing(450);
        hb2.setPadding(new Insets(10));
        hb2.setAlignment(Pos.TOP_CENTER);

        return hb2;
    }


    private AnchorPane createBottomPane() {

        Button buttonContinue = new Button("Continue");
        buttonContinue.setOnAction(event -> {
            changeToArchitectMenu(buttonContinue);
            closePreviousStage(event);
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> {
            changeToArchitectMenu(buttonCancel);
            closePreviousStage(event);
        });

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


    private void changeToArchitectMenu(Button button) {
        Parent view = new ArchitectMenuScene().getView();
        Scene scene = new Scene(view, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        Stage stage = new Stage();
        stage.initOwner(button.getScene().getWindow());
        stage.setTitle(Consts.APPLICATION_TITLE + " View Messages");
        stage.setScene(scene);
        stage.show();
    }


    private void closePreviousStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stagePrev = (Stage) source.getScene().getWindow();
        stagePrev.close();
    }


}
