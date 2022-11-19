

int stride = 8;
void scrape() {
  // scrape for the strips
  loadPixels();
  if (observer.hasStrips) {
    registry.startPushing();
    List<Strip> strips = registry.getStrips();
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
        color c = get(xpos, ypos);
        color corrected = color(blue(c)/2, green(c)/2, red(c)/2);
        strip.setPixel(corrected, stripx);
      }
      currentStrip++;
    }
  }
  updatePixels();
}
