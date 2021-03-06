package xyz.moment.here.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import xyz.moment.here.model.Chess;
import xyz.moment.here.model.chessPoint;
import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;


public class ChessBoardController implements Runnable{

    @FXML
    private Canvas ChessBoard;
    @FXML
    private Canvas playerCanvas;
    @FXML
    private Label position;
    @FXML
    private Label choiceLabel;
    @FXML
    private Label processLabel;
    @FXML
    private TextField messageField;
    @FXML
    private javafx.scene.control.TextArea messageArea;

    private chessPoint[][] points = new chessPoint[9][10];
    private chessPoint tempPoint1 = new chessPoint(0,0);
    private chessPoint tempPoint2 = new chessPoint(0,0);
    private Chess[] chess = new Chess[32];
    private int idx = -1;
    private int space = -1;
    private int[][] map = new int[9][10];
    private int otherPort;
    private int recievePort;
    public static String ip = "";
    private boolean flag;
    public static short localPlayer;
    public static short theBlack = 0;
    public static short theRed = 1;
    private boolean isMyTurn;
    private String message = "...";
    private GraphicsContext gc = null;
    private GraphicsContext pc = null;


    public void initialize()
    {

        gc = ChessBoard.getGraphicsContext2D();
        pc = playerCanvas.getGraphicsContext2D();
        getPoints();
        startJoin();
        System.out.println("isMyTurn: "+isMyTurn);
        initializeChess();
        drawBoard();
        drawPlayer(pc);
        //message = "...";
    }

    public void drawPlayer(GraphicsContext pc)
    {
        Image RED = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\55.png");
        Image BLACK = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\5.png");
        if(localPlayer == theRed)
        {
            pc.drawImage(RED,0,0, 120, 120);
        }
        else
        {
            pc.drawImage(BLACK,0,0, 120, 120);
        }
    }

