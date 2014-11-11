package aimazed2d;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

//Animated PNG Spritesheet Reader
//Usage: 
//       APSReader mySpriteSheet = new APSReader(filename, w, h, pw, steps, stepLength);
//Detail:
//   filename      - the filename of full sprite sheet, which consists of multiple images separated by a padding width
//                 - the sprite sheet must be only one row with at minimum one sprite
//    w,h          - height and width of each sprite in the sprite sheet - must be consistent
//    pw           - padding width between each sprite - must be consistent
//    steps        - the number of sprites in the sheet
//    stepLength   - the length of time that passes before using the next sprite in sequence (cyclically)

public class APSReader {
    BufferedImage sprite_sheet;    
    int sprite_width;
    int sprite_height;
    int internal_sheet_padding_width;
    int animation_steps;
    int current_step = 0;
    double time = 0.0;
    double step_length;   
    BufferedImage sprites[];
    
    
    
    public APSReader(String filename, int w, int h, int pw, int steps, double stepLength)
    {
        //Read sprite sheet as a BufferedImage
        URL imageSrc = null;
        try { imageSrc = getClass().getResource(filename); }
        catch (Exception e) {}      
        try { sprite_sheet = ImageIO.read(imageSrc);}
        catch (IOException e) {System.out.println("Image not found");}
        
        sprite_width = w;        sprite_height = h;
        internal_sheet_padding_width = pw;
        animation_steps = steps;
        step_length = stepLength;
        sprites = new BufferedImage[steps];
        
        int iw = 0;
        int i = 0;
        
        while (iw < sprite_sheet.getWidth())
        {
            sprites[i] = sprite_sheet.getSubimage(iw, 0, sprite_width, sprite_height);
            iw+= sprite_width + internal_sheet_padding_width;
            i++;
        }
        
        new Timer().scheduleAtFixedRate(new TimerTask() {
			public void run() {
				updateStep();
			}
	}, 0, (int)(step_length));
    }
    
    public BufferedImage getSprite(){ return sprites[Math.min(animation_steps-1, Math.max(0, current_step))]; }
    public void setStepLength(double stepLength) { step_length = stepLength; }
    
    public void updateStep(double deltaTime)
    {
        time += deltaTime;
        if (time > step_length)
        {
            time = 0.0;
            current_step++;
            if (current_step >= animation_steps) current_step = 0;
        }
    }
    
    public void resetStep()
    {
        current_step = 0;
    }
    
    public void updateStep()
    {
        current_step++;
        if (current_step >= animation_steps) current_step = 0;
    }
}