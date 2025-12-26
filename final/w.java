import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class app_1 extends JFrame implements ActionListener {

    Container cp;
    
    // --- 變數宣告 ---
    RotatedPanel imageLabel_target; // 改用我們自訂的旋轉面板
    JLabel imageLabel_ken;
    JButton shot = new JButton("Shot");
    
    // 飛刀 (Ken) 的變數
    boolean flying = false;
    int ken_y = 500;
    int ken_x = (400 - 75) / 2; // 置中計算
    Timer flyTimer; // 控制飛行的計時器

    // 旋轉 (靶心) 的變數
    Timer rotateTimer; // 控制旋轉的計時器

    public app_1() {
        cp = this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.gray);

        // --- 圖片路徑 (請確認路徑正確) ---
        String imagePathtree = "C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";
        String imagePathknief = "C:\\javaHW\\javaHomework\\final\\Gemini_Generated_Image_99w8ld99w8ld99w8.png"; // 假設這是你的 Ken 圖

        // --- 1. 設定 Ken (飛刀) ---
        ImageIcon icon_ken = new ImageIcon(imagePathknief);
        Image imgken = icon_ken.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        imageLabel_ken = new JLabel(new ImageIcon(imgken));
        imageLabel_ken.setBounds(ken_x, ken_y, 75, 75);
        cp.add(imageLabel_ken);

        // --- 2. 設定靶心 (會旋轉) ---
        ImageIcon icon_target = new ImageIcon(imagePathtree);
        Image imgtarget = icon_target.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        
        // ** 使用自訂的旋轉面板 **
        imageLabel_target = new RotatedPanel(imgtarget);
        int centerX = (400 - 100) / 2;
        imageLabel_target.setBounds(centerX, 150, 100, 100);
        cp.add(imageLabel_target);

        // --- 3. 設定按鈕 ---
        shot.setBounds(150, 650, 100, 40);
        shot.addActionListener(this); // 綁定監聽器
        cp.add(shot);

        // --- 4. 啟動旋轉計時器 (自動旋轉) ---
        rotateTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 每次呼叫，角度 +5
                imageLabel_target.angle += 5;
                if (imageLabel_target.angle >= 360) {
                    imageLabel_target.angle = 0;
                }
                imageLabel_target.repaint(); // 重新繪製畫面
            }
        });
        rotateTimer.start(); // 程式一執行就開始轉

        // --- 5. 設定飛行計時器 (按下按鈕才啟動) ---
        flyTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveKen(); // 呼叫移動邏輯
            }
        });

        // --- 視窗設定 ---
        setTitle("自動旋轉靶心");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new app_1();
    }

    // --- 按鈕事件 ---
    public void actionPerformed(ActionEvent e) {
        if (!flying) { // 避免重複發射
            flying = true;
            flyTimer.start();
        }
    }

    // --- 飛刀移動邏輯 ---
    public void moveKen() {
        if (flying) {
            ken_y = ken_y - 15;
            imageLabel_ken.setLocation(ken_x, ken_y);

            // 碰撞偵測: 檢查 Ken 是否撞到 Target
            if (imageLabel_ken.getBounds().intersects(imageLabel_target.getBounds())) {
                flying = false;
                flyTimer.stop();
                // 撞到後稍微退後一點，視覺上比較像插在上面
                imageLabel_ken.setLocation(ken_x, imageLabel_target.getY() + 50);
                System.out.println("命中!");
            }

            // 邊界檢查: 飛出去了
            if (ken_y < -100) {
                flying = false;
                flyTimer.stop();
                // 重置 Ken 的位置
                ken_y = 500;
                imageLabel_ken.setLocation(ken_x, ken_y);
            }
        }
    }

    // ==========================================
    //  ** 內部類別：自訂可旋轉的 Panel **
    // ==========================================
    class RotatedPanel extends JPanel {
        Image img;
        public int angle = 0; // 旋轉角度

        public RotatedPanel(Image img) {
            this.img = img;
            // 設定背景透明，不然會有灰色底色擋住背景
            this.setOpaque(false); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // 1. 設定旋轉中心點 (圖片的中心)
            int cx = this.getWidth() / 2;
            int cy = this.getHeight() / 2;

            // 2. 執行旋轉 (將角度轉為弧度)
            g2d.rotate(Math.toRadians(angle), cx, cy);

            // 3. 畫出圖片
            g2d.drawImage(img, 0, 0, this);
        }
    }
}