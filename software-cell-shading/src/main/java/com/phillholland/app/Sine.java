package com.phillholland.app;

public class Sine
{
	float buffer[];

	public Sine()
	{
		buffer = new float[629];

		for(int i=0;i<629;i++)
		{
			buffer[i] = (float)Math.sin(((float)i) / 100.0f);			
		}

	}
	
	public float get(float radians) { return buffer[(int)(radians * 100.0f)]; }
	public float getFromIndex(int index) { return buffer[index]; }	
}
