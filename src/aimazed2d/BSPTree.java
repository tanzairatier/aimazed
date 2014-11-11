
package aimazed2d;

import java.util.Random;

public class BSPTree {
     
    static int _DX[] = {1, -1, 0, 0};
    static int _DY[] = {0, 0, -1, 1};
    int width = 0;
    int height = 0;
    int grid[][][] = new int[width][height][4];
    int level;
    boolean visited[][] = new boolean[width][height];
    
    int bias_odds = 50;

    public BSPTree(int w, int h, int l)
    {
        width = w;
        height = h;
        level = l;
        grid = new int[width][height][4];
        for (int i=0; i<width; i++)
        {
            for (int j=0; j<height; j++)
            {
                for (int k=0; k<4; k++)
                {
                    grid[i][j][k] = 0;
                }
            }
        }
        
        visited = new boolean[w][h];
        for (int i=0; i<w; i++)
        {
            for (int j=0; j<h; j++)
            {
                visited[i][j] = false;
            }
        }
        generate_maze();
    }
    
    public void split(BSPNode bsptree)
    {
        Random rgen = new Random();
        int orientation, position;
        
        //generate tree
        //1) root nodeBSPNode
        
        
        //2) choose an initial direction (vertical/horizontal)
        if (rgen.nextInt(99) > bias_odds) 
        {
            orientation = 0;
            bias_odds+=17;
        }
        else
        {
            orientation = 1;
            bias_odds-=17;
        }
        
        //3) random position along orientation axis
        int minW = 18 + rgen.nextInt(15);
        int minH = 18 + rgen.nextInt(15);
        int safeRadius = minW/4;
        if ((orientation == 0 && bsptree.width > minW) || (orientation == 1 && bsptree.height <= minH))
        {
            do
            {
                position = bsptree.width/2 + (rgen.nextInt(10)) - 5;
            } while ((bsptree.width-position) < 8 && (position > 8));
            //4) do the split and add children        
            bsptree.child1 = new BSPNode(bsptree.x,          bsptree.y, position, bsptree.height);
            bsptree.child2 = new BSPNode(bsptree.x+position, bsptree.y, bsptree.width-position, bsptree.height);
        }
        else
        {
            
            do
            {
                position = bsptree.height/2 + (rgen.nextInt(10)) - 5;
            } while ((bsptree.height-position) < 8 && (position > 8));
            //4) do the split and add children        
            bsptree.child1 = new BSPNode(bsptree.x, bsptree.y,          bsptree.width, position);
            bsptree.child2 = new BSPNode(bsptree.x, bsptree.y+position, bsptree.width, bsptree.height-position);
        }
        
        //System.out.println(bsptree.x+","+bsptree.y+","+bsptree.width+","+bsptree.height+","+position+","+orientation);
        
        //5) recurse with each child unless small enough
        if (bsptree.child1.width > minW || bsptree.child1.height > minH)
            split(bsptree.child1);
        
        if (bsptree.child2.width > minW || bsptree.child2.height > minH)
            split(bsptree.child2);
        
    }
    
    public void add_rooms(BSPNode traveler)
    {
        Random rgen = new Random();
        int rx, ry, rw, rh;
        
        if (traveler.child1 == null && traveler.child2 == null)
        {    
            //random room dimensions
            rw = traveler.width-4; //rgen.nextInt(20-8)+8;
            rh = traveler.height-4;//rgen.nextInt(20-8)+8;
            
            //place room in center with 2-cell padding
            for (int i = (traveler.x+(traveler.width-rw)/2); i<((traveler.x+(traveler.width-rw)/2)+rw); i++)
            {
                for (int j = (traveler.y+(traveler.height-rh)/2); j<((traveler.y+(traveler.height-rh)/2)+rh); j++)
                {
                    //System.out.println(i+","+j);
                    if ((i > 0) && (i < width) && (j > 0) && (j < height))
                        visited[i][j] = true;
                }
            }
            
            //add middle piece
            if (rgen.nextInt(1000) < 500 && rw > 6 && rh > 6)
            {
                for (int i = (traveler.x+(rw/2)); i <= (traveler.x+(rw/2)+2); i++)
                {
                    for (int j = (traveler.y+(rh/2)); j <= (traveler.y+(rh/2)+2); j++)
                    {
                        visited[i][j] = false;
                    }
                }
            }
            
        }
        
        if (traveler.child1 != null) add_rooms(traveler.child1);
        
        if (traveler.child2 != null) add_rooms(traveler.child2);
        
    }
    
