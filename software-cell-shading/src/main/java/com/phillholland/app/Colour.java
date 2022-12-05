package com.phillholland.app;

public class Colour
{
	public float red,green,blue;
	public float alpha;

	public Colour() { red = green = blue = alpha = 0.0f; }
	public Colour(float _red,float _green,float _blue) { red = _red; blue = _blue; green = _green; }
	public Colour(float _red,float _green,float _blue,float _alpha) { red = _red; blue = _blue; green = _green; alpha = _alpha; }

	public void copy(Colour source)
	{
		red = source.red;
		green = source.green;
		blue = source.blue;

		alpha = source.alpha;
	}
}