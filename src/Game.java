import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Game extends Thread {
    private int delay = 20;
    private long pretime;
    private int cnt;

    private Image player = new ImageIcon("src/image/player.png").getImage();

    private int playerX, playerY;
    private int playerWidth = player.getWidth(null);
    private int playerHeight = player.getHeight(null);
    private int playerSpeed = 10; //키 입력이 한번 인식 됐을때 플레이어가 이동할 거리
    private int playerHp = 30;

    private boolean up, down, left, right, shooting;  //플레이어의 움직임을 제어할 변수 선언가
    // ArrayList 에 플레이어, 적의 공격을 담음
    private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
    private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
    private ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();

    private PlayerAttack playerAttack;
    private Enemy enemy;
    private EnemyAttack enemyAttack;

    private Audio backgroundMusic;
    private Audio hitSound;


    @Override
    public void run() { // run메소드 이 쓰레드를 시작할 씨 실행될 내용
        cnt = 0;
        playerX = 10;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;

       backgroundMusic = new Audio("src/audio/gameBGM.wav",true);
       hitSound = new Audio("src/audio/hitSound.wav", false);

        backgroundMusic.start();

    /* cnt를 앞에서 설정한 delay 밀리초가 지날때마다 증가시켜 줍니다.
       단순하게 Thread.sleep(delay);도 가능 but, 좀 더 정확한 주기를 위해 현재시간 -
       (cnt 가 증가하기 전 시간) < delay 일 경우 그 차이만큼 스레드에 차이를 줌 */
        while (true) {
            pretime = System.currentTimeMillis();
            if (System.currentTimeMillis() - pretime < delay) {
                try {
                    Thread.sleep(delay - System.currentTimeMillis() +  pretime);
                    keyProcess();
                    playerAttackProcess();
                    enemyAppearProcess();
                    enemyMoveProcess();
                    enemyAttackProcess();
                    cnt++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void keyProcess() { //화면에서 안나가는 선에서 playerX playerY 값 조정
        if (up && playerY - playerSpeed > 0) playerY -= playerSpeed;
        if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += playerSpeed;
        if (left && playerX - playerSpeed > 0) playerX -= playerSpeed;
        if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) playerX += playerSpeed;
        if (shooting && cnt % 15 == 0) {
            playerAttack = new PlayerAttack(playerX + 222, playerY + 25);
            playerAttackList.add(playerAttack);
        }

    }
    private void playerAttackProcess() {
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            playerAttack.fire();


        for (int j = 0; j < enemyList.size(); j++) {
            enemy = enemyList.get(j);
            if (playerAttack.x > enemy.x && playerAttack.x < enemy.x + enemy.width && playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height) {
                enemy.hp -= playerAttack.attack;
                playerAttackList.remove(playerAttack);
                //이미지가 겹쳐 있을시 hp를 줄이고 해당공격를 삭제
            }
            if (enemy.hp <= 0) { //적의 hp가 0 이라면 삭제
                hitSound.start();
                enemyList.remove(enemy);
            }
        }
        }
    }

    private void enemyAppearProcess() { //주기적으로 적을 출현시키는 메소드
        if (cnt % 80 == 0) {
            enemy = new Enemy(1120, (int) (Math.random() * 621));
            enemyList.add(enemy); //화면 끝에서 랜덤한 위치에 출현 시키기 위해 y값이 1-620 랜덤으로 나오고 이를 arrayList에 추가
        }
    }
    private void enemyMoveProcess() {
        for (int i = 0; i< enemyList.size(); i++) {
            enemy = enemyList.get(i);
            enemy.move();
        }
    }
    //일정 주기마다 적의 공격을 생성해 ArrayList 안에 추가
    private void enemyAttackProcess() {
        if (cnt % 50 == 0) {
            enemyAttack = new EnemyAttack(enemy.x -79, enemy.y +35);
            enemyAttackList.add(enemyAttack);
        }
        for (int i = 0; i < enemyAttackList.size(); i++) {
            enemyAttack = enemyAttackList.get(i);
            enemyAttack.fire();

            if (enemyAttack.x > playerX & enemyAttack.x < playerX + playerWidth && enemyAttack.y > playerY && enemyAttack.y < playerY + playerHeight) {
                hitSound.start();
                playerHp -= enemyAttack.attack;
                enemyAttackList.remove(enemyAttack);
            }
        }
    }

    public void gameDraw(Graphics g) { // 게임안의 요소들 그려줄 메소드
        playerDraw(g);
        enemyDraw(g);

    }
    public void playerDraw(Graphics g) {
        //플레이어 이미지를 playerX,Y에 그려주기
        g.drawImage(player, playerX, playerY, null);
        g.setColor(Color.PINK);
        g.fillRect(playerX - 1, playerY - 40, playerHp * 6, 10);
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
        }
    }
    public void enemyDraw(Graphics g) {
        for (int i = 0; i< enemyList.size(); i++) {
            enemy = enemyList.get(i);
            g.drawImage(enemy.image, enemy.x, enemy.y, null);
           g.setColor(Color.GRAY);
           g.fillRect(enemy.x + 1, enemy.y - 40, enemy.hp * 15, 10);
        }
        for (int i = 0; i < enemyAttackList.size(); i++) {
           enemyAttack = enemyAttackList.get(i);
           g.drawImage(enemyAttack.image, enemyAttack.x, enemyAttack.y, null);
        }
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setShooting(boolean shooting) {
        this.shooting = shooting;
    }
}
