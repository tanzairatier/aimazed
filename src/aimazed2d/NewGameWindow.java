

package aimazed2d;

//---------------------------------------------------------------
// AiMazed2D
// --- Author: Joe Krall
//---------------------------------------------------------------

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.newdawn.easyogg.OggClip;

//---------------------------------------------------------------
// New Game Window
// --- Comprises the GUI elements of the New Game Window
//---------------------------------------------------------------

public class NewGameWindow extends JPanel{
    
    Image gamelogo = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/game_logo.png"));
    Image game_bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/bg_gameBG.png"));
    JCheckBox musicToggler;
    JCheckBox soundToggler;
    
    public NewGameWindow() {
        setLayout(new BorderLayout());
        Button_Panel buttonPanel = new Button_Panel();
        add("South", buttonPanel); 
        buttonPanel.setOpaque(false);
        this.setBackground(Color.BLACK);
    }
    
    public void loadWindow()
    {
        AiMazed2D.AiMazed2DAF.playAudio(0);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(660,520);
    }

    public void paintComponent(Graphics g) {
        //super.paintComponent(g);       

        //---------------------------------------------------------------
        // Draw the Game Logo
        //---------------------------------------------------------------
        //g.drawImage(game_bg, 0, 0, getWidth(), getHeight(), null);
        g.drawImage(gamelogo, 0, 0, getWidth(), getHeight(), null);
        //repaint();
    }  
    
    class Button_Panel extends JPanel{
        JButton NewGameButton;
        JButton HiScoresButton;
        JButton ExitButton;
        JButton CreditsButton;
        
        public Button_Panel()
        {
            this.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            
            
            //---------------------------------------------------------------
            // New Game Button
            //---------------------------------------------------------------
            NewGameButton = new JButton("New Game");
            NewGameButton.setPreferredSize(new Dimension(80, 30));
            NewGameButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (AiMazed2D.FIRST_TIME == true)
                    {
                        AiMazed2D.FIRST_TIME = false;
                        AiMazed2D.windowController = 5;
                        
                    }
                    else
                    {
                        AiMazed2D.gameWindow.NewGame();
                        AiMazed2D.windowController = 1;
                    }
                    repaint();
                }
            });
            this.add(NewGameButton);

            //---------------------------------------------------------------
            // High Scores Button
            //---------------------------------------------------------------
            HiScoresButton = new JButton("High Scores");
            HiScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            HiScoresButton.setPreferredSize(new Dimension(80, 30));
            HiScoresButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.highScoresWindow.readHighScores();
                    AiMazed2D.windowController = 3;
                    repaint();
                }
            });
            this.add(HiScoresButton);
            
            //---------------------------------------------------------------
            // Credits Button
            //---------------------------------------------------------------
            CreditsButton = new JButton("Credits");
            CreditsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            CreditsButton.setPreferredSize(new Dimension(80, 30));
            CreditsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.windowController = 2;
                    repaint();
                }
            });
            this.add(CreditsButton);
            
            //---------------------------------------------------------------
            // Tutorial Button
            //---------------------------------------------------------------
            ExitButton = new JButton("Tutorial");
            ExitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            ExitButton.setPreferredSize(new Dimension(80, 30));
            ExitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.windowController = 5;
                    repaint();
                }
            });
            this.add(ExitButton);
                    
            this.setBackground(Color.BLACK);
            
            repaint();
        }
        
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            repaint();
        }
        public Dimension getPreferredSize() {
            return new Dimension(660,96);
        }
        
    }

}


