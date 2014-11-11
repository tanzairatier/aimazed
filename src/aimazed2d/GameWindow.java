
package aimazed2d;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.*;

/**
 *
 * @author joe
 */

public class GameWindow extends JPanel {
    
    static int cell_width = 8;
    static int cell_height = 8;
    static int offset_x, offset_y;

    static int _D[][] = {{1, 0, 1, 1}, {0, 0, 0, 1}, {0, 0, 1, 0}, {0, 1, 1, 1}};
    
    boolean left_key, right_key, up_key, down_key, shift_key;
    private int LEFT = 1, RIGHT = 0, DOWN = 3, UP = 2;
    
    JLabel objective = new JLabel();
    ExitButton exitButton;
    
    double last_time = System.nanoTime();
    double timer1 = 0.0;
    double cur_time;
    double delta_time;
    public  Font tFont = null;
    
    JPanel req1Panel, req2Panel, req3Panel,scorePanel,hudPanel,gaugePanel;
    GamePanel gamePanel;
    
    JPanel overlay = new JPanel();
    
    public static Vector<TextFloat> textFloats = new Vector<TextFloat>();
    public static Vector<TextFloat> textFloats_heartDeaths = new Vector<TextFloat>();
    
    
    
    class ExitButton extends JButton{
            Image buttonImage = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/buttons_quit.png"));
            int off_x, off_y, wide, tall;
            boolean down = false;
            
            public ExitButton(String label) { super(label); }
            
            protected void paintComponent(Graphics g)
            {
                Dimension size = new Dimension(buttonImage.getWidth(null), buttonImage.getHeight(null));
                if (!down)
                {
                    g.drawImage(buttonImage, 0, 0, size.width, size.height, null);
                }
                else
                {
                    g.drawImage(buttonImage, 2, 2, size.width-4, size.height-4, null);
                }
            }
            
            public Dimension getPreferredSize(){
                return new Dimension(81, 35);
            }
        }
    
    public class GameManager {
        
        boolean paused = false;
        Player player;
        
        
        public GameManager()
        {
            
        }
        public void buildPlayer()
        {
            player = new Player();
            player.paused = true;
        }
        
        public void newGame()
        {
            player.paused = false;
            player.newGame();
        }
    }
    
    GameManager game;
    Image game_bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/panels_mainGame.png"));
    
    public void paintComponent(Graphics g)
    {
        //g.drawImage(game_bg, 0, 0, getWidth(), getHeight(), null);
    }
    
    public GameWindow() throws FontFormatException, IOException {
        
        //initialize the game
        game = new GameManager();
        game.player = new Player();
        
        //---------------------------------------------------------------
        // Register a new Custom Font for Quadranta
        //---------------------------------------------------------------
        InputStream is = this.getClass().getResourceAsStream("fonts/Quadranta-Regular.otf");
        tFont = Font.createFont(Font.TRUETYPE_FONT, is);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(tFont);
        tFont = tFont.deriveFont(16.0f);
        
        //---------------------------------------------------------------
        // Initialize overlaying components panel
        // - - The graphics consist of drawing the game panel first
        // - - and then an overlaying panel consisting of components
        // - - such as game score, level, life, etc.
        //---------------------------------------------------------------
        
        overlay = new OverlayingGamePanel();
        
        //---------------------------------------------------------------
        // Initialize the HUD Panel.  This panel consists of the
        // game play components that are drawn on a strip at the top
        // of the window.
        //---------------------------------------------------------------
        
        
        
        /*
        
        
        
        
        
        
        exitButton = new ExitButton("Exit");
        exitButton.addMouseListener(new MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                exitButton.down = true;
            }
        });
        exitButton.addMouseListener(new MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                exitButton.down = false;
            }
        });
        exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.windowController = 0;
                    //repaint();
                }
            });
        
       
        
        
        JCheckBox musicToggler = new JCheckBox("<HTML><Font Color=#000000>Music</HTML>");
        musicToggler.setOpaque(false);
        musicToggler.setSelected(AiMazed2D.musicFlag);
        musicToggler.addItemListener( new ItemListener() {
            public void itemStateChanged(ItemEvent e)
            {
                if (AiMazed2D.musicFlag == false) 
                {
                    AiMazed2D.musicFlag = true;
                    
                }
                else AiMazed2D.musicFlag = false;
            
                if (AiMazed2D.musicFlag == false) AiMazed2D.BGM01.pause();
                else AiMazed2D.BGM01.resume();
            }
        });
        
        JCheckBox soundToggler = new JCheckBox("<HTML><Font Color=#000000>Sound FX</HTML>");
        soundToggler.setSelected(AiMazed2D.soundFlag);
        soundToggler.setOpaque(false);
        soundToggler.addItemListener( new ItemListener() {
            public void itemStateChanged(ItemEvent e)
            {
                System.out.println("sound toggled");
                if (AiMazed2D.soundFlag == false) AiMazed2D.soundFlag = true;
                else AiMazed2D.soundFlag = false;
            }
        });
        rightSide.setLayout(new BorderLayout());
        rightSide.add(musicToggler, "North");
        rightSide.add(soundToggler, "South");
  */      
        //---------------------------------------------------------------
        // Main Game Panel
        //---------------------------------------------------------------
        gamePanel = new GamePanel();
        gamePanel.setBackground((new Color(0, 0, 45, 255)));
        gamePanel.setOpaque(false);
        
        //---------------------------------------------------------------
        // Add the game panel to the base frame
        //---------------------------------------------------------------
        this.setLayout(new GridLayout(1,1));
        add(gamePanel);
        
        gamePanel.setLayout(new BorderLayout());
        overlay.setOpaque(false);
        gamePanel.add(overlay, "North");
        
        //Register Key Listener
        this.addKeyListener(new key_handler());
    }

