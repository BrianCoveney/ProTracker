package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.helpers.SceneUtil;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by brian on 10/02/17.
 */
public class ArchitectMenuScene {


    private BorderPane view;
    private Mediator mediator;


    public ArchitectMenuScene() {
        view = new BorderPane();
        view.setTop(homeButtonContainer());
        view.setCenter(architectMenu());

    }

    public Parent getView() {
        return view;
    }






    private VBox architectMenu() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 0, 20, 20));
        gridPane.setVgap(20);

        Button btn1 = new Button();
        Button btn2 = new Button();
        Button btn3 = new Button();

        List<String> buttonText = Arrays.asList("Create New", "Manage Existing", "View Messages");
        List<Button> buttonList = Arrays.asList(btn1, btn2, btn3);

        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).getStyleClass().add("client_menu_buttons");
            buttonList.get(i).setText(buttonText.get(i));
        }

        for (Button button : buttonList) {
            button.setOnAction(event -> {
                if (event.getSource().equals(btn1)) {
                    Parent view = new CreateNewProjectScene().getView();
                    SceneUtil.changeScene(view);
                    closePreviousStage(event);
                }
                else if (event.getSource().equals(btn2)) {
                    Parent view = new ManageProjectScene().getView();
                    SceneUtil.changeScene(view);
                    closePreviousStage(event);
                }
                else if (event.getSource().equals(btn3)) {
                    Parent view = new ViewMessagesScene().getView();
                    SceneUtil.changeScene(view);
                    closePreviousStage(event);
                }
            });
        }


        Image imageLogo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
        ImageView imageViewLogo = new ImageView(imageLogo);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        gridPane.add(btn1, 0, 1);
        gridPane.add(btn2, 0, 2);
        gridPane.add(btn3, 0, 3);

        vBox.getChildren().addAll(imageViewLogo, gridPane);

        return vBox;
    }

    private void closePreviousStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stagePrev = (Stage) source.getScene().getWindow();
        stagePrev.close();
    }





    public AnchorPane homeButtonContainer() {

        AnchorPane anchorPane = new AnchorPane();

        Button buttonHome = new Button("Log Out");
        buttonHome.setOnAction(event -> {
            try {
                closePreviousStage(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        AnchorPane.setTopAnchor(buttonHome, 10.0);
        AnchorPane.setLeftAnchor(buttonHome, 10.0);
        anchorPane.getChildren().add(buttonHome);

        return anchorPane;
    }





}
