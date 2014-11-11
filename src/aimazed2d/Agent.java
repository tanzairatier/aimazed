/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.util.Random;
import java.util.Vector;

/**
 *
 * @author joe
 */
public class Agent extends Player {
    
    
            int _DX[] = {1, -1, 0, 0};
        int _DY[] = {0, 0, -1, 1};
      
    Vector<Node> GazePath = new Vector<Node>();
        
    public Agent()
    {
        
    }
    public class Darkway
    {
        int x;
        int y;
        
        public Darkway(int a, int b)
        {
            x = a;
            y = b;
        }
    }
    
    
    
    public static double choice_points[][] = {
        /*
         * left down right up
         */
        {0.32, 0.00, 0.68, 0.00}, //CP4   //[0]
        {0.00, 0.42, 0.58, 0.00}, //CP5   //[1]
        {0.51, 0.49, 0.00, 0.00}, //CP6 
        {0.00, 0.33, 0.67, 0.00}, //CP7   //[3]
        {0.00, 0.00, 0.44, 0.56}, //CP8
        {0.00, 0.59, 0.41, 0.00}, //CP9
        {0.51, 0.00, 0.49, 0.00}, //CP10  //[6]
        {0.46, 0.00, 0.00, 0.54}, //CP11
        {0.00, 0.00, 0.54, 0.46}, //CP12  //[8]
        {0.00, 0.57, 0.00, 0.43}, //CP13
        {0.46, 0.54, 0.00, 0.00}, //CP14  //[10]
        {0.46, 0.00, 0.00, 0.54}, //CP15
        {0.00, 0.21, 0.52, 0.27}, //CP16  //[12]
        {0.22, 0.00, 0.30, 0.48}, //CP17
        {0.43, 0.18, 0.00, 0.39}, //CP18  //[14]
        {0.31, 0.54, 0.15, 0.00}  //CP19        
    };
    
    public void moveAgent()
    {
        //determine what direction to move in (north/south/east/west)
        
        //this is the direction the agent will move in
        int chosen_direction;
        
        //if agent doesnt have the key and sees the key
        if ((!hasKey) && (seesKey))
        {
            //pathfind towards the key
            //System.out.println("moving towards the key");
            BFS bfs = new BFS();
            Vector<Node> path = bfs.findPath(x,y,dungeon.key.x, dungeon.key.y, dungeon);
            if (path != null)
            {
                Node togo = path.lastElement();
                handleNewLocation(togo.x, togo.y);
            }
        }
        else if ((hasKey) && (seesExit) && ((double)(crystalsObtained)/(double)(totalCrystals)) > 0.80)  //*****TODO: CRYSTAL REQUIREMENT*****//
        {
            //pathfind towards exit 
            //System.out.println("moving towards the exit");
            BFS bfs = new BFS();
            Vector<Node> path = bfs.findPath(x,y,dungeon.exit.x, dungeon.exit.y, dungeon);
            if (path != null)
            {
                Node togo = path.lastElement();
                handleNewLocation(togo.x, togo.y);
            }
        }
        else
        {
            //run darkway decision algorithm
            if (gazing == false)
            {
                //System.out.println("running cp picker");
                chosen_direction = cp_picker();
                if (canGo(chosen_direction)) handleNewLocation(x+_DX[chosen_direction],y+_DY[chosen_direction]);
                if (gazing == true)
                {
                    if (x == gaze_target.x && y == gaze_target.y) gazing = false;
                }
            }
            else
            {
                //System.out.println("moving towards a darkway");
                BFS bfs = new BFS();
                Vector<Node> path = bfs.findPath(x,y,gaze_target.x, gaze_target.y, dungeon);
                //System.out.println(togo);
                if (path != null) 
                {
                    Node togo = path.lastElement();
                    handleNewLocation(togo.x, togo.y);
                    if (togo.x == gaze_target.x && togo.y == gaze_target.y) gazing = false;
                }
                
            }
        }
        
        
    }
    public static double distanceEuclidean(Node A, Node B)
    {
        return Math.sqrt( Math.pow((A.x - B.x),2) + Math.pow((A.y - B.y),2) );
    }
    public static double distanceManhattan(Node A, Node B)
    {
        return Math.abs(A.x - B.x) + Math.abs(A.y - B.y);
    }
    