    public Dimension getPreferredSize() {
        return new Dimension(660,520);
    }

    public void NewGame()
    {        
        
        //reset the key flags
        left_key = false;
        right_key = false;
        up_key = false;
        down_key = false;
        shift_key = false;
        
        //start the game
        AiMazed2D.gameWindow.game.newGame();
    }
    
    public class key_handler extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_A: case KeyEvent.VK_LEFT:
                { left_key = true; }
                break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                {
                    right_key = true;
                }
               
                break;
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                {
                    up_key = true;
                }
                    
                break;
                case KeyEvent.VK_X:
                case KeyEvent.VK_DOWN:
                {
                    down_key = true;
                }
                break;
                case KeyEvent.VK_SHIFT:
                {
                    shift_key = true;
                    System.out.println("Shift");
                }   
                break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_A:case KeyEvent.VK_LEFT:   {left_key = false;} break;
                case KeyEvent.VK_D:case KeyEvent.VK_RIGHT:  {right_key = false;}break;
                case KeyEvent.VK_W:case KeyEvent.VK_UP:     {up_key = false;}   break;
                case KeyEvent.VK_X:case KeyEvent.VK_DOWN:   {down_key = false;} break;
                case KeyEvent.VK_SHIFT:                     {shift_key = false;} break;
                    
                /* Deprecated Zoom In/Out:
                case KeyEvent.VK_O:
                    AiMazed2D.gameWindow.game.player.numCols = Math.min(AiMazed2D.gameWindow.game.player.numCols+1, AiMazed2D.gameWindow.game.player.dungeon.width);
                    AiMazed2D.gameWindow.game.player.numRows = Math.min(AiMazed2D.gameWindow.game.player.numRows+1, AiMazed2D.gameWindow.game.player.dungeon.height);
                    AiMazed2D.gameWindow.game.player.shift.update(AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y, AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y);
                    break;
                case KeyEvent.VK_P:
                    AiMazed2D.gameWindow.game.player.numCols = Math.max(AiMazed2D.gameWindow.game.player.numCols-1, 16);
                    AiMazed2D.gameWindow.game.player.numRows = Math.max(AiMazed2D.gameWindow.game.player.numRows-1, 12);
                    AiMazed2D.gameWindow.game.player.shift.update(AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y, AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y);
                */
                    
