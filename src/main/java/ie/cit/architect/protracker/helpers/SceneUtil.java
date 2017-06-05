package ie.cit.architect.protracker.helpers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by brian on 05/06/17.
 */
public class SceneUtil {

    public static void changeScene(Parent view) {
        Scene scene = new Scene(view, Consts.APP_WIDTH, Consts.APP_HEIGHT);
        scene.getStylesheets().add("/stylesheet.css");
        Stage stage = new Stage();
        stage.setTitle(Consts.APPLICATION_TITLE + " View Messages");
        stage.setScene(scene);
        stage.show();
    }
}
