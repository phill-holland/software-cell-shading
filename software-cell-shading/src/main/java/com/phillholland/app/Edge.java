package com.phillholland.app;

import java.util.Random;

public class Edge
{
	public int a, b;

	public int polygons[] = null;
	public int length;

	public float seedX, seedY;
	
	public Edge(int total_polygons)
	{
		a = b = length = 0;

		polygons = new int[total_polygons];

		Random r = new Random();

		seedX = (float)r.nextDouble();
		seedY = (float)r.nextDouble();
	}

	public Edge(int x, int y,int total_polygons) 
	{ 
		a = x; 
		b = y;

		length = 0;

		polygons = new int[total_polygons];
	}

	public void add(int index)
	{
		polygons[length] = index;
		++length;
	}
}