                case KeyEvent.VK_P:
                    if (AiMazed2D.gameWindow.game.player.paused == true) 
                    {
                        AiMazed2D.gameWindow.game.player.paused = false;
                    } 
                    else
                    {
                        AiMazed2D.gameWindow.game.player.paused = true;
                    }
            }
        }
    }
    
    public class GamePanel extends JPanel{
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image floor[] = new Image[4];
        Image hidden_cell;
        
        Image wall[] = new Image[3];

        int crystalStep = 0;
        
        APSReader crystals[] = new APSReader[12];
        APSReader key = new APSReader("images/key_spritesheet.png", 41, 44, 1, 4, 200);
        APSReader exit = new APSReader("images/exits/exit_unlocked.png", 24, 24, 1, 1, 500);
        APSReader torch = new APSReader("images/torches/torch_spritesheet.png", 14, 24, 1, 4, 100);
        APSReader sparkle = new APSReader("images/sparkle.png", 17, 15, 1, 5, 100);
        
        APSReader blocky[] = new APSReader[4];
        APSReader qubey_blazing[] = new APSReader[4];
        APSReader ghostRoaming[] = new APSReader[4];
        APSReader ghostStunned[] = new APSReader[4];
        APSReader ghostAggravated[] = new APSReader[4];
        
        APSReader moRoaming[] = new APSReader[4];
        APSReader moStunned[] = new APSReader[4];
        APSReader moAggravated[] = new APSReader[4];
        
        APSReader LeopardoRoaming[] = new APSReader[4];
        APSReader LeopardoStunned[] = new APSReader[4];
        APSReader LeopardoAggravated[] = new APSReader[4];
        
        APSReader GraysonRoaming[] = new APSReader[4];
        APSReader GraysonStunned[] = new APSReader[4];
        APSReader GraysonAggravated[] = new APSReader[4];
        
        int wallcnt;
        
        
        public GamePanel()
        {
            floor[0] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/floors/floor.png"));
            floor[1] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/floors/floor2.png"));
            floor[2] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/floors/floor3.png"));
            floor[3] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/floors/floor4.png"));
            
            wall[0] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/walls/wall.png"));
            wall[1] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/walls/wall2.png"));
            wall[2] = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/walls/wall3.png"));
            
            crystals[0] = new APSReader("images/crystals/blueCrystal_spritesheet.png", 24, 24, 1, 5, 200);
            crystals[1] = new APSReader("images/crystals/diamond_spritesheet.png", 24, 24, 1, 8, 200);
            crystals[2] = new APSReader("images/crystals/emerald_spritesheet.png", 24, 24, 1, 8, 200);
            crystals[3] = new APSReader("images/crystals/goldBar_spritesheet.png", 24, 24, 1, 4, 200);
            crystals[4] = new APSReader("images/crystals/goldCoin_spritesheet.png", 24, 24, 1, 8, 200);
            crystals[5] = new APSReader("images/crystals/greenCrystal_spritesheet.png", 24, 24, 1, 5, 200);
            crystals[6] = new APSReader("images/crystals/qubeyCoin_spritesheet.png", 24, 24, 1, 5, 200);
            crystals[7] = new APSReader("images/crystals/redCrystal_spritesheet.png", 24, 24, 1, 5, 200);
            crystals[8] = new APSReader("images/crystals/ruby_spritesheet.png", 24, 24, 1, 8, 200);
            crystals[9] = new APSReader("images/crystals/sapphire_spritesheet.png", 24, 24, 1, 8, 200);
            crystals[10] = new APSReader("images/crystals/silverBar_spritesheet.png", 24, 24, 1, 4, 200);
            crystals[11] = new APSReader("images/crystals/topaz_spritesheet.png", 24, 24, 1, 8, 200);
            
            blocky[UP] = new APSReader("images/qubey/default/roaming/qubey_default_roaming_upFace_spritesheet.png", 24, 24, 1, 4, 400);
            blocky[DOWN] = new APSReader("images/qubey/default/roaming/qubey_default_roaming_downFace_spritesheet.png", 24, 24, 1, 4, 400);
            blocky[RIGHT] = new APSReader("images/qubey/default/roaming/qubey_default_roaming_rightFace_spritesheet.png", 24, 24, 1, 4, 400);
            blocky[LEFT] = new APSReader("images/qubey/default/roaming/qubey_default_roaming_leftFace_spritesheet.png", 24, 24, 1, 4, 400);
            
            qubey_blazing[UP] = new APSReader("images/qubey/default/blazing/qubey_default_blazing_upFace_spritesheet.png", 24, 24, 1, 4, 400);
            qubey_blazing[DOWN] = new APSReader("images/qubey/default/blazing/qubey_default_blazing_downFace_spritesheet.png", 24, 24, 1, 4, 400);
            qubey_blazing[RIGHT] = new APSReader("images/qubey/default/blazing/qubey_default_blazing_rightFace_spritesheet.png", 24, 24, 1, 4, 400);
            qubey_blazing[LEFT] = new APSReader("images/qubey/default/blazing/qubey_default_blazing_leftFace_spritesheet.png", 24, 24, 1, 4, 400);
            
            ghostRoaming[UP] = new APSReader("images/enemies/godfrey/roaming/godfrey_roaming_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostRoaming[DOWN] = new APSReader("images/enemies/godfrey/roaming/godfrey_roaming_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostRoaming[RIGHT] = new APSReader("images/enemies/godfrey/roaming/godfrey_roaming_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostRoaming[LEFT] = new APSReader("images/enemies/godfrey/roaming/godfrey_roaming_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            ghostStunned[UP] = new APSReader("images/enemies/godfrey/stunned/godfrey_stunned_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostStunned[DOWN] = new APSReader("images/enemies/godfrey/stunned/godfrey_stunned_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostStunned[RIGHT] = new APSReader("images/enemies/godfrey/stunned/godfrey_stunned_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostStunned[LEFT] = new APSReader("images/enemies/godfrey/stunned/godfrey_stunned_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            ghostAggravated[UP] = new APSReader("images/enemies/godfrey/aggravated/godfrey_aggravated_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostAggravated[DOWN] = new APSReader("images/enemies/godfrey/aggravated/godfrey_aggravated_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostAggravated[RIGHT] = new APSReader("images/enemies/godfrey/aggravated/godfrey_aggravated_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            ghostAggravated[LEFT] = new APSReader("images/enemies/godfrey/aggravated/godfrey_aggravated_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            moRoaming[UP]  = new APSReader("images/enemies/mo/roaming/mo_roaming_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            moRoaming[DOWN]   = new APSReader("images/enemies/mo/roaming/mo_roaming_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            moRoaming[RIGHT]   = new APSReader("images/enemies/mo/roaming/mo_roaming_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            moRoaming[LEFT]   = new APSReader("images/enemies/mo/roaming/mo_roaming_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            moStunned[UP]    = new APSReader("images/enemies/mo/stunned/mo_stunned_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            moStunned[DOWN]    = new APSReader("images/enemies/mo/stunned/mo_stunned_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            moStunned[RIGHT]    = new APSReader("images/enemies/mo/stunned/mo_stunned_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            moStunned[LEFT]    = new APSReader("images/enemies/mo/stunned/mo_stunned_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            moAggravated[UP]    = new APSReader("images/enemies/mo/aggravated/mo_aggravated_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            moAggravated[DOWN]    = new APSReader("images/enemies/mo/aggravated/mo_aggravated_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            moAggravated[RIGHT]    = new APSReader("images/enemies/mo/aggravated/mo_aggravated_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            moAggravated[LEFT]    = new APSReader("images/enemies/mo/aggravated/mo_aggravated_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            LeopardoRoaming[UP]    = new APSReader("images/enemies/leopardo/roaming/leopardo_roaming_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoRoaming[DOWN]    = new APSReader("images/enemies/leopardo/roaming/leopardo_roaming_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoRoaming[RIGHT]    = new APSReader("images/enemies/leopardo/roaming/leopardo_roaming_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoRoaming[LEFT]    = new APSReader("images/enemies/leopardo/roaming/leopardo_roaming_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            LeopardoStunned[UP]    = new APSReader("images/enemies/leopardo/stunned/leopardo_stunned_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoStunned[DOWN]    = new APSReader("images/enemies/leopardo/stunned/leopardo_stunned_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoStunned[RIGHT]    = new APSReader("images/enemies/leopardo/stunned/leopardo_stunned_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoStunned[LEFT]    = new APSReader("images/enemies/leopardo/stunned/leopardo_stunned_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            LeopardoAggravated[UP]    = new APSReader("images/enemies/leopardo/aggravated/leopardo_aggravated_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoAggravated[DOWN]    = new APSReader("images/enemies/leopardo/aggravated/leopardo_aggravated_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoAggravated[RIGHT]    = new APSReader("images/enemies/leopardo/aggravated/leopardo_aggravated_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            LeopardoAggravated[LEFT]    = new APSReader("images/enemies/leopardo/aggravated/leopardo_aggravated_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            GraysonRoaming[UP] = new APSReader("images/enemies/grayson/roaming/grayson_roaming_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonRoaming[DOWN] = new APSReader("images/enemies/grayson/roaming/grayson_roaming_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonRoaming[RIGHT] = new APSReader("images/enemies/grayson/roaming/grayson_roaming_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonRoaming[LEFT] = new APSReader("images/enemies/grayson/roaming/grayson_roaming_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            GraysonStunned[UP] = new APSReader("images/enemies/grayson/stunned/grayson_stunned_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonStunned[DOWN] = new APSReader("images/enemies/grayson/stunned/grayson_stunned_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonStunned[RIGHT] = new APSReader("images/enemies/grayson/stunned/grayson_stunned_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonStunned[LEFT] = new APSReader("images/enemies/grayson/stunned/grayson_stunned_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            GraysonAggravated[UP] = new APSReader("images/enemies/grayson/aggravated/grayson_aggravated_upFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonAggravated[DOWN] = new APSReader("images/enemies/grayson/aggravated/grayson_aggravated_downFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonAggravated[RIGHT] = new APSReader("images/enemies/grayson/aggravated/grayson_aggravated_rightFace_spritesheet.png", 24, 24, 1, 4, 500);
            GraysonAggravated[LEFT] = new APSReader("images/enemies/grayson/aggravated/grayson_aggravated_leftFace_spritesheet.png", 24, 24, 1, 4, 500);
            
            hidden_cell = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/floors/hidden_cell.png"));
            cell_height = 24; //getHeight()/AiMazed2D.gameWindow.game.player.numRows;
            cell_width = 24;// getWidth()/AiMazed2D.gameWindow.game.player.numCols;
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g); 

            //accumulate delta time between calls
            cur_time = System.nanoTime();
            delta_time = (cur_time - last_time);
            last_time = cur_time;
            timer1 = timer1 + (delta_time/1000000.0);

            
            //update offsets
            offset_x = (getWidth()-(AiMazed2D.gameWindow.game.player.numCols*cell_width))/2;
            offset_y = (getHeight()-(AiMazed2D.gameWindow.game.player.numRows*cell_height))/2;
            
            //draw the dungeon grid elements
            for (int di=0; di < AiMazed2D.gameWindow.game.player.numCols; di++)
            {
                for (int dj=0; dj < AiMazed2D.gameWindow.game.player.numRows; dj++)
                {
                    int i = di+AiMazed2D.gameWindow.game.player.shift.x;
                    int j = dj+AiMazed2D.gameWindow.game.player.shift.y;

                    if (j < 0)
                    {
                        //draw dark cells (this is the area above the map, to prevent clutter with interface elements)
                        g.setColor(new Color(0, 0, 45, 255));
                        g.fillRect(offset_x+di*cell_width+1, offset_y+dj*cell_height+1, cell_width, cell_height);
                    }
                    else
                    {
                        if (AiMazed2D.gameWindow.game.player.dungeon.darkCells[i][j] == true)
                        {
                            //draw dark cells
                            //g.setColor(new Color(0, 0, 45, 255));
                            //g.fillRect(offset_x+di*cell_width+1, offset_y+dj*cell_height+1, cell_width, cell_height);
                            g.drawImage(hidden_cell, offset_x+di*cell_width+1-2, offset_y+dj*cell_height+1-2, null);
                        }
                        else
                        {
                            //draw clean floors
                            g.drawImage(floor[2], offset_x+di*cell_width+1, offset_y+dj*cell_height+1, cell_width, cell_height, null);
                            g.setColor(Color.BLUE);         
                            wallcnt = 0;
                            for (int k=0; k<4; k++)
                            {
                                if (AiMazed2D.gameWindow.game.player.dungeon.grid[i][j][k] == 0)
                                {
                                    wallcnt++;
                                    //g.drawLine(offset_x+(di+_D[k][0])*cell_width, offset_y+(dj+_D[k][1])*cell_height, offset_x+(di+_D[k][2])*cell_width, offset_y+(dj+_D[k][3])*cell_height);
                                }
                            }
                            if (wallcnt == 4)
                            {
                                //draw walls
                                g.setColor(Color.BLUE);
                                //g.fillRect(offset_x+di*cell_width+1, offset_y+dj*cell_height+1, cell_width-1, cell_height-1);
                                g.drawImage(wall[(i*j)%3], offset_x+di*cell_width, offset_y+dj*cell_height, cell_width, cell_height, null);

                            }

                            //Draw Crystals
                            g.setColor(Color.YELLOW);
                            if (AiMazed2D.gameWindow.game.player.dungeon.crystals[i][j] == true)
                            {
                                //g.fillOval(offset_x+di*cell_width+3, offset_y+dj*cell_height+3, 3, 3);
                                g.drawImage(crystals[((i*j)%2) + (AiMazed2D.gameWindow.game.player.level%10)].getSprite(), (offset_x+di*cell_width), (offset_y+dj*cell_height), null);
                            }
                        }
                    }
                }
            }


            //Draw Key & Exit
            if (AiMazed2D.gameWindow.game.player.dungeon.darkCells[AiMazed2D.gameWindow.game.player.dungeon.key.x][AiMazed2D.gameWindow.game.player.dungeon.key.y] == false && AiMazed2D.gameWindow.game.player.hasKey == false)
            {
                //g.setColor(Color.BLUE);
                //g.fillRect(offset_x+(AiMazed2D.gameWindow.game.player.dungeon.key.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.dungeon.key.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width-1, cell_height-1);
                g.drawImage(key.getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.dungeon.key.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.dungeon.key.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
            }

            if (AiMazed2D.gameWindow.game.player.dungeon.darkCells[AiMazed2D.gameWindow.game.player.dungeon.exit.x][AiMazed2D.gameWindow.game.player.dungeon.exit.y] == false)
            {
                //g.setColor(Color.PINK);
                //g.fillRect(offset_x+(AiMazed2D.gameWindow.game.player.dungeon.exit.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.dungeon.exit.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width-1, cell_height-1);
                g.drawImage(exit.getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.dungeon.exit.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.dungeon.exit.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
            }
            
            
            //draw textFloats and update
            for (int i = 0; i < textFloats.size(); i++)
            {
                if (textFloats.elementAt(i).timeLeft > 0)
                {
                    
                    g.setColor(textFloats.elementAt(i).color);
                    textFloats.elementAt(i).color = new Color(textFloats.elementAt(i).color.getRed(), textFloats.elementAt(i).color.getGreen(), textFloats.elementAt(i).color.getBlue(), (int)(255*(textFloats.elementAt(i).timeLeft/1000.0)));
                    g.drawString(textFloats.elementAt(i).text, textFloats.elementAt(i).x, textFloats.elementAt(i).y);
                    g.drawImage(sparkle.getSprite(), textFloats.elementAt(i).x, textFloats.elementAt(i).y, cell_width, cell_height, null);
                    textFloats.elementAt(i).timeLeft -= delta_time/1000000.0;
                    textFloats.elementAt(i).y -= (delta_time/1000000.0)/16.0;
                }
            }
            
            //remove dead textFloats
            for (int h = textFloats.size()-1; h>= 0; h--)
            {
                if (textFloats.elementAt(h).timeLeft <= 0)
                {
                    textFloats.remove(h);
                }
            }

            //initializant for qubey face
            if (AiMazed2D.gameWindow.game.player.face < 0 || AiMazed2D.gameWindow.game.player.face > 3) AiMazed2D.gameWindow.game.player.face = new Random().nextInt(4);
            
            //draw qubey
            if (AiMazed2D.gameWindow.game.player.dead == false) 
            {
                if (AiMazed2D.gameWindow.game.player.stunTime > 0)
                {
                    g.drawImage(qubey_blazing[AiMazed2D.gameWindow.game.player.face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                }
                else
                {
                    g.drawImage(blocky[AiMazed2D.gameWindow.game.player.face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                }
                
            }
            else if (AiMazed2D.gameWindow.game.player.dead == true)
            {
                g.drawImage(blocky[(int)(timer1)%4].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
            }
            

            //draw items            
            for (int i = 0; i < AiMazed2D.gameWindow.game.player.dungeon.items.size(); i++)
            {
                if (AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).exists == true && AiMazed2D.gameWindow.game.player.dungeon.darkCells[AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).x][AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).y] == false)
                {
                    if (AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).type.equals(Item.REDCRYSTAL))
                    {
                        g.drawImage(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).image.getSprite(), (offset_x+(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width), (offset_y+(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height), null);
                    }
                    else
                    {
                        g.drawImage(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).image.getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width, offset_y+(AiMazed2D.gameWindow.game.player.dungeon.items.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height, cell_width, cell_height, null);
                    }
                }
            }
            
            //draw ghosts
            for (int i = 0; i < AiMazed2D.gameWindow.game.player.ghosts.size(); i++)
            {
                //draw ghosts
                    if (AiMazed2D.gameWindow.game.player.dungeon.darkCells[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x][AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y] == false)
                    {


                        if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode != Ghost.DEAD) 
                        {


                            if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode == Ghost.STUNNED)
                            {
                                if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GODFREY)
                                {
                                    g.drawImage(ghostStunned[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.MO)
                                {
                                    g.drawImage(moStunned[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.LEOPARDO)
                                {
                                    g.drawImage(LeopardoStunned[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GRAYSON)
                                {
                                    g.drawImage(GraysonStunned[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                            }
                            else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode == Ghost.ROAMING)
                            {
                                if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GODFREY)
                                {
                                    g.drawImage(ghostRoaming[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.MO)
                                {
                                    g.drawImage(moRoaming[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.LEOPARDO)
                                {
                                    g.drawImage(LeopardoRoaming[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GRAYSON)
                                {
                                    g.drawImage(GraysonRoaming[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                            }
                            else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode == Ghost.AGGRAVATED)
                            {
                                if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GODFREY)
                                {
                                    g.drawImage(ghostAggravated[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.MO)
                                {
                                    g.drawImage(moAggravated[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.LEOPARDO)
                                {
                                    g.drawImage(LeopardoAggravated[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                                else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).name == Ghost.GRAYSON)
                                {
                                    g.drawImage(GraysonAggravated[AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).face].getSprite(), offset_x+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x)*cell_width+1, offset_y+(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y)*cell_height+1, cell_width, cell_height, null);
                                }
                            }
                        }
                    }
            }
            
            if (AiMazed2D.gameWindow.game.player.paused == false)
            {
                //Update the Player Location
                if (AiMazed2D.gameWindow.game.player.dead == false && timer1 > 50)
                {

                    /*
                    BFS bfs = new BFS();
                    Node togo = new Node();
                    togo = bfs.findPath(AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y, goalx, goaly, AiMazed2D.gameWindow.game.player.dungeon);
                    timer1 = 0;

                    if (togo != null) AiMazed2D.gameWindow.game.player.handleNewLocation(togo.x, togo.y);
                    */



                    timer1 = 0;
                    if (left_key)
                    {
                        if (AiMazed2D.gameWindow.game.player.canGo(LEFT))
                        {
                            AiMazed2D.gameWindow.game.player.handleNewLocation(AiMazed2D.gameWindow.game.player.x-1, AiMazed2D.gameWindow.game.player.y);
                            timer1 = 0;
                        }

                    }

                    if (right_key)
                    {
                        if (AiMazed2D.gameWindow.game.player.canGo(RIGHT))
                        {
                            AiMazed2D.gameWindow.game.player.handleNewLocation(AiMazed2D.gameWindow.game.player.x+1, AiMazed2D.gameWindow.game.player.y);
                            timer1 = 0;
                        }
                    }

                    if (up_key)
                    {
                        if (AiMazed2D.gameWindow.game.player.canGo(UP))
                        {
                            AiMazed2D.gameWindow.game.player.handleNewLocation(AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y-1);
                            timer1 = 0;
                        }
                    }

                    if (down_key)
                    {
                        if (AiMazed2D.gameWindow.game.player.canGo(DOWN))
                        {
                            AiMazed2D.gameWindow.game.player.handleNewLocation(AiMazed2D.gameWindow.game.player.x, AiMazed2D.gameWindow.game.player.y+1);
                            timer1 = 0;
                        }
                    }

                    if (shift_key == true)
                    {
                        down_key = false;
                        up_key = false;
                        right_key = false;
                        left_key = false;
                    }
                }
                else if (AiMazed2D.gameWindow.game.player.dead == true)
                {
                    timer1 = 0;
                }


                //move ghosts
                for (int i = 0; i< AiMazed2D.gameWindow.game.player.ghosts.size(); i++)
                {

                    



                    //run into ghost
                    if (AiMazed2D.gameWindow.game.player.dead == false && AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x == AiMazed2D.gameWindow.game.player.x && AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y == AiMazed2D.gameWindow.game.player.y)
                    {
                        if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode == Ghost.STUNNED)
                        {
                            AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode = Ghost.DEAD;
                            GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).x-AiMazed2D.gameWindow.game.player.shift.x), GameWindow.offset_y + GameWindow.cell_height*(AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).y-AiMazed2D.gameWindow.game.player.shift.y), "+250", 1000.0, new Color(255, 0, 0, 255)));
                            AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).scheduleRevival();

                            AiMazed2D.gameWindow.game.player.score+= 250;
                            AiMazed2D.gameWindow.game.player.addSoulJar();
                        }
                        else if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).mode != Ghost.DEAD)
                        {
                            //you go kerbonkered and need to respawn somewhere else
                            AiMazed2D.gameWindow.game.player.dead = true;
                            AiMazed2D.gameWindow.game.player.life--;
                            textFloats_heartDeaths.add(new TextFloat(AiMazed2D.gameWindow.game.player.life, 0, "+1", 1000.0, new Color(255, 255, 0, 255)));
                            AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).px = AiMazed2D.gameWindow.game.player.x;
                            AiMazed2D.gameWindow.game.player.ghosts.elementAt(i).py = AiMazed2D.gameWindow.game.player.y;
                            for (int z = 0; z < AiMazed2D.gameWindow.game.player.ghosts.size(); z++)
                            {
                                AiMazed2D.gameWindow.game.player.ghosts.elementAt(z).mode = Ghost.ROAMING;
                            }

                            new java.util.Timer().schedule(new TimerTask() {
                                public synchronized void run() {
                                    AiMazed2D.gameWindow.game.player.dead = false;
                                    if (AiMazed2D.gameWindow.game.player.life < 1)
                                    {
                                        AiMazed2D.gameWindow.game.player.gameOver();
                                    }
                                    else
                                    {
                                        AiMazed2D.gameWindow.game.player.setRandomLocation();

                                        //lose some visibility
                                        Random rgen = new Random();
                                        for (int a = 0; a < AiMazed2D.gameWindow.game.player.dungeon.width; a++)
                                        {
                                            for (int j = 0; j < AiMazed2D.gameWindow.game.player.dungeon.height; j++)
                                            {
                                                if (rgen.nextInt(1000) < 15)
                                                {
                                                    //blob a radius of darkness
                                                    for (int da = -3; da <= 3; da ++)
                                                    {
                                                        for (int dj = -3; dj <= 3; dj++)
                                                        {
                                                            if ((Math.abs(da)+Math.abs(dj)) <= 3)
                                                            {
                                                                if (da+a >= 0 && da+a < AiMazed2D.gameWindow.game.player.dungeon.width && dj+j >= 0 && dj+j < AiMazed2D.gameWindow.game.player.dungeon.height)
                                                                {
                                                                    AiMazed2D.gameWindow.game.player.dungeon.darkCells[a+da][j+dj] = true;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        
                                        this.cancel();

                                    }
                                }
                            }, 3000);

                        }

                        
                    }
                }   
            }
        }  
    }
}
