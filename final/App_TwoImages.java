import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App_TwoImages extends JFrame implements ActionListener {
    
    Container cp; 
    
    // --- 變數宣告 ---
    JLabel knifeLabel1; // 左邊的圖片
    JLabel knifeLabel2; // 右邊的圖片
    
    JButton btnShoot1 = new JButton("射左圖");
    JButton btnShoot2 = new JButton("射右圖");
    JButton btnReset = new JButton("重置");

    // 左圖位置與開關
    int x1 = 50, y1 = 550;   
    boolean isFlying1 = false;

    // 右圖位置與開關
    int x2 = 220, y2 = 550;  
    boolean isFlying2 = false;

    Timer gameTimer;

    // 指定你的圖片路徑 (請確認路徑完全正確)
    String imagePath = "C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";

    public App_TwoImages(){
        cp = this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.LIGHT_GRAY); // 改個背景色比較好看清楚圖片

        // --- 1. 處理圖片 (載入與縮放) ---
        // 建立圖片圖示
        ImageIcon iconOriginal = new ImageIcon(imagePath);
        
        // ** 重要優化 ** : 如果圖片太大，這裡將它縮小成 100x100
        // 如果你不縮放，圖片可能會蓋住整個視窗
        Image img = iconOriginal.getImage();
        Image newImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
        ImageIcon iconScaled = new ImageIcon(newImg);

        // --- 2. 設定 Label 1 (左) ---
        knifeLabel1 = new JLabel(iconScaled); // 把圖片放入 Label
        knifeLabel1.setBounds(x1, y1, 100, 100); // 設定位置與大小
        cp.add(knifeLabel1);

        // --- 3. 設定 Label 2 (右) ---
        // 這裡我們用同一張圖片，如果要不同圖片，就再 load 一次不同的路徑
        knifeLabel2 = new JLabel(iconScaled); 
        knifeLabel2.setBounds(x2, y2, 100, 100);
        cp.add(knifeLabel2);

        // --- 4. 設定按鈕 ---
        btnShoot1.setBounds(30, 700, 100, 30);
        btnShoot1.addActionListener(this);
        cp.add(btnShoot1);

        btnShoot2.setBounds(180, 700, 100, 30);
        btnShoot2.addActionListener(this);
        cp.add(btnShoot2);

        btnReset.setBounds(300, 700, 70, 30);
        btnReset.addActionListener(this);
        cp.add(btnReset);

        // --- 5. 設定 Timer ---
        gameTimer = new Timer(20, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveImages();
            }
        });
        
        setTitle("雙圖片獨立控制");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new App_TwoImages();
    }

    // --- 按鈕邏輯 ---
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnShoot1) {
            isFlying1 = true;
            gameTimer.start();
        } 
        else if (e.getSource() == btnShoot2) {
            isFlying2 = true;
            gameTimer.start();
        }
        else if (e.getSource() == btnReset) {
            resetGame();
        }
    }

    // --- 移動邏輯 ---
    public void moveImages() {
        if (isFlying1) {
            y1 -= 10;
            knifeLabel1.setLocation(x1, y1);
        }
        if (isFlying2) {
            y2 -= 15; // 右邊飛比較快
            knifeLabel2.setLocation(x2, y2);
        }
    }

    public void resetGame() {
        gameTimer.stop();
        isFlying1 = false;
        isFlying2 = false;
        y1 = 550;
        y2 = 550;
        knifeLabel1.setLocation(x1, y1);
        knifeLabel2.setLocation(x2, y2);
    }
}