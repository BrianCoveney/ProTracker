package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.helpers.Consts;
import javafx.animation.PathTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by Adam on 04/03/2017.
 */
public class ClientViewProjectTimeline
{
    private Mediator mainMediator;

    public ClientViewProjectTimeline(Mediator mainMediator)
    {
        this.mainMediator = mainMediator;
    }

    public void start(Stage stage)
    {
        BorderPane pane = new BorderPane();
        pane.setTop(homeButtonContainer());
        pane.setCenter(createProjectTimeline());
        pane.setBottom(createBottomPane());

        Scene scene = new Scene(pane, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        stage.setScene(scene);
        stage.setTitle(Consts.APPLICATION_TITLE + "Project Timeline");
        stage.show();
    }

    private Pane createProjectTimeline()
    {
        Group rootGroup = new Group();
        String projName = ViewProjectTimeline.getClientProjName();
        Label Title = new Label();
        if(projName == null)
        {
            Title.setText("Project Timeline: The architect will update this space");
            Title.setFont(new Font("Arial", 30));
        }
        else
        {
            Title.setText("Project Timeline: " + projName );
            Title.setFont(new Font("Arial", 30));
        }

        VBox vb = new VBox(Title,rootGroup);
        applyAnimation(rootGroup);
        vb.setSpacing(90);
        vb.setPadding(new Insets(1,1,180,1));
        vb.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setCenter(vb);

        return pane;
    }


    //Variable for changing length of completed portion of timeline
    //String stage = "Construction";
    //Adjustable path for shape per point on timeline

    private Path generateStraightPath()
    {
        String stage = ViewProjectTimeline.getAdjustPath();
        final Path path = new Path();
        path.getElements().add(new MoveTo(20,200));
        if(stage=="Design")
        {path.getElements().add(new LineTo(200,200));}
        else if(stage =="Planning")
        {path.getElements().add(new LineTo(400,200));}
        else if(stage =="Tender")
        {path.getElements().add(new LineTo(500,200));}
        else if(stage =="Construction")
        {path.getElements().add(new LineTo(600,200));}
        else if(stage =="Completion")
        {path.getElements().add(new LineTo(700,200));}
        path.setOpacity(1.0);
        return path;
    }

    //Underlying Straight Path
    private Path generateStraightPath2()
    {
        final Path path = new Path();
        path.getElements().add(new MoveTo(20,200));
        path.getElements().add(new LineTo(700,200));
        path.setOpacity(1.0);
        return path;
    }

    private PathTransition generatePathTransition(final Shape shape, final Path path)
    {
        final PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(4.0));
        pathTransition.setDelay(Duration.seconds(0.5));
        pathTransition.setPath(path);
        pathTransition.setNode(shape);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);
        pathTransition.setAutoReverse(true);
        return pathTransition;
    }

    private void applyAnimation(final Group group)
    {
        final Circle circle = new Circle(20, 200, 10);
        circle.setFill(Color.BLUE);
        final Path path = generateStraightPath();
        final Path path2 = generateStraightPath2();
        group.getChildren().add(path);
        group.getChildren().add(path2);
        group.getChildren().add(circle);

        //a
        Label a1 = new Label("Project Begins");
        a1.setTranslateY(170);
        a1.setTranslateX(-10);
        group.getChildren().add(a1);
        group.getChildren().add(new Circle(20, 200, 5));
        //b
        Label a2 = new Label("Design");
        a2.setTranslateY(170);
        a2.setTranslateX(180);
        group.getChildren().add(a2);
        group.getChildren().add(new Circle(200, 200, 5));
        //c
        Label a3 = new Label("Planning");
        a3.setTranslateY(170);
        a3.setTranslateX(380);
        group.getChildren().add(a3);
        group.getChildren().add(new Circle(400, 200, 5));
        //d
        Label a4 = new Label("Tender");
        a4.setTranslateY(170);
        a4.setTranslateX(480);
        group.getChildren().add(a4);
        group.getChildren().add(new Circle(500, 200, 5));
        //e
        Label a5 = new Label("Construction");
        a5.setTranslateY(170);
        a5.setTranslateX(570);
        group.getChildren().add(a5);
        group.getChildren().add(new Circle(600, 200, 5));
        //f
        Label a6 = new Label("Completion");
        a6.setTranslateY(170);
        a6.setTranslateX(670);
        group.getChildren().add(a6);
        group.getChildren().add(new Circle(700, 200, 5));
        final PathTransition transition = generatePathTransition(circle, path);
        transition.play();
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














































