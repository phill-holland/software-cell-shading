import java.awt.*;
import java.applet.*;
import java.awt.image.*;

public class texture
{
	public float width,height;
	public int pixels[];
	public int max;

	public texture(Image i,int w,int h)
	{
	
		width = (float)w;
		height = (float)h;

		max = w * h;

		pixels = new int[max];
		
		try
		{
			PixelGrabber pg = new PixelGrabber(i,0,0,(int)width,(int)height,pixels,0,(int)width);
			pg.grabPixels();
		}
		catch (java.lang.InterruptedException e)
		{
			return;
		}
		
	}

	public colour get(float u,float v)
	{
		int y = (int)(v * (height - 1.0f));
		int x = (int)(u * (width - 1.0f));

		int offset = (int)((width * y) + x);
		
		float alpha = 0.0f;
		float red = 0.0f;
		float green = 0.0f;
		float blue = 0.0f;

		if((offset>=0)&&(offset<max))
		{		
			alpha = (float)((pixels[offset] >> 24) & 0xFF);
			red = (float)((pixels[offset] >> 16) & 0xFF);
			blue = (float)((pixels[offset] >> 8) & 0xFF);
			green = (float)(pixels[offset] & 0xFF);
		}


		return new colour(red,green,blue,alpha);
		
	}	
}