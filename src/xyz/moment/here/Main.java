package xyz.moment.here;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/Start.fxml"));
        Scene sc = new Scene(root);
        primaryStage.setTitle("中国象棋");
        primaryStage.setScene(sc);
        //在退出程序的时候，子线程也一起退出
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        //System.exit(primaryStage.getOnCloseRequest());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
