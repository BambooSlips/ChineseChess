package xyz.moment.here.model;


import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Chess {

    public static final short theRed = 1;
    public static final short theBlack = 0;

    public short player;
    public String typeName;
    private int x, y;
    private int col, row;
    private boolean exist = true;
    private Image chessImage;

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }

    public Chess(short player, String typeName, int x, int y)
    {
        this.x = x;
        this.y = y;
        if(player == theRed)
        {
            if(typeName.equals("车"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\11.png");

            }
            else if(typeName.equals("马"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\22.png");
            }
            else if(typeName.equals("相"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\33.png");
            }
            else if(typeName.equals("士"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\44.png");
            }
            else if(typeName.equals("帅"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\55.png");
            }
            else if(typeName.equals("炮"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\66.png");

            }
            else if(typeName.equals("兵"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\77.png");
            }
        }
        else if(player == theBlack)
        {
            if(typeName.equals("车"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\1.png");
            }
            else if(typeName.equals("马"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\2.png");
            }
            else if(typeName.equals("象"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\3.png");
            }
            else if(typeName.equals("士"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\4.png");
            }
            else if(typeName.equals("将"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\5.png");
            }
            else if(typeName.equals("炮"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\6.png");

            }
            else if(typeName.equals("卒"))
            {
                chessImage = new Image("FILE:C:\\Users\\众神消亡\\编程人生\\Java\\FXprojects\\ChineseChess\\res\\7.png");
            }
        }
        this.typeName = typeName;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCol() {
        col = (x-12)/53;
        return col;
    }

    public int getRow() {
        //row = (y-12)/60;
        if(y >= 300)
            row = y/60;
        else
            row = (y-12)/60;
        return row;
    }

    public void setPos(int x, int y)
    {
        /*pos.setX(x);
        pos.setY(y);*/
        this.x = x;
        this.y = y;
    }

    public void moveChess(chessPoint point)
    {
        this.x = point.getX();
        this.y = point.getY();
    }
    public void reversePos()
    {
        /*pos.setX(10-pos.getX());
        pos.setY(11-pos.getY());*/
        this.x = x;
        this.y = y;
    }

    public void paint(GraphicsContext gc)
    {
        //Group root = new Group();
        //Canvas canvas = new Canvas(300, 250);
        gc.drawImage(chessImage,x, y, 50, 50);
        //System.out.println(x+"  "+y);
        //gc.restore();
        //root.getChildren().add(canvas);
        //st.setScene(new Scene(root, 300,200));
    }

    public void drawSelectedChess(GraphicsContext gc)
    {
        /*gc.setStroke(Color.CHOCOLATE);
        //gc.fillRect(0, 150, 50, 50);
        gc.strokeRect(x*40, y*40, 40, 40);
        */
        gc.setStroke(Color.YELLOW);
        //gc.fillRect(x, y, 50, 50);
        gc.strokeRect(x, y, 50, 50);
        System.out.println(x+"  "+y);
        //gc.restore();
    }
}
