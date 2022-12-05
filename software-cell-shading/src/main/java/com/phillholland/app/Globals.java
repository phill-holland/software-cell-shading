package com.phillholland.app;

public class Globals
{
	public static Sine Sine = new Sine();
	public static Cosine Cosine = new Cosine();

	public static Matrix yMatrix(float a)
	{
		Matrix result = new Matrix();

		result.add(0, 0, (float)Cosine.get(a));
		result.add(2, 0, -(float)Sine.get(a));
		result.add(0, 2, (float)Sine.get(a));
		result.add(2, 2, (float)Cosine.get(a));

		result.add(1, 1, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static Matrix xMatrix(float a)
	{
		Matrix result = new Matrix();

		result.add(1, 1, (float)Cosine.get(a));
		result.add(2, 1, (float)Sine.get(a));
		result.add(1, 2, -(float)Sine.get(a));
		result.add(2, 2, (float)Cosine.get(a));
		result.add(0, 0, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static Matrix zMatrix(float a)
	{
		Matrix result = new Matrix();

		result.add(0, 0, (float)Cosine.get(a));
		result.add(1, 0, (float)Sine.get(a));
		result.add(0, 1, -(float)Sine.get(a));
		result.add(1, 1, (float)Cosine.get(a));
		result.add(2, 2, 1.0f);
		result.add(3, 3, 1.0f);

		return result;
	}

	public static Matrix tMatrix(Point v)
	{
		Matrix result = new Matrix();

		for (int x = 0; x < 3; x++) result.add(x, x, 1.0f);

		result.row(v.x, v.y, v.z, 1.0f, 3);

		return result;
	}

	public static Matrix sMatrix(Point v)
	{
		Matrix result = new Matrix();

		result.add(0, 0, v.x);
		result.add(1, 1, v.y);
		result.add(2, 2, v.z);
		result.add(3, 3, 1.0f);

		return result;
	}
}
		