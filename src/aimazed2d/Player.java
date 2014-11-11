/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.awt.Color;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joe
 */
public class Player {
        DungeonGenerator dungeon;
        int x;
        int y;
        int numRows=22;
        int numCols=29;
        int face;
        int score;
        int level;
        int type = 0;
        int life;
        int souljars;
        int torches;
        
        boolean dead = false;
        
        boolean paused = false;
        
        int stunTime;
        boolean stunActivated;
        
        Timer ghostStunner, detorcher, demagnetizer;
        
        Vector<Ghost> ghosts;
        
        public final int LEFT = 1;
        public final int RIGHT = 0;
        public final int DOWN = 3;
        public final int UP = 2;
        boolean hasKey, hasExit, hasJoy;
        boolean seesExit;
        boolean gazing = false;
        
        //player attributes
        int moveSpeed = 80;     //speed of movement.                  MIN: 30,NORM =80, MAX:200
        double moveTimer = 0.0;
        int crystalRadius = 0;  //crystals draw in to you from afar.  MIN: 0, NORM = 1, MAX: 7
        int torchRadius = 3;    //light up the area around you.       MIN: 0, NORM = 3, MAX: 7
        double exploreReq = 0.80;
        int totalCrystals = 0;
        int crystalsNeeded = 0;
        int crystalsObtained = 0;
        int crystalPoint = 1;  //points you get for each crystal.

        Agent.Darkway gaze_target;
        Vector<Agent.Darkway> Darkways = new Vector<Agent.Darkway>();
        boolean seesKey;
                        int g = 0;

        static int _DX[] = {1, -1, 0, 0};
        static int _DY[] = {0, 0, -1, 1};

        class MoverThread extends Thread
        {
            int gg;
            public MoverThread(int ghost, Object something)
            {
                super((Runnable)(something));
                gg = ghost;
            }
        }
        
        public boolean safeRadiusFromGhosts() {
            for (int g = 0; g < ghosts.size(); g++)
            {
                if (Agent.distanceEuclidean(new Node(ghosts.elementAt(g).x, ghosts.elementAt(g).y), new Node(x, y)) < 20) return false;
            }
            return true;
        }
        
        public class Shift{
            int x;
            int y;
            public Shift()
            {
                x = 0;
                y = 0;
            }
            public void update(int old_x, int old_y, int new_x, int new_y)
            {
                
                
                x = Math.min(Math.max(0, (new_x - (numCols/2))), (dungeon.width - numCols));
                y = Math.min(Math.max(-3, (new_y - (numRows/2))), (dungeon.height - numRows));
                    
                
                
            }
        }

        Shift shift;
    
        public void newGame()
        {
            //defaults
            torches = 3;
            life = 3;
            torchRadius = crystalRadius+3;
            crystalRadius = 1;
            score = 0;
            level = 0;
            
            if (detorcher != null)
            {
                detorcher.cancel();
            }
            detorcher = new Timer();
            detorcher.scheduleAtFixedRate(new TimerTask() {
                int seconds = 0;
                boolean firstRun = true;
                public void run() {
                    if (AiMazed2D.gameWindow.game.player.paused == false)
                    {
                        seconds += 1;
                        if (seconds >= 60)
                        {
                            firstRun = false;
                            seconds = 0;

                            torches--;
                            System.out.println("Detorching -1.  Torch Status: " + torches);
                            if (torches < 0)
                            {
                                gameOver();
                            }
                        }
                    }
                }
            }, 0, 1000);
            
            
            if (demagnetizer != null)
            {
                demagnetizer.cancel();
            }
            demagnetizer = new Timer();
            demagnetizer = new Timer();
            demagnetizer.scheduleAtFixedRate(new TimerTask() {
                boolean firstRun = true;
                int seconds = 0;
                public void run() {
                    if (AiMazed2D.gameWindow.game.player.paused == false)
                    {
                        seconds += 1;
                        if (seconds >= 180)
                        {
                            firstRun = false;
                            seconds = 0;
                            crystalRadius = Math.max(1, crystalRadius-1);
                            System.out.println("Demagnet -1.  Magnet Status: " + crystalRadius);
                        }
                    }
                }
            }, 0, 1000);
            
            if (ghostStunner != null)
            {
                ghostStunner.cancel();
            }
            ghostStunner = new Timer();
            ghostStunner.scheduleAtFixedRate(new TimerTask() {
                int seconds = 0;
                public void run() {
                    if (AiMazed2D.gameWindow.game.player.paused == false && stunTime > 0)
                    {
                        stunTime-= 1;
                        if (stunTime <= 0)
                        {
                            for (int g = 0; g < ghosts.size(); g++)
                            {
                                if (ghosts.elementAt(g).mode != Ghost.DEAD)
                                {
                                    ghosts.elementAt(g).mode = Ghost.ROAMING;
                                }
                            }
                        }
                    }
                }
            }, (int)(1000), (int)(1000));
            
            newDungeon();
            
            
            
            
        }
        public Player()
        {
            System.out.println("boo");
            x = 0;
            y = 0;
            score = 0;
            level = 0;
            hasKey = false;
            hasExit = false;
            shift = new Shift();
            ghosts = new Vector<Ghost>();         
            paused = true;
            
            dungeon = new DungeonGenerator();
            
            
            
            
            
        }
        
