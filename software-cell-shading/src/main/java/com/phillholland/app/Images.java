package com.phillholland.app;

import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Images
{
	public BufferedImage images[];
	public int length;

	public boolean isError;	

	public Images(String filenames[]) { reset(filenames); }

	public void reset(String filenames[])
	{
		isError = false;

		length = filenames.length;
		
		images = new BufferedImage[length];

		for(int i=0;i<length;i++)
		{
			try
			{
				images[i] = ImageIO.read(new File(filenames[i]));		 
			}
			catch(IOException e)
			{
				e.printStackTrace();			
			}
		}
	}
}