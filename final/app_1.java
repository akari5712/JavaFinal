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
    
    // ★★★ 設定安全距離 (度數) ★★★
    // 如果兩把刀角度差小於這個數值，就算撞到。數值越大越難玩。
    final int SAFE_ANGLE_GAP = 15; 

    boolean flying = false;
    boolean isGameOver = false; // 新增遊戲結束狀態

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

        setTitle("射飛刀");
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
        // 如果正在飛，或者遊戲已經結束，就不能發射
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

            // 判斷是否命中樹幹
            if (knifeRect.intersects(targetRect)) {
                flying = false;
                flyTimer.stop();
                
                // 計算這把刀的插入角度
                int currentHitAngle = -imageLabel_target.angle;
                
                // ★★★ 關鍵邏輯：檢查是否撞到其他刀 ★★★
                if (imageLabel_target.checkCollision(currentHitAngle, SAFE_ANGLE_GAP)) {
                    // 撞到了！遊戲結束
                    gameOver();
                } else {
                    // 沒撞到，成功插入
                    imageLabel_target.addKnife(currentHitAngle);
                    imageLabel_target.repaint();
                    
                    // 重置下一把刀
                    ken_y = START_KEN_Y;
                    imageLabel_ken.setLocation(ken_x, ken_y);
                    imageLabel_ken.setVisible(true);
                }
            }
            else if (ken_y < -50) {
                // 沒射中飛出去了
                flying = false;
                flyTimer.stop();
                ken_y = START_KEN_Y;
                imageLabel_ken.setLocation(ken_x, ken_y);
                imageLabel_ken.setVisible(true);
            }
        }
    }
    
    //遊戲結束處理
    public void gameOver() {
        System.out.println("Game Over");
        isGameOver = true;
        rotatian.stop(); 
        flyTimer.stop(); 
        
        imageLabel_ken.setVisible(true); 

        // 1. 準備自訂圖示
        // 你可以換成任何你想要的圖片路徑，例如一顆骷髏頭或爆炸圖
        // 這裡我先用你原本的飛刀圖片當範例，並設定大小為 64x64
        String iconPath = "C:\\javaHW\\javaHomework\\final\\Adobe Express - file-fail.png";
        ImageIcon customIcon = loadIcon(iconPath, 62, 64);

        // 2. 跳出視窗 (注意參數變多了)
        JOptionPane.showMessageDialog(
            this,                                   // 父視窗
            "撞到了!!!", // 內容文字
            "失敗",                             // 標題
            JOptionPane.PLAIN_MESSAGE,              // 訊息類型 (用 PLAIN，因為我們要用自己的圖)
            customIcon                              // ★★★ 這裡放入你的圖片 ★★★
        ); 
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
        
        ArrayList<Integer> stuckKnives = new ArrayList<>();

        public RotatedPanel(Image tree, Image knife) {
            this.imgTree = tree;
            this.imgKnife = knife;
            setOpaque(false); 
        }

        public void addKnife(int hitAngle) {
            stuckKnives.add(hitAngle);
        }

        public boolean checkCollision(int newAngle, int safeGap) {
            for (int oldAngle : stuckKnives) {
                // 例如：355度和5度，數學差350，但實際距離只有10度
                int diff = Math.abs(newAngle - oldAngle) % 360;
                int distance = Math.min(diff, 360 - diff);
                
                if (distance < safeGap) {
                    return true; // 太近了，撞到
                }
            }
            return false; // 安全
        }

        //@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.translate(getWidth() / 2, getHeight() / 2);
            g2d.rotate(Math.toRadians(angle));
            
            if (imgKnife != null) {
                for (int knifeAngle : stuckKnives) {
                    var oldTransform = g2d.getTransform(); 
                    
                    g2d.rotate(Math.toRadians(knifeAngle)); 
                    g2d.drawImage(imgKnife, -25, 30, this); 
                    
                    g2d.setTransform(oldTransform); 
                }
            }

            if (imgTree != null) {
                g2d.drawImage(imgTree, -50, -50, this); 
            }
        }
    }
}
