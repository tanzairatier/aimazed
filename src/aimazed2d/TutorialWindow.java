/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author joe
 */
public class TutorialWindow extends JPanel{
    
    JButton continueButton;
    JButton backButton;
    
    JButton page1, page2, page3;
    JPanel buttonPanel;
    JPanel tutorialPanel;
    
    JLabel heading;
    
    int page = 0;
    
    Image game_bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/bg_gameBG.png"));
    
    public TutorialWindow()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        heading = new JLabel();
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(heading, c);
        heading.setText("<HTML><font color=#ffffff><CENTER>A Brief Tutorial.</CENTER></font></HTML>");
        
        backButton = new JButton("Go Back");
        backButton.setPreferredSize(new Dimension(64, 28));
        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.windowController = 0;
                    repaint();
                }
            });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 40;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 0.8;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(backButton, c);
        
        tutorialPanel = new JPanel()
        {
            
            Image slide1 = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/intro/slide1.png"));
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                
                g.drawString("Page #" + (page+1), 30, 30);
                
                if (page == 0)
                {
                    g.drawImage(slide1, (getWidth()-slide1.getWidth(null))/2, (getHeight()-slide1.getHeight(null))/2, null);
                    /*
                    g.drawString("Qubey, the wandering penguin, one day finds", 10, 45);
                    g.drawString("the entrance to a mysterious cavern aglimmer", 10, 60);
                    g.drawString("with the appeal of treasures to his delight.", 10, 75);
                    g.drawString("", 20, 45);
                    g.drawString("Inside, Qubey finds himself trapped by the ", 10, 105);
                    g.drawString("mysterious powers of the cavern itself.", 10, 120);
                    g.drawString("", 20, 45);
                    g.drawString("Now entrapped in the deep dungeon, Qubey", 10, 135);
                    g.drawString("must find his way out.  At the same time, ", 10, 150);
                    g.drawString("his lust and desire for treasures demands", 10, 165);
                    g.drawString("he obtain a wealth of joy during his escape", 10, 180);
                    g.drawString("", 10, 195);
                    g.drawString("Inside the Deep Dungeon, Qubey must find a ", 10, 195);
                    g.drawString("way out by locating exits and keys to unseal", 10, 210);
                    g.drawString("each exit while amassing a joy of wealth.", 10, 225);
                    g.drawString("", 10, 240);
                    */
                    
                    
                    
                    
                }
                else if (page == 1)
                {
                    g.drawString("Crystals give Qubey 1 point.", 10, 45);
                    g.drawString("Collect as many crystals as you can.", 10, 60);
                    g.drawString("Every 1000 points, Qubey gains another life.", 10, 75);
                    g.drawString("", 20, 45);
                    g.drawString("Avoid enemies; they will ransack Qubey and", 10, 105);
                    g.drawString("if he is caught, he will lose a life.", 10, 120);
                    g.drawString("", 20, 45);
                    g.drawString("Aliens seem to also inhabit this dungeon.", 10, 150);
                    g.drawString("Inside are mysterious devices that will ", 10, 165);
                    g.drawString("stun all enemies for five seconds, allowing", 10, 180);
                    g.drawString("Qubey to capture any stunned enemy.", 10, 195);
                    g.drawString("", 10, 195);
                    g.drawString("Qubey needs to have a lit torch to survive.", 10, 225);
                    g.drawString("Unfortunately, torches stay lit for a ", 10, 240);
                    g.drawString("limited time.  Qubey will need to find more", 10, 255);
                    g.drawString("torches to replace those that burn out.", 10, 270);
                }
                else if (page == 2)
                {
                    g.drawString("To complete a level, satisfy three goals", 10, 45);
                    g.drawString("listed at the bottom of the screen.", 10, 60);
                    g.drawString("", 10, 75);
                    g.drawString("Key: Locate and obtain the key.", 20, 90);
                    g.drawString("Exit: Locate the exit.", 10, 105);
                    g.drawString("Joy: Satisfy Qubey's lust for gems.", 10, 120);
                    g.drawString("", 20, 45);
                    g.drawString("Controls:", 10, 150);
                    g.drawString("Arrow Keys - to move Qubey.", 10, 165);
                    g.drawString("P - to pause the game.", 10, 180);
                    g.drawString("", 10, 195);
                    g.drawString("", 10, 195);
                    g.drawString("", 10, 195);
                    g.drawString("", 10, 210);
                    g.drawString("", 10, 225);
                    g.drawString("", 10, 240);
                }
            }
        };
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 0.8;
        c.insets = new Insets(0, 0, 0, 0);
        //tutorialPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        this.add(tutorialPanel, c);
        tutorialPanel.setOpaque(false);
        
        continueButton = new JButton("New Game");
        continueButton.setPreferredSize(new Dimension(64, 28));
        continueButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.gameWindow.NewGame();
                    AiMazed2D.windowController = 1;
                    repaint();
                }
            });
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.ipadx = 40;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 0.8;
        c.insets = new Insets(0, 0, 0, 0);
        this.add(continueButton, c);
        
        
        buttonPanel = new JPanel();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.insets = new Insets(0, 0, 0, 0);
        //buttonPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        this.add(buttonPanel, c);
        buttonPanel.setOpaque(false);
        
        buttonPanel.setLayout(new GridLayout(1, 3, 1, 1));
        page1 = new JButton("How to Play");
        page2 = new JButton("Story");
        page3 = new JButton("Characters");
        buttonPanel.add(page1);
        buttonPanel.add(page2);
        buttonPanel.add(page3);
        
        page1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    page = 0;
                }
            });
        page2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    page = 1;
                }
            });
        page3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    page = 2;
                }
            });
    }
    
    public void paintComponent(Graphics g)
    {
        g.drawImage(game_bg, 0, 0, getWidth(), getHeight(), null);
    }
    public Dimension getPreferredSize() {
        return new Dimension(480,360);
    }
}
