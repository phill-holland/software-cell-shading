package com.phillholland.app;

import java.awt.*;
import java.awt.image.*;

public class PixelBuffer16
{
	public int width;
	public int height;

	public int halfWidth, halfHeight;

	private int maxBytes;

	public int buffer[];

	public PixelBuffer16(int w, int h)
	{
		width = w; height = h;
		halfWidth = width / 2; halfHeight = height / 2;

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

	public Colour getColour(int x, int y)
	{
		int offset = (y * width) + x;
		Colour result = new Colour();

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

	public void paint(Graphics g)
	{
		try
		{
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			DataBuffer db = new DataBufferInt(buffer, buffer.length);
			Raster r = Raster.createRaster(img.getSampleModel(), db, null );
			img.setData(r);

			g.drawImage(img, 0, 0, null);
		}
		catch(RasterFormatException rfx)
		{
			rfx.printStackTrace();
		}
		catch(NullPointerException npx)
		{
			npx.printStackTrace();
		}
	}

	public void paint(Graphics g, int x, int y)
	{
		try
		{
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			DataBuffer db = new DataBufferInt(buffer, buffer.length);
			Raster r = Raster.createRaster(img.getSampleModel(), db, null );
			img.setData(r);

			g.drawImage(img, x, y, null);
		}
		catch(RasterFormatException rfx)
		{
			rfx.printStackTrace();
		}
		catch(NullPointerException npx)
		{
			npx.printStackTrace();
		}
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
		int filter[][] = {{113,113,113},{113,113,113},{113,113,113}};
		
		int yoffset = width;

		for (int y = 1; y < height - 1; y++)
		{
			for (int x = 1; x < width - 1; x++)
			{
				int offset = yoffset + x;
				int values[][] = {{buffer[offset-1-width],buffer[offset-width],buffer[offset-width+1]},{buffer[offset-1],buffer[offset],buffer[offset+1]},{buffer[offset-1+width],buffer[offset+width],buffer[offset+1+width]}};

				int red = 0, green = 0, blue = 0;

				for(int j = 0; j < 3; j++)
				{
					red += ((red(values[0][j]) * filter[0][j]) + (red(values[1][j]) * filter[1][j]) + (red(values[2][j]) * filter[2][j]));
					green += ((green(values[0][j]) * filter[0][j]) + (green(values[1][j]) * filter[1][j]) + (green(values[2][j]) * filter[2][j]));
					blue += ((blue(values[0][j]) * filter[0][j]) + (blue(values[1][j]) * filter[1][j]) + (blue(values[2][j]) * filter[2][j]));

				}
								
				red = (red >> 10) & 0xff;
				green = (green >> 10) & 0xff;
				blue = (blue >> 10) & 0xff;

				buffer[offset] = (0xff000000 | red << 16 | green << 8 | blue);
			}
			yoffset += width;
		}

	}
}
