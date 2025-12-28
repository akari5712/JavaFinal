import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class app_1 extends JFrame implements ActionListener, KeyListener {

    Container cp;
    RotatedPanel imageLabel_target; 
    JLabel imageLabel_ken;
    JButton shot = new JButton("shot");
    
    // 刀子尺寸
    final int KNIFE_W = 50;
    final int KNIFE_H = 50;
    
    // 安全距離
    final int SAFE_ANGLE_GAP = 15; 

    boolean flying = false;
    boolean isGameOver = false;

    final int START_KEN_X = (400 - KNIFE_W) / 2;
    final int START_KEN_Y = 500;
    int ken_x = START_KEN_X;
    int ken_y = START_KEN_Y;
    
    Timer flyTimer;
    Timer rotatian; 

    public app_1() {
        cp = this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.gray);

        String imagePathtree = "C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";
        String imagePathknife = "C:\\javaHW\\javaHomework\\final\\Gemini_Generated_Image_99w8ld99w8ld99w8.png";

        ImageIcon icon_ken = loadIcon(imagePathknife, KNIFE_W, KNIFE_H);
        ImageIcon icon_target = loadIcon(imagePathtree, 100, 100);

        // --- 設定飛刀 ---
        imageLabel_ken = new JLabel(icon_ken);
        if (icon_ken.getIconWidth() == -1) {
            imageLabel_ken.setOpaque(true);
            imageLabel_ken.setBackground(Color.RED);
            imageLabel_ken.setText("刀");
        }
        imageLabel_ken.setBounds(ken_x, ken_y, KNIFE_W, KNIFE_H);
        cp.add(imageLabel_ken);

        // --- 設定旋轉面板 ---
        imageLabel_target = new RotatedPanel(icon_target.getImage(), icon_ken.getImage());
        int centerX = (400 - 100) / 2;
        imageLabel_target.setBounds(centerX - 100, 100, 300, 300); 
        cp.add(imageLabel_target);

        setTitle("射飛刀 - 無限挑戰版");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        shot.setBounds(150, 650, 100, 40);
        shot.setFocusable(false); 
        cp.add(shot);
        shot.addActionListener(this); 

        flyTimer = new Timer(20, this); 
        rotatian = new Timer(40, this); 
        
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();

        rotatian.start(); 

        setVisible(true);
    }

    public static void main(String[] args) {
        new app_1();
    }

    public void startShooting() {
        if (!flying && !isGameOver) {
            flying = true;
            flyTimer.start();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == shot) {
            startShooting();
        }
        else if (e.getSource() == flyTimer) {
            shotken();
        }
        else if (e.getSource() == rotatian) {
            if (imageLabel_target != null && !isGameOver) {
                imageLabel_target.angle += 5; 
                if (imageLabel_target.angle >= 360) {
                    imageLabel_target.angle = 0;
                }
                imageLabel_target.repaint(); 
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            startShooting();
        }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public void shotken() {
        if (flying) {
            ken_y = ken_y - 20; 
            imageLabel_ken.setLocation(ken_x, ken_y);
            
            Rectangle knifeRect = imageLabel_ken.getBounds();
            Rectangle targetRect = imageLabel_target.getBounds();
            targetRect.grow(-110, -110); 

            if (knifeRect.intersects(targetRect)) {
                flying = false;
                flyTimer.stop();
                
                int currentHitAngle = -imageLabel_target.angle;
                
                if (imageLabel_target.checkCollision(currentHitAngle, SAFE_ANGLE_GAP)) {
                    gameOver();
                } else {
                    imageLabel_target.addKnife(currentHitAngle);
                    imageLabel_target.repaint();
                    
                    ken_y = START_KEN_Y;
                    imageLabel_ken.setLocation(ken_x, ken_y);
                    imageLabel_ken.setVisible(true);
                }
            }
            else if (ken_y < -50) {
                flying = false;
                flyTimer.stop();
                ken_y = START_KEN_Y;
                imageLabel_ken.setLocation(ken_x, ken_y);
                imageLabel_ken.setVisible(true);
            }
        }
    }
    
    // 遊戲結束處理
    public void gameOver() {
        System.out.println("Game Over");
        isGameOver = true;
        rotatian.stop(); // 停止旋轉
        flyTimer.stop(); // 停止飛刀
        
        imageLabel_ken.setVisible(true); 

        // 自訂圖示 (這裡使用原本的飛刀圖片)
        String iconPath = "C:\\javaHW\\javaHomework\\final\\Gemini_Generated_Image_99w8ld99w8ld99w8.png";
        ImageIcon customIcon = loadIcon(iconPath, 64, 64);

        // 顯示視窗 (程式會停在這裡，直到按下確定)
        JOptionPane.showMessageDialog(this, 
            "太可惜了！刀子撞在一起了！\n按下確定後重新開始。", 
            "挑戰失敗", 
            JOptionPane.PLAIN_MESSAGE,
            customIcon);
        
        // ★★★ 當使用者按下確定後，執行重置 ★★★
        resetGame();
    }

    // ★★★ 新增：重置遊戲的方法 ★★★
    public void resetGame() {
        // 1. 重置狀態旗標
        isGameOver = false;
        flying = false;
        
        // 2. 重置飛刀位置
        ken_y = START_KEN_Y;
        imageLabel_ken.setLocation(ken_x, ken_y);
        imageLabel_ken.setVisible(true);
        
        // 3. 清空樹上的刀子
        imageLabel_target.reset();
        
        // 4. 重新啟動旋轉
        rotatian.start();
    }

    private ImageIcon loadIcon(String path, int w, int h) {
        File f = new File(path);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } else {
            return new ImageIcon();
        }
    }

    class RotatedPanel extends JPanel {
        Image imgTree;
        Image imgKnife;
        int angle = 0;
