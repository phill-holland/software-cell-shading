
public class globals
{
	public static sine Sine = new sine();
	public static cosine Cosine = new cosine();

	public static matrix yMatrix(float a)
	{
		matrix result = new matrix();

		result.add(0, 0, (float)Cosine.get(a));
		result.add(2, 0, -(float)Sine.get(a));
		result.add(0, 2, (float)Sine.get(a));
		result.add(2, 2, (float)Cosine.get(a));

		result.add(1, 1, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static matrix xMatrix(float a)
	{
		matrix result = new matrix();

		result.add(1, 1, (float)Cosine.get(a));
		result.add(2, 1, (float)Sine.get(a));
		result.add(1, 2, -(float)Sine.get(a));
		result.add(2, 2, (float)Cosine.get(a));
		result.add(0, 0, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static matrix zMatrix(float a)
	{
		matrix result = new matrix();

		result.add(0, 0, (float)Cosine.get(a));
		result.add(1, 0, (float)Sine.get(a));
		result.add(0, 1, -(float)Sine.get(a));
		result.add(1, 1, (float)Cosine.get(a));
		result.add(2, 2, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static matrix tMatrix(point v)
	{
		matrix result = new matrix();

		for (int x = 0; x < 3; x++) result.add(x, x, 1.0f);

		result.row(v.x, v.y, v.z, 1.0f, 3);

		return result;
	}

	public static matrix sMatrix(point v)
	{
		matrix result = new matrix();

		result.add(0, 0, v.x);
		result.add(1, 1, v.y);
		result.add(2, 2, v.z);
		result.add(3, 3, 1.0f);

		return result;
	}
}
		