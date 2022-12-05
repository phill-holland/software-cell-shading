import java.awt.*;
import java.applet.*;
import java.io.*;
import java.net.*;
import java.awt.image.*;

public class images
{
	public Image images[];
	public int length;

	public boolean isError;	

	public images(Applet a,URL url,String names[]) { reset(a,url,names); }

	public images(Applet a,String names[]) { reset(a,a.getDocumentBase(),names); }

	public void reset(Applet a,URL url,String names[])
	{
		isError = false;

		length = names.length;

		MediaTracker tracker = new MediaTracker(a);
		images = new Image[length];

		for(int i=0;i<length;i++)
		{
			images[i] = a.getImage(url,names[i]);
			tracker.addImage(images[i],i);

			try
			{
				tracker.waitForID(i);
			} catch(InterruptedException ex) { return; }
		}

	}
}