package com.example;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.DatabaseConnection;
import com.example.usercrud;
import com.example.Maps;


/**
 * JavaFX App
 */
public class Gamecontroller  {

    private String username;
    private int mapindex;
    public Gamecontroller(String Username)
    {
        this.username= Username;
    }
    
    public Gamecontroller(String Username , int mapid)
    {
       this.username = Username;
       this.mapindex = mapid;
    }

    public Gamecontroller()
    {}
    
    public String Getusername()
    {
        return username;
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final double SQUARE_SIZE = WIDTH / ROWS;// đơn vị ô 40x40px
    private static final String[] FOODS_IMAGE = new String[] { "/ic_orange.png", "/ic_apple.png",
            "/ic_cherry.png",
            "/ic_berry.png", "/ic_coconut_.png", "/ic_peach.png", "/ic_watermelon.png",
            "/ic_orange.png",
            "/ic_pomegranate.png" };

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private static final Image wall = new Image("/wall.png");
  //  private static double [][] map = new double[ROWS][COLUMNS];

    private GraphicsContext gc;
    private List<Point2D> snakeBody = new ArrayList();
    private Point2D snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;
    public static Stage gamestage;
    private Maps mapsnake; // Biến thành viên để lưu trữ đối tượng Maps
    

    // creter a screen with another fxml file
    public void startgame() {    

       
      
    
       try {
         gamestage = new Stage(); 
         gamestage.setTitle("snakegame");
      Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        gamestage.setScene(scene);
       
        gamestage.show();
        gc = canvas.getGraphicsContext2D();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                //gọi biến khi ấn tạo sự kiện cho rắn là code
                KeyCode code = event.getCode();
                // set điều kiệu cho Code bằng Nút D hoặc Right dấu mũi tên 
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    // Để cho người dùng hiểu cơ chế nhìn từ góc nhìn của người dùng
                    // ví dụ đang quẹo phải bấm quẹo trái sẽ không được. Mún quẹo trái tính theo đầu rắn phải bấm lên
                    // Chạy từ góc nhìn của ng dùng
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (code == KeyCode.LEFT || code == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (code == KeyCode.UP || code == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point2D(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        mapsnake = new Maps();
        // lúc có giao diện sẽ cho người dùng chọn các img map để pust hình đó vào map index đây là ví dụ lấy map số 2
        DatabaseConnection connection = new DatabaseConnection();
        mapsnake.setdatamap(connection.getMapData(mapindex));

        mapsnake.Drawmap();
//pushmaplensever
/*
  DatabaseConnection connection = new DatabaseConnection();
        mapsnake.Pullmap(3);
        connection.saveMap(mapsnake.getdatamap());
 */
         
    
         generateFood();
       
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
      } catch (Exception e) {
        // TODO: handle exception
        e.printStackTrace();
      } 
      
    }
        /*  scene = new Scene(loadFXML("test"), 640, 480);
        stage.setScene(scene);
        stage.show();
        */
    // vẽ foodImage vào vị trí foodx foody với square là đơn vị hàng (Weight / rows == đơn vị cho 1 ô , 2 cái cuối là kích thươc add kích thước nó bằng với 1 ô map)
    private void drawFood(GraphicsContext gc) {

        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

   private void run(GraphicsContext gc)
    { 
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            return;
        }
        drawBackground(gc);
       drawFood(gc);
       drawSnake(gc);
       drawScore();
        // tru 1 point trong snake để không xuất hiện màu của con rắn cũ hàm xóa vết nè và cho còn rắn bò nè
         for (int i = snakeBody.size() - 1; i >= 1; i--) {
        Point2D snakebodytemp = new Point2D(snakeBody.get(i-1).getX(), snakeBody.get(i-1).getY());
        snakeBody.set(i, snakebodytemp);
        }
        
       switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:    
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }
        snakeBody.set(0, snakeHead);
          eatFood();
          
        gameOver();
      
       
    }
    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));//lay mau
        // 2 cái đuôi 35 dưới đẻ lấy bo tròn , kích thước -1 để đảm bảo rắng không vượt quá 1 ô (đơn vị) ô = weight / row 
        // khác với fillrect là chỉ fill full hình chữ nhật thì fillroundrect vẽ bo tròn với setting được kích thước fill màu
        // nhân vào để tính đơn vị ô lúc này tính đang ở ô mấy
        // mảng bang đầu đã chia thành đơn vị ô để dễ fill dự và SQuare_Size
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
        //  quét full body của rắn tình sao khi đã fill đầu răng thì tô từ đầu rắn về đuôi chạy từ 1 lý do là đầu răng đã được fill đầu rắng là snakebody(0)
          for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1,SQUARE_SIZE - 1, 30, 30);
        }
        
        
        
    }

    
    private void drawBackground(GraphicsContext gc) {
       
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (mapsnake.map[i][j] == 0) {
                    // Ô trống
                    if ((i + j) % 2 == 0) {
                        gc.setFill(Color.web("AAD751"));
                        gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    } else {
                        gc.setFill(Color.web("A2D149"));
                        gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                } else if (mapsnake.map[i][j] == 1) {
                    // Ô chứa vật cản
                    gc.drawImage(wall, i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
                
            }
        }
    }
    /* 
    // fill màu cho map
       private void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }
    */
    
    // hàm tạo thức ăn thui và nó sẽ được recall liên tục đó
    private void generateFood() {
        start:
        while (true) {
            // gán giá trị ngẫu nhiên từ  0 đến 20 rùi ép kiểu int
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);
            //kiểm tra coi nó phải vật cảng không 
            if (mapsnake.map[foodX][foodY] == 1) {
                continue start;
            }

            //check coi cái food mới respone ra có trung với con rằng không 
            // chạy hết người con rắn coi coi nó có đồ ăn mới tạo ở trong đó không
            for (Point2D snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            // lấy 1 cái hình từ file img hàm ở dưới là lấy đại 1 cái hình ngẫy nhiên trong mảng FOODS_IMAGES
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
            
            break;
        }
    }
    
    private void moveRight() {
    Point2D moverighthead = new Point2D(snakeHead.getX() +1,snakeHead.getY());
    snakeHead = moverighthead;
    }
    private boolean checkCollisionWithWalls() {
        // Kiểm tra xem đầu rắn có va chạm với các tường trong bản đồ hay không
        double wall =0;
        int x = (int) snakeHead.getX();
        int y = (int) snakeHead.getY();
        if (mapsnake.map[x][y] == 1) {
            return true; // Đầu rắn va chạm với tường
        }
        return false; // Đầu rắn không va chạm với tường
    }

    private void moveLeft() {
    Point2D moveLefthead = new Point2D(snakeHead.getX() -1,snakeHead.getY());
    snakeHead = moveLefthead;
    }

    private void moveUp() {
       Point2D moveUphead= new Point2D(snakeHead.getX() ,snakeHead.getY()-1);
    snakeHead = moveUphead;
    }

    private void moveDown() {
    Point2D moveDownhead = new Point2D(snakeHead.getX() ,snakeHead.getY() +1);
    snakeHead = moveDownhead;
    }
        

    // cho đụng nó chết
    // sao này mà gét map thì thêm tọa độ chướng ngại vật vào map nếu cái đàu nó đụng dzo thì ón die hoyy
    // tạo thêm luông logic để cho thim 1 con nữa và lưu điểm tạo map
    public void gameOver() {
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH || snakeHead.getY() * SQUARE_SIZE >= HEIGHT) {
            DatabaseConnection connection = new  DatabaseConnection();
        connection.saveScore(username,score);
      
            gameOver = true;
        }
        //vat can
        if (snakeHead.getX() < 0 || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH || snakeHead.getY() * SQUARE_SIZE >= HEIGHT || checkCollisionWithWalls()) {
            gameOver = true;
        }

        //destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.getX() == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                DatabaseConnection connection = new  DatabaseConnection();
        connection.saveScore(username,score);
      
                gameOver = true;
                break;
            }
        }
        
       
    }
    
    private void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point2D(-1, -1));
            generateFood();
            score += 5;
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

}

   

   