        public void setRandomLocation()
        {
            Random rgen = new Random();
            do
            {
                x = rgen.nextInt(dungeon.width);
                y = rgen.nextInt(dungeon.height);
            } while  (dungeon.isNotWalkableCell(x,y));
            dungeon.lightUpCellsAround(x,y,torchRadius+5);
            shift.update(0, 0, x, y);
        }
        
        public void generateDungeon(int w, int j, String dunType)
        {
            dungeon.buildDungeon(w, j, level, dunType);
        }
      
        public void gameOver()
        {
            paused = true;
            AiMazed2D.gameWrapWindow.setScore(score);
            AiMazed2D.windowController = 4;
        }
        public void newDungeon()
        {
            //build new dungie
                score+= 1000;
                if (level == 0) 
                {
                    //generateDungeon(64, 48, "BSP Tree Dungeon");
                    generateDungeon(64, 48, "Level");
                    score = 0;
                }
                else if (level > 9)
                {
                    generateDungeon(dungeon.width+10, dungeon.height+8, "BSP Tree Dungeon");
                }
                else 
                {
                    //generateDungeon(dungeon.width+(dungeon.width)/(level+2), dungeon.height+(dungeon.height)/(level+2), "BSP Tree Dungeon");
                    //generateDungeon(dungeon.width+10, dungeon.height+8, "BSP Tree Dungeon");
                    generateDungeon(64, 48, ("Level"));
                }
                setRandomLocation();
                hasExit = false;
                hasKey = false;
                seesExit = false;
                seesKey = false;
                hasJoy = false;
                gazing = false;
                Darkways.clear();
                
                level+= 1;
                Random rgen = new Random();
                exploreReq = Math.min(1, (rgen.nextInt(20) + 50)/100.0);
                totalCrystals = dungeon.countCrystals();
                crystalsNeeded = (int)(exploreReq * totalCrystals);
                crystalsObtained = 0;
                face = -9; // reset code in agent
                handleNewLocation(x, y);
                
                //Add one more ghost
                if (level % 3 == 2)
                {
                    ghosts.add(new Ghost(dungeon));
                }
                
                //reset each ghost to accomodate new dungeon
                for (int i = 0; i < ghosts.size(); i++)
                {
                    ghosts.elementAt(i).dungeon = dungeon;
                    ghosts.elementAt(i).setRandomLocation();
                    ghosts.elementAt(i).px = x;
                    ghosts.elementAt(i).py = y;
                }
                
                paused = false;
        }
        
        public boolean canGo(int dir)
        {
            return dungeon.canGo(dir, x, y);
        }
        
