package com.phillholland.app;

import java.lang.Math;

public class Vector
{
	public float x,y,z,w;

	public Vector() { x = y = z = w = 0.0f; }
	public Vector(float a, float b, float c) { x = a; y = b; z = c; w = 0.0f; }
	public Vector(float a,float b,float c,float d) { x = a; y = b; z = c; w = d; }
	public Vector(Point p) { x = p.x; y = p.y; z = p.z; }
	public Vector(Vector r) { copy(r); }

	public Vector cross(Vector r)
	{
		return new Vector(y * r.z - z * r.y,z * r.x - x * r.z,x * r.y - y * r.x,0.0f);     
	}


	public float dot(Vector r)
	{
		return x * r.x + y * r.y + z * r.z;
	}

	public float length()
	{
		return (float)Math.sqrt((double)(x * x + y * y + z * z));
	}

	public Vector normalise()
	{
		float l = length();
		if(l==0.0f) l = 1.0f;

		return new Vector(x / l,y / l,z / l,w);
	}

	public boolean compare(Vector b)
	{
		boolean result = false;

		int ax = (int)(x * 100.0f), ay = (int)(y * 100.0f), az = (int)(z * 100.0f);
		int bx = (int)(b.x * 100.0f), by = (int)(b.y * 100.0f), bz = (int)(b.z * 100.0f);

		if ((ax == bx) && (ay == by) && (az == bz)) result = true;

		return result;
	}

	public void copy(Vector r)
	{
		x = r.x; y = r.y; z = r.z; w = r.w;
	}

	public void display()
	{
		System.out.println("V " + x + " " + y + " " + z);
	}
}

