package xyz.moment.here.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;

public class StartController {

    @FXML
    private Canvas theme;
    @FXML
    private TextField IPField;
    @FXML
    private TextField portField;
    @FXML
    private RadioButton theRed;
    @FXML
    private RadioButton theBlack;

    public static String ip = "";
    public static int port = -1;
    public static short player = -1;
    private GraphicsContext gc = null;

    public StartController()
    {

    }

    public void initialize()
    {
        Image t = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\中国象棋.png");
        gc = theme.getGraphicsContext2D();
        gc.drawImage(t, 0, 0, 400, 200);
    }


    public void Start() throws IOException {


        ip = IPField.getText();
        port = Integer.parseInt(portField.getText());
        if(theBlack.isSelected() && !theRed.isSelected())
        {
            player = 0;
        }
        else if(!theBlack.isSelected() && theRed.isSelected())
        {
            player = 1;
        }
        else
        {
            System.out.println("请正确选择您的角色！");
            Alert al = new Alert(Alert.AlertType.ERROR, "请正确选择您的角色！", ButtonType.OK);
            al.showAndWait();
        }

        if(!ip.equals("") && (port > 0) && (player != -1))
        {
            AnchorPane rootLayout = FXMLLoader.load(getClass().getResource("/xyz/moment/here/view/ChessBoard.fxml"));
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            System.out.println(scene.toString());
            Stage mianStage = new Stage();
            mianStage.setTitle("中国象棋");
            mianStage.setScene(scene);
            mianStage.show();
            mianStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });
            Stage toClose = (Stage)IPField.getScene().getWindow();
            toClose.close();
        }
        else
        {
            System.out.println("请完善游戏信息！");

            Alert al = new Alert(Alert.AlertType.ERROR, "请完善游戏信息！", ButtonType.OK);
            al.showAndWait();
        }
    }
}
