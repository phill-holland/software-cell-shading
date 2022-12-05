import java.awt.*;
import java.applet.*;
import java.awt.image.*;
import java.util.Random;

public class pixelBuffer16
{
	public int width;
	public int height;

	public int halfWidth, halfHeight;

	private int maxBytes;
	private Applet applet;

	public int buffer[];

	public pixelBuffer16(Applet app, int w, int h)
	{
		width = w; height = h;
		halfWidth = width / 2; halfHeight = height / 2;

		applet = app;

		maxBytes = width * height;

		buffer = new int[maxBytes];
	}

	public void clear()
	{
		for (int i = 0; i < maxBytes; i++)
		{
			buffer[i] = 0xffffffff;
		}
	}

	public void clear(int c)
	{
		for (int i = 0; i < maxBytes; i++)
		{
			buffer[i] = c;
		}
	}

	public void pixel(int r, int g, int b, int x, int y)
	{
		int offset = (y * width) + x;
		if ((offset > 0) && (offset < buffer.length))
			buffer[offset] = (0xff000000 | r << 16 | g << 8 | b);
	}

	public void pixel(int a, int r, int g, int b, int x, int y)
	{
		int offset = (y * width) + x;
		if ((offset > 0) && (offset < buffer.length))
			buffer[offset] = (a << 24 | r << 16 | g << 8 | b);
	}

	public void pixel(int a, int r, int g, int b, int topLeftX, int topLeftY, int width)
	{
		for (int x = topLeftX; x < topLeftX + width; x++)
		{
			for (int y = topLeftY; y < topLeftY + width; y++)
			{
				pixel(a, r, g, b, x, y);
			}
		}
	}

	public int get(int x, int y)
	{
		int result = 0;
		int offset = (y * width) + x;
		if ((offset > 0) && (offset < buffer.length))
			result = buffer[offset];

		return result;
	}

	public colour getColour(int x, int y)
	{
		int offset = (y * width) + x;
		colour result = new colour();

		result.red = (buffer[offset] >> 16) & 0xFF;
		result.green = (buffer[offset] >> 8) & 0xFF;
		result.blue = buffer[offset] & 0xFF;

		return result;
	}

	public void line(int minX, int minY, int maxX, int maxY, int r, int g, int b)
	{
		int lineWidth;
		int lineHeight;

		if (minX == maxX)
		{
			int smallestY = minY, largestY = maxY;
			if (minY > maxY) { smallestY = maxY; largestY = minY; }

			for (int i = smallestY; i < largestY; i++) pixel(r, g, b, minX, i);

			return;
		}
		if (minY == maxY)
		{
			int smallestX = minX, largestX = maxX;
			if (minX > maxX) { smallestX = maxX; largestX = minX; }

			for (int i = smallestX; i < largestX; i++) pixel(r, g, b, i, minY);

			return;
		}

		int smallestX = minX, largestX = maxX;
		int smallestY = minY, largestY = maxY;

		if (minX > maxX) { smallestX = maxX; largestX = minX; }

		lineWidth = largestX - smallestX;
		lineHeight = largestY - smallestY;

		if (lineWidth > Math.abs(lineHeight))
		{
			float gradient = (float)lineHeight / (float)lineWidth;
			float Y;

			float startY = (float)smallestY;
			if (smallestX != minX) { startY = (float)maxY; gradient *= -1.0f; }

			for (int X = 0; X < lineWidth; X++)
			{
				Y = startY + (((float)X) * gradient);
				pixel(r, g, b, X + smallestX, (int)Y);
			}
		}
		else
		{
			float gradient = (float)lineWidth / (float)lineHeight;
			float X;

			float startX = (float)smallestX;
			if (smallestX != minX) { startX = (float)largestX; gradient *= -1.0f; }
			if (lineHeight < 0) { gradient *= -1.0f; }

			for (int Y = 0; Y < Math.abs(lineHeight); Y++)
			{
				X = startX + (((float)Y) * gradient);
				if (lineHeight < 0)
					pixel(r, g, b, (int)X, smallestY - Y);
				else pixel(r, g, b, (int)X, Y + smallestY);
			}
		}
	}