        public void grabCrystals(int cx, int cy, int recurse)
        {
            Shift cshift = new Shift();
            cshift.update(0, 0, cx, cy);
            
            for (int k = 0; k < 4; k++)
            {
                if (dungeon.crystals[cx][cy] == true)
                {
                    dungeon.crystals[cx][cy] = false;
                    GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(cx-shift.x), GameWindow.offset_y + GameWindow.cell_height*(cy-shift.y), "+1", 500.0, new Color(255, 255, 0, 255)));
                    score+= crystalPoint;
                    if (AiMazed2D.gameWindow.game.player.stunTime > 0)
                    {
                        //double score bonus
                        score += crystalPoint;
                    }
                    crystalsObtained++;
                }
                if (!dungeon.isNotWalkableCell(cx+_DX[k], cy+_DY[k]) && recurse > 0) 
                {
                    grabCrystals(cx+_DX[k],cy+_DY[k],recurse-1);
                }
            }
        }
        public void handleNewLocation(int new_x, int new_y)
        {
            //Update Shift
            shift.update(x, y, new_x, new_y);
            
            //get new face
            if ((new_x - x) > 0) face = RIGHT;
            else if ((new_x - x) < 0) face = LEFT;
            else if ((new_y - y) > 0) face = DOWN;
            else if ((new_y - y) < 0) face = UP;
            
            //System.out.println(face);
            //Update location
            x = new_x;
            y = new_y;
            
            //Handle Crystals
            int score_before = score;
            grabCrystals(x,y,crystalRadius);
            if (score > score_before)
            {
                //AiMazed2D.AiMazed2DAF.playAudio(1);
            }
            
            if (hasJoy == false)
            {
                if (crystalsObtained >= crystalsNeeded)
                {
                    hasJoy = true;
                }
            }
            
            //Handle Walking on Key
            if (dungeon.key.x == x && dungeon.key.y == y && hasKey == false)
            {
                hasKey = true;
                dungeon.exit.exists = false;
                score+= dungeon.key.points;
                GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(dungeon.key.x-shift.x), GameWindow.offset_y + GameWindow.cell_height*(dungeon.key.y-shift.y), "+" + dungeon.key.points, 1000.0, new Color(255, 0, 0, 255)));
            }
            
            //Handle Walking on Exit for first time
            if (dungeon.exit.x == x && dungeon.exit.y == y && hasExit == false)
            {
                hasExit = true;
                score+= dungeon.exit.points;
                GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(dungeon.exit.x-shift.x), GameWindow.offset_y + GameWindow.cell_height*(dungeon.exit.y-shift.y), "+"+dungeon.exit.points, 1000.0, new Color(255, 0, 0, 255)));
            }
            
            //Handle Walking on Exit subsequent times
            if (dungeon.exit.x == x && dungeon.exit.y == y && hasKey == true && hasJoy == true)
            {
                newDungeon();
            }
            
            
            //handle pick up of items
            Item thisItem;
            for (int i = 0; i < dungeon.items.size(); i++)
            {
                thisItem = dungeon.items.elementAt(i);
                if (thisItem.exists == true && thisItem.x == x && thisItem.y == y)
                {
                    thisItem.exists = false;
                    if (thisItem.type.equals(Item.REDCRYSTAL))
                    {
                        score += 25;
                        GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(thisItem.x-shift.x), GameWindow.offset_y + GameWindow.cell_height*(thisItem.y-shift.y), "+25", 1000.0, new Color(255, 0, 0, 255)));
                        for (int g = 0; g < ghosts.size(); g++)
                        {
                            if (ghosts.elementAt(g).mode != Ghost.DEAD)
                            {
                                ghosts.elementAt(g).mode = Ghost.STUNNED;
                            }
                        }
                        stunTime = 5;
                    }
                    else if (thisItem.type.equals(Item.MAGNET))
                    {
                        AiMazed2D.AiMazed2DAF.playAudio(2);
                        crystalRadius += 1;
                        score += 100;
                        GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(thisItem.x-shift.x), GameWindow.offset_y + GameWindow.cell_height*(thisItem.y-shift.y), "+50", 1000.0, new Color(255, 0, 0, 255)));
                        if (crystalRadius > 6) crystalRadius = 6;
                        torchRadius = crystalRadius +3;
                    }
                    else if (thisItem.type.equals(Item.TORCH))
                    {
                        torches += 1;
                        score += 50;
                        GameWindow.textFloats.add(new TextFloat(GameWindow.offset_x + GameWindow.cell_width*(thisItem.x-shift.x), GameWindow.offset_y + GameWindow.cell_height*(thisItem.y-shift.y), "+50", 1000.0, new Color(255, 0, 0, 255)));
                        if (torches > 5) torches = 5;
                    }
                    i--;
                }
            }
            //Handle lighting up cells
            dungeon.lightUpCellsAround(x,y,torchRadius);
            
            
            //update where ghosts know player is
            for (int i = 0; i < ghosts.size(); i++)
            {
                ghosts.elementAt(i).px = x;
                ghosts.elementAt(i).py = y;
            }
            
            
            
            
        }
        
        
        
        public void addSoulJar()
        {
            souljars++;
            if (souljars>=5)
            {
                souljars=0;
                life++;
                if (life > 5) life = 5;
            }
        }
        
    
}