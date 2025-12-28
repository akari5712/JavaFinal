import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class app_1 extends JFrame implements ActionListener, KeyListener {

    Container cp;
    RotatedPanel imageLabel_target; 
    JLabel imageLabel_ken;
    
    // ★★★ 新增：計分板 Label ★★★
    JLabel scoreLabel; 
    
    JButton shot = new JButton("shot");
    
    final int KNIFE_W = 50;
    final int KNIFE_H = 50;
    final int SAFE_ANGLE_GAP = 15; 

    boolean flying = false;
    boolean isGameOver = false;

    // ★★★ 新增：分數變數 ★★★
    int score = 0;       // 目前插了幾支
    int highScore = 0;   // 最高紀錄

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

        // --- 設定計分板 ---
        scoreLabel = new JLabel("目前分數: 0 |  歷史最高紀錄: 0");
        scoreLabel.setForeground(Color.WHITE); // 設定字體顏色
        scoreLabel.setFont(new Font("微軟正黑體", Font.BOLD, 20)); // 設定字型大小
        scoreLabel.setBounds(20, 20, 300, 30); // 設定位置在左上角
        cp.add(scoreLabel);

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

        setTitle("Knife Hit");
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
                    // 成功插入
                    imageLabel_target.addKnife(currentHitAngle);
                    imageLabel_target.repaint();
                    
                    // ★★★ 更新分數 ★★★
                    score++;
                    updateScoreBoard();
                    
                    // 重置下一把刀
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
    
    // ★★★ 更新計分板顯示 ★★★
    public void updateScoreBoard() {
        scoreLabel.setText("目前分數: " + score + "  |  歷史最高紀錄: " + highScore);
    }

    public void gameOver() {
        System.out.println("Game Over");
        isGameOver = true;
        rotatian.stop(); 
        flyTimer.stop(); 
        
        imageLabel_ken.setVisible(true); 

        // ★★★ 檢查是否打破紀錄 ★★★
        if (score > highScore) {
            highScore = score; // 更新最高分
            String iconPath1 = "C:\\javaHW\\javaHomework\\final\\Reset (2).png";
            ImageIcon customIcon1 = loadIcon(iconPath1, 64, 64);
            updateScoreBoard();
            JOptionPane.showMessageDialog(this, 
                "撞到了！\n本次得分: " + score+"\n新紀錄!!!" , "新紀錄", JOptionPane.PLAIN_MESSAGE,customIcon1);
        } else {
            String iconPath2 = "C:\\javaHW\\javaHomework\\final\\Adobe Express - file-fail.png";
            ImageIcon customIcon2 = loadIcon(iconPath2, 64, 64);
    
            JOptionPane.showMessageDialog(this, 
                "撞到了！\n本次得分: " + score + "\n最高紀錄: " + highScore, "gameOver", JOptionPane.PLAIN_MESSAGE,customIcon2);
        }
        
        resetGame();
    }

    public void resetGame() {
        isGameOver = false;
        flying = false;
        
        // ★★★ 分數歸零，但最高分保留 ★★★
        score = 0;
        updateScoreBoard();
        
        ken_y = START_KEN_Y;
        imageLabel_ken.setLocation(ken_x, ken_y);
        imageLabel_ken.setVisible(true);
        
        imageLabel_target.reset();
        
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
        
        ArrayList<Integer> stuckKnives = new ArrayList<>();

        public RotatedPanel(Image tree, Image knife) {
            this.imgTree = tree;
            this.imgKnife = knife;
            setOpaque(false); 
        }

        public void addKnife(int hitAngle) {
            stuckKnives.add(hitAngle);
        }
        
        public void reset() {
            stuckKnives.clear(); 
            repaint(); 
        }

        public boolean checkCollision(int newAngle, int safeGap) {
            for (int oldAngle : stuckKnives) {
                int diff = Math.abs(newAngle - oldAngle) % 360;
                int distance = Math.min(diff, 360 - diff);
                
                if (distance < safeGap) {
                    return true; 
                }
            }
            return false; 
        }

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
