import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JavaProgramGame extends JPanel implements KeyListener {

        //Variables for the game objects.
    int pongX = 10, pongY = 100, lineX = 5, lineY = 50, line2X = 380, line2Y = 50;

        //for ball speed
    int right = 5;
    int left = - 5;
    int up = 5;
    int down = - 5;
    int width, height;

    int scorePlayer1 = 0, scorePlayer2 = 0, life1 = 1, life2 = 2;
    int GameOver = 1;

    JLabel Start = new JLabel("<HTML><FONT COLOR=GREEN>Press Start</FONT></HTML>"), exit = new JLabel("<HTML><FONT COLOR=RED>Exit </FONT></HTML>"),
            HTP = new JLabel("<HTML><FONT COLOR=ORANGE>How to play</FONT></HTML>");

    int GameState = 0;
    //0 = idle
    //1 = play
    //2 = game over

    boolean cntrlUP, cntrlDOWN, cntrlUP2, cntrlDOWN2; //for controls

    boolean Playing = false; //for game start

    public JavaProgramGame() {
        StartGame();
        addKeyListener(this);
        setFocusable(true);
        Thread td = new Thread(new Runnable() {
            public void run() {
                beforeStart();
                while(true) {
                    try {
                        update();
                        rendered();
                        Thread.sleep(35);
                        }
                    catch(Exception ex) {}
                    }
                }
            }); td.start();
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed (KeyEvent e) {
        if (GameState == 1) Playing = true; {
            //for player 1
            if(e.getKeyCode() == KeyEvent.VK_W) {
                cntrlUP = true; //key Pressed control to up is true
            }
            if(e.getKeyCode() == KeyEvent.VK_S) {
                cntrlDOWN = true; //key Pressed control to down is true
            }
            //for player 2
            if(e.getKeyCode() == KeyEvent.VK_UP) {
                cntrlUP2 = true; //key Pressed control to up is true
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                cntrlDOWN2 = true;  //key Pressed control to down is true
            }
        }
    }

    public void keyReleased(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_W) {
                cntrlUP = false;    //key Released control to up is false
            }
            if(e.getKeyCode()==KeyEvent.VK_S) {
                cntrlDOWN = false;  //key Released control to down is false
            }
            if(e.getKeyCode()==KeyEvent.VK_UP) {
                cntrlUP2 = false;   //key Released control to up is false
            }
            if(e.getKeyCode()==KeyEvent.VK_DOWN) {
                cntrlDOWN2 = false; //key Released control to down is false
            }
        }

    //Move of player 1
    public void moverPlayer1() {
        if(cntrlUP == true && lineY >= 0) lineY += down; //Control for up
        if(cntrlDOWN == true && lineY <= (getHeight() - 53)) lineY += up; //and down
        player1Line(lineX, lineY);
    }

    //Player 1 line position of x and y
    public void player1Line(int x, int y) {
        lineX = x;  //line X position player 1
        lineY = y;  //line X position player 1
        repaint();
    }

    //move of player 2
    public void moverPlayer2() {
        if(cntrlUP2 == true && line2Y >= 0) line2Y += down; //Control for up
        if(cntrlDOWN2 == true && line2Y <= (getHeight() - 53)) line2Y += up; //and down
        player2Line(line2X, line2Y);
    }

    //Player 2 line position of x and y
    public void player2Line(int x, int y) {
        line2X = x; //line2 X position player 2
        line2Y = y; //line2 Y position player 2
        repaint();
    }

    public void PositionXandY(int nx, int ny) {
        pongX = nx; //Ball X
        pongY = ny; //Ball Y
        width = getWidth(); //Width
        height = getHeight(); //height
        repaint();
    }

    public void moverOfPlayer() {
        moverPlayer1();
        moverPlayer2();
    }

        //boolean for Ball direction
    boolean BallIsLeftToRight = false;
    boolean BallIsUpToDown = false;

    public void update() {
        PositionXandY(pongX, pongY);
        moverOfPlayer();
        Scoring();
        BallStrokes();
            if (BallIsLeftToRight) {
                //Ball move to right
                pongX += right;
                if(pongX >= (width - 8)) BallIsLeftToRight = false;
                }
                else {
                //Ball move to left
                pongX += left;
                if(pongX <= 0) BallIsLeftToRight = true;
            }

            if(BallIsUpToDown) {
                //Ball move to up
                pongY += up;
                if(pongY >= (height - 8)) BallIsUpToDown = false;
                }
            else {
                //Ball move to down
                pongY += down;
                if(pongY <= 0) BallIsUpToDown = true;
            }
    }

    public void rendered() {
        repaint();
    }

    public void Scoring() {
        if(GameState == 1) {
            //Scoring of player 1 increment
            if(pongX >= (width - 8)) {
                scorePlayer1++;
                }

            //Life of player 1 decrease
            if(pongX >= (width - 8)) {
                life2--;
            }

            //Scoring of player 2 increment
            if (pongX == 0)
                scorePlayer2++;

            //Life of player 2 decrease
            if(pongX == 0) {
                life1--;
            }
                //Setting for Game over
            if (scorePlayer1 == GameOver || scorePlayer2 == GameOver) {
                Playing = false;
                //Game over
                GameIsOver();
            }
        }
    }

    //setting for Player 1 ball stroke
    public void BallStrokes() {
        if (GameState == 1) {
            if(pongX == lineX + 10 && pongY >= lineY && pongY <= (lineY + 50)) //Player 1 ball stroke
                BallIsLeftToRight = true;

                //setting for Player 2 ball stroke
            if(pongX == (line2X - 5) && pongY >= line2Y && pongY <= (line2Y + 50)) //player 2 ball stroke
                BallIsLeftToRight = false;
        }
    }

    public void StartGame() {
        Start.addMouseListener(new MouseAdapter() { //Start Main Menu Game
            public void mousePressed(MouseEvent e) {
                if(e.getSource() == Start) {
                    if(GameState == 0) {
                        if(Playing == false) GameState = 1;
                        remove(Start);
                        remove(exit);
                        remove(HTP);
                    }
                    else if(Playing == false) {
                        Playing = false;
                    }
                }
            }
        });
        exit.addMouseListener(new MouseAdapter() {  //Exit Main Menu Game
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        HTP.addMouseListener(new MouseAdapter() {   //How to play Main Menu Game
            public void mousePressed(MouseEvent e) {
                GameState = 3;
            }
        });

        add(Start);
        add(exit);
        add(HTP);
    }

    //Before Start the Game
    public void beforeStart() {
        GameState = 0;
    }

    //Game Over
    public void GameIsOver() {
        GameState = 2;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw ball
        g.setColor(Color.BLACK);
        g.fillOval(pongX, pongY, 10, 10);

        //draw line/rect
        g.fillRect(lineX, lineY, 10, 50);
        g.fillRect(line2X, line2Y, 10, 50);

        //draw scores
        g.drawString("Player 1: "+"Life: "+life1+" Score: " + scorePlayer1, 23, 15);
        g.drawString("Player 2: "+"Life: "+life2+" Score: " + scorePlayer2, 240, 15);

        //Start screen
        if (GameState == 0) {
            Playing = false;
            pongX = 100; pongY = 150; lineX = 5; lineY = 50; line2X = 380; line2Y = 50;

            g.setColor(Color.WHITE);
            g.drawRect(100, 150, 10, 50);

            g.setColor(Color.WHITE);
            g.drawRect(380, 50, 10, 50);

            //g.setColor(new Color(0, 150, 150, 150));
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            Font fnt = new Font("Arial", Font.BOLD, 20);
            g.setFont(fnt);
            g.setColor(Color.WHITE);
            g.drawString("Press Start", 145, 160);
        }

        //Dim screen, draw game over
        if (GameState == 2) {
            g.fillRect(0,0,400,400);

            Font fnts = new Font("Arial", Font.BOLD, 20);
            g.setFont(fnts);
            g.setColor(Color.RED);
            g.drawString("Game Over", 140, 160);


            //Player face Game Over
            g.setColor(Color.WHITE);
            g.fillArc(150, 30, 80, 80, 0, 360); //face

            g.setColor(Color.BLACK);
            g.fillArc(170, 50, 10, 10, 0, 360); //eyes left
            g.fillArc(200, 50, 10, 10, 0, 360); //eyes right
            g.setColor(Color.BLACK);
            g.drawLine(190, 75, 190, 60); //nose

            g.setColor(Color.BLACK);
            g.fillArc(170, 80, 40, 40, 0, 180); //mouth


            //Player face win
            g.setColor(Color.WHITE);
            g.fillArc(150, 245, 80, 80, 0, 360); //face
            g.setColor(Color.BLACK);
            g.fillArc(170, 275, 10, 10, 0, 360); //eyes left
            g.fillArc(200, 275, 10, 10, 0, 360); //eyes right
            g.setColor(Color.BLACK);
            g.drawLine(190, 300, 190, 285 ); //nose
            g.setColor(Color.BLACK);
            g.drawArc(160, 255, 60, 60, 0, -180); //mouth


            if(scorePlayer1 == GameOver) {
                Font stnf = new Font("Arial", Font.BOLD, 20);
                setFont(stnf);
                g.setColor(Color.GREEN);
                g.drawString("Player 1 Wins", 130,220);
            }

            else if(scorePlayer2 == GameOver) {
                Font stnf = new Font("Arial", Font.BOLD, 20);
                g.setFont(stnf);
                g.setColor(Color.GREEN);
                g.drawString("Player 2 Wins", 130,220);
            }
        }

        if(GameState == 3)  {
            remove(Start);
            remove(exit);
            remove(HTP);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            Font stnf = new Font("Arial", Font.BOLD, 20);
            g.setFont(stnf);
            g.setColor(Color.WHITE);
            g.drawString("How to play", 140,25);
            g.drawString("Welcome to my Game Program", 20,80);
            g.drawString("Ping pong 2 Player.",20,120);
            g.drawString("Player Controls",120, 170);
            g.drawString("Player 1",20,200);
            g.drawString("W - UP",20,240);
            g.drawString("S - DOWN",20,280);
            g.drawString("Player 2",295,200);
            g.drawString("ARROW UP - UP",215,240);
            g.drawString("ARROW DOWN - DOWN",160,280);

        }
    }

    public static void main(String[] args) {
        JFrame wnd = new JFrame("Ping Pong");
        wnd.setSize(400,400);
        wnd.setResizable(false);
        wnd.setLocationRelativeTo(null);
        wnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        wnd.setContentPane(new JavaProgramGame());
        wnd.setVisible(true);
    }

}
