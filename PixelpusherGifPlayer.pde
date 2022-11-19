import gifAnimation.*;

/*
 *  Play a gif on the 8 16x16 LED panels
 */

import com.heroicrobot.dropbit.registry.*;
import com.heroicrobot.dropbit.devices.pixelpusher.Pixel;
import com.heroicrobot.dropbit.devices.pixelpusher.Strip;
import java.util.*;

DeviceRegistry registry;
PusherObserver observer;
PImage[] images;
int currentIdx = 0;

void setup() {
  size(120,16);
  noSmooth();
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

void draw() {
  background(0);
  if(frameCount%8==0) {
    currentIdx++;
    if(currentIdx == images.length) {
      currentIdx = 0;
    }
  }
  image(images[currentIdx], 0, 0);
  scrape();
}
