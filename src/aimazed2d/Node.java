/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

/**
 *
 * @author joe
 */
public class Node {
    
    Node parent;
    int path_length;
    double distance_from_home;
    double distance_from_goal;
    int x;
    int y;
    
    public Node()
    {
        parent = null;
        path_length = 0;
        distance_from_home = 0.0;
        distance_from_goal = 0.0;
        x = 0;
        y = 0;
    }
    
    public Node(int a, int b)
    {
        x = a;
        y = b;
    }
    
    
}
