/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aimazed2d;

import java.util.Date;

/**
 *
 * @author joe
 */
class Score implements Comparable<Score>{
        int score;
        String nickname;
        Date date;
        public Score(int s, String n)
        {
            score = s;
            nickname = n;
        }
        
        public int compareTo(Score other) {
            return (score > other.score ? -1 :
               (score == other.score ? 0 : 1));
        }
    }