    public void add_corridors(BSPNode room1, BSPNode room2)
    {
         
        //get midpoint along the directive wall of room1
        int mid_x = room1.x + (room1.width/2);
        int mid_y = room1.y + (room1.height/2);
        
        int target_x = room2.x + (room2.width/2);
        int target_y = room2.y + (room2.height/2);
        
        int cur_x = mid_x;
        int cur_y = mid_y;
        //drill out of room1 to center of room2
        
        while (cur_x != target_x || cur_y != target_y)
        {
            for (int dx = -1; dx < 2; dx++)
            {
                for (int dy = -1; dy < 2; dy++)
                {
                    visited[cur_x+dx][cur_y+dy] = true;
                }
            }
            
            if (cur_x != target_x) cur_x+= -(cur_x - target_x) / (target_x - cur_x);
            if (cur_y != target_y) cur_y+= -(cur_y - target_y) / (target_y - cur_y);
                
        }
        
    }
    
    public void join(BSPNode tree1, BSPNode tree2)
    {
        if (tree1.child1 != null)
            join(tree1.child1, tree1.child2);
        
        if (tree2.child1 != null)
            join(tree2.child1, tree2.child2);
        
        add_corridors(tree1, tree2);
    }
    
    public void generate_maze()
    {
        Random rgen = new Random();
        BSPNode bsptree = new BSPNode(0, 0, width, height);
        int rx, ry, rw, rh;
        
        //generate BSP Tree for dungeon
        split(bsptree);
        
        //add rooms to each node component
        add_rooms(bsptree);
        
        
        
        
        //erode
        
        int active_neighbors = 0;
///*
        for (int e=0; e<1; e++)
        {
            for (int i=0; i<width; i++)
            {
                for (int j=0; j<height; j++)
                {
                    active_neighbors = 0;
                    //change state of cell depending on neighbors
                    for (int dx=-1; dx<=1; dx++)
                    {
                        for (int dy=-1; dy<=1; dy++)
                        {
                            if ((i+dx >= 0) && (i+dx < width) && (j+dy >= 0) && (j+dy < height))
                            {
                                if (!(dx == 0 && dy == 0))
                                {
                                    if (visited[i+dx][j+dy] == true) active_neighbors++;
                                }
                            }
                        }
                    }
                    if (active_neighbors > 5)
                    {
                        if (i > 0 && j > 0 && i < width && j < height)
                            visited[i][j] = true; 
                    }
                        
                    else if (active_neighbors < (rgen.nextInt(6-3)+3))
                    {
                        if (i > 0 && j > 0 && i < width && j < height)
                            visited[i][j] = false;
                    }
                }
            }
        }
//*/
        //connect pairs
        join(bsptree.child1, bsptree.child2);
        
        //complete grid interworkings
        connect();
    }
    public void connect()
    {
        for (int i=0; i<width; i++)
        {
            for (int j=0; j<height; j++)
            {
                if (visited[i][j] == true)
                {
                    for (int k=0; k<4; k++)
                    {
                        if ((i+_DX[k] >= 0) && (i+_DX[k] < width) && (j+_DY[k] >= 0) && (j+_DY[k] < height))
                        {
                            if (visited[i+_DX[k]][j+_DY[k]] == true)
                            {
                                grid[i][j][k] = 1;
                            }
                        }
                    }
                }
            }
        }
    }
    
}
