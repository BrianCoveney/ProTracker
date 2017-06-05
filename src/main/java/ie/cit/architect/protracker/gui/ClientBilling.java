package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.model.Project;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.net.URL;

/**
 * Created by Adam on 05/03/2017
 * Edited by Brian on 05/06/2017
 * Pdf Preview via PDF.js
 *
 * Please Note: the PDF viewer displays correctly on my Windows PC, but is not displaying on a Linux PC - adammanleykelly
 */
public class ClientBilling
{
    private Mediator mainMediator;
    private ViewProjectTimeline viewProjectTimeline;
    private static String projectName;
    private String projectClientName;
    private double projectFee;


    public ClientBilling (Mediator mainMediator)
    {
        this.mainMediator = mainMediator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        pane.setTop(homeButtonContainer());
        pane.setCenter(createClientBilling());
        pane.setBottom(createBottomPane());

        Scene scene = new Scene(pane, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + "Billing");
        stage.show();
    }


    private Pane createClientBilling()
    {
        Project project = DBController.getInstance().readProjectDetails(getProjectName());
        projectFee = project.getFee();
        projectClientName = project.getClientName();

        Label status = new Label();
        status.setManaged(false);
        status.setVisible(false);
        if(getProjectName() ==null && projectClientName ==null&& projectFee == 0.0)
        {
            status.setText("The architect will update this space");
            status.setManaged(true);
            status.setVisible(true);
        }

        Label cName = new Label();
        Label ainfo = new Label("Account Details");
        ainfo.setFont(new Font("Arial", 30));

        cName.setText("Client Name: " + projectClientName);
        Label pName = new Label ("Project Name: " + getProjectName());
        Label cFee = new Label("Currernt Fee: "+ projectFee);

        VBox vb = new VBox(ainfo,status, cName, pName, cFee);
        vb.setSpacing(5);
        vb.setPadding(new Insets(10,0,0,10));
        vb.setAlignment(Pos.TOP_LEFT);
        vb.setId("whitebackground");

        // Create our browser which contains the Google Map
        ClientBilling.Browser browser = new ClientBilling.Browser();

        //we need a StackPane to customise our browser's size
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(browser);
        stackPane.setPrefHeight(300);
        stackPane.setPrefWidth(500);

        VBox vbPdf = new VBox(stackPane);
        vbPdf.setSpacing(10);
        vbPdf.setPadding(new Insets(1));
        vbPdf.setAlignment(Pos.TOP_RIGHT);

        VBox vb2 = new VBox(vb);
       // vb2.setSpacing(5);
        //vb2.setPadding(new Insets(10));
        vb2.setAlignment(Pos.TOP_LEFT);

        HBox hb1 = new HBox(vb2, vbPdf);
        hb1.setSpacing(90);
        hb1.setPadding(new Insets(1));
        hb1.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setCenter(hb1);

        return pane;
    }

    public HBox homeButtonContainer()
    {
        Button buttonHome = new Button("Home");
        Image logo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
        ImageView iview1 = new ImageView(logo);
        iview1.setFitWidth(236.25);
        iview1.setFitHeight(62.5);

        buttonHome.setOnAction(event -> {
            try {
                mainMediator.changeToClientMenuScene();
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

        Button buttonCancel = new Button("Cancel");
        Button buttonContinue = new Button("Continue");
        Button buttonChat = new Button("Chat");
        Button buttonSaveInvoice = new Button("Save PdfInvoice");


        buttonCancel.setOnAction(event -> {
            try {
                mainMediator.changeToClientMenuScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttonContinue.setOnAction(event -> {
            try {
                mainMediator.changeToClientMenuScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        buttonChat.setOnAction(event -> {
            try {
                mainMediator.changeToClientMessages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // layout
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStyleClass().add("anchorpane_color");
        AnchorPane.setTopAnchor(buttonCancel, 10.0);
        AnchorPane.setBottomAnchor(buttonCancel, 10.0);
        AnchorPane.setRightAnchor(buttonCancel, 150.0);

        AnchorPane.setBottomAnchor(buttonContinue, 10.0);
        AnchorPane.setRightAnchor(buttonContinue, 10.0);

        AnchorPane.setBottomAnchor(buttonChat, 10.0);
        AnchorPane.setRightAnchor(buttonChat, 280.0);

        AnchorPane.setBottomAnchor(buttonSaveInvoice, 10.0);
        AnchorPane.setRightAnchor(buttonSaveInvoice, 410.0);

        anchorPane.getChildren().addAll(buttonCancel, buttonContinue, buttonChat, buttonSaveInvoice);

        return anchorPane;
    }

    //Pdf Viewer via pdf.js
    public class Browser extends StackPane
    {
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        public Browser() {

            getStyleClass().add("browser");
            final URL urlPdf = getClass().getResource("/pdf.html");
            webEngine.load(urlPdf.toExternalForm());
            getChildren().add(browser);
        }
    }
}