    public int get_on_track()
    {
        //tool used when the agent is at a spot in the dungeon where each direction is already explored
        //route the agent to the nearest darkway point and return the direction in that step closer
        
        //System.out.println("Getting on track...");
        
        double min = 999999999.0;
        double test;
        Darkway nearest = new Darkway(-2,-2);
        
        if (gazing == false)
        {
            //find nearest darkway
            for (int i = 0; i < Darkways.size(); i++)
            {
                test = distanceEuclidean(new Node(x, y), new Node(Darkways.elementAt(i).x, Darkways.elementAt(i).y));
                if (test <= min && test > 0)
                {
                    min = test;
                    nearest = Darkways.elementAt(i);
                }
            }
            //System.out.println("chosen nearest darkway: " + "(" + nearest.x + "," +nearest.y+")");
            gazing = true;
            gaze_target = new Darkway(nearest.x, nearest.y);
            
        }
        
        if (nearest.x == -2 && nearest.y == -2) 
        {
            min = 99999999.9;
            //find nearest crystal instead
            for (int i = 0; i < dungeon.width; i++)
            {
                for (int j = 0; j < dungeon.height; j++)
                {
                    if (dungeon.darkCells[i][j] == false && dungeon.crystals[i][j] == true)
                    {
                        test = distanceEuclidean(new Node(x,y), new Node(i,j));
                        if (test <= min)
                        {
                            min = test;
                            nearest = new Darkway(i,j);
                        }
                    }
                }
            }
            gaze_target = new Darkway(nearest.x, nearest.y);
        }
        
        BFS bfs = new BFS();
        Vector<Node> list = bfs.findPath(x,y,gaze_target.x, gaze_target.y, dungeon);
        if (list != null)
        {
            Node togo = list.lastElement();
            if (togo != null)
            {
                    if ((togo.x - x) > 0) return RIGHT;
                    else if ((togo.x - x) < 0) return LEFT;
                    else if ((togo.y - y) > 0) return DOWN;
                    else if ((togo.y - y) < 0) return UP;
                    else return -1;
            }
            else
            {
                return -17;
            }
        }
        else
        {
            return -23;
        }
        
        
        
        
        
    }
    
