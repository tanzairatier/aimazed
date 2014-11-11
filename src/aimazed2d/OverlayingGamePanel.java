
package aimazed2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @Author "J.K. Games"
 * 
 * @Description The Overlaying Game Panel consists of the graphical elements in the game
 * which are overlaid above the main game panel, wherein the main game 
 * elements are drawn such as Qubey and the shiny things.
 * 
 */
public class OverlayingGamePanel extends JPanel {
    
    JPanel scorePanel;  //section of the interface where score is drawn
    JPanel levelPanel;  //section of the interface where level is drawn
    JPanel lifePanel;   //section of the interface where hearts are drawn
    JPanel lightPanel;  //section of the interface where torches are drawn
    JPanel keyPanel;    //section of the interface where the key is drawn
    JPanel jewelPanel;  /*section of the interface indicating whether you have
                         *obtained enough jewels to move on to the next level */
    
    
    public OverlayingGamePanel()
    {
        /* Constructor */
        
        /*-----------------------------*/
        /*--- Construct each panel --- */
        /*-----------------------------*/
        
        scorePanel = new ScorePanel();
        levelPanel = new LevelPanel();
        lifePanel = new LifePanel();
        lightPanel = new LightPanel();
        keyPanel = new KeyPanel();
        jewelPanel = new JewelPanel();
        
        /*------------------------------*/
        /*--- Organize each panel on ---*/
        /*--- to a GridBagLayout.    ---*/
        /*------------------------------*/
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        //organizing the scorePanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scorePanel, c);
        scorePanel.setOpaque(false);
        //scorePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //organizing the levelPanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(levelPanel, c);
        levelPanel.setOpaque(false);
        //levelPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //organizing the lifePanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(lifePanel, c);
        lifePanel.setOpaque(false);
        //lifePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //organizing the lightPanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(lightPanel, c);
        lightPanel.setOpaque(false);
        //lightPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //organizing the keyPanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(keyPanel, c);
        keyPanel.setOpaque(false);
        //keyPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
        //organizing the jewelPanel
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.gridheight = 1;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(jewelPanel, c);
        jewelPanel.setOpaque(false);
        //jewelPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
        
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(660, 72);
    }
    
    public class ScorePanel extends JPanel
    {
        
        BufferedImage numbers;
        int[] numbits = {0,0,0,0,0,0,0};
        int tempscore;
        
        public ScorePanel()
        {
            try
            {
                numbers = ImageIO.read(getClass().getResource("images/numbers3_spritesheet.png"));
            }
            catch (IOException ex) {}
        }
        
        public void paintComponent(Graphics g)
        {
            //temporary score so we can modify it as we parse it by bits
            tempscore = AiMazed2D.gameWindow.game.player.score;

            //math to parse each number separately and draw from custom number image
            for (int i = 0; i < 7; i++)
            {
                numbits[i] = (int)(tempscore / (Math.pow(10, (6-i))));
                tempscore -= numbits[i]*(Math.pow(10, (6-i)));
                g.drawImage(numbers.getSubimage(numbits[i]*22 + numbits[i]*1, 0, 22, 30), (getWidth() - 7*22)/2 +  i*22, 2, null);
            }
        }

    }
    
    public class LevelPanel extends JPanel
    {
        BufferedImage numbers; 
        BufferedImage theL;
        int[] numbits = {0,0,0};
        int templevel;
        
        public LevelPanel()
        {
            try
            {
                numbers = ImageIO.read(getClass().getResource("images/numbers3_spritesheet.png"));
                theL = ImageIO.read(getClass().getResource("images/theL.png"));
            } 
            catch (IOException ex) {}
        }
        
        public void paintComponent(Graphics g)
        {

            //temporary level so we can modify it as we parse the number bits
            templevel = AiMazed2D.gameWindow.game.player.level;

            //math to parse each number separately and draw from custom number image
            for (int i = 0; i < 3; i++)
            {
                numbits[i] = (int)(templevel / (Math.pow(10, (2-i))));
                templevel -= numbits[i]*(Math.pow(10, (2-i)));
                g.drawImage(numbers.getSubimage(numbits[i]*22 + numbits[i]*1, 0, 22, 30), (getWidth() - 4*22)/2 + (i+1)*22, 2, null);
            }
            
            //draw the "L" to the left of the number
            g.drawImage(theL, (getWidth() - 4*22)/2 - 1, 3, null);
        }
    }
    
    public class LifePanel extends JPanel
    {
        APSReader heart;
        double now, before, delta, accum;
        
        public LifePanel()
        {
            heart = new APSReader("images/hearts/heart_spritesheet.png", 21, 17, 1, 8, 200);
        }
        
        public void paintComponent(Graphics g)
        {
            for (int j = 0; j < 5; j++)
            {
                if (AiMazed2D.gameWindow.game.player.life > j)
                {
                    g.drawImage(heart.getSprite(), (getWidth() - 5*24)/2 + 24*j + 3*(j), 10, null);
                }
            }
            
            now = System.nanoTime();
            delta = (now - before);
            before = now;
            accum += (delta/1000000.0);
            
            if (AiMazed2D.gameWindow.game.player.life < 2)
            {
                g.setColor(new Color(255, 0, 0, (int)((255.0)*(Math.abs(Math.cos(accum/250.0))))));
                g.drawRect((getWidth() - 5*24)/2, 8, 5*24+4, 26);
            }
        }
    }
    
    public class LightPanel extends JPanel
    {
        APSReader torch;
        double now, before, delta, accum;
        
        public LightPanel()
        {
            torch = new APSReader("images/torches/torch_ui_spritesheet.png", 21, 17, 1, 4, 175);
        }
        
        public void paintComponent(Graphics g)
        {
            for (int j = 0; j < 5; j++)
            {
                if (AiMazed2D.gameWindow.game.player.torches > j)
                {
                    g.drawImage(torch.getSprite(), (getWidth() - 5*24)/2 + 24*j + 3*(j), 2, null);
                }
            }
            
            now = System.nanoTime();
            delta = (now - before);
            before = now;
            accum += (delta/1000000.0);
            
            if (AiMazed2D.gameWindow.game.player.torches < 1)
            {
                g.setColor(new Color(255, 0, 0, (int)((255.0)*(Math.abs(Math.cos(accum/250.0))))));
                g.drawRect((getWidth() - 5*24)/2, 2, 5*24+4, 26);
            }
        }
    }
    
    public class KeyPanel extends JPanel
    {
        Image key;
        
        public KeyPanel()
        {
            key = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/ui/key.png"));
        }
        public void paintComponent(Graphics g)
        {
            if (AiMazed2D.gameWindow.game.player.hasKey)
            {
                g.drawImage(key, (getWidth()-24)/2, 0, 24, 24, null);
            }
        }
    }
    
    public class JewelPanel extends JPanel
    {
        int _fill;
        double _percent;
        int width;
        APSReader moneybag[];
        
        double now, before, delta, accum;
        
        public void JewelPanel()
        {
            moneybag = new APSReader[11];
        }
        
        public void paintComponent(Graphics g)
        {
            
            now = System.nanoTime();
            delta = (now - before);
            before = now;
            accum += (delta/1000000.0);
            
            //fill gauge background
            _percent = (double)(1.0);
            _fill = (int)((width)*_percent);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect((getWidth()-width)/2+1, 3, _fill, 11);
            
            //draw gauge outline 
            width = (int)(getWidth()*0.65);
            g.setColor(Color.blue);
            g.drawRect((getWidth()-width)/2, 2, width+1, 12);
            
            //fill gauge contents
            _percent = (double)(AiMazed2D.gameWindow.game.player.crystalsObtained)/(double)(AiMazed2D.gameWindow.game.player.totalCrystals);
            if (_percent >= AiMazed2D.gameWindow.game.player.exploreReq)
            {
                //glowy gauge if req fulfilled
                g.setColor(new Color(255, 255, 0, (int)((255.0)*(Math.abs(Math.cos(accum/250.0))))));
            }
            else
            {
                g.setColor(Color.yellow);
            }
            _fill = (int)((width)*_percent);
            g.fillRect((getWidth()-width)/2+1, 3, _fill, 11);
            
            //draw moneybag icon depending on how much collected
            if (_percent >= 1.0)
            {
                
            }
            else if (_percent >= 0.90)
            {
                
            }
            else if (_percent >= 0.80)
            {
                
            }
            else if (_percent >= 0.70)
            {
                
            }
            else if (_percent >= 0.60)
            {
                
            }
            else if (_percent >= 0.50)
            {
                
            }
            else if (_percent >= 0.40)
            {
                
            }
            else if (_percent >= 0.30)
            {
                
            }
            else if (_percent >= 0.20)
            {
                
            }
            else if (_percent >= 0.10)
            {
                
            }
            else
            {
                
            }
        }
    }
    
}
