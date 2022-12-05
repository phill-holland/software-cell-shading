
public class colour
{
	public float red,green,blue;
	public float alpha;

	public colour() { red = green = blue = alpha = 0.0f; }
	public colour(float _red,float _green,float _blue) { red = _red; blue = _blue; green = _green; }
	public colour(float _red,float _green,float _blue,float _alpha) { red = _red; blue = _blue; green = _green; alpha = _alpha; }

	public void copy(colour source)
	{
		red = source.red;
		green = source.green;
		blue = source.blue;

		alpha = source.alpha;
	}
}