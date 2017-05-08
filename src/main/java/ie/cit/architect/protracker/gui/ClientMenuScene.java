package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.helpers.Consts;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by brian on 10/02/17.
 * Modified by Adam on 4/03/17
 * github test change**
 */
public class ClientMenuScene
{
    private Mediator mainMediator;

       /**
     * Each GUI class has a constructor that passes a Mediator object.
     * Within this (and other GUI classes), this mediator reference will pass the selected stage
     * back to the Mediator, which will in turn 'start' that stage.
     * @see #createClientMenu() () -> ...mediator.changeToViewMessagesScene();
     * Reference:
     * @link { http://stackoverflow.com/a/14168529/5942254 }
     */

    public ClientMenuScene(Mediator mainMediator)
    {
        this.mainMediator = mainMediator;
    }

    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        pane.setTop(homeButtonContainer());
        pane.setCenter(createClientMenu());
        Scene scene = new Scene(pane, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + "ClientUser Menu");
        stage.show();
    }

    private Pane createClientMenu()
    {
        Button btnTimeline = new Button("View Project Timeline");
        btnTimeline.setOnAction(event -> {
            try {
                mainMediator.changeToClientTimeline();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button btnBilling = new Button("View Billing");
        btnBilling.setOnAction(event -> {
            try {
                mainMediator.changeToClientBilling();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Button btnMessages = new Button("View Messages");
        btnMessages.setOnAction(event -> {
            try {
                mainMediator.changeToClientMessages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Button btnContact = new Button("Contact Us");
        btnContact.setOnAction(event -> {
            try {
                mainMediator.changeToClientContact();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<Button> buttonList = Arrays.asList(btnTimeline, btnBilling, btnMessages, btnContact);
        for(Button butons : buttonList) {
            butons.getStyleClass().add("client_menu_buttons");
        }

        Image logo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
        ImageView iview1 = new ImageView(logo);

        VBox vb = new VBox(iview1, btnTimeline, btnBilling, btnMessages,btnContact);
        vb.setSpacing(15);
        vb.setPadding(new Insets(1));
        vb.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setCenter(vb);

        return pane;
    }

    public HBox homeButtonContainer()
    {
        Button buttonHome = new Button("Log Out");
        buttonHome.setOnAction(event -> {
            try {
                mainMediator.changeToHomeScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        HBox hb = new HBox(buttonHome);
        hb.setSpacing(450);
        hb.setPadding(new Insets(20));
        hb.setAlignment(Pos.TOP_LEFT);

        return hb;
    }
}

