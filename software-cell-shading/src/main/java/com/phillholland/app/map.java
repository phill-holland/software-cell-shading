
public class map
{
	float u, v;

	public map()
	{
		reset(0.0f, 0.0f);
	}

	public map(float a, float b) { reset(a, b); }

	public void reset(float a, float b) { u = a; v = b; }
}
