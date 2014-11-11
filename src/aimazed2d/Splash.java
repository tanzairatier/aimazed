package aimazed2d;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author joe
 */

public class Splash extends JPanel{
    
    Image JKGamesLogo = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/jklogo.png"));
    double last_time = System.nanoTime();
    double timer1 = 0.0;
    double cur_time;
    double delta_time;
    
    public Splash()
    {
        this.setLayout(new FlowLayout());
        this.setBackground(Color.WHITE);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        cur_time = System.nanoTime();
        delta_time = (cur_time - last_time);
        last_time = cur_time;

        timer1 = timer1 + (delta_time/1000000.0);
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(Math.min(1.0, 1.2*timer1/5000))));
        g2d.drawImage(JKGamesLogo, getWidth()/2 - 360/2, getHeight()/2 - 58/2, 360, 58, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        repaint();
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(480,360);
    }
}
