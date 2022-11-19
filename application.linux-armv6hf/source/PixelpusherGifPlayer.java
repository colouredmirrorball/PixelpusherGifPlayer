import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import gifAnimation.*; 
import javax.swing.*; 
import processing.video.*; 
import com.heroicrobot.dropbit.registry.*; 
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel; 
import com.heroicrobot.dropbit.devices.pixelpusher.Strip; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class PixelpusherGifPlayer extends PApplet {



/*
 *  Play a gif on the 8 16x16 LED panels
 */

 






Movie myMovie;
DeviceRegistry registry;
PusherObserver observer;
PImage[] images;
int currentIdx = 0;

public void setup() {
  
  
  registry = new DeviceRegistry();
  observer = new PusherObserver();
  registry.addObserver(observer);
  registry.setAntiLog(true);
  Gif gif = new Gif(this, "byob-resized.gif");
  images = gif.getPImages();
  for(int i = 0; i< images.length; i++) {
      images[i].resize(width, height);
  }
}

public void draw() {
  if(frameCount%10==0) {
    currentIdx++;
    if(currentIdx == images.length) {
      currentIdx = 0;
    }
  }
  image(images[currentIdx], 0, 0);
  scrape();
}

class PusherObserver implements Observer {
  public boolean hasStrips = false;
  public void update(Observable registry, Object updatedDevice) {
    println("Registry changed!");
    if (updatedDevice != null) {
      println("Device change: " + updatedDevice);
    }
    this.hasStrips = true;
  }
};


int stride = 8;
public void scrape() {
  // scrape for the strips
  loadPixels();
  if (observer.hasStrips) {
    registry.startPushing();
    List<Strip> strips = registry.getStrips();
    boolean phase = false;
    // for every strip:
    int currentStrip = 0;
    int xscale = 16;

    for (Strip strip : strips) {

      // for every pixel in the physical strip
      for (int stripx = 0; stripx < strip.getLength(); stripx++) {

        /*
        int xpixel = stripx % stride;
         int stridenumber = stripx / stride; 
         int xpos, ypos; 
         
         if ((stridenumber & 1) == 0) { // we are going top to bottom
         xpos = xpixel * xscale; 
         ypos = ((stripy*strides_per_strip) + stridenumber) * yscale;
         } else { // we are going bottom to top
         xpos = ((stride - 1)-xpixel) * xscale;
         ypos = ((stripy*strides_per_strip) + stridenumber) * yscale;
         }
         */
        int xpos = stripx/16 + xscale*currentStrip;
        boolean odd = xpos % 2 == 1;
        int ypos = odd ? 16-stripx%16 : stripx%16;
        strip.setPixel(get(xpos, ypos), stripx);
      }
      currentStrip++;
    }
  }
  updatePixels();
}

  public void settings() {  size(120,16);  noSmooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "PixelpusherGifPlayer" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