    //不停地绘制棋盘
    public void drawBoard ()
    {
        int i = 0;
        Timer timer = new Timer();
        Image board = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\WOOD.GIF");
        try {
            timer.schedule(new TimerTask() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            gc.drawImage(board, 0, 0, 500, 600);
                            drawChess(gc);
                            //drawPlayer(pc);
                            if (isMyTurn) {
                                processLabel.setText("请落子...");
                            } else {
                                processLabel.setText("对方正在思考...");
                            }
                            choiceLabel.setText(message);
                            ChessBoard.setOnMouseClicked(e -> {
                                // 鼠标左键单击
                                if (e.getButton() == MouseButton.PRIMARY) {

                                    double x = e.getX();
                                    double y = e.getY();
                                    position.setText(String.format("(%.1f,%.1f)", x, y));
                                    System.out.println("isMyTurn: " + isMyTurn);
                                    if (isMyTurn) {
                                        checkPoints((int) x, (int) y, gc);


                                        send(String.format("move|%d|%d|%d", idx, (int) x, (int) y));

                                        processLabel.setText("请落子...");
                                    } else {
                                        System.out.println("对方还未落子！");
                                        Alert al = new Alert(Alert.AlertType.ERROR, "对方还未落子！", ButtonType.OK);
                                        al.showAndWait();
                                        processLabel.setText("对方正在思考...");
                                    }
                                    //choiceLabel.setText(message);
                                    judge();
                                }
                                ChessBoard.requestFocus(); //刷新，否则不能接收到鼠标事件了
                            });
                        }
                    });
                }
            }, 0, 500);
        }
        catch (Exception e)
        {

        }

    }

    public void initializeChess()
    {
        //Chess[] chess = new Chess[32];
        String[] red = {"车","马","相","士","帅","士","相","马","车","炮","炮","兵","兵","兵","兵","兵"};
        String[] black = {"车","马","象","士","将","士","象","马","车","炮","炮","卒","卒","卒","卒","卒"};

        int m = 12, n = 12;
        int q = 53, p = 60, w = 12;
        for(int i = 0; i < 32; i++)
        {
            if(i < 9)
                chess[i] = new Chess(Chess.theRed, red[i], m + i*q,n);
            else if(i == 9)
                chess[i] = new Chess(Chess.theRed, red[i], m + q,n+2*p);
            else if(i == 10)
                chess[i] = new Chess(Chess.theRed, red[i], m + 7*q,n + 2*p);
            else if(i==11)
                chess[i] = new Chess(Chess.theRed, red[i], m ,n + 3*p);
            else if(i == 12)
                chess[i] = new Chess(Chess.theRed, red[i], m + 2*q ,n + 3*p);
            else if(i == 13)
                chess[i] = new Chess(Chess.theRed, red[i], m + 4*q,n + 3*p);
            else if(i==14)
                chess[i] = new Chess(Chess.theRed, red[i], m + 6*q,n + 3*p);
            else if(i==15)
                chess[i] = new Chess(Chess.theRed, red[i], m + 8*q,n + 3*p);
            else if(i>=16 && i<25)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+(i-16)*q,9*p);
            else if(i==25)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+q,n+7*p-w);
            else if(i==26)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+7*q,n+7*p-w);
            else if(i==27)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m,n+6*p-w);
            else if(i==28)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+2*q,n+6*p-w);
            else if(i==29)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+4*q,n+6*p-w);
            else if(i==30)
                chess[i] = new Chess(Chess.theBlack, black[i-16], m+6*q,n+6*p-w);
            else if(i==31)
                chess[i] = new Chess(Chess.theBlack, black[i-16], points[8][6].getX(),points[8][6].getY());

            //chess[i].paint(gc);
            //System.out.println("棋子5："+chess[4].typeName);
            //chess[i] = new Chess(Chess.theBlack, black[i-16], m+8*q,n+6*p-w);
        }
        //chess[2].paint(gc);
        map = new int[][]
                {{0,   1,   2,   3,   4,   5,   6,   7,   8},
                 {0,   0,   0,   0,   0,   0,   0,   0,   0},
                 {0,   9,   0,   0,   0,   0,   0,  10,   0},
                 {11,  0,   12,  0,   13,  0,   14,  0,   15},
                 {0,   0,   0,   0,   0,   0,   0,   0,   0},
                 {0,   0,   0,   0,   0,   0,   0,   0,   0},
                 {27,  0,   28,  0,   29,  0,   30,  0,   31},
                 {0,   25,  0,   0,   0,   0,   0,   26,  0},
                 {0,   0,   0,   0,   0,   0,   0,   0,   0},
                 {16,  17,  18,  19,  20,  21,  22,  23,  24},};

        System.out.println("Initializing Chess");
    }


    public void drawChess(GraphicsContext gc )
    {
        for(int i = 0; i < 32; i++){
            if(chess[i].isExist())
            {
                chess[i].paint(gc);
            }
        }

        /*for (int i = 0; i< 9; i++)  //框出可落子点
        {
            for(int j =0; j<10; j++)
            {
                gc.setStroke(Color.YELLOW);
                //gc.fillRect(x, y, 50, 50);
                gc.strokeRect(points[i][j].getX(), points[i][j].getY(), 50, 50);
            }
        }*/

    }

    //计算可落子点
    public void getPoints() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                if(j<5)
                    points[i][j] = new chessPoint(12 + i*53, 12 + j*60);
                else if(j>=5)
                    points[i][j] = new chessPoint(12 + i*53, 12 + j*60-12);
            }
        }
    }

    public void checkPoints(int x, int y, GraphicsContext gc){

        if(idx < 0){
            for(int i = 0; i < 32; i++){
            /*if(Math.abs(tempPoint.getX() - ch.getX()) < 50 && Math.abs(tempPoint.getY() - ch.getY()) < 50){
                ch.drawSelectedChess(gc);
            }*/
                if((Math.abs(x-25 - chess[i].getX()) < 20) && (Math.abs(y -25 - chess[i].getY()) < 20)
                    && chess[i].isExist())
                {
                    chess[i].drawSelectedChess(gc);
                    tempPoint1.move(chess[i].getX(), chess[i].getY());
                    tempPoint2.move(chess[i].getX(), chess[i].getY());
                    idx = i;
                    if(chess[i].player != localPlayer )
                    {
                        System.out.println("选择了对方棋子!");
                        /*Alert al = new Alert(Alert.AlertType.ERROR, "选择了对方棋子!", ButtonType.OK);
                        al.showAndWait();*/
                        idx = -1;
                        return;
                    }
                    else if(chess[i].player == localPlayer )
                    {
                        System.out.println("当前行："+chess[i].getRow()+"当前列："+chess[i].getCol());
                        System.out.println("选择棋子："+chess[i].player+chess[i].typeName);
                        message = String.format("%s[%d][%d]",chess[i].typeName,chess[i].getRow(),chess[i].getCol());
                    }
                }
            }
        }
        else{
            int col = (x-12)/53;
            int row = 0;
            if(y >= 300)
                row = y/60;
            else
                row = (y-12)/60;
            System.out.println("已选行: "+row+" 已选列: "+col);
            space = 1;

            if(space > 0){

                int n = chess[idx].getCol();
                int m = chess[idx].getRow();
                System.out.println(String.format("在map[%d][%d]中:%d",m,n,map[m][n]));

                if(checkRules(idx, col, row, n, m))
                {

                    if(map[row][col] != 0)
                    {
                        if(chess[map[row][col]].player != chess[idx].player)
                        {
                            map[m][n] = 0;
                            chess[idx].moveChess(points[col][row]);
                            chess[map[row][col]].setExist(false);
                            map[row][col] = 0;
                            System.out.println(chess[idx].typeName+" 吃 "+chess[map[row][col]].typeName);
                            map[row][col] = idx;
                            System.out.println(String.format("已修改map[%d][%d]中%d:",row,col,map[row][col]));
                            System.out.println("已改变棋子位置");
                            message = String.format("%s[%d][%d]",chess[idx].typeName,chess[idx].getRow()+1,chess[idx].getCol()+1);
                            isMyTurn = !isMyTurn;
                        }
                        else
                        {
                            System.out.println("走棋无效！");
                            //Alert al = new Alert(Alert.AlertType.ERROR, "走棋无效！", ButtonType.OK);
                            //al.showAndWait();
                        }
                    }
                    else if(chess[idx].player == localPlayer)
                    {
                        map[m][n] = 0;
                        chess[idx].moveChess(points[col][row]);
                        map[row][col] = idx;
                        System.out.println(String.format("已修改map[%d][%d]中%d:",row,col,map[row][col]));
                        System.out.println("已改变棋子位置");
                        message = String.format("%s[%d][%d]",chess[idx].typeName,chess[idx].getRow()+1,chess[idx].getCol()+1);
                        isMyTurn = !isMyTurn;
                    }

                }
                space = -1;
                idx = -1;
                //isMyTurn = !isMyTurn;
            }
        }
    }

    //判断下棋规则
    public boolean checkRules(int idx, int dcol, int drow, int ccol, int crow)
    {
        if(idx < 16)
        {
            //红方棋子
            if(idx == 0 || idx == 8)  //车
            {
                if(dcol != ccol && drow != crow)
                {
                    System.out.println("走棋错误！"+dcol+"!=" +ccol +"&&"+ drow +"!="+ crow);
                    return false;
                }
                else if(getChessNumberInARow(crow, ccol, drow, dcol) > 0 || getChessNumberInACol(crow, ccol, drow, dcol) > 0){
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if(idx == 1 || idx == 7)  //马
            {
                if((dcol - ccol) == 1 && (drow - crow) == -2 && map[crow-1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 1 && (drow - crow) == 2 && map[crow+1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -1 && (drow - crow) == -2 && map[crow-1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -1 && (drow - crow) == 2 && map[crow+1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 2 && (drow - crow) == 1 && map[crow][ccol+1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 2 && (drow - crow) == -1 && map[crow][ccol+1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -2 && (drow - crow) == 1 && map[crow][ccol-1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -2 && (drow - crow) == -1 && map[crow][ccol-1] == 0)
                {
                    return true;
                }
                else
                {
                    System.out.println("走棋错误！"+"dcol - ccol = "+(dcol - ccol)+"drow - crow = "+(drow - crow)+"map[ccol][crow+1]="+map[ccol][crow+1]);
                    System.out.println(String.format("map[%d][%d]",ccol,crow+1));
                    return false;
                }
            }
            else if(idx == 2 || idx == 6)  //相
            {
                if((drow == 1 && dcol == 0) || (drow == 4 && dcol == 2)|| (drow == 2 && dcol == 4)|| (drow == 4 && dcol == 6)
                        || (drow == 0 && dcol == 6) || (drow == 2 && dcol == 8) || (drow == 4 && dcol == 8) || (drow == 2 && dcol == 0)
                        || (drow == 0 && dcol == 2))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 3 || idx == 5)  //士
            {
                if(Math.abs(dcol-ccol)==1 && Math.abs(drow-crow)==1 && dcol>2 && dcol<6 && drow<3){
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 4)  //帅
            {
                if((Math.abs(dcol-ccol)==1 || Math.abs(drow-crow)==1) && dcol>2 && dcol<6 && drow<3
                && !(Math.abs(dcol-ccol)==1 && Math.abs(drow-crow)==1))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 9 || idx == 10)  //炮
            {
                if((dcol == ccol ) &&
                        (getChessNumberInACol(crow, ccol, drow, dcol) == 0 ))
                {
                    System.out.println("9---1");
                    return true;
                }
                else if((drow == crow ) &&
                        (getChessNumberInARow(crow, ccol, drow, dcol) == 0 ))
                {
                    System.out.println("9---2");
                    return true;
                }
                else if((getChessNumberInARow(crow, ccol, drow, dcol) == 1 || getChessNumberInACol(crow, ccol, drow, dcol) == 1)
                && (map[drow][dcol] != 0))
                {
                    System.out.println("9---3");
                    return true;
                }
                else
                {
                    System.out.println("走棋错误！"+dcol+" " +ccol +" "+ drow +" "+ crow);
                    return false;
                }
            }
            else if(idx > 10)  //兵
            {
                System.out.println("dcol: "+dcol+"  ccol:"+ccol);
                System.out.println("drow: "+drow+"  crow:"+crow+" Math.abs(dcol-ccol)="+Math.abs(drow-crow));
                if(drow - crow == 1 && dcol == ccol)
                {
                    return true;
                }
                else if( Math.abs(dcol-ccol)==1 && drow == crow && crow > 4){
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            //黑方棋子
            if(idx == 16 || idx == 24){
                if(dcol != ccol && drow != crow)
                {
                    System.out.println("走棋错误！"+dcol+"!=" +ccol +"&&"+ drow +"!="+ crow);
                    return false;
                }
                else if(getChessNumberInARow(crow, ccol, drow, dcol) > 0 || getChessNumberInACol(crow, ccol, drow, dcol) > 0){
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else if(idx == 17 || idx == 23)
            {
                if((dcol - ccol) == 1 && (drow - crow) == -2 && map[crow-1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 1 && (drow - crow) == 2 && map[crow+1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -1 && (drow - crow) == -2 && map[crow-1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -1 && (drow - crow) == 2 && map[crow+1][ccol] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 2 && (drow - crow) == 1 && map[crow][ccol+1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == 2 && (drow - crow) == -1 && map[crow][ccol+1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -2 && (drow - crow) == 1 && map[crow][ccol-1] == 0)
                {
                    return true;
                }
                else if((dcol - ccol) == -2 && (drow - crow) == -1 && map[crow][ccol-1] == 0)
                {
                    return true;
                }
                else
                {
                    //+"dcol - ccol = "+(dcol - ccol)+"drow - crow = "+(drow - crow)+"map[ccol][crow+1]="+map[ccol][crow+1]);
                    System.out.println("走棋错误！");
                    System.out.println(String.format("map[%d][%d]",ccol,crow+1));
                    return false;
                }
            }
            else if(idx == 18 || idx == 22)  //象
            {
                if((drow == 9 && dcol == 2) || (drow == 7 && dcol == 0)|| (drow == 5 && dcol == 2)|| (drow == 7 && dcol == 4)
                        || (drow == 5 && dcol == 6) || (drow == 9 && dcol == 6) || (drow == 7 && dcol == 8) )
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 19 || idx == 21)  //士
            {
                if(Math.abs(dcol-ccol)==1 && Math.abs(drow-crow)==1 && dcol>2 && dcol<6 && drow>6){
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 20)  //将
            {
                if((Math.abs(dcol-ccol)==1 || Math.abs(drow-crow)==1) && dcol>2 && dcol<6 && drow>6
                        && !(Math.abs(dcol-ccol)==1 && Math.abs(drow-crow)==1))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if(idx == 25 || idx == 26)  //炮
            {
                if((dcol == ccol ) &&
                        (getChessNumberInACol(crow, ccol, drow, dcol) == 0 ))
                {
                    System.out.println("9---1");
                    return true;
                }
                else if((drow == crow ) &&
                        (getChessNumberInARow(crow, ccol, drow, dcol) == 0 ))
                {
                    System.out.println("9---2");
                    return true;
                }
                else if((getChessNumberInARow(crow, ccol, drow, dcol) == 1 || getChessNumberInACol(crow, ccol, drow, dcol) == 1)
                        && (map[drow][dcol] != 0))
                {
                    System.out.println("9---3");
                    return true;
                }
                else
                {
                    System.out.println("走棋错误！"+dcol+" " +ccol +" "+ drow +" "+ crow);
                    return false;
                }
            }
            else if(idx > 26)  //卒
            {
                System.out.println("dcol: "+dcol+"  ccol:"+ccol);
                System.out.println("drow: "+drow+"  crow:"+crow+" Math.abs(dcol-ccol)="+Math.abs(drow-crow));
                if(drow - crow == -1 && dcol == ccol)
                {
                    return true;
                }
                else if( Math.abs(dcol-ccol)==1 && drow == crow && crow < 5){
                    return true;
                }
                else
                {
                    return false;
                }
            }

        }

        return true;
    }

    //计算当前位置到目标位置一行内的棋子数
    public int getChessNumberInARow(int crow, int ccol, int drow, int dcol){
        int sum = 0;
        if(dcol > ccol)
        {
            for(int i = ccol+1; i < dcol; i++){
                if(map[crow][i] != 0)
                {
                    sum++;
                    System.out.println(String.format("map[%d][%d] = %d", crow, i, (map[crow][i])));
                }

            }
        }
        else
        {
            for(int i = ccol-1; i > dcol; i--){
                if(map[crow][i] != 0)
                {
                    sum++;
                    System.out.println(String.format("map[%d][%d] = %d", crow, i, (map[crow][i])));
                }

            }
        }
        System.out.println(sum);
        return sum;
    }

    //计算当前位置到目标位置一列内的棋子数
    public int getChessNumberInACol(int crow, int ccol, int drow, int dcol){
        int sum = 0;
        if(drow > crow)
        {
            for(int i = crow+1; i < drow; i++){
                if(map[i][ccol] != 0)
                {
                    sum++;
                    System.out.println(String.format("map[%d][%d] = %d", i, ccol, (map[i][ccol])));
                }

            }
        }
        else
        {
            for(int i = crow-1; i > drow; i--){
                if(map[i][ccol] != 0)
                {
                    sum++;
                    System.out.println(String.format("map[%d][%d] = %d", i, ccol, (map[i][ccol])));
                }

            }
        }
        System.out.println(sum);
        return sum;
    }

    //开始联机
    public void startJoin()
    {
        localPlayer = StartController.player;
        if(localPlayer == theRed)
        {
            isMyTurn = true;
        }
        else {
            isMyTurn = false;
        }
        this.ip = StartController.ip;
        this.otherPort = StartController.port;
        if(this.otherPort == 3003)
        {

            this.recievePort = 3004;
        }
        else if(this.otherPort == 3004)
        {

            this.recievePort = 3003;
        }
        System.out.println("this.otherPort: "+this.otherPort);
        System.out.println("this.recievePort:"+this.recievePort);
        System.out.println("localPlayer:"+localPlayer);
        send("join|");
        Thread th = new Thread(this);
        flag = true;
        th.start();
        message = "等待联机......";
    }

    //发送信息给对方
    public void send(String str)
    {
        DatagramSocket s = null;
        try{
            s = new DatagramSocket();
            byte[] buffer;
            buffer = new String(str).getBytes();
            //InetAddress ia = InetAddress.getLocalHost();
            InetAddress ia = InetAddress.getByName(ip);
            System.out.println(ip);
            DatagramPacket dgp = new DatagramPacket(buffer, buffer.length, ia, otherPort);
            s.send(dgp);
            System.out.println("发送信息："+str);

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(s != null)
                s.close();
        }
    }

    //获得要发送的信息
    public void getMessage()
    {
        String msg = messageField.getText();
        if(localPlayer == theRed)
            messageArea.appendText("\n"+"红方："+msg);
        else
            messageArea.appendText("\n"+"黑方："+msg);
        send(msg);
        messageField.clear();
    }

    //判断输赢
    public void judge()
    {
        if(!chess[4].isExist())
        {
            System.out.println("黑方胜！");
            Alert al = new Alert(Alert.AlertType.INFORMATION, "黑方胜！", ButtonType.OK);
            al.showAndWait();
            send("succ|");
        }
        else if(!chess[20].isExist())
        {
            System.out.println("红方胜！");
            Alert al = new Alert(Alert.AlertType.INFORMATION, "红方胜！", ButtonType.OK);
            al.showAndWait();
            send("succ|");
        }
    }

    //认输
    public void toLose()
    {
        if(localPlayer == theRed)
        {
            chess[4].setExist(false);
            judge();
        }
        else
        {
            chess[20].setExist(false);
            judge();
        }
    }

    //复盘
    public void reStart()
    {
        System.out.println("isMyTurn: "+isMyTurn);
        initializeChess();
        drawBoard();
        drawPlayer(pc);
        send("rest|");
    }


    @Override
    public void run() {
        try {
        DatagramSocket s = new DatagramSocket(recievePort);  //放在while之外！

        while(flag)
        {

            System.out.println("running");
            System.out.println("isMyTurn: "+isMyTurn);
            //judge();
            byte[] data = new byte[100];
            DatagramPacket dgp = new DatagramPacket(data, data.length);

            s.receive(dgp);
            String strData = new String(data);
            if(localPlayer == theRed)
                messageArea.appendText("\n"+"黑方："+strData);
            else
                messageArea.appendText("\n"+"红方："+strData);
            String[] a = new String[6];
            a = strData.split("\\|");
            if(a[0].equals("join"))
            {
                System.out.println("------------------------已连接！");
                //localPlayer = theBlack;
                message = "已联机";
                //
                if(localPlayer == theRed)
                {

                }
                else
                {

                }
                send("conn|");
            }
            else if(a[0].equals("conn"))
            {
                message = "已联机";
                if(localPlayer == theRed)
                {
                    //setMyTurn(true);
                }
                else
                {
                    //setMyTurn(false);
                }
            }
            else if(a[0].equals("succ"))
            {
                /*if(a[1].equals("黑方胜!"))
                {
                    //
                }
                else if(a[1].equals("红方胜！"))
                {
                    //
                }*/
                message = "你可以重新开局了！";
            }
            else if(a[0].equals("rest"))
            {
                System.out.println("isMyTurn: "+isMyTurn);
                initializeChess();
                drawBoard();
                drawPlayer(pc);
            }
            else if(a[0].equals("move"))
            {
                if(localPlayer == theRed)
                {
                    localPlayer = theBlack;
                    int idx = Integer.parseInt(a[1].trim());
                    int x = Integer.parseInt(a[2].trim());
                    int y = Integer.parseInt(a[3].trim());
                    checkPoints((int)x, (int)y, gc);
                    localPlayer = theRed;
                }
                else
                {
                    localPlayer = theRed;
                    int idx = Integer.parseInt(a[1].trim());
                    int x = Integer.parseInt(a[2].trim());
                    int y = Integer.parseInt(a[3].trim());
                    checkPoints((int)x, (int)y, gc);
                    localPlayer = theBlack;
                }
                //send(String.format("move|%d|%d|%d",idx,(int)x, (int)y));
            }
        }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
