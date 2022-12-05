import java.awt.*;
import java.awt.image.*;
import java.applet.*;
import java.lang.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class main extends Applet implements Runnable
{
	// need to try drawing lines with perlin noise (1d) applied too them
	// them rotating cube, with cel shading lines
	// HSR !!

	private Thread thread;

	private boolean running = true;
	private pixelBuffer16 pb = null;

	private int width = 255, height = 255;

	private float x = 0.0f, y = 0.0f;

	private Image backBuffer = null;
	private Graphics backGraphics = null;

	private camera c = null;
	private object o = null;
	private light _light = null;

	private images _images = null;
	private texture _textures[] = null;

	//private texture _texture = null;

	private float angle = 0.0f;

	private line l = null;

	/*
	public void test1D()
	{
		noise n = new noise();
		float offset = 0.0f;

		for (int i = 0; i < 100; i++)
		{
			System.out.println(n.perlin(offset));
			offset += 0.01f;
		}
	}
	*/

	// ****
	public float[][] map(int totalTextures)
	{
		//int totalTextures = 4,
		int length = 60;//100;//60;//50;
		float result[][] = new float[totalTextures][length];

		int division = length / totalTextures;
		int half = (int)(((float)division) / 2.0f);

		float offset = 1.0f / (float)division;

		for (int i = 0; i < totalTextures; i++)
		{
			float temp = 1.0f;
			int w = (i * division);

			for (int j = 0; j < division; j++)
			{
				result[i][j + w] = temp;
				if(i<totalTextures-1) temp -= offset;
			}
			
			if (i > 0)
			{
				temp = 0.0f;
				for (int j = 0; j < division; j++)
				{
					result[i][j + w - division] = temp;
					temp += offset;
				}
			}
			 
		}

		// *** display
		for (int y = 0; y < totalTextures; y++)
		{
			for (int x = 0; x < length; x++)
			{
				System.out.print(result[y][x] + ",");
			}
			System.out.println(" ");
			System.out.println(" ");
		}
		// ***

		return result;
	}

	// ****
	public void init3d()
	{
		// ***
		/*
		vector a = new vector(1.0f, 1.0f, 1.0f);
		vector b = new vector(-1.0f, -1.0f, -1.0f);

		System.out.println("aaaa " + a.dot(a));
		System.out.println("bbbb " + b.dot(a));
		 * */
		// ***

		//String img[] = {"lowa.jpg","lowb.jpg","lowc.jpg"};
		String img[] = { "d.jpg", "d.jpg", "blank.jpg", "blank.jpg" };

		try
		{
			_images = new images(this, new URL("http://127.0.0.1/development/images/set1/"), img);
			//_images = new images(this, new URL("http://www.owddev.co.uk/demos/cube/images/"), img);
		}
		catch (Exception e) { }

		_textures = new texture[img.length];
		for (int i = 0; i < _images.images.length; i++)
		{
			_textures[i] = new texture(_images.images[i], _images.images[i].getWidth(this), _images.images[i].getHeight(this));
		}

		// ***
		float tonalMap[][] = map(_textures.length);
		// ***

		//_texture = new texture(_images.images[0], _images.images[0].getWidth(this), _images.images[0].getHeight(this));
		//System.out.println("(wxh) " + _texture.width + " " + _texture.height);

		c = new camera(255, 255);
		c.set(new point(0.0f, 0.0f, -3.0f), new vector(0.0f, 0.0f, 0.0f), 0.0f);
		c.calculate();

		o = new object(8, 6);

		//_light = new light(1.0f, 0.0f, 0.0f);
		_light = new light(new vector(0.0f, 0.0f, 1.0f), new point(-5.0f, 0.0f, 0.0f), 10.0f, new colour(1.0f, 1.0f, 1.0f));

		point points[] = { new point(-1.0f, -1.0f, -1.0f), new point(1.0f, -1.0f, -1.0f), new point(1.0f, 1.0f, -1.0f), new point(-1.0f, 1.0f, -1.0f), new point(-1.0f, -1.0f, 1.0f), new point(1.0f, -1.0f, 1.0f), new point(1.0f, 1.0f, 1.0f), new point(-1.0f, 1.0f, 1.0f) };
		o.points = points;

		map mappings[] = { new map(0.0f, 0.0f), new map(1.0f, 0.0f), new map(1.0f, 1.0f), new map(0.0f, 1.0f) };

		int front[] = { 0, 1, 2, 3 };
		int back[] = { 7, 6, 5, 4 };
		int left[] = {3, 7, 4, 0 };
		int right[] = { 1, 5, 6, 2 };//{2, 6, 5 ,1}; 
		int top[] = { 0, 4, 5, 1 };
		int bottom[] = { 2, 6, 7, 3 };////{ 3, 7, 6, 2 };

		int texture_map[] = { 0, 3, 2, 1 };

		for (int i = 0; i < o.polygons.length; i++)
		{
			o.polygons[i]._textures = _textures;//[0];
			o.polygons[i].tonalMap = tonalMap;
		}

		o.polygons[0].reset(points, front, mappings, texture_map);
		o.polygons[1].reset(points, back, mappings, texture_map);
		o.polygons[2].reset(points, left, mappings, texture_map);
		o.polygons[3].reset(points, right, mappings, texture_map);
		o.polygons[4].reset(points, top, mappings, texture_map);
		o.polygons[5].reset(points, bottom, mappings, texture_map);

		o.copy();
		o.compute();
	}

	//try wonky line on cubes
	// lines need to be less "wonky"
	// wonky lines, try getting them "fixed"
	// and then smooth animation for lines
	// shading, basic lighting ...
	// drawing lines only on edges
	// character animation....

	// smooth animation of lines may not be correct!

	public void process()
	{
		o.copy();

		matrix rotate = globals.xMatrix(angle);
		angle += 0.01f;
		if (angle > 6.28f) angle = 0.0f;
		o.transform(rotate);

		rotate = globals.yMatrix(angle);
		o.transform(rotate);

		rotate = globals.zMatrix(angle);
		o.transform(rotate);

		o.normals();
		o.compute(_light);

		o.backfaces(c.center); // also computes normals for polygons

		o.transform(c.zrotate);
		o.transform(c.yrotate);
		o.transform(c.xrotate);
		o.transform(c.translation);
		
		o.transform(c);		

		matrix translate = globals.tMatrix(new point(pb.width / 2, pb.height / 2));

		o.transform(translate);

		pb.clear();
		o.paint(pb,l);
		pb.FSAA();

		// ***
		/*
		matrix mp = new point(10.0f, 20.0f, 0.0f).get();
		mp.display();
		matrix result = mp.multiply(translate);
		result.display();
		point t = new point();
		t.set(result);
		t.display();
		 * */
		//temp[i].set(result);

		//o.polygons[0].reset(
	}
	
	public void init()
	{
		pb = new pixelBuffer16(this, width, height);

		backBuffer = createImage(width, height);
		backGraphics = backBuffer.getGraphics();

		l = new line();
		
		l.width = 3;
		l._colour = new colour(0.0f, 0.0f, 0.0f);

		l.intensityX = 0.45f;
		l.intensityY = 0.45f;

		l.noiseIncX = 0.0f;
		l.noiseIncY = 0.0f;

		init3d();
		/*
		noise n = new noise();
		n.octaves = 8;
		n.persistance = 2.0f;
		n.draw(pb);
		*/

		/*
		line l = new line();
		l.noiseIncX = 0.0f;
		l.noiseIncY = 0.0f;

		l.draw2(pb, new point(10, 10), new point(200, 200));
		 * */
		/*
		pb.lineNoise(100, 10, 110, 200, 0, 255, 0);

		pb.lineNoise(10, 10, 200, 200, 0, 0, 255);
		*/

		thread = new Thread(this);
		thread.start();
	}

	public void run()
	{
		while (running)
		{
			try
			{				
				repaint();
				//Thread.sleep(100);
			}
			catch (Exception ex) { }
		}
	}

	public void draw(Graphics g)
	{
		
		backGraphics.setColor(Color.WHITE);
		backGraphics.fillRect(0, 0, width, height);

		process();
		/*
		int x1 = 10, y1 = 10, x2 = 200, y2 = 200;
		pb.clear();

		line l = new line();

		l.noiseIncX = x;//0.0f;
		l.noiseIncY = y;//0.0f;

		l.draw(pb, new point(10, 10), new point(200, 200));
		
		//int t1 = x1 - x2, t2 = y1 - y2;
		//float length = (float)Math.sqrt((double)(t1 * t1 + t2 * t2));
		//System.out.println(length);

		//point p[] = { new point(10.0f, 10.0f), new point(100.0f, 100.0f), new point(150.0f, 150.0f), new point(200.0f, 200.0f), new point(210.0f,210.0f),new point(220.0f,220.0f),new point(230.0f,230.0f),new point(240.0f,240.0f) };
		//catmull cm = new catmull(p,new point(200.0f,200.0f));

		//cm.render(pb);//,new point(200.0f,200.0f));

		x += 0.0001f;
		y += 0.0001f;
		*/

		pb.paint(backGraphics);
		g.drawImage(backBuffer, 0, 0, this);


		//g.setColor(Color.green);
		//g.drawLine(x1, y1, x2, y2);
		
	}

	public void update(Graphics g) { paint(g); }
	
	public void paint(Graphics g) { draw(g); }
	
}