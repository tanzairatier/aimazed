/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author joe
 */
public class BFS {
    Vector<Node> OPENLIST;
    Vector<Node> CLOSEDLIST;
    Node Home;
    Node Goal;
    
    static int _DX[] = {1, -1, 0, 0};
    static int _DY[] = {0, 0, -1, 1};
    
    public BFS()
    {
        OPENLIST = new Vector<Node>();
        CLOSEDLIST = new Vector<Node>();
        Home = new Node();
        Goal = new Node();
    }
    
    public double distanceEuclidean(Node A, Node B)
    {
        return Math.sqrt( Math.pow((A.x - B.x),2) + Math.pow((A.y - B.y),2) );
    }
    
    public Node findBestNodeInOpen()
    {
        double min = 99999999;
        Node best = new Node();
        for (int i = 0; i < OPENLIST.size(); i++)
        {
            if (OPENLIST.elementAt(i).distance_from_goal <= min) 
            {
                min = OPENLIST.elementAt(i).distance_from_goal;
                best = OPENLIST.elementAt(i);
            }
        }
        return best;
        
    }
    
    public int alreadyInList(String list, Node query)
    {
        if (list == "open")
        {
            for (int i = 0; i < OPENLIST.size(); i++)
            {
                if (query.x == OPENLIST.elementAt(i).x && query.y == OPENLIST.elementAt(i).y) return i;
            }
        }
        else if (list == "closed")
        {
            for (int i = 0; i < CLOSEDLIST.size(); i++)
            {
                if (query.x == CLOSEDLIST.elementAt(i).x && query.y == CLOSEDLIST.elementAt(i).y) return i;
            }
        }
        return -1;
    }
    
    public Vector<Node> getPath(Node node)
    {
        Vector<Node> path = new Vector<Node>();
        Node next = node;
        
        while (next.parent != null)
        {
            path.add(next);
            next = next.parent;
        }
        
        return path;
        
        /*
        Node lastNode = new Node();
        
        while ((node.x != Home.x || node.y != Home.y))
        {
            lastNode = node;
            node = node.parent;
            System.out.println("(" + node.x + "," + node.y + ")");
        }
        
        return lastNode;
        /*
        if ((lastNode.x - node.x) == 1) return 0;//right
        else if ((lastNode.x - node.x) == -1) return 1; //left
        else if ((lastNode.y - node.y) == 1) return 3; //down
        else if ((lastNode.y - node.y) == -1) return 2; //up
        
        return -1;
        * 
        */
    }
    public Vector<Node> findPath(int x, int y, int gx, int gy, DungeonGenerator dungeon)
    {
        Node best_node = new Node();
        Node neighbor = new Node();
        int inOpen, inClosed;
        
        //System.out.println(x + ", " + y + " | " + gx + ", " + gy);
        
        Home = new Node();
        Home.x = x;
        Home.y = y;
        Home.parent = null;
        Home.path_length = 0;
        Home.distance_from_home = 0;
        Home.distance_from_goal = distanceEuclidean(Home, Goal);
        
        Goal = new Node();
        Goal.x = gx;
        Goal.y = gy;
        Goal.distance_from_home = distanceEuclidean(Goal, Home);
        Goal.distance_from_goal = 0;
        
        //1. Add Home to OPENLIST
        OPENLIST.add(Home);
        
        //2. Main Loop: Evaluating Nodes in Open List
        while (OPENLIST.size() > 0)
        {
            //move best node to closed list
            best_node = new Node();
            best_node = findBestNodeInOpen();
            CLOSEDLIST.add(best_node);
            OPENLIST.remove(best_node);
            
            //check if best_node is the goal
            if (best_node.x == Goal.x && best_node.y == Goal.y) return getPath(best_node);
            
            //examine neighbors
            for (int k = 0; k < 4; k++)
            {
                //test if valid cell (walkable and explored
                if ((best_node.x + _DX[k]) > 0 && (best_node.x + _DX[k]) < dungeon.width && (best_node.y + _DY[k]) > 0 && (best_node.y + _DY[k]) < dungeon.width)
                {
                    if (!dungeon.isNotWalkableCell(best_node.x + _DX[k], best_node.y + _DY[k]))
                    {
                        if (dungeon.darkCells[best_node.x + _DX[k]][best_node.y + _DY[k]] == false)
                        {


                            //neighborNode = getneighbor
                            neighbor = new Node();
                            neighbor.x = best_node.x + _DX[k];
                            neighbor.y = best_node.y + _DY[k];
                            neighbor.parent = best_node;
                            neighbor.path_length = best_node.path_length + 1;
                            neighbor.distance_from_home = distanceEuclidean(neighbor, Home);
                            neighbor.distance_from_goal = distanceEuclidean(neighbor, Goal);

                            //if neighbor is not in open nor closed, then add it to open
                            inOpen = alreadyInList("open", neighbor);
                            inClosed = alreadyInList("closed", neighbor);

                            if (inOpen == -1 && inClosed == -1) OPENLIST.add(neighbor);
                            else
                            {
                                if (inOpen > -1) 
                                {
                                    ///node is in open list
                                    if (OPENLIST.elementAt(inOpen).path_length > neighbor.path_length)
                                    {
                                        OPENLIST.elementAt(inOpen).path_length = neighbor.path_length;
                                        OPENLIST.elementAt(inOpen).parent = neighbor;
                                    }
                                }
                                else if (inClosed > -1)
                                {
                                    ///node is in closed list
                                    if (CLOSEDLIST.elementAt(inClosed).path_length > neighbor.path_length)
                                    {
                                        CLOSEDLIST.elementAt(inClosed).path_length = neighbor.path_length;
                                        CLOSEDLIST.elementAt(inClosed).parent = neighbor;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return null;
        
    }
}
