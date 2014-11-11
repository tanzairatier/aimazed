/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.awt.Color;

/**
 *
 * @author joe
 */
public class TextFloat {
    int x;
    int y;
    String text;
    double timeLeft;
    Color color;
    
    public TextFloat()
    {
        
    }
    public TextFloat(int a, int b, String t, double tL, Color c)
    {
        x = a;
        y = b;
        text = t;
        timeLeft = tL;
        color = c;
    }
}
