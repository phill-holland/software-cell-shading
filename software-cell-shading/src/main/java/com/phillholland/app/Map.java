package com.phillholland.app;

public class Map
{
	float u, v;

	public Map()
	{
		reset(0.0f, 0.0f);
	}

	public Map(float a, float b) { reset(a, b); }

	public void reset(float a, float b) { u = a; v = b; }
}