	public void line(int minX, int minY, int maxX, int maxY, int a, int r, int g, int b, int width)
	{
		int lineWidth;
		int lineHeight;

		int halfWidth = width / 2;

		if (minX == maxX)
		{
			int smallestY = minY, largestY = maxY;
			if (minY > maxY) { smallestY = maxY; largestY = minY; }

			for (int i = smallestY; i < largestY; i++) pixel(a, r, g, b, minX - halfWidth, i - halfWidth,width);

			return;
		}
		if (minY == maxY)
		{
			int smallestX = minX, largestX = maxX;
			if (minX > maxX) { smallestX = maxX; largestX = minX; }

			for (int i = smallestX; i < largestX; i++) pixel(a, r, g, b, i - halfWidth, minY - halfWidth,width);

			return;
		}

		int smallestX = minX, largestX = maxX;
		int smallestY = minY, largestY = maxY;

		if (minX > maxX) { smallestX = maxX; largestX = minX; }

		lineWidth = largestX - smallestX;
		lineHeight = largestY - smallestY;

		if (lineWidth > Math.abs(lineHeight))
		{
			float gradient = (float)lineHeight / (float)lineWidth;
			float Y;

			float startY = (float)smallestY;
			if (smallestX != minX) { startY = (float)maxY; gradient *= -1.0f; }

			for (int X = 0; X < lineWidth; X++)
			{
				Y = startY + (((float)X) * gradient);
				pixel(a, r, g, b, X + smallestX - halfWidth, (int)Y - halfWidth, width);
			}
		}
		else
		{
			float gradient = (float)lineWidth / (float)lineHeight;
			float X;

			float startX = (float)smallestX;
			if (smallestX != minX) { startX = (float)largestX; gradient *= -1.0f; }
			if (lineHeight < 0) { gradient *= -1.0f; }

			for (int Y = 0; Y < Math.abs(lineHeight); Y++)
			{
				X = startX + (((float)Y) * gradient);
				if (lineHeight < 0)
					pixel(a, r, g, b, (int)X - halfWidth, smallestY - Y - halfWidth, width);
				else pixel(a, r, g, b, (int)X - halfWidth, Y + smallestY - halfWidth, width);
			}
		}
	}
	/*
	public void lineNoise(int minX, int minY, int maxX, int maxY, int r, int g, int b)
	{
		Random rnd = new Random();
		noise n = new noise();
		float noise_ofs = (float)rnd.nextDouble();
		float multipler = 3.0f;

		int lineWidth;
		int lineHeight;

		if (minX == maxX)
		{
			int smallestY = minY, largestY = maxY;
			if (minY > maxY) { smallestY = maxY; largestY = minY; }

			lineHeight = largestY - smallestY;
			float incl = (float)lineHeight / 8.0f;
			float inc = 1.0f / incl;
			float effect = 0.0f;

			for (int i = smallestY; i < largestY; i++)
			{
				if (i < smallestY + (int)incl)
				{
					effect += inc;
				}
				else if (i > largestY - (int)incl)
				{
					effect -= inc;
				}

				int t = (int)((n.perlin(noise_ofs) * multipler) * effect);
				noise_ofs += 0.0005f;

				pixel(r, g, b, minX + t, i);
			}

			return;
		}
		if (minY == maxY)
		{
			int smallestX = minX, largestX = maxX;
			if (minX > maxX) { smallestX = maxX; largestX = minX; }

			lineWidth = largestX - smallestX;
			float incl = (float)lineWidth / 8.0f;
			float inc = 1.0f / incl;
			float effect = 0.0f;

			for (int i = smallestX; i < largestX; i++)
			{
				if (i < smallestX + (int)incl)
				{
					effect += inc;
				}
				else if (i > largestX - (int)incl)
				{
					effect -= inc;
				}
				int t = (int)((n.perlin(noise_ofs) * multipler) * effect);
				noise_ofs += 0.0005f;

				pixel(r, g, b, i, minY + t);
			}

			return;
		}

		int smallestX = minX, largestX = maxX;
		int smallestY = minY, largestY = maxY;

		if (minX > maxX) { smallestX = maxX; largestX = minX; }

		lineWidth = largestX - smallestX;
		lineHeight = largestY - smallestY;

		if (lineWidth > Math.abs(lineHeight))
		{
			System.out.println("A");
			float gradient = (float)lineHeight / (float)lineWidth;
			float Y;

			float startY = (float)smallestY;
			if (smallestX != minX) { startY = (float)maxY; gradient *= -1.0f; }

			float incl = (float)lineWidth / 8.0f;
			float inc = 1.0f / incl;
			float effect = 0.0f;

			for (int X = 0; X < lineWidth; X++)
			{
				if (X < (int)incl)
				{
					effect += inc;
				}
				else if (X > lineWidth - (int)incl)
				{
					effect -= inc;
				}
				Y = startY + (((float)X) * gradient);

				int t = (int)((n.perlin(noise_ofs) * multipler) * effect);
				noise_ofs += 0.0005f;

				pixel(r, g, b, (X + smallestX), (int)Y + t);
			}
		}
		else
		{
			System.out.println("B");
			float gradient = (float)lineWidth / (float)lineHeight;
			float X;

			float startX = (float)smallestX;
			if (smallestX != minX) { startX = (float)largestX; gradient *= -1.0f; }
			if (lineHeight < 0) { gradient *= -1.0f; }

			float incl = (float)lineHeight / 8.0f;
			float inc = 1.0f / incl;
			float effect = 0.0f;

			for (int Y = 0; Y < Math.abs(lineHeight); Y++)
			{
				if (Y < (int)incl)
				{
					effect += inc;
				}
				else if (Y > lineHeight - (int)incl)
				{
					effect -= inc;
				}

				X = startX + (((float)Y) * gradient);

				int t = (int)((n.perlin(noise_ofs) * multipler) * effect);
				noise_ofs += 0.0005f;

				if (lineHeight < 0)
					pixel(r, g, b, (int)X + t, (smallestY - Y));
				else pixel(r, g, b, (int)X + t, (Y + smallestY));
			}
		}
	}

	*/
	public void paint(Graphics g)
	{
		Image i = applet.createImage(new MemoryImageSource(width, height, buffer, 0, width));
		g.drawImage(i, 0, 0, applet);
	}

