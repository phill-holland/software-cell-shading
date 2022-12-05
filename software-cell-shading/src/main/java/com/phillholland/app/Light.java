package com.phillholland.app;

public class Light
{
	public Vector direction = new Vector();
	public Point position = new Point();

	public float radius = 1.0f;

	public Colour _colour = new Colour(1.0f, 1.0f, 1.0f);

	public Light()
	{
		
	}

	public Light(Vector _direction, Point _position, float _radius, Colour _col)
	{
		reset(_direction, _position, _radius, _col);
	}

	public void reset(Vector _direction, Point _position, float _radius, Colour _col)
	{
		direction.copy(_direction);
		position.copy(_position);
		_colour.copy(_col);

		radius = _radius;
	}
}
