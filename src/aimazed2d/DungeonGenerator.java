/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;
import aimazed2d.levelmaps.QMapFactory;
import aimazed2d.levelmaps.QMapFactory.QMap;
import java.util.*;

/**
 *
 * @author Joe Krall
 */
public class DungeonGenerator {
    
    static int _DX[] = {1, -1, 0, 0};
    static int _DY[] = {0, 0, -1, 1};
    int width = 0;
    int height = 0;
    int grid[][][] = new int[width][height][4];
    boolean crystals[][];
    boolean visibility[][] = new boolean[width][height];
    boolean darkCells[][];
    Vector<Item> items;
    public int magnetIndex;
    public int torchIndex;
    public Timer torcher, magnetizer;
    
    public class Key {
        int x,y;
        int points = 100;
        boolean exists;
        public void setLocation()
        {
            Random rgen = new Random();
            do
            {
                x = rgen.nextInt(width);
                y = rgen.nextInt(height);
            } while (isNotWalkableCell(x,y));
        }
    }
    public class Exit {
        int x,y;
        int points = 100;
        boolean exists;
        public void setLocation()
        {
            Random rgen = new Random();
            do
            {
                x = rgen.nextInt(width);
                y = rgen.nextInt(height);
            } while (isNotWalkableCell(x,y));
        }
    }
    Key key;
    Exit exit;
    Random rgen;
    
    public int[][][] getGrid()
    {
        return grid;
    }
    public int opp(int dir)
    {
        if (dir == 0) return 2;
        else if (dir == 1) return 3;
        else if (dir == 2) return 0;
        else if (dir == 3) return 1;
        else return -1;
    }
    
    public boolean canGo(int dir, int x, int y)
    {
        if (x > width || y > height)
        {
            return false;
        }
        else 
        {
            return grid[x][y][dir] == 1;
        }
    }
    
    public DungeonGenerator()
    {
        key = new Key();
        exit = new Exit();
        items = new Vector<Item>();
        rgen = new Random();
        
        
        
        
        
    }
    
    public void setDims(int w, int h)
    {
        width = w;
        height = h;
        grid = new int[width][height][4];
        crystals = new boolean[width][height];
        darkCells = new boolean[width][height];
    }
    public DungeonGenerator(DungeonGenerator copy)
    {
        grid = copy.grid;
        height = copy.height;
        width = copy.width;
        crystals = new boolean[width][height];
        darkCells = new boolean[width][height];
        for (int i=0; i<width; i++)
        {
            for (int j=0; j<height; j++)
            {
                darkCells[i][j] = false;
                crystals[i][j] = false;
            }
        }
    }
    
