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

    private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
    //플레이어의 공격을 담음
    private PlayerAttack playerAttack;

    @Override
    public void run() { // run메소드 이 쓰레드를 시작할 씨 실행될 내용
        cnt = 0;
        playerX = 10;
        playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;

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
                     /*enemyAppearProcess();
                    enemyMoveProcess();
                    enemyAttackProcess(); */
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
        }
    }
    public void gameDraw(Graphics g) { // 게임안의 요소들 그려줄 메소드
        playerDraw(g);


    }
    public void playerDraw(Graphics g) {
        //플레이어 이미지를 playerX,Y에 그려주기
        g.drawImage(player, playerX, playerY, null);
        for (int i = 0; i < playerAttackList.size(); i++) {
            playerAttack = playerAttackList.get(i);
            g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
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
