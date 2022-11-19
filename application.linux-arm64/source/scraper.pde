

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
        int xpos = stripx/16 + xscale*currentStrip;
        boolean odd = xpos % 2 == 1;
        int ypos = odd ? 16-stripx%16 : stripx%16;
        color c = get(xpos, ypos);
        color corrected = color(red(c)/2, blue(c)/2, green(c)/2);
        strip.setPixel(corrected, stripx);
      }
      currentStrip++;
    }
  }
  updatePixels();
}
