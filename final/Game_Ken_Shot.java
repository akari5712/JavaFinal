import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game_Ken_Shot extends JFrame implements ActionListener {
    
    Container cp; 
    
    // --- 元件宣告 ---
    JLabel targetLabel; // 圖片1：固定靶心
    JLabel kenLabel;    // 圖片2：會移動的角色 (Ken)
    
    JButton btnShoot = new JButton("昇龍拳!"); // 改個熱血一點的名字
    JButton btnReset = new JButton("重置");

    // 視窗與圖片大小設定
    int frameWidth = 400;
    int frameHeight = 800;
    int iconSize = 100; // 統一圖片大小

    // 移動角色的座標
    int kenX, kenY;
    int startY = 600; // 起始高度

    // 遊戲狀態
    boolean isFlying = false;
    Timer gameTimer;

    // --- 圖片路徑設定 ---
    // 記得路徑中的反斜線 \ 在 Java 字串中要寫成 \\
    String pathTarget = "C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";
    String pathKen    = "C:\\javaHW\\javaHomework\\final\\game_ken.png";

    public Game_Ken_Shot(){
        cp = this.getContentPane();
        cp.setLayout(null); 
        cp.setBackground(Color.DARK_GRAY);

        // --- 1. 處理第一張圖 (靶心) ---
        ImageIcon icon1 = new ImageIcon(pathTarget);
        Image img1 = icon1.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        targetLabel = new JLabel(new ImageIcon(img1));
        
        // 設定靶心位置 (固定在上方中間)
        int centerX = (frameWidth - iconSize) / 2;
        targetLabel.setBounds(centerX, 100, iconSize, iconSize);
        cp.add(targetLabel);

        // --- 2. 處理第二張圖 (Ken) ---
        ImageIcon icon2 = new ImageIcon(pathKen);
        Image img2 = icon2.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
        kenLabel = new JLabel(new ImageIcon(img2));

        // 設定 Ken 的位置 (起始在下方中間)
        kenX = centerX; 
        kenY = startY; 
        kenLabel.setBounds(kenX, kenY, iconSize, iconSize);
        cp.add(kenLabel);

        // --- 3. 設定按鈕 ---
        btnShoot.setBounds(50, 700, 100, 40);
        btnShoot.addActionListener(this);
        cp.add(btnShoot);

        btnReset.setBounds(230, 700, 100, 40);
        btnReset.addActionListener(this);
        cp.add(btnReset);

        // --- 4. 設定 Timer ---
        gameTimer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveKen();
            }
        });

        // --- 視窗設定 ---
        setTitle("Ken 昇龍拳練習");
        setSize(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 視窗置中
        setVisible(true);
    }

    public static void main(String[] args) {
        new Game_Ken_Shot();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnShoot) {
            if (!isFlying) {
                isFlying = true;
                gameTimer.start();
            }
        } else if (e.getSource() == btnReset) {
            resetGame();
        }
    }

    // --- 移動邏輯 ---
    public void moveKen() {
        if (isFlying) {
            kenY -= 15; // 往上飛的速度
            kenLabel.setLocation(kenX, kenY);

            // 碰撞偵測 (Ken 打到靶心)
            if (kenLabel.getBounds().intersects(targetLabel.getBounds())) {
                gameTimer.stop();
                isFlying = false;
                System.out.println("K.O.!");
                // 打到後停在目標下方一點點
                kenLabel.setLocation(kenX, targetLabel.getY() + 50); 
            }

            // 邊界偵測 (飛出去了)
            if (kenY < -100) {
                gameTimer.stop();
                isFlying = false;
                System.out.println("Miss!");
            }
        }
    }

    public void resetGame() {
        gameTimer.stop();
        isFlying = false;
        kenY = startY;
        kenLabel.setLocation(kenX, kenY);
    }
}