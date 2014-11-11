/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author joe
 */
public class HighScoresWindow extends JPanel {
    
    JLabel label;
    JButton backButton;
    String htmlString = "";
    
    List<Score> scores = new ArrayList();
    
    Image game_bg = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("aimazed2d/images/bg_gameBG.png"));
    public void paintComponent(Graphics g)
    {
        g.drawImage(game_bg, 0, 0, getWidth(), getHeight(), null);
    }
    
    public HighScoresWindow()
    {
        
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        
        
        
        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        c.fill = GridBagConstraints.VERTICAL;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(label, c);
        
        backButton = new JButton("Done Viewing");
        backButton.setSize(64, 48);
        backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AiMazed2D.windowController = 0;
                    repaint();
                }
            });
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 16;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(backButton, c);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(480,360);
    }
    
    
    public void readHighScores()
    {
        htmlString = "<HTML><font color=#000000><CENTER><TABLE>";
        
        
        
        
        //--------------------------------------
        
        scores.clear();
        htmlString += connect();

        
        Collections.sort(scores, new Comparator<Score>() {
            public int compare(Score one, Score other)
            {
                return (one.compareTo(other));
            }
        });
        
        htmlString += "<TR> <TH COLSPAN=3><b>AiMazed Top Ten</b></TH></TR>";
        htmlString += "<TR>";
        
            htmlString +=      "<TD>" + "Rank"  + "</TD>";
            htmlString +=      "<TD>" + "Name"  + "</TD>";
            htmlString +=      "<TD>" + "Score" + "</TD>";
        htmlString += "</TR>";
        
        for (int i = 0; i < (Math.min(10, scores.size())); i++)
        {
            htmlString += "<TR>";
            htmlString +=      "<TD>" + (i+1) +                        "</TD>";
            htmlString +=      "<TD>" + scores.get(i).nickname + "</TD>";
            htmlString +=      "<TD>" + scores.get(i).score +    "</TD>";
            htmlString += "</TR>";
        }
        
        htmlString += "</TABLE></CENTER></font></HTML>";
        label.setText(htmlString);
    }
    
    
    public String connect()
    {
        Connection conn = null;
        String username = "";
        String pass = "";
        String url = "";
        String internal = "";
        
        
        String msg = "";
        Statement s;
           try
           {
               username = "tanza";
               pass = "iratier030";
               url = "jdbc:mysql://aimazedthedb.c9ezyau4y0gf.us-east-1.rds.amazonaws.com:3306/aimazedthedb";
               //url = "jdbc:mysql://instance26372.db.xeround.com:17290/aimazed2d";
               Class.forName("com.mysql.jdbc.Driver").newInstance();
           }
           catch (Exception e)
           {
               msg += "Problem with driver";
           }
           try
           {
               conn = DriverManager.getConnection(url, username, pass);
               System.out.println ("Database connection established");
               
               s = conn.createStatement ();
               s.executeQuery ("SELECT autoid, nickname, score FROM hiscores");
               ResultSet rs = s.getResultSet();
               int count = 0;
               while (rs.next ())
                {
                    int idVal = rs.getInt ("autoid");
                    String nameVal = rs.getString ("nickname");
                    int scoreVal = rs.getInt ("score");
                    scores.add(new Score(scoreVal, nameVal));
                    
                    internal+= "<TR>";
                    internal+= "<TD>" + nameVal + "</TD>";
                    internal+= "<TD>" + scoreVal + "</TD>";
                    internal+= "</TR>";
                    System.out.println (
                            "id = " + idVal
                            + ", name = " + nameVal
                            + ", score = " + scoreVal);
                    ++count;
                }
                rs.close ();
                s.close ();
                System.out.println (count + " rows were retrieved");
           }
           catch (Exception e)
           {
               msg += "and problem with connection";
           }
           finally
           {
               if (conn != null)
               {
                   try
                   {
                       conn.close ();
                       System.out.println ("Database connection terminated");
                   }
                   catch (Exception e) { /* ignore close errors */ }
               }
               
               return msg;
           }
    }
}