	public void paint(Graphics g, int x, int y)
	{
		Image i = applet.createImage(new MemoryImageSource(width, height, buffer, 0, width));
		g.drawImage(i, x, y, applet);
	}
				
	private int red(int v)
	{
		return ((v >> 16) & 0xFF);
	}

	private int green(int v)
	{
		return ((v >> 8) & 0xFF);
	}

	private int blue(int v)
	{
		return (v & 0xFF);
	}

	public void FSAA()
	{
		// do I need a temp intermediate buffer for this?

		int filter[][] = {{113,113,113},{113,113,113},{113,113,113}};
		//int filter[][] = { { 100, 100, 100 }, { 100, 155, 100 }, { 100, 100, 100 } };
		
		int yoffset = width;

		for (int y = 1; y < height - 1; y++)
		{
			for (int x = 1; x < width - 1; x++)
			{
				int offset = yoffset + x;//(y * width) + x;
				int values[][] = {{buffer[offset-1-width],buffer[offset-width],buffer[offset-width+1]},{buffer[offset-1],buffer[offset],buffer[offset+1]},{buffer[offset-1+width],buffer[offset+width],buffer[offset+1+width]}};
				//int value = 0;

				//colour values[][] = { { getColour(x - 1, y - 1), getColour(x, y - 1), getColour(x + 1, y - 1) }, { getColour(x - 1, y), getColour(x, y), getColour(x + 1, y) },{ getColour(x - 1, y + 1), getColour(x, y + 1), getColour(x + 1, y + 1) }};
				int red = 0, green = 0, blue = 0;

				for(int j = 0; j < 3; j++)
				{
					red += ((red(values[0][j]) * filter[0][j]) + (red(values[1][j]) * filter[1][j]) + (red(values[2][j]) * filter[2][j]));
					green += ((green(values[0][j]) * filter[0][j]) + (green(values[1][j]) * filter[1][j]) + (green(values[2][j]) * filter[2][j]));
					blue += ((blue(values[0][j]) * filter[0][j]) + (blue(values[1][j]) * filter[1][j]) + (blue(values[2][j]) * filter[2][j]));
					
					//red += ((values[0][j].red * filter[0][j]) + (values[1][j].red * filter[1][j]) + (values[2][j].red * filter[2][j]));
					//green += ((values[0][j].green * filter[0][j]) + (values[1][j].green * filter[1][j]) + (values[2][j].green * filter[2][j]));
					//blue += ((values[0][j].blue * filter[0][j]) + (values[1][j].blue * filter[1][j]) + (values[2][j].blue * filter[2][j]));
					
					//value += ((colours[0][j] * filter[0][j]) + (colours[1][j] * filter[1][j]) + (colours[2][j] * filter[2][j]));
				}
								
				red = (red >> 10) & 0xff;
				green = (green >> 10) & 0xff;
				blue = (blue >> 10) & 0xff;

				buffer[offset] = (0xff000000 | red << 16 | green << 8 | blue);
				//pixel(red >> 10,green >> 10,blue >> 10,x,y);
				//buffer[offset] = value >> 10;
			}
			yoffset += width;
		}

	}

