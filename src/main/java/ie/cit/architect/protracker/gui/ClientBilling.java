package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.helpers.PdfInvoice;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Adam on 05/03/2017.
 */
public class ClientBilling
{
    private Mediator mainMediator;

    public ClientBilling (Mediator mainMediator)
    {
        this.mainMediator = mainMediator;
    }

    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        pane.setTop(homeButtonContainer());
        pane.setCenter(createClientBilling());
        pane.setBottom(navButtonContainer());

        Scene scene = new Scene(pane, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + "Billing");
        stage.show();
    }


    private Pane createClientBilling()
    {
        //Account Details
        Label ainfo = new Label("Account Details");
        ainfo.setFont(new Font("Arial", 30));
        Label accNum = new Label ("Account Number: +{account number}");
        Label cName = new Label ("Company Name: +{company name}");
        Label cAddress = new Label("Company Address: +{company address}");
        Label clName = new Label("ClientUser Name: + {client name}");
        Label clAddress = new Label("ClientUser Address: +{client address}");

        VBox vb = new VBox(ainfo, accNum, cName, cAddress, clName, clAddress);
        vb.setSpacing(5);
        vb.setPadding(new Insets(10));
        vb.setAlignment(Pos.TOP_LEFT);

        //PdfInvoice Details
        Label invoice = new Label("PdfInvoice Preview");
        invoice.setFont(new Font("Arial", 30));
        VBox vbIn = new VBox(invoice);
        vbIn.setSpacing(15);
        vbIn.setPadding(new Insets(1));
        vbIn.setAlignment(Pos.TOP_RIGHT);

        HBox hb1 = new HBox(vb, vbIn);
        hb1.setSpacing(150);
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

    public HBox navButtonContainer()
    {
        Button buttonCancel = new Button("Cancel");
        Button buttonContinue= new Button("Continue");
        Button buttonChat = new Button("Chat");
        Button buttonSaveInvoice = new Button("Save PdfInvoice");

        buttonChat.setOnAction(event -> {
            try {
                mainMediator.changeToClientMessages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        buttonSaveInvoice.setOnAction(event -> {
            try {
                try {
                    PdfInvoice.getInstance().createPdfDocument();
                    MessageBox.show("PdfInvoice Saved to Desktop", "Saved");
                }catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

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

        HBox hb2 = new HBox(buttonChat, buttonSaveInvoice);
        hb2.setSpacing(10);
        hb2.setPadding(new Insets(10));
        hb2.setAlignment(Pos.TOP_RIGHT);

        HBox hb = new HBox(buttonCancel, buttonContinue);
        hb.setSpacing(10);
        hb.setPadding(new Insets(10));
        hb.setAlignment(Pos.TOP_RIGHT);

        HBox hb3 = new HBox(hb2, hb);
        hb3.setSpacing(10);
        hb3.setPadding(new Insets(10));
        hb3.setAlignment(Pos.TOP_RIGHT);

        return hb3;
    }
}
