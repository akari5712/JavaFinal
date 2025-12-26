import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class app_1 extends JFrame implements ActionListener{
    
        //JButton btn = new JButton("Click Me"); 
        Container cp; 
        //ImageIcon pic[] = new ImageIcon[1];
        //JLabel imageLabel_target;
        RotatedPanel imageLabel_target;
        JLabel imageLabel_ken;
        JButton shot=new JButton("shot");
        boolean flying=false;
        int ken_y=500,ken_x=(400-75)/2;
        Timer flyTimer,rotatian;
        
    
    public app_1(){
        cp=this.getContentPane();
        cp.setLayout(null);
        cp.setBackground(Color.gray);
        //cp.add(btn);
        //btn.addActionListener(this);

        String imagePathtree = "C:\\javaHW\\javaHomework\\final\\下載-Photoroom.png";
        String imagePathknief = "C:\\javaHW\\javaHomework\\final\\Gemini_Generated_Image_99w8ld99w8ld99w8.png";
        
        ImageIcon icon_traget =new ImageIcon(imagePathtree);
        ImageIcon icon_ken =new ImageIcon(imagePathknief);
       
        Image imgken = icon_ken.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        imageLabel_ken = new JLabel(new ImageIcon(imgken));
        imageLabel_ken.setBounds(ken_x, ken_y, 75, 75);
        cp.add(imageLabel_ken);

        Image imgtarget = icon_target.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon icon_traget
        imageLabel_target = new RotatedPanel(imgtarget);
        int centerX = (400 - 100) / 2;
        imageLabel_target.setBounds(centerX, 150,150, 100, 100);
        cp.add(imageLabel_target);

        // ** 使用自訂的旋轉面板 **
        imageLabel_target = new RotatedPanel(imgtarget);
        int centerX = (400 - 100) / 2;
        imageLabel_target.setBounds(centerX, 150, 100, 100);
        cp.add(imageLabel_target);


        Image imgtarget = icon_traget.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageLabel_target = new JLabel(new ImageIcon(imgtarget));
        int centerX = (400 - 100) / 2;
        imageLabel_target.setBounds(centerX,200,100,100);
        cp.add(imageLabel_target);

        setTitle("射飛刀");
        setSize(400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stops program when window closes
        shot.setBounds(150, 650, 100, 40);
        cp.add(shot);
        setVisible(true);
        
    }

    public static void main(String[] args) {
        new app_1();
        
    }
    public void actionPerformed(ActionEvent e){
        new shotken();
    }
    public void shotken(){
        if(flying){
            ken_y=ken_y-15;
            imageLabel_ken.setLocation(ken_x, ken_y);
            if(imageLabel_ken.getBounds().intersects(imageLabel_ken.getBounds())){
                flying=false;
                imageLabel_ken.setLocation(ken_x, imageLabel_ken.getY()+50);
            }
        }
    }
}
