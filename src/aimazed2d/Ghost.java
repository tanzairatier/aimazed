/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

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




public class Ghost extends Agent{
    int px, py;
    int mode;
    int face;
    String name;
    static String LEOPARDO = "LEOPARDO";
    static String MO = "MO";
    static String GODFREY = "GODFREY";
    static String GRAYSON = "GRAYSON";
    static int ROAMING = 0;
    static int STUNNED = 1;
    static int AGGRAVATED = 2;
    static int DEAD = 3;
    boolean reviverActivated;
    int reviverTimer;
    Timer ghostMover, ghostReviver;
    TimerTask GM;
    
    public Ghost(DungeonGenerator dgn)
    {
        dungeon = dgn;
        mode = ROAMING;
        
        int r = new Random().nextInt(4);
        if (r == 0)
        {
            name = Ghost.GODFREY;
            
        }
        else if (r == 1)
        {
            name = Ghost.MO;
        }
        else if (r == 2)
        {
            name = Ghost.LEOPARDO;
        }
        else if (r == 3)
        {
            name = Ghost.GRAYSON;
        }
        
        ghostMover = new Timer();
         GM = new TimerTask() {
            int timer = 0;
            public void run() {
                    if (AiMazed2D.gameWindow.game.paused==false && mode != DEAD)
                        {
                            timer += 25;
                            if (timer >= moveSpeed)
                            {
                                timer = 0;
                                moveAgent();
                            }
                        }
                }
        };
        ghostMover.scheduleAtFixedRate(GM, 0, 25);
        
       
        
        
        ghostReviver = new Timer();
        ghostReviver.scheduleAtFixedRate(new TimerTask() {
                boolean revived = false;
                public void run() {
                    if (!revived && AiMazed2D.gameWindow.game.player.paused==false && reviverTimer > 0)
                        {
                            reviverTimer-= 1;
                            if (reviverTimer <= 0 && revived == false)
                            {
                                revived = true;
                                mode = ROAMING;
                                reviverActivated = false;
                                setRandomLocation();
                            }
                        }
                }
            }, 0, (int)(1000));

    }
    
    @Override
    public void setRandomLocation()
    {
        Random rgen = new Random();
        do
        {
            x = rgen.nextInt(dungeon.width);
            y = rgen.nextInt(dungeon.height);
        } while  (dungeon.isNotWalkableCell(x,y));
    }
    

    
    @Override
    public void moveAgent()
    {
        //determine what direction to move in (north/south/east/west)
        
        //this is the direction the agent will move in
        int chosen_direction;
        
        //if agent doesnt have the key and sees the key
        
        if (mode == ROAMING)
        {
            moveSpeed = 300;
            Random rgen = new Random();
            //chosen_direction = cp_picker();
            chosen_direction = rgen.nextInt(4);
            if (canGo(chosen_direction)) handleNewLocation(x+_DX[chosen_direction],y+_DY[chosen_direction]);
        }
        else if (mode == STUNNED)
        {
            moveSpeed = 2000;
            Random rgen = new Random();
            //chosen_direction = cp_picker();
            chosen_direction = rgen.nextInt(4);
            if (canGo(chosen_direction)) handleNewLocation(x+_DX[chosen_direction],y+_DY[chosen_direction]);
        }
        else if (mode == AGGRAVATED)
        {
            moveSpeed = 100;
            BFS bfs = new BFS();
            if (bfs != null)
            {
                Vector<Node> path = bfs.findPath(x,y,px, py, dungeon);
                if (path != null)
                {
                    if (path.size() > 0)
                    {
                        Node togo = path.lastElement();
                        handleNewLocation(togo.x, togo.y);
                    }
                }
            }
            if (distanceEuclidean(new Node(x, y), new Node(px, py)) > 15) mode = ROAMING;    
        }
        
        
    }
    
    public boolean scan(int x0, int y0, int gx, int gy, int depth)
    {
        if (distanceManhattan(new Node(x0,y0), new Node(gx,gy)) < 10)
        {
            BFS bfs = new BFS();
            Vector<Node> path = new Vector<Node>();
            path = bfs.findPath(x0,y0, gx,gy, dungeon);
            if (path != null)
            {
                if (path.size() <= 15) return true;
            }
        }
        return false;
    }
    
    public void handleNewLocation(int new_x, int new_y)    
    {
        
        //set the face
        int oldface = face;
        if ((new_x - x) > 0) face = RIGHT;
        else if ((new_x - x) < 0) face = LEFT;
        else if ((new_y - y) > 0) face = DOWN;
        else if ((new_y - y) < 0) face = UP;

        if (oldface != face) moveSpeed*= 2;
        x = new_x;
        y = new_y;
        
        
            
        
        if (mode == Ghost.ROAMING)
        {
            if (AiMazed2D.gameWindow.game.player.dead == false)
            {
                if (scan(x, y, px, py, 5) == true)
                {
                    mode = Ghost.AGGRAVATED;
                }
            }
        }
        
        //ghost runs into some other ghost
        int collisionCount = 0;
        for (int j = 0; j < AiMazed2D.gameWindow.game.player.ghosts.size(); j++)
        {
            if (AiMazed2D.gameWindow.game.player.ghosts.elementAt(j).x == x && 
                AiMazed2D.gameWindow.game.player.ghosts.elementAt(j).y == y)
            {
                collisionCount++;
                
            }
            
        }
        
        //collisionCount SHOULD be >= 1.  Because it should always find itself at least once.
        if (collisionCount > 1)
        {
            mode = DEAD;
            scheduleRevival();
        }
        
    }

   
    public void lightUp()
    {
        for (int i=0; i<dungeon.width; i++)
        {
            for (int j=0; j<dungeon.height; j++)
            {
                dungeon.darkCells[i][j] = false;
                dungeon.crystals[i][j] = false;
            }
        }
    }
    
    
    public void scheduleRevival()
    {
        reviverActivated = true;
        reviverTimer = 60;
    }
}
