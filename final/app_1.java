import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class app_1 extends JFrame implements ActionListener{

    Container cp;
    RotatedPanel imageLabel_tree; 
    JLabel imageLabel_ken;
    
    //分數
    JLabel scoreLabel; 
    JButton shot=new JButton("shot");
    
    final int knifesize_W=50;
    final int knifesize_H=50;
    
    final int safetyangle=15; 

    boolean flying=false;
    boolean GameOver=false;

    int score=0;       
    int highScore=0; 

    final int kenstart_X=(400-knifesize_W)/2;
    final int kenstart_Y=500;
    int ken_x=kenstart_X;
    int ken_y=kenstart_Y;
    
    Timer flyTimer;//刀子
    Timer rotatian;//樹幹

    public app_1(){
        cp=this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.gray);

        String imagePathtree="C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";
        String imagePathknife="C:\\javaHW\\javaHomework\\final\\Gemini_Generated_Image_99w8ld99w8ld99w8.png";

        ImageIcon icon_ken=image(imagePathknife, knifesize_W, knifesize_H);
        ImageIcon icon_tree=image(imagePathtree, 100, 100);

        // --- 設定計分板 ---
        scoreLabel=new JLabel("目前分數: 0  |  歷史最高紀錄: 0");
        scoreLabel.setForeground(Color.WHITE); // 設定字體顏色
        scoreLabel.setFont(new Font("微軟正黑體",Font.BOLD,20)); // 設定字型大小
        scoreLabel.setBounds(20,20,300,30); // 設定位置在左上角
        cp.add(scoreLabel);

        // --- 設定飛刀 ---
        imageLabel_ken=new JLabel(icon_ken);
        if(icon_ken.getIconWidth()==-1){
            imageLabel_ken.setOpaque(true);
            imageLabel_ken.setBackground(Color.RED);
            imageLabel_ken.setText("刀");
        }
        imageLabel_ken.setBounds(ken_x,ken_y,knifesize_W,knifesize_H);
        cp.add(imageLabel_ken);

        // --- 設定旋轉面板 ---
        imageLabel_tree=new RotatedPanel(icon_tree.getImage(),icon_ken.getImage());
        int centerX=(400-100)/2;
        imageLabel_tree.setBounds(centerX-100,100,300,300); 
        cp.add(imageLabel_tree);

        setTitle("Knife Hit");
        setSize(400,800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        shot.setBounds(150,650,100,40);
        cp.add(shot);
        shot.addActionListener(this); 

        flyTimer=new Timer(20,this); 
        rotatian=new Timer(40,this); 
        
        rotatian.start(); 

        setVisible(true);
    }

    public static void main(String[] args){
        new app_1();
    }

    public void startShooting(){
        if(!flying && !GameOver){
            flying = true;
            flyTimer.start();
        }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==shot){
            startShooting();
        }
        else if(e.getSource()==flyTimer){
            shotken();
        }
        else if (e.getSource()==rotatian){
            if(imageLabel_tree!=null &&! GameOver){
                imageLabel_tree.angle+=5; 
                if(imageLabel_tree.angle>=360){
                    imageLabel_tree.angle=0;
                }
                imageLabel_tree.repaint(); 
            }
        }
    }

    public void shotken(){
        if(flying){
            ken_y=ken_y-20; 
            imageLabel_ken.setLocation(ken_x,ken_y);
            
            Rectangle knifeRect=imageLabel_ken.getBounds();
            Rectangle targetRect=imageLabel_tree.getBounds();
            targetRect.grow(-110,-110); 

            if(knifeRect.intersects(targetRect)){
                flying=false;
                flyTimer.stop();
                
                int currentHitAngle=-imageLabel_tree.angle;
                
                if(imageLabel_tree.checkCollision(currentHitAngle,safetyangle)){
                    gameOver();
                }
                else{
                    // 成功插入
                    imageLabel_tree.newKnife(currentHitAngle);
                    imageLabel_tree.repaint();
                    
                    // 更新分數
                    score++;
                    updateSB();
                    
                    // 重置下一把刀
                    ken_y = kenstart_Y;
                    imageLabel_ken.setLocation(ken_x, ken_y);
                    imageLabel_ken.setVisible(true);
                }
            }
            else if(ken_y<-50){
                flying=false;
                flyTimer.stop();
                ken_y=kenstart_Y;
                imageLabel_ken.setLocation(ken_x, ken_y);
                imageLabel_ken.setVisible(true);
            }
        }
    }
    
    // 更新計分板顯示
    public void updateSB(){
        scoreLabel.setText("目前分數: " + score + "  |  歷史最高紀錄: " + highScore);
    }

    public void gameOver(){
        System.out.println("Game Over");
        GameOver=true;
        rotatian.stop(); 
        flyTimer.stop(); 
        
        imageLabel_ken.setVisible(true); 

        // 檢查是否打破紀錄
        if(score>highScore){
            highScore=score; // 更新最高分
            String iconPath1="C:\\javaHW\\javaHomework\\final\\Reset (2).png";
            ImageIcon customIcon1=image(iconPath1, 64, 64);
            updateSB();
            JOptionPane.showMessageDialog(this,"撞到了！\n本次得分: "+score+"\n新紀錄!!!","新紀錄",JOptionPane.PLAIN_MESSAGE,customIcon1);
        }
        else{
            String iconPath2="C:\\javaHW\\javaHomework\\final\\Adobe Express - file-fail.png";
            ImageIcon customIcon2=image(iconPath2, 64, 64);
    
            JOptionPane.showMessageDialog(this,"撞到了！\n本次得分: "+score+"\n最高紀錄: "+highScore,"gameOver",JOptionPane.PLAIN_MESSAGE,customIcon2);
        }
        
        reset();
    }

    public void reset(){
        GameOver=false;
        flying=false;
        
        score=0;
        updateSB();
        
        ken_y=kenstart_Y;
        imageLabel_ken.setLocation(ken_x,ken_y);
        imageLabel_ken.setVisible(true);
        
        imageLabel_tree.reset();
        rotatian.start();
    }

    private ImageIcon image(String path, int w, int h){
        File f=new File(path);
        if (f.exists()) {
            ImageIcon icon=new ImageIcon(path);
            Image img=icon.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        else{
            return new ImageIcon();
        }
    }

    class RotatedPanel extends JPanel{
        Image imgTree;
        Image imgKnife;
        int angle = 0; 
        
        ArrayList<Integer> stuckKnives=new ArrayList<>();

        public RotatedPanel(Image tree,Image knife){
            this.imgTree=tree;
            this.imgKnife=knife;
            setOpaque(false); 
        }

        public void newKnife(int hitAngle){
            stuckKnives.add(hitAngle);
        }
        
        public void reset(){
            stuckKnives.clear(); 
            repaint(); 
        }

        public boolean checkCollision(int newAngle, int safeGap){
            for(int oldAngle:stuckKnives){
                int diff=Math.abs(newAngle-oldAngle)%360;
                int distance=Math.min(diff,360-diff);
                
                if(distance<safeGap){
                    return true; 
                }
            }
            return false; 
        }

        //畫樹,刀
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            
            Graphics2D g2d=(Graphics2D)g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.translate(getWidth()/2, getHeight()/2);
            g2d.rotate(Math.toRadians(angle));
            
            if(imgKnife!=null){
                for(int knifeAngle:stuckKnives){
                    var oldTransform=g2d.getTransform(); 
                    
                    g2d.rotate(Math.toRadians(knifeAngle)); 
                    g2d.drawImage(imgKnife,-25,30,this); 
                    
                    g2d.setTransform(oldTransform); 
                }
            }

            if(imgTree!=null){
                g2d.drawImage(imgTree,-50,-50,this); 
            }
        }
    }
}
