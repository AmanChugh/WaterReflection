package com.ammy.waterreflection.haoran.ImageFilter;


public class WaterReflection {

	int   _amount ;
	
	/**
    amount >= 2.
	*/
	public WaterReflection(int amount)
	{
	    _amount = ((amount >= 2) ? amount : 2) ;
	}

    public Image process(Image imageIn) {
    	int r, g, b, m_current = 0;
		  int width = imageIn.getWidth();
		  int height = imageIn.getHeight();
		  Image clone = imageIn.clone();
		  for(int y = 0 ; y < height ; y++){
			  for(int x = 0 ; x < width ; x++){
				   if (x == 0) {
					   m_current = (getRandomInt(-255, 0xff) % _amount) * ((getRandomInt(-255, 0xff) % 2 > 0) ? 1 : -1) ;
				   }
				   int sx = FClamp(x+m_current, 0, width-1);
			       r = clone.getRComponent(sx, y);
				   g = clone.getGComponent(sx, y);
				   b = clone.getBComponent(sx, y);
				   imageIn.setPixelColor(x, y, r, g, b);
			  }
		  }
        return imageIn;     
    }

	public int getRandomInt(int a, int b) {
		int min = Math.min(a, b);
		int max = Math.max(a, b);
		return min + (int)(Math.random() * (max - min + 1));
	}

	public static int FClamp(final int t, final int tLow, final int tHigh)
	{
		if (t < tHigh)
		{
			return ((t > tLow) ? t : tLow) ;
		}
		return tHigh ;
	}

}