    public void buildPredefinedDungeon(QMap qmap)
    {
        width = 0;
        height = 0;
        char c;
        //initial parse to find width/height
        for (int i = 0; i < qmap.str.length()-1; i++)
        {
            if (qmap.str.charAt(i) == '%')
            {
                height++;
            }
            else
            {
                if (height == 0) width ++;
            }
        }
        
        System.out.println(width+", "+height);
        
        width++;
        height++;
        
        grid = new int[width][height][4];
        int h = 0;
        int w = 0;
        for (int i = 0; i < qmap.str.length()-1; i++)
        {
            if (qmap.str.charAt(i) == '%')
            {
                h++;
                w = 0;
            }
            else
            {
                if (qmap.str.charAt(i) == '#')
                {
                    for (int k = 0; k < 4; k++)
                    {
                        grid[w][h][k] = 0;
                    }
                }
                else if (qmap.str.charAt(i) == ' ')
                {
                    for (int k = 0; k < 4; k++)
                    {
                        grid[w][h][k] = 1;
                    }
                }
                w++;
            }
        }
        
        //third pass to generate walls
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (grid[i][j][0] == 1)
                {
                    for (int k = 0; k < 4; k++)
                    {
                        if (isNotWalkableCell(i+_DX[k], j+_DY[k]))
                        {
                            grid[i][j][k] = 0;
                        }
                    }
                }
            }
        }
    }
    public void buildDungeon(int w, int h, int l, String algorithm)
    {
        width = w;
        height = h;
        
        if ("BSP Tree Dungeon".equals(algorithm))
        {
            grid = (new BSPTree(w, h, l)).grid;
        }
        else if (("Level").equals(algorithm))
        {
            buildPredefinedDungeon(AiMazed2D.AiMazed2DQF.Levels[l]);
        }
        
        visibility = new boolean[w][h];
        crystals = new boolean[w][h];
        darkCells = new boolean[w][h];
        
        
        key.setLocation();
        key.exists = true;
        
        exit.setLocation();
        exit.exists = true;
        
        items.clear();
        
        
        for (int i=0; i<w; i++)
        {
            for (int j=0; j<h; j++)
            {
                visibility[i][j] = false;
                darkCells[i][j] = true;
                if (isNotWalkableCell(i,j)) crystals[i][j] = false;
                else 
                {
                    if (!((key.x == i && key.y == j) || (exit.x == i && exit.y == j)))
                    {
                        if (rgen.nextInt(1000) >= 5)
                        {
                            crystals[i][j] = true;
                        } 
                        else
                        {
                            items.add(new Item(i, j, Item.REDCRYSTAL));
                        }
                    }
                }
            }
        }
        
        //magnet controller
        if (magnetizer != null)
        {
            System.out.println("Cancelling old magnetizer");
            magnetizer.cancel();
        }
        magnetizer = new Timer();
        magnetizer.scheduleAtFixedRate(new TimerTask() {
            
            int seconds = 0;
            Random rgen = new Random();
            int mx, my;
            boolean firstRun = true;
            public void run() {
                
                if (AiMazed2D.gameWindow.game.player.paused == false)
                {
                    seconds += 1;
                    if (firstRun || seconds > 30+(rgen.nextInt(30)))
                    {
                        firstRun = false;
                        seconds = 0;
                        do
                        {
                            mx = rgen.nextInt(width);
                            my = rgen.nextInt(height);
                        } while (isNotWalkableCell(mx, my) || (key.x == mx && key.y == my) || (exit.x == mx && exit.y == my));

                        items.add(new Item(mx, my, Item.MAGNET));
                        magnetIndex = items.size()-1;

                        System.out.println("ADDED A MAGNET!");

                        //item disappears after too long
                        new Timer().schedule(new TimerTask() {
                            int seconds = 0;
                            public void run() {
                                seconds += 1;
                                if (seconds >= 30)
                                {
                                    seconds = 0;
                                    items.elementAt(magnetIndex).exists = false;
                                    this.cancel();
                                }
                            }
                        }, 1000);
                    }
                }
            }
        }, 0, 1000);
        
        //torch controller
        if (torcher != null)
        {
            System.out.println("Cancelling old torcher");
            torcher.cancel();
        }
        torcher = new Timer();
        torcher.scheduleAtFixedRate(new TimerTask() {
            int seconds = 0;
            int mx, my;
            Random rgen = new Random();
            boolean firstRun = true;
            public void run() {
                if (AiMazed2D.gameWindow.game.player.paused == false)
                {
                    seconds += 1;
                    if (firstRun || seconds > 30+(rgen.nextInt(30)))
                    {
                        firstRun = false;
                        seconds = 0;
                        do
                        {
                            mx = rgen.nextInt(width);
                            my = rgen.nextInt(height);
                        } while (isNotWalkableCell(mx, my) || (key.x == mx && key.y == my) || (exit.x == mx && exit.y == my));

                        items.add(new Item(mx, my, Item.TORCH));
                        torchIndex = items.size()-1;

                        System.out.println("ADDED A TORCH!");

                        //item disappears after too long
                        new Timer().scheduleAtFixedRate(new TimerTask() {
                            int seconds = 0;
                            boolean removal = false;
                            public void run() {
                                seconds += 1;
                                if (seconds >= 30)
                                {
                                    for (int i = 0; i < items.size(); i++)
                                    {
                                        if (removal == false && items.elementAt(i).type == Item.TORCH)
                                        {
                                            System.out.println("A torch disappeared.");
                                            removal = true;
                                            items.removeElementAt(i);
                                            this.cancel();
                                        }
                                    }
                                    this.cancel();
                                }
                            }
                        }, 0, 1000);
                    }
                }
            }
        }, 0, 1000);
        
    }
    
    public void lightUpCellsAround(int x, int y, int recurse)
    {
        
        /*
        for (int k = 0; k < 4; k++)
        {
            darkCells[x+_DX[k]][y+_DY[k]] = false;
            if (!isNotWalkableCell(x+_DX[k], y+_DY[k]))
            {
                for (int k2 = 0; k2 < 4; k2++)
                {
                    darkCells[x+_DX[k2]+_DX[k]][y+_DY[k2]+_DY[k]] = false;
                    if (!isNotWalkableCell(x+_DX[k2]+_DX[k], y+_DY[k2]+_DY[k]))
                    {
                        for (int k3 = 0; k3 < 4; k3++)
                        {
                            darkCells[x+_DX[k3]+_DX[k2]+_DX[k]][y+_DY[k3]+_DY[k2]+_DY[k]] = false;
                        }
                    }
                }
            }
        }*/
        
        for (int k = 0; k < 4; k++)
        {
            if (x+_DX[k] > 0 && y+_DY[k] > 0 && x+_DX[k] < width && y+_DY[k] < height)
            {
                darkCells[x+_DX[k]][y+_DY[k]] = false;
            }
            if (!isNotWalkableCell(x+_DX[k], y+_DY[k]) && recurse > 0) 
            {
                lightUpCellsAround(x+_DX[k], y+_DY[k], recurse-1);
            }
        }
    }
    
    public int is_deadend(int x, int y)
    {
        int cnt = 0;
        int deadend = -1;
        
        for (int i=0; i<4; i++)
        {
            if (grid[x][y][i] == 1)
            {
                deadend = i;
                cnt++;
            }
        }
        if (cnt == 1) 
            return deadend;
        else
            return -1;
    }
    
    public int opposite(int a)
    {
        if (a == 0) return 1;
        else if(a == 1) return 0;
        else if (a == 2) return 3;
        else return 2;
    }
    
    public void sparsify(int repeat)
    {
        int dd = -1;
        int nx = 0;
        int ny = 0;
        int od = 0;
        
        while (repeat > 0)
        {
            for (int i=0; i<width; i++)
            {      
                for (int j=0; j<height; j++)
                {
                    dd = is_deadend(i, j);
                    if (dd > -1)
                    {
                        nx = _DX[dd] + i;
                        ny = _DY[dd] + j;
                        od = opposite(dd);
                        grid[i][j][dd] = 0;
                        grid[nx][ny][od] = 0;
                    }
                }
            }
            repeat--;
        }
    }
    
    public void shuffle(int d[])
    {
        Random rgen = new Random();  // Random number generator
        
        //--- Shuffle by exchanging each element randomly
        for (int i=0; i<d.length; i++) {
        int randomPosition = rgen.nextInt(d.length);
        int temp = d[i];
        d[i] = d[randomPosition];
        d[randomPosition] = temp;
        }
    }
    
    public void remove_deadend(int x, int y)
    {
        Random rgen = new Random();
        int nx = 0;
        int ny = 0;
        int od = 0;
        int dir_cnt = 0;
        double dbias = 0.15;

        
        int directions[] = {0, 1, 2, 3};
        shuffle(directions);
        
        dir_cnt = 0;
        int i = 0;
        boolean intersected = false;
        while(!intersected)
        {          
            while (dir_cnt < 4)
            {
                dir_cnt++;
                nx = _DX[directions[i]] + x;
                ny = _DY[directions[i]] + y;
                od = opposite(directions[i]);
                
                if (nx >= 0 && nx < width && ny >= 0 && ny < height && grid[nx][ny][od]==0)
                {
                    if (grid[nx][ny][0] == 1 || grid[nx][ny][1] == 1 || grid[nx][ny][2] == 1 || grid[nx][ny][3] == 1)
                        intersected = true;
                    
                    grid[nx][ny][od] = 1;
                    grid[x][y][directions[i]] = 1;
                    x = nx;
                    y = ny;
                    dir_cnt = 0;
                    if (dbias*100 >= rgen.nextInt(100))
                        shuffle(directions);
                }
                else
                {
                    i++;
                    if (i >= 4)
                        i = 0;
                }
            }

            dir_cnt = 0;
            intersected = true; //(giveup)
            
        }             
    }
    
    public void add_loops()
    {
        Random rgen = new Random();
        int dd = -1;
        //find a deadend
        for (int i=0; i<width; i++)
        {      
            for (int j=0; j<height; j++)
            {
                dd = is_deadend(i, j);
                if (dd > -1)
                {
                    if (0.25*100 >= rgen.nextInt(100))
                    remove_deadend(i, j);
                }
                
            }
        }
    }
    
    public boolean isNotWalkableCell(int x, int y)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
        {
            return true;
        }
        else
        {
            return (grid[x][y][0] == 0 &&
                     grid[x][y][1] == 0 &&
                     grid[x][y][2] == 0 &&
                     grid[x][y][3] == 0);
        }
    }
    
    
    
    public int countCrystals()
    {
        int total = 0;
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                if (crystals[i][j] == true)
                {
                    total++;
                }
            }
        }
        return total;
    }
}