	/*
		template <class X> void xInterface<X>::FSAA()
	{	
		unsigned long filter[3][3] = {{113UL,113UL,113UL},{113UL,113UL,113UL},{113UL,113UL,113UL}};
	//	float result;
		unsigned long x,y,tempx;
		unsigned long yoffset = scr_width * sizeof(X);
		X *temp,pixel;			
		unsigned long bufY=0UL,newX=0UL,yyoff;
		DWORD redclr = (redclearmask << redshift),greenclr = (greenclearmask << greenshift);	
		unsigned long red,green,blue;

		for(y=yoffset;y<(scr_height * yoffset)-yoffset;y+=yoffset)
		{				
			newX = 1UL;
			memcpy(&buffer[bufY * scr_width],&thescreen[y-yoffset],sizeof(X));
		
			for(x=sizeof(X);x<(scr_width*sizeof(X))-sizeof(X);x+=sizeof(X))
			{						
				tempx = x - sizeof(X);																																																																																																							
				red = green = blue = 0UL;

				yyoff = y - yoffset;
				for(unsigned long j=0UL;j<3;j++)
				{				
					temp = (X *)&thescreen[yyoff + tempx];
				
					red += ((temp[0] & redclr) >> redshift) * filter[0][j];
					green += ((temp[0] & greenclr) >> greenshift) * filter[0][j];
					blue += (temp[0] & blueclearmask) * filter[0][j];

					red += ((temp[1] & redclr) >> redshift) * filter[1][j];
					green += ((temp[1] & greenclr) >> greenshift) * filter[1][j];
					blue += (temp[1] & blueclearmask) * filter[1][j];

					red += ((temp[2] & redclr) >> redshift) * filter[2][j];
					green += ((temp[2] & greenclr) >> greenshift) * filter[2][j];
					blue += (temp[2] & blueclearmask) * filter[2][j];
				
					yyoff += yoffset;

				}
				pixel = transformcolourformat((unsigned char)(red >> FILTER_SHIFT),(unsigned char)(green >> FILTER_SHIFT),(unsigned char)(blue >> FILTER_SHIFT));
				memcpy(&buffer[(bufY * scr_width) + newX],&pixel,sizeof(X));

				newX ++;
			
			}
			memcpy(&buffer[(bufY * scr_width) + newX],&thescreen[y + yoffset - sizeof(X)],sizeof(X));
	
			++bufY;
			if(bufY>=3UL)
			{
				memcpy(&thescreen[y - (yoffset * 2)],buffer,yoffset * 3); 
				bufY = 0UL;		
			}
		}	
		if(bufY>1UL) memcpy(&thescreen[y - (yoffset * 2)],buffer,scr_width * (bufY-1UL) * sizeof(X)); 
	}
	*/
}
