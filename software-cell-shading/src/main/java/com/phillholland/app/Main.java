package com.phillholland.app;

import java.awt.*;

public class Main extends Panel implements Runnable
{
	private Thread thread;

	private boolean running = true;
	private PixelBuffer16 pb = null;

	private int width = 512, height = 512;
	
	private Image backBuffer = null;
	private Graphics backGraphics = null;

	private Camera c = null;
	private Object o = null;
	private Light _light = null;

	private Images _images = null;
	private Texture _textures[] = null;

	private float angle = 0.0f;

	private Line l = null;

	// ****
	public float[][] map(int totalTextures)
	{
		int length = 60;
		float result[][] = new float[totalTextures][length];

		int division = length / totalTextures;
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
		String img[] = { "images/a.jpg", "images/a.jpg", "images/d.jpg", "images/d.jpg" };

		_images = new Images(img);

		_textures = new Texture[img.length];
		for (int i = 0; i < _images.images.length; i++)
		{
			_textures[i] = new Texture(_images.images[i], _images.images[i].getWidth(this), _images.images[i].getHeight(this));
		}

		// ***
		float tonalMap[][] = map(_textures.length);
		// ***

		c = new Camera(512, 512);
		c.set(new Point(0.0f, 0.0f, -3.0f), new Vector(0.0f, 0.0f, 0.0f), 0.0f);
		c.calculate();

		o = new Object(8, 6);

		_light = new Light(
					new Vector(0.0f, 0.0f, 1.0f), 
					new Point(-5.0f, 0.0f, 0.0f),
					10.0f, 
					new Colour(1.0f, 1.0f, 1.0f));

		Point points[] = { 
			new Point(-1.0f, -1.0f, -1.0f), 
			new Point(1.0f, -1.0f, -1.0f), 
			new Point(1.0f, 1.0f, -1.0f), 
			new Point(-1.0f, 1.0f, -1.0f), 
			new Point(-1.0f, -1.0f, 1.0f), 
			new Point(1.0f, -1.0f, 1.0f), 
			new Point(1.0f, 1.0f, 1.0f), 
			new Point(-1.0f, 1.0f, 1.0f) 
		};

		o.points = points;

		Map mappings[] = { 
			new Map(0.0f, 0.0f), 
			new Map(1.0f, 0.0f), 
			new Map(1.0f, 1.0f), 
			new Map(0.0f, 1.0f) 
		};

		int front[] = { 0, 1, 2, 3 };
		int back[] = { 7, 6, 5, 4 };
		int left[] = {3, 7, 4, 0 };
		int right[] = { 1, 5, 6, 2 };
		int top[] = { 0, 4, 5, 1 };
		int bottom[] = { 2, 6, 7, 3 };

		int texture_map[] = { 0, 3, 2, 1 };

		for (int i = 0; i < o.polygons.length; i++)
		{
			o.polygons[i]._textures = _textures;
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

	public void process()
	{
		o.copy();

		Matrix rotate = Globals.xMatrix(angle);
		angle += 0.01f;
		if (angle > 6.28f) angle = 0.0f;
		o.transform(rotate);

		rotate = Globals.yMatrix(angle);
		o.transform(rotate);

		rotate = Globals.zMatrix(angle);
		o.transform(rotate);

		o.normals();
		o.compute(_light);

		o.backfaces(c.center);

		o.transform(c.zrotate);
		o.transform(c.yrotate);
		o.transform(c.xrotate);
		o.transform(c.translation);
		
		o.transform(c);		

		Matrix translate = Globals.tMatrix(new Point(pb.width / 2, pb.height / 2));

		o.transform(translate);

		pb.clear();
		o.paint(pb,l);
		pb.FSAA();
	}
	
	public void init()
	{
		pb = new PixelBuffer16(width, height);

		backBuffer = createImage(width, height);
		backGraphics = backBuffer.getGraphics();

		l = new Line();
		
		l.width = 3;
		l._colour = new Colour(0.0f, 0.0f, 0.0f);

		l.intensityX = 1.45f;
		l.intensityY = 1.45f;

		l.noiseIncX = 0.0f;
		l.noiseIncY = 0.0f;

		init3d();
		
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

		pb.paint(backGraphics);
		g.drawImage(backBuffer, 0, 0, this);
		
	}

	public void update(Graphics g) { paint(g); }
	
	public void paint(Graphics g) { draw(g); }
	
}