    public int roll_biased_dice(double odds[])
    {
        double cumulation = 0.0;
        
        Random rgen = new Random();
        double rand = (rgen.nextInt(99)+1)/100.0;
        
        for (int i = 0; i < 4; i++)
        {
            cumulation += odds[i];
            if (rand <= cumulation) return i;
        }
        return 0; //immposible branch
        
        
    }
    public boolean open_tester(boolean open_dirs[], boolean b1, boolean b2, boolean b3, boolean b4)
    {
        return ((open_dirs[0] == b1) && (open_dirs[1] == b2) && (open_dirs[2] == b3) && (open_dirs[3] == b4));
    }
    
    
    public int cp_picker()
    {
        int chosen_direction = -9;
        Random rgen = new Random();
        boolean open_dirs[] = {false, false, false, false};
        boolean closed_dirs[] = {false, false, false, false};
        boolean dark_dirs[] = {false, false, false, false};
        
        for (int i = 0; i < 4; i++) dark_dirs[i] = (open_dirs[i] && closed_dirs[i]);
        
        //popoulate open and closed

        for (int i = 0; i < 4; i++)
        {
            if (dungeon.grid[x][y][i] == 1) 
            {
                open_dirs[i] = true;
                if (dungeon.crystals[x+_DX[i]][y+_DY[i]] == false)
                {
                    closed_dirs[i] = true; 
                }
            }
            
        }
        
        //face reset
        if (face == -9)
        {
            if ((open_dirs[0] == false) && (open_dirs[1] == true)) face = RIGHT;
            else if ((open_dirs[0] == true) && (open_dirs[1] == false)) face = LEFT;
            else if ((open_dirs[2] == false) && (open_dirs[3] == true)) face = UP;
            else if ((open_dirs[2] == true) && (open_dirs[3] == false)) face = DOWN;
        }
        
        //different combinations of open and closed:
        //no where to go at all
        if (open_tester(open_dirs, false, false, false, false))
        {
            //cannot go anywhere
            chosen_direction = -983;
        }
        
        //only one way to go
        else if (open_tester(open_dirs, true, false, false, false))
        {
            chosen_direction = RIGHT;
        }
        else if (open_tester(open_dirs, false, true, false, false))
        {
            chosen_direction = LEFT;
        }
        else if (open_tester(open_dirs, false, false, true, false))
        {
            chosen_direction = UP;
        }
        else if (open_tester(open_dirs, false, false, false, true))
        {
            chosen_direction = DOWN;
        }
        
        //two ways to go
        
        //can go: {right,left}
        else if (open_tester(open_dirs, true, true, false, false))
        {
            //some of {right,left} are already explored
            if ((closed_dirs[RIGHT]) && (closed_dirs[LEFT])) chosen_direction = get_on_track();
            else if ((closed_dirs[RIGHT])) chosen_direction = LEFT;
            else if ((closed_dirs[LEFT])) chosen_direction = RIGHT;
            else
            {
                switch(face)
                {
                    case LEFT:
                        chosen_direction = LEFT;
                        break;
                    case RIGHT:
                        chosen_direction = RIGHT;
                        break;
                    case UP:
                        //not possible
                        break;
                    case DOWN:
                        //not possible
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
        }
        
        //can go: {right,up}
        else if (open_tester(open_dirs, true, false, true, false))
        {
            //some of {right,up} are already explored
            if ((closed_dirs[RIGHT]) && (closed_dirs[UP])) chosen_direction = get_on_track();
            else if ((closed_dirs[RIGHT])) chosen_direction = UP;
            else if ((closed_dirs[UP])) chosen_direction = RIGHT;
            else
            {
                switch(face)
                {
                    case LEFT:
                        chosen_direction = UP;
                        break;
                    case RIGHT:
                        //not possible
                        break;
                    case UP:
                        //not possible
                        break;
                    case DOWN:
                        chosen_direction = RIGHT;
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
        }
        
        //can gp: {right,down}
        else if (open_tester(open_dirs, true, false, false, true))
        {
            //some of {right,down} are already explored
            if ((closed_dirs[RIGHT]) && (closed_dirs[DOWN])) chosen_direction = get_on_track();
            else if ((closed_dirs[RIGHT])) chosen_direction = DOWN;
            else if ((closed_dirs[DOWN])) chosen_direction = RIGHT;
            else
            {
                switch(face)
                {
                    case LEFT:
                        chosen_direction = UP;
                        break;
                    case RIGHT:
                        //not possible
                        break;
                    case UP:
                        chosen_direction = RIGHT;
                        break;
                    case DOWN:
                        //not possible
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
        }
        
        //can go: {left,up}
        else if (open_tester(open_dirs, false, true, true, false))
        {
            //some of {left,up} are already explored
            if ((closed_dirs[LEFT]) && (closed_dirs[UP])) chosen_direction = get_on_track();
            else if ((closed_dirs[LEFT])) chosen_direction = UP;
            else if ((closed_dirs[UP])) chosen_direction = LEFT;
            else
            {
                switch(face)
                {
                    case LEFT:
                        //not possible
                        break;
                    case RIGHT:
                        chosen_direction = UP;
                        break;
                    case UP:
                        //not possible
                        break;
                    case DOWN:
                        chosen_direction = LEFT;
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
            
        }
        
        //can go: {left,down}
        else if (open_tester(open_dirs, false, true, false, true))
        {
            //some of {left,down} are already explored
            if ((closed_dirs[LEFT]) && (closed_dirs[DOWN])) chosen_direction = get_on_track();
            else if ((closed_dirs[LEFT])) chosen_direction = DOWN;
            else if ((closed_dirs[DOWN])) chosen_direction = LEFT;
            else
            {
                switch(face)
                {
                    case LEFT:
                        //not possible
                        break;
                    case RIGHT:
                        chosen_direction = DOWN;
                        break;
                    case UP:
                        chosen_direction = LEFT;
                        break;
                    case DOWN:
                        //not possible
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
        }
        
        //can go: {up,down}
        else if (open_tester(open_dirs, false, false, true, true))
        {
            //some of {up,down} are already explored
            if ((closed_dirs[UP]) && (closed_dirs[DOWN])) chosen_direction = get_on_track();
            else if ((closed_dirs[UP])) chosen_direction = DOWN;
            else if ((closed_dirs[DOWN])) chosen_direction = UP;
            else
            {
                switch(face)
                {
                    case LEFT:
                        //not possible
                        break;
                    case RIGHT:
                        //not possible
                        break;
                    case UP:
                        chosen_direction = UP;
                        break;
                    case DOWN:
                        chosen_direction = DOWN;
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);   
                }
            }
        }
        
        //three ways to go
        //can go {right,left,up}
        else if (open_tester(open_dirs, true, true, true, false))
        {
            //some of {right,left,up} are closed
            if (closed_dirs[RIGHT] == true && closed_dirs[LEFT] == true && closed_dirs[UP] == true) chosen_direction = get_on_track();
            else if (closed_dirs[RIGHT] == true && closed_dirs[LEFT] == true) chosen_direction = UP;
            else if (closed_dirs[RIGHT] == true && closed_dirs[UP] == true) chosen_direction = LEFT;
            else if (closed_dirs[LEFT] == true && closed_dirs[UP] == true) chosen_direction = RIGHT;
            else
            {
                //{right,left,up} are all not closed
                switch (face)
                {
                    case LEFT:
                        chosen_direction = roll_biased_dice(choice_points[7]);
                        break;
                    case RIGHT:
                        chosen_direction = roll_biased_dice(choice_points[8]);
                        break;
                    case UP:
                        //not possible
                        break;
                    case DOWN:
                        chosen_direction = roll_biased_dice(choice_points[6]);
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);                           
                }
            }
        }
        
        //can go {right,left,down}
        else if (open_tester(open_dirs, true, true, false, true))
        {
            //some of {right,left,down} are closed
            if (closed_dirs[RIGHT] == true && closed_dirs[LEFT] == true && closed_dirs[DOWN] == true) chosen_direction = get_on_track();
            else if (closed_dirs[RIGHT] == true && closed_dirs[LEFT] == true) chosen_direction = DOWN;
            else if (closed_dirs[RIGHT] == true && closed_dirs[DOWN] == true) chosen_direction = LEFT;
            else if (closed_dirs[LEFT] == true && closed_dirs[DOWN] == true) chosen_direction = RIGHT;
            else
            {
                //{right,left,down} are all not closed
                switch (face)
                {
                    case LEFT:
                        chosen_direction = roll_biased_dice(choice_points[2]);
                        break;
                    case RIGHT:
                        chosen_direction = roll_biased_dice(choice_points[1]);
                        break;
                    case UP:
                        chosen_direction = roll_biased_dice(choice_points[0]);
                        break;
                    case DOWN:
                        //not possible
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);                           
                }
            }
        }
        
        //can go {down,right,up}
        else if (open_tester(open_dirs, true, false, true, true))
        {
            //some of {down,right,up} are closed
            if (closed_dirs[DOWN] == true && closed_dirs[RIGHT] == true && closed_dirs[UP] == true) chosen_direction = get_on_track();
            else if (closed_dirs[DOWN] == true && closed_dirs[RIGHT] == true) chosen_direction = UP;
            else if (closed_dirs[DOWN] == true && closed_dirs[UP] == true) chosen_direction = RIGHT;
            else if (closed_dirs[RIGHT] == true && closed_dirs[UP] == true) chosen_direction = DOWN;
            else
            {
                //{down,right,up} are all not closed
                switch (face)
                {
                    case LEFT:
                        chosen_direction = roll_biased_dice(choice_points[9]);
                        break;
                    case RIGHT:
                        //not possible
                        break;
                    case UP:
                        chosen_direction = roll_biased_dice(choice_points[8]);
                        break;
                    case DOWN:
                        chosen_direction = roll_biased_dice(choice_points[5]);
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);                           
                }
            }
        }
        
        //can go {left, down, or up}
        else if (open_tester(open_dirs, false, true, true, true))
        {
            //some of {left,down,up} are closed
            if (closed_dirs[LEFT] == true && closed_dirs[DOWN] == true && closed_dirs[UP] == true) chosen_direction = get_on_track();
            else if (closed_dirs[LEFT] == true && closed_dirs[DOWN] == true) chosen_direction = UP;
            else if (closed_dirs[LEFT] == true && closed_dirs[UP] == true) chosen_direction = DOWN;
            else if (closed_dirs[DOWN] == true && closed_dirs[UP] == true) chosen_direction = LEFT;
            else
            {
                //{left,down,up} are all not closed
                switch (face)
                {
                    case LEFT:
                        //not possible
                        break;
                    case RIGHT:
                        chosen_direction = roll_biased_dice(choice_points[9]);
                        break;
                    case UP:
                        chosen_direction = roll_biased_dice(choice_points[11]);
                        break;
                    case DOWN:
                        chosen_direction = roll_biased_dice(choice_points[10]);
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);                           
                }
            }
        }
        
        //can go any direction
        else if (open_tester(open_dirs, true, true, true, true))
        {
            //some of {left,right,down,up} are already explored
            if (open_tester(closed_dirs, true, true, true, true)) chosen_direction = get_on_track();
            else if (closed_dirs[LEFT] == true && closed_dirs[RIGHT] == true && closed_dirs[UP] == true) chosen_direction = DOWN;
            else if (closed_dirs[LEFT] == true && closed_dirs[RIGHT] == true && closed_dirs[DOWN] == true) chosen_direction = UP;
            else if (closed_dirs[LEFT] == true && closed_dirs[UP] == true && closed_dirs[DOWN] == true) chosen_direction = RIGHT;
            else if (closed_dirs[RIGHT] == true && closed_dirs[UP] == true && closed_dirs[DOWN] == true) chosen_direction = LEFT;
            
            else
            {
                //none of {left,right,down,up} are explored
                switch (face)
                {
                    case LEFT:
                        chosen_direction = roll_biased_dice(choice_points[14]);
                        break;
                    case RIGHT:
                        chosen_direction = roll_biased_dice(choice_points[12]);
                        break;
                    case UP:
                        chosen_direction = roll_biased_dice(choice_points[13]);
                        break;
                    case DOWN:
                        chosen_direction = roll_biased_dice(choice_points[15]);
                        break;
                    default: 
                        chosen_direction = rgen.nextInt(4);

                }
            }
        }

        
        if (chosen_direction < 0) chosen_direction = randDir();
        return chosen_direction;
    }


    public int randDir()
    {
        int dir;
        Random rgen = new Random();
        do
        {
            dir = rgen.nextInt(4);
        }while (!canGo(dir));
        
        return dir;
    }
    
    @Override
    public void handleNewLocation(int new_x, int new_y)    
    {

        super.handleNewLocation(new_x, new_y);
        
        
        //Handle seeing the key or exit
        if (dungeon.darkCells[dungeon.key.x][dungeon.key.y] == false) seesKey = true;
        if (dungeon.darkCells[dungeon.exit.x][dungeon.exit.y] == false) seesExit = true;
        
        //if location has a darkway on it, remove it
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                for (int i = Darkways.size()-1; i >= 0; i--)
                {
                    if (Darkways.elementAt(i).x == (x+dx) && Darkways.elementAt(i).y == (y+dy)) 
                    {
                        Darkways.remove(i);
                        break;
                    }
                }   
            }
        }

        //add darkways
        for (int dx=-2; dx<=2; dx++)
        {
            for (int dy=-2; dy<=2; dy++)
            {
                if ((x+dx) >= 0 && (y+dy) >= 0 && (x+dx) <dungeon.width && (y+dy) <dungeon.height)
                {
                    if (!dungeon.isNotWalkableCell(x+dx, y+dy) && dungeon.crystals[x+dx][y+dy] == true && dungeon.darkCells[x+dx][y+dy] == false)
                    {
                        if (Math.abs(dx) == 1 || Math.abs(dy) == 1)
                        {
                            
                            boolean found = false;
                            
                            for (int e = 0; e < Darkways.size(); e++)
                            {
                                if (Darkways.elementAt(e).x == (x+dx) && Darkways.elementAt(e).y == (y+dy)) 
                                {
                                    found = true;
                                    break;
                                }
                            }
                            
                            if (!found) Darkways.add(new Darkway(x+dx, y+dy));
                        }
                    }
                }
            }
        }
            
     }
}

