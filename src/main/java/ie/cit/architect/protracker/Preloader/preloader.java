package ie.cit.architect.protracker.Preloader;

import ie.cit.architect.protracker.App.Mediator;
import ie.cit.architect.protracker.helpers.Consts;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import javafx.stage.Stage;

/**
 * Created by Adam on 12/04/2017.
 * Assisted By https://blog.codecentric.de/en/2015/09/javafx-how-to-easily-implement-application-preloader-2/
 */

public class preloader extends Preloader
{
    private Stage preloaderStage;
    private Scene scene;
    private Label progress;

    public preloader()
    {
        System.out.println(Mediator.STEP() + "MyPreloader constructor called, thread: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
        System.out.println(Mediator.STEP() + "MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());
         Platform.runLater(() ->
         {
             Image logo = new Image(this.getClass().getResource("/Protracker_big.png").toString());
             ImageView iview1 = new ImageView(logo);

             progress = new Label("0%");

             VBox vbPre = new VBox(iview1, progress);
             vbPre.setAlignment(Pos.CENTER);
             vbPre.setId("vbPre");

             scene = new Scene(vbPre, Consts.APP_WIDTH, Consts.APP_HEIGHT);
             scene.getStylesheets().add("/stylesheet.css");
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(Mediator.STEP() + "MyPreloader#start (showing preloader stage), thread: " + Thread.currentThread().getName());

        this.preloaderStage = primaryStage;

        preloaderStage.getIcons().add(new Image(this.getClass().getResource("/icon.png").toString()));
        preloaderStage.setTitle(Consts.APPLICATION_TITLE);
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        // Handle application notification in this point (see MyApplication#init).
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                // Called after MyPreloader#start is called.
                System.out.println(Mediator.STEP() + "BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                // Called before MyApplication#init is called.
                System.out.println(Mediator.STEP() + "BEFORE_INIT");
                break;
            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
                System.out.println(Mediator.STEP() + "BEFORE_START");

                preloaderStage.hide();
                break;
        }
    }
}

