
package aimazed2d;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.easyogg.OggClip;

/**
 *
 * @Author J.K. "Games"
 * @Description Audio factory for management of sounds and music
 * 
 */

public class AudioFactory {
    
    final String Sound = "Sound";
    final String Music = "Music";
    
    Audio tracks[];
    boolean musicIsOn = true;
    boolean soundIsOn = true;
    
    public class Audio
    {
        int id;
        int channel;
        String type;
        OggClip track;
        
        public Audio(int _channel, int _id, String _type, String _file)
        {
            channel = _channel;
            id = _id;
            type  = _type;
            try
            {
                track = new OggClip(_file);
            } 
            catch (IOException ex) {}
        }
    }
    
    public AudioFactory()
    {
        tracks = new Audio[3];
        
        tracks[0] = new Audio(1, 0, Music, "aimazed2d/Music/bgm01.ogg");
        tracks[1] = new Audio(2, 1, Sound, "aimazed2d/sfx/Money/sfx000.ogg");
        tracks[2] = new Audio(3, 2, Sound, "aimazed2d/sound/sfx001.ogg");
    }
    
    public void playAudio(int _id)
    {
        //clearChannel(tracks[_id].channel);
        
        if ((tracks[_id].type == Sound && soundIsOn) || (tracks[_id].type == Music && musicIsOn))
        {
            if (tracks[_id].track.isPaused() || tracks[_id].track.stopped())
            {
                if (tracks[_id].type == Sound)
                {
                    tracks[_id].track.play();
                }
                else
                {
                    tracks[_id].track.loop();
                }
            }
        }
    }
    
    public void clearChannel(int _channel)
    {
        for (int i = 0; i < tracks.length; i++)
        {
            if (tracks[i].channel == _channel)
            {
                tracks[i].track.stop();
            }
        }
    }
    
    public void pauseAll()
    {
        for (int i = 0; i < tracks.length; i++)
        {
            tracks[i].track.pause();
        }
    }
    
}
