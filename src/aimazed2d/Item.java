package aimazed2d;

import java.util.Timer;
import java.util.TimerTask;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author joe
 */
public class Item {
   String type;
   double buffTimer;
   boolean permanence;
   int x;
   int y;
   boolean exists;
   
   APSReader image;
   
   //Type Catalog
   final static String TORCH = "Torch";
   final static String MAGNET = "Magnet";
   final static String REDCRYSTAL = "Red Crystal";
   final static String BOOTS = "Boots";
   
   //Item Catalog
   public static class Items
   {
       APSReader MAGNET, REDCRYSTAL, TORCH;
       public Items()
       {
           MAGNET = new APSReader("images/magnet_spritesheet.png", 35, 34, 1, 4, 50);
           REDCRYSTAL = new APSReader("images/redcrystal_spritesheet.png", 24, 24, 1, 8, 200);
           TORCH = new APSReader("images/torches/torch_spritesheet.png", 14, 24, 1, 4, 100);
       }
   }
   
   Items Items = new Items();
   
   public Item()
   {
       x = 0;
       y = 0;
       buffTimer = 200;  //ms
       permanence = true;
       type = Item.TORCH;
       exists = true;
   }
   
   public Item(int a, int b, String itemkind)
   {
       x = a;
       y = b;
       permanence = true;
       type = itemkind;
       loadImage();
       exists = true;
   }
   
   public void loadImage()
   {
       if (type.equals(Item.REDCRYSTAL))
       {
           image = Items.REDCRYSTAL;
           
       }
       else if (type.equals(Item.MAGNET))
       {
           image = Items.MAGNET;
       }
       else if (type.equals(Item.TORCH))
       {
           image = Items.TORCH;
       }
   }
   
}
