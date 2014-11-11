/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author joe
 */
public class GameWrapWindow extends JPanel{
    
    JButton continueButton, skipButton;
    JLabel result;
    JTextField textbox;
    
    String nickname = "";
    
    
    
    static int score = 0;
    
    public GameWrapWindow()
    {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        result = new JLabel();
        result.setAlignmentX(JTextField.CENTER);
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
        //result.setBorder(BorderFactory.createLineBorder(Color.RED)); 
        this.add(result, c);
        
        textbox = new JTextField();
        textbox.setColumns(10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(textbox, c);
        
        skipButton = new JButton("Skip");
        skipButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AiMazed2D.windowController = 0;
            }
        });
        this.add(skipButton);
        
        continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setPreferredSize(new Dimension(80, 30));
        continueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    //TODO: CHeck if made high score board and decide where to go
                    
                    //validate name input
                    if (textbox.getText().length() < 3 || textbox.getText().length() > 24)
                    {
                        result.setText(""
                            + "<HTML><font color=#000000>"
                            + " <CENTER> "
                            +       "GAME OVER! "
                            + "     <BR> "
                            + "     <BR> "
                            +       "Your final score is: "
                            + "     <BR> "
                            + "     <BR> "
                            +       score 
                            + "     <BR> "
                            + "     <BR> "
                            +       "Please Enter your Name to submit your score: "
                            +       "<BR>"
                            +       "<font color=#ff0000>Error: The name should be between 3 and 24 characters long.</font>"
                            + " </CENTER>"
                            + "</font></HTML>");
                    }
                    else
                    {
                        nickname = textbox.getText();
                        highScoreTest();
                        AiMazed2D.windowController = 0;
                        repaint();
                    }
                } catch (SQLException ex){ }
            }
        });
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.NORTH;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.ipadx = 16;
        c.ipady = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(5, 5, 5, 5);
        this.add(continueButton, c);
        
    }
    
    public void highScoreTest() throws SQLException
    {
        //connect to database
        Connection conn = connectToDB();
        
        if (conn != null)
        {
            //grab scores
            Vector<Score> scores;
            scores = new Vector<Score>();
            Statement s;
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
                ++count;
            }
            rs.close ();
            s.close ();
             
            //sort the scores
            Collections.sort(scores, new Comparator<Score>() {
            public int compare(Score one, Score other)
                {
                    return (one.compareTo(other));
                }
            });
            
            //test your score
            if (score > scores.elementAt(Math.min(scores.size()-1,9)).score)
            {
                //You've made the high score board
                
            }
        }
        else
        {
            //problem connecting to db
        }
        
        //insert your score into the db
        Statement s;
        s = conn.createStatement ();
        s.executeUpdate("INSERT INTO hiscores(nickname, score) VALUES('" + nickname + "', '" + score + "')");
        
        try { conn.close(); } catch (Exception e) {};
    }
    
    public Connection connectToDB()
    {
        Connection conn = null;
        String username = "";
        String pass = "";
        String url = "";
        
        username = "tanza";
            pass = "iratier030";
            url = "jdbc:mysql://aimazedthedb.c9ezyau4y0gf.us-east-1.rds.amazonaws.com:3306/aimazedthedb";
            
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch (Exception e)
        {}
        
        try
        {
            conn = DriverManager.getConnection(url, username, pass);
        }
        catch (Exception e)
        {}
        
        return conn;
    }
    public void setScore(int s)
    {
        score = s;
        result.setText(""
                + "<HTML><font color=#000000>"
                + " <CENTER> "
                +       "GAME OVER! "
                + "     <BR> "
                + "     <BR> "
                +       "Your final score is: "
                + "     <BR> "
                + "     <BR> "
                +       score 
                + "     <BR> "
                + "     <BR> "
                +       "Please Enter your Name to submit your score: "
                + " </CENTER>"
                + "</font></HTML>");
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(480,360);
    }
}