import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener{
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNIT = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 200;
    final int[] x = new int[GAME_UNIT];
    final int[] y = new int[GAME_UNIT];

    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    static char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.requestFocus();
        StartGame();
    }
    @Override
    public boolean isFocusable() {
        return true;
    }
    public void StartGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    private void draw(Graphics g) {
     if (running){
         //Grid lines
    /*    for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }*/
         //apple color
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
        //loop for bodyparts joins
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(250));
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        } g.setColor(Color.red);
         g.setFont(new Font("INK FREE",Font.BOLD,35));
         FontMetrics metrics2 = getFontMetrics(getFont());
         g.drawString("Score: "+appleEaten,(SCREEN_WIDTH - metrics2.stringWidth("Score: "+appleEaten))/2,g.getFont().getSize());
     }
     else {
         GameOver(g);
     }
    }
    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH /UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT /UNIT_SIZE)*UNIT_SIZE;
    }
    public void Move(){
        for (int i = bodyParts; i >0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //direction
        switch (direction){
            case 'U':
                y[0] = y[0]-UNIT_SIZE;
                break;
                case 'D':
                y[0] = y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0]+UNIT_SIZE;
                break;
        }
    }
    public void CheckApples(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            appleEaten++;
            newApple();
        }
    }
    public void CheckCollisions(){
        for (int i = bodyParts; i >0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        //check if head touches left border
        if (x[0] <0){
            running = false;
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }
        //check if head touches top border
        if (y[0] <0){
        running = false;
    }
        //check if head touches bottom border

        if (y[0] >SCREEN_HEIGHT){
        running = false;
        }
        if (!running){
            timer.stop();
        }
    }
    public void GameOver(Graphics g){
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("INK FREE",Font.BOLD,25));
        FontMetrics metrics1 = getFontMetrics(getFont());
        g.drawString("Score: "+appleEaten,(SCREEN_WIDTH - metrics1.stringWidth("Score: "+appleEaten))/2,g.getFont().getSize());
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("INK FREE",Font.BOLD,45));
        FontMetrics metrics2 = getFontMetrics(getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/3,SCREEN_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            Move();
            CheckApples();
            CheckCollisions();
        }
        repaint();
    }
    static class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
    public void setFocusable(boolean b) {
    }
}