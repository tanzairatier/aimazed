
package aimazed2d;

//---------------------------------------------------------------
// AiMazed2D
// --- Author: Joe Krall
//---------------------------------------------------------------

import aimazed2d.levelmaps.QMapFactory;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;

//---------------------------------------------------------------
// AiMazed2D: The Main Class
// --- Manages high level control over objects within AiMazed2D
//---------------------------------------------------------------

public class AiMazed2D extends JFrame {//JApplet {

    //Windows
    static NewGameWindow newGameWindow;
    static GameWindow gameWindow;
    static CreditsWindow creditsWindow;
    static HighScoresWindow highScoresWindow;
    static Splash splashWindow;
    static TutorialWindow tutorialWindow;
    static GameWrapWindow gameWrapWindow;
    
    //Window Controller Parameter
    static int windowController = -1;
    
    //Window Manager
    JPanel cards;
    CardLayout card_manager;
    
    //First Time usage
    static boolean FIRST_TIME = true;
    
    //Window Strings for Card Manager
    private String NEWGAMEWINDOW = "AiMazed2D: New Game";
    private String GAMEWINDOW = "AiMazed2D: Game Play";
    private String CREDITSWINDOW = "AiMazed2D: Credits";
    private String HIGHSCORESWINDOW = "AiMazed2D: High Scores";
    private String TUTORIALWINDOW = "AiMazed2D: Tutorial";
    private String SPLASHWINDOW = "AiMazed2D: Splash";
    private String GAMEWRAPWINDOW = "AiMazed2D: Game Over!";
    
    
    public static String images_qubey_default_roaming_upFace = "images/qubey/default/roaming/qubey_default_roaming_upFace_spritesheet.png";
    public static String images_qubey_default_roaming_downFace = "images/qubey/default/roaming/qubey_default_roaming_downFace_spritesheet.png";
    public static String images_qubey_default_roaming_leftFace = "images/qubey/default/roaming/qubey_default_roaming_leftFace_spritesheet.png";
    public static String images_qubey_default_roaming_rightFace = "images/qubey/default/roaming/qubey_default_roaming_rightFace_spritesheet.png";
    
    //Music
    public static AudioFactory AiMazed2DAF = new AudioFactory();
    
    //Levels
    public static QMapFactory AiMazed2DQF = new QMapFactory();
    
    JPanel content;
    
    
    public AiMazed2D () throws IOException, FontFormatException
    //@Override
    //public void init()
    {
        //super("AiMazed2D");
        this.setSize(getPreferredSize());
        this.setVisible(true);
        this.setBackground(Color.DARK_GRAY);
             
        //build each window
        newGameWindow = new NewGameWindow();
        gameWindow = new GameWindow();
        creditsWindow = new CreditsWindow();
        highScoresWindow = new HighScoresWindow();
        splashWindow = new Splash();
        tutorialWindow = new TutorialWindow();
        gameWrapWindow = new GameWrapWindow();
        
        //add each window to window manager
        cards = new JPanel(new CardLayout());
        cards.add(newGameWindow, NEWGAMEWINDOW);
        cards.add(gameWindow,GAMEWINDOW);
        cards.add(creditsWindow, CREDITSWINDOW);
        cards.add(highScoresWindow, HIGHSCORESWINDOW);
        cards.add(splashWindow, SPLASHWINDOW);
        cards.add(tutorialWindow, TUTORIALWINDOW);
        cards.add(gameWrapWindow, GAMEWRAPWINDOW);
        card_manager = (CardLayout)(cards.getLayout());
        this.add(cards);
        

        
        //run the splash window then switch to new game window
        windowController = 6;
        new Timer().schedule(new TimerTask() {
            public void run() {
                newGameWindow.loadWindow();
                windowController = 0;
            }
        }, 1000);
        repaint();
    }
    
    public void changeScreen(JPanel next)
    {
	
        for(Component c: cards.getComponents())
        {
            c.setVisible(false);
            
        }
        
        content = next;
        content.requestFocus(true);
        
        content.setVisible(true);
        
        content.validate();
        content.revalidate();
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(660,520);
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        switch (windowController)
        {
            case 0:
                //card_manager.show(cards, NEWGAMEWINDOW);
                changeScreen(newGameWindow);
                break;
            case 1:
                //card_manager.show(cards, GAMEWINDOW);
                changeScreen(gameWindow);
                gameWindow.requestFocusInWindow();
                break;
            case 2:
                card_manager.show(cards, CREDITSWINDOW);
                break;
            case 3: 
                card_manager.show(cards, HIGHSCORESWINDOW);
                break;
            case 4: 
                card_manager.show(cards, GAMEWRAPWINDOW);
                break;
            case 5: 
                card_manager.show(cards, TUTORIALWINDOW);
                break;
            case 6: 
                card_manager.show(cards, SPLASHWINDOW);
                break;
        }
        repaint();
    }
    
    public static void main(String[] args) throws IOException, FontFormatException {
        AiMazed2D application = new AiMazed2D();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}