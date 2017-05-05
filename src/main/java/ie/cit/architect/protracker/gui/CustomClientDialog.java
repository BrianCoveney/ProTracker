package ie.cit.architect.protracker.gui;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.controller.DBController;
import ie.cit.architect.protracker.controller.UserController;
import ie.cit.architect.protracker.model.ClientUser;
import ie.cit.architect.protracker.model.IUser;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by brian on 3/2/2017.
 */
public class CustomClientDialog
{
    private Mediator mediator;
    private Dialog<Pair<String, String>> dialog;
    private String userEmail;
    private String userPass;
    private String passwordTextField;
    private String emailTextField;


    public CustomClientDialog(Mediator mediator) {
        this.mediator = mediator;
    }


    // Custom Dialog ref: http://code.makery.ch/blog/javafx-dialogs-official/
    public Dialog<Pair<String, String>> signInClientDialog() {

        // Create the custom dialog.
        dialog = new Dialog<>();
        dialog.setTitle("Login Client User");
        dialog.setHeaderText("Please Sign In");

        // Set the icon (must be included in the project).
        dialog.setGraphic(new ImageView(this.getClass().getResource("/login_icon_client.png").toString()));



        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);


        // Create the username and password labels and fields.
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField textFieldEmail = new TextField();
        textFieldEmail.setPromptText("Email");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Label labelCheckEmail = new Label();

        gridPane.add(new Label("Email:"), 0, 0);
        gridPane.add(labelCheckEmail, 3, 0);
        gridPane.add(textFieldEmail, 1, 0);
        gridPane.add(new Label("Password:"), 0, 1);
        gridPane.add(password, 1, 1);

        // Label width
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(30);

        // TextField width
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(70);
        gridPane.getColumnConstraints().addAll(col1, col2);


        dialog.getDialogPane().setContent(gridPane);

        // colour the dialogs header
        GridPane grid = (GridPane)dialog.getDialogPane().lookup(".header-panel");
        grid.setStyle("-fx-background-color: white;");

        // Request focus on the username field by default.
        Platform.runLater(() -> textFieldEmail.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {


            if (dialogButton == loginButtonType) {

                passwordTextField = password.getText();
                emailTextField = textFieldEmail.getText();

                Button buttonLogin = (Button) dialog.getDialogPane().lookupButton(loginButtonType);
                buttonLogin.setOnAction(event -> validateClientCredentials());


                return new Pair<>(emailTextField, passwordTextField);
            }



            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(emailPassword -> {
            userEmail = emailPassword.getKey();
            userPass = emailPassword.getValue();
        });



        return dialog;

    }

    // If email and password do not match employee credentials - display error message, then the sign in dialog again.
    // Otherwise open the Architect Menu Scene
    private void validateClientCredentials() {

        Platform.runLater(() -> {

            if (!UserController.getInstance().isClientUserEmailValid(emailTextField)) {
                createEmailErrorDialog();
                mediator.changeToClientCustomDialog();
            }
            else if(!UserController.getInstance().isUserPasswordValid(passwordTextField)) {
                createPasswordErrorDialog();
                mediator.changeToClientCustomDialog();
            }
            else {
                Platform.runLater(() -> addUserToDB());
                mediator.changeToClientMenuScene();
            }

        });

    }


    private Dialog createEmailErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Email Address Not Recognised!");
        alert.setHeaderText(null);
        alert.setContentText("Please check your email!");
        alert.showAndWait();
        return alert;
    }


    private Dialog createPasswordErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Not Recognised!");
        alert.setHeaderText(null);
        alert.setContentText("Please check your password!");
        alert.showAndWait();
        return alert;
    }


    public void addUserToDB() {

        IUser clientUser = UserController.getInstance().createClientUser(userEmail, userPass);

        if (clientUser != null) {
            DBController.getInstance().addUser((ClientUser) clientUser);
        }

        DBController.getInstance().saveClientUser();

    }
}
