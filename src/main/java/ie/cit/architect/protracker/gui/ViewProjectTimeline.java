package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.Controller;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.helpers.Consts;
import ie.cit.architect.protracker.helpers.SceneUtil;
import ie.cit.architect.protracker.model.Project;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by brian on 30/05/17.
 */
public class ViewProjectTimeline {

    private static String projectStage;

    private static String projectName;
    private String projClientName;
    private double yValueDesignFee;
    private double yValuePlanningFee;
    private double yValueTenderFee;
    private double yValueConstructionFee;
    private Mediator mediator;
    private BorderPane view;

    public ViewProjectTimeline(Mediator mediator) {
        this.mediator = mediator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }



    public ViewProjectTimeline(){
        view = new BorderPane();
        view.setBottom(createBottomPaneTimLine());
        view.setCenter(createBarChart());
        view.setLeft(createLeftBillingPane());
    }

    public Parent getView() {
        return view;
    }




    private Group createBarChart() {

        //** find the project in the database using the project name
        Project project = DBController.getInstance().readProjectDetails(getProjectName());

        double projectFee = project.getFee();
        projClientName = project.getClientName();

        Mediator mediator = new Mediator();
        // Pass the selected project name to ClientBilling Scene,
        // with help from the Mediator class
        mediator.passProjectNameToClientBilling(getProjectName());


        // define the X Axis
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setTickLabelRotation(90);
        xAxis.setCategories(FXCollections.observableArrayList(
                Arrays.asList(Consts.DESIGN, Consts.PLANNING, Consts.TENDER, Consts.CONSTRUCTION)));


        int lowerBound = 0;
        double upperBound = projectFee / 2;
        double unitTick = projectFee / 10;


        // define the Y Axis
        NumberAxis yAxis = new NumberAxis(lowerBound, upperBound, unitTick);
        yAxis.setLabel("Fee");


        // create the Bar chart
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Project: " + project.getName() + " | Fee: " + projectFee + " | Client: " +  project.getClientName());


        //Prepare XYChart.Series
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        XYChart.Series<String, Number> series4 = new XYChart.Series<>();

        series1.setName(Consts.DESIGN);
        series2.setName(Consts.PLANNING);
        series3.setName(Consts.TENDER);
        series4.setName(Consts.CONSTRUCTION);


        yValueDesignFee = projectFee * 0.4;
        yValuePlanningFee = projectFee * 0.2;
        yValueTenderFee = projectFee * 0.1;
        yValueConstructionFee = projectFee * 0.3;




        // Timeline Animation of the project progress, which is displayed as a percentage on the Y-Axis.
        // The animation is run only once - when the user enters the scene.
        Timeline tl = new Timeline();
        tl.getKeyFrames().add(new KeyFrame(Duration.millis(500),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        // set the XYChart.Series objects data
                        series1.getData().add(new XYChart.Data<>(Consts.DESIGN, yValueDesignFee));

                        series2.getData().add(new XYChart.Data<>(Consts.PLANNING, yValuePlanningFee));

                        series3.getData().add(new XYChart.Data<>(Consts.TENDER, yValueTenderFee));

                        series4.getData().add(new XYChart.Data<>(Consts.CONSTRUCTION, yValueConstructionFee));
                    }
                }));

        tl.play();

        barChart.getData().addAll(series1, series2, series3, series4);

        //Creating a Group object
        Group groupBarChart = new Group(barChart);

        return groupBarChart;
    }


    private VBox createLeftBillingPane() {
        VBox vBox = new VBox();
        Button buttonDesign = new Button("Design Invoice");
        Button buttonPlanning = new Button("Planning Invoice");
        Button buttonTender = new Button("Tender Invoice");
        Button buttonConstruction = new Button("Construction Invoice");

        List<Button> buttonList = new ArrayList<>(Arrays.asList(buttonDesign, buttonPlanning, buttonTender, buttonConstruction));
        for(Button button : buttonList) {
            button.setMinWidth(175);
        }

        buttonDesign.setOnAction(event -> {
            setAdjustPath("Design");
            createInvoice(projectName, projClientName, yValueDesignFee);
        });

        buttonPlanning.setOnAction(event -> {
            setAdjustPath("Planning");
            createInvoice(projectName, projClientName, yValuePlanningFee);
        });
        buttonTender.setOnAction(event -> {
            setAdjustPath("Tender");
            createInvoice(projectName, projClientName, yValueTenderFee);
        });
        buttonConstruction.setOnAction(event -> {
            setAdjustPath("Construction");
            createInvoice(projectName, projClientName, yValueConstructionFee);
        });

        VBox.setMargin(buttonDesign, new Insets(100, 37.5, 0, 37.5));
        VBox.setMargin(buttonPlanning, new Insets(30, 37.5, 0, 37.5));
        VBox.setMargin(buttonTender, new Insets(30, 37.5, 0, 37.5));
        VBox.setMargin(buttonConstruction, new Insets(30, 37.5, 0, 37.5));
        vBox.getChildren().addAll(buttonDesign, buttonPlanning, buttonTender, buttonConstruction);
        return vBox;
    }




    private void setAdjustPath(String projectStage)
    {
        ViewProjectTimeline.projectStage = projectStage;
    }


    /**
     * @see ClientViewProjectTimeline#generatePathTransition(Shape, Path)
     */
    public static String getAdjustPath() {
        return projectStage;
    }


    private void createInvoice(String name, String client, double fee) {
        Controller.getInstance().createDesignInvoice(name, client, fee);
    }


    private void closePreviousStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stagePrev = (Stage) source.getScene().getWindow();
        stagePrev.close();
    }

    private AnchorPane createBottomPaneTimLine() {
        Button buttonContinue = new Button("Continue");

        buttonContinue.setOnAction(event -> {
            Parent view = new ArchitectMenuScene().getView();
            SceneUtil.changeScene(view);
            closePreviousStage(event);
        });

        Button buttonCancel = new Button("Cancel");
        buttonCancel.setOnAction(event -> {
            Parent view = new ArchitectMenuScene().getView();
            SceneUtil.changeScene(view);
            closePreviousStage(event);
        });;

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
