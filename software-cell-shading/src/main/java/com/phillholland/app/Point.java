package com.phillholland.app;

public class Point
{
	public float x, y, z;

	public Colour _colour = new Colour();
	
	public Point()
	{
		x = y = z = 0.0f;
	}

	public Point(float x1, float y1) { set(x1, y1, 0.0f); }
	public Point(float x1, float y1, float z1) { set(x1, y1, z1); }

	public void copy(Point p)
	{
		x = p.x;
		y = p.y;
		z = p.z;
	}

	public void set(float x1, float y1,float z1)
	{
		x = x1; y = y1; z = z1;
	}

	public Point transform(Camera c)
	{
		return new Point(c.focus * (x / z), c.focus * (y / z), z);
	}

	public Point minus(Point r)
	{
		return new Point(x - r.x, y - r.y, z - r.z);
	}

	public Point add(Point r)
	{
		return new Point(x + r.x, y + r.y, z + r.z);
	}

	public Matrix get()
	{
		Matrix m = new Matrix();
		m.row(x, y, z, 1.0f, 0);
		return m;
	}

	public void set(Matrix m)
	{
		x = m.get(0, 0);
		y = m.get(0, 1);
		z = m.get(0, 2);
	}

	public void display()
	{
		System.out.println(x + " " + y + " " + z);
	}
}
