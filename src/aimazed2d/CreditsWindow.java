package aimazed2d;


import aimazed2d.AiMazed2D;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joe
 */
public class CreditsWindow extends JPanel{
 
    JLabel credits;
    JButton backButton;
    
    Image game_bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/bg_gameBG.png"));
    public void paintComponent(Graphics g)
    {
        g.drawImage(game_bg, 0, 0, getWidth(), getHeight(), null);
    }
    
    public CreditsWindow()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        credits = new JLabel();
        credits.setAlignmentX(JTextField.CENTER);
        c.fill = GridBagConstraints.VERTICAL;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        //result.setBorder(BorderFactory.createLineBorder(Color.RED)); 
        this.add(credits, c);
        
        
        credits.setText(""
                + "<HTML><font color = #000000><CENTER><TABLE WIDTH=480>"
                    + "<TR><TH COLSPAN=2><CENTER><b>Development Team</b></CENTER></TH></TR>"
                    + "<TR><TD><CENTER>Joe Krall</CENTER></TD><TD><CENTER>Executive Producer & Lead Programmer</CENTER></TD>"
                    + "<TR><TD><CENTER>Danny Gagaoin</CENTER></TD><TD><CENTER>Producer & Creative Director</CENTER></TD>"
                    + "<TR><TD><CENTER>Anthony Cascio</CENTER></TD><TD><CENTER>Lead Sprite Artist</CENTER></TD>"
                    + "<TR><TD><CENTER>Masato Kaida</CENTER></TD><TD><CENTER>Audiographical Director</CENTER></TD>"
                    + "<TR><TD><CENTER>Brad Englehardt</CENTER></TD><TD><CENTER>Audio Producer</CENTER></TD>"
                    + "<TR><TD><CENTER>Sam Hum</CENTER></TD><TD><CENTER>Art Producer</CENTER></TD>"
                    + "<TR></TR>"
                    + "<TR><TH COLSPAN=2><CENTER><b>Special Thanks to</b></CENTER></TH></TR>"
                    + "<TR><TH COLSPAN=2><CENTER>J.D. Armijos-Lopez</CENTER></TH></TR>"
                    + "<TR><TH COLSPAN=2><CENTER>And everyone else who helped but whose names have not been mentioned.</CENTER></TH></TR>"
                + "</TABLE></CENTER></font></HTML>"
                );
        this.add(credits);
        
        backButton = new JButton("Done Viewing");
        backButton.setSize(64, 48);
        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.newGameWindow.loadWindow();
                    AiMazed2D.windowController = 0;
                }
            });
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 16;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(backButton, c);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(480,360);
    }
}
