import javax.swing.*;
import java.awt.*;

public class PlayerAttack {
    Image image = new ImageIcon("src/image/player_attack.png").getImage();
    //공격의 이미지, 위치, 공격력 등에 대한 정보
    //공격의 충돌 판정을 위해 이미지의 너비와 높이도 작성
    int x, y;
    int width = image.getWidth(null);
    int height = image.getHeight(null);
    int attack = 5;


    public PlayerAttack(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void fire() { //shooting method
        this.x += 15; //공격이 오른쪽 나가니까 x 값 증가
    }}
