
public class light
{
	public vector direction = new vector();
	public point position = new point();

	public float radius = 1.0f;

	public colour _colour = new colour(1.0f, 1.0f, 1.0f);

	public light()
	{
		//reset(0.0f, 0.0f, 0.0f);
	}

	//public light(float a, float b, float c) { reset(a, b, c); }

	public light(vector _direction, point _position, float _radius, colour _col)
	{
		reset(_direction, _position, _radius, _col);
	}

	public void reset(vector _direction, point _position, float _radius, colour _col)
	{
		direction.copy(_direction);
		position.copy(_position);
		_colour.copy(_col);

		radius = _radius;
	}
	/*
	public void reset(float a, float b, float c) 
	{
		direction.x = a; direction.y = b; direction.z = c;
	}
	 * */
}
