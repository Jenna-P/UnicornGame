import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;

    private Image mainScreen = new ImageIcon("src/image/main_screen.png").getImage();
    private Image loadingScreen = new ImageIcon("src/image/loading_screen.png").getImage();
    private Image gameScreen = new ImageIcon("src/image/game_screen.png").getImage();

    private boolean isMainScreen, isLoadingScreen, isGameScreen;

    public static Game game = new Game();

    private Audio backgroundMusic;

    public ShootingGame() {
        setTitle("Shooting Game");
        setUndecorated(true);
        setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

       init();

    }

    //초기화를 해줄 init 메소드 : isMainScreen 만 true
    private void init() {
        isMainScreen = true;
        isLoadingScreen = false;
        isGameScreen = false;

        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();
        KeyListener keylistener = new KeyListener();
        addKeyListener(keylistener);
    }

    //로딩, 게임 화면을 넘어 가기 위한 gameStart 메소드
    private  void gameStart() {
    isMainScreen = false;
    isLoadingScreen = true;

    Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
                backgroundMusic.stop();
                isLoadingScreen = false;
                isGameScreen = true;
                game.start(); //Game class의  Thred 를 시작하기 위해
            }
        };
        //Timer 와 TimerTask 를 이용해 로딩화면에서 3초 후에 게임화면으로 넘어가도록 함
        loadingTimer.schedule(loadingTask, 3000);

    }

    //페인트 메소드에서 버퍼 이미지를 만들고 이를 화면에 뿌려줌으로써 깜빡임을 최소화 한다.
    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage,0,0,null);
    }
//각 화면 변수가 트루 일때마다 다른 화면을 그려주기 위해 if문 작
    public void screenDraw(Graphics g) {
        if (isMainScreen) {
            g.drawImage(mainScreen, 0, 0, null);
        }
        if (isLoadingScreen) {
            g.drawImage(loadingScreen, 0, 0, null);
        }
        if (isGameScreen) {
            g.drawImage(gameScreen, 0, 0, null);
            game.gameDraw(g);
        }
        this.repaint();

    }
        class KeyListener extends KeyAdapter {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        game.setUp(true);
                        break;
                    case KeyEvent.VK_S:
                        game.setDown(true);
                        break;
                    case KeyEvent.VK_A:
                        game.setLeft(true);
                        break;
                    case KeyEvent.VK_D:
                        game.setRight(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.setShooting(true);
                        break;
                    case KeyEvent.VK_ENTER:
                        if (isMainScreen){
                            gameStart();
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(0);
                        break;
                }
            }
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                        game.setUp(false);
                        break;
                    case KeyEvent.VK_S:
                        game.setDown(false);
                        break;
                    case KeyEvent.VK_A:
                        game.setLeft(false);
                        break;
                    case KeyEvent.VK_D:
                        game.setRight(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        game.setShooting(false);
                        break;
                }

            }
        }


    }

