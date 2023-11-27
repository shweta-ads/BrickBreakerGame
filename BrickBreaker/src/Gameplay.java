import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false; //game shouldn't start itself
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer; //set time for ball, move
    private int delay = 8;
    private int playerX = 310;
    private int ballPositionX = 120;  //starting position
    private int ballPositionY = 350;
    private int ballXDirection = -1; //set Direction
    private int ballYDirection = -2;

    private BrickMapGenerator mapObj;

    public Gameplay(){
        mapObj = new BrickMapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //draw map
        mapObj.draw((Graphics2D) g);

        //borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //the paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //the ball
        g.setColor(Color.blue);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        //scores
        g.setColor(Color.YELLOW);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(""+score, 590, 30);

        //if all brick are intersected, start again
        if(totalBricks <= 0){
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!", 260, 300); //should be shown at center

            //
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 350); //should be shown at center

        }

        //game over, ball falls off from paddle
        if(ballPositionY > 570){
            play = false;
            ballXDirection = 0;
            ballYDirection = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300); //should be shown at center

            //
            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart ", 230, 350); //should be shown at center

        }

        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //this method is automatically called
        timer.start();
        //if key is pressed
        if(play){
            //intersec ball and paddle
            if(new Rectangle(ballPositionX, ballPositionY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYDirection = -ballYDirection;
            }

            //iterate thr brick
            A : for(int i = 0; i < mapObj.map.length; i++){
                for(int j = 0; j < mapObj.map[0].length; j++){
                    if(mapObj.map[i][j] > 0){
                        int brickX = j * mapObj.brickWidth + 80;
                        int brickY = i * mapObj.brickHeight + 50;
                        int brickWidth = mapObj.brickWidth;
                        int brickHeight = mapObj.brickWidth;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            mapObj.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            //for left and right intersection
                            if(ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x
                                + brickRect.width){
                                ballXDirection = -ballXDirection;
                            }else{
                                ballYDirection = -ballYDirection;
                            }

                            break A; //if ball intersects the brick and brick is invisible, break out of loop

                        }
                    }
                }
            }

            ballPositionX += ballXDirection;
            ballPositionY += ballYDirection;
            if(ballPositionX < 0){  //left border
                ballXDirection = -ballXDirection;
            }

            if(ballPositionY < 0){ //top
                ballYDirection = -ballYDirection;
            }

            if(ballPositionX > 670){ //right
                ballXDirection = -ballXDirection;
            }

            repaint();
        }
        //call paint method again
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //detect right arrow key or left
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }else{
                moveRight();
            }
        }

        //detect right arrow key or left
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }else{
                moveLeft();
            }
        }

        //start game when enter is pressed
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPositionY = 350;
                ballPositionX = 120;
                ballXDirection = -1;
                ballYDirection = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                mapObj = new BrickMapGenerator(3, 7);
                repaint();
            }

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    //this class is for panel in which we run the game
    //first add panel to the jframe
    //kl - detecting arrowkey
    //al = move ball

    public void moveRight(){
        play = true;
        playerX += 20;
    }

    public void moveLeft(){
        play = true;
        playerX -= 20;
    }

}
