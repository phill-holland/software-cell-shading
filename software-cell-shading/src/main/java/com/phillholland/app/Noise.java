package com.phillholland.app;

public class Noise
{
	public int octaves = 8;
	public float persistance = 1.0f;

	public Noise()
	{

	}

	private float cosineInterpolate(float a, float b, float x)
	{
		float ft = x * 3.1415927f;
		float f = (1.0f - (float)Math.cos((double)ft)) * 0.5f;

		return a * (1.0f - f) + b * f;
	}

	private float hermiteInterpolate(float current, float next, float currentPosition)
	{		
		float time2 = currentPosition * currentPosition;
		float time3 = time2 * currentPosition;

		float a = 2.0f * time3 - 3.0f * time2 + 1.0f;
		float d = -2.0f * time3 + 3.0f * time2;

		return a * current + d * next; // can be extended to 3D interpolation :-)
	}

	private float fnoise(int x, int y)
	{
		int n = x + y * 57;
		n = (n << 13) ^ n;

		return (1.0f - (float)((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}

	private float fnoise(int x)
	{		
		int n = (x << 13) ^ x;
		return (float)(1.0f - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}

	private float smooth(int x, int y)
	{
		float corners = (fnoise(x - 1, y - 1) + fnoise(x + 1, y - 1) + fnoise(x - 1, y + 1) + fnoise(x + 1, y + 1)) / 16.0f;
		float sides = (fnoise(x - 1, y) + fnoise(x + 1, y) + fnoise(x, y - 1) + fnoise(x, y + 1)) / 8.0f;
		float center = fnoise(x, y) / 4.0f;
		return corners + sides + center;
	}

	private float interpolated(float x, float y)
	{
		int x1 = (int)x;
		float frac_x = x - x1;

		int y1 = (int)y;
		float frac_y = y - y1;

		float v1 = smooth(x1, y1);
		float v2 = smooth(x1 + 1, y1);
		float v3 = smooth(x1, y1 + 1);
		float v4 = smooth(x1 + 1, y1 + 1);

		float i1 = hermiteInterpolate(v1, v2, frac_x);
		float i2 = hermiteInterpolate(v3, v4, frac_x);

		return hermiteInterpolate(i1, i2, frac_y);
	}

	public float perlin(float x, float y)
	{
		float result = 0.0f;
		float amplitude, frequency;

		for (int n = 0; n < octaves; n++)
		{
			frequency = 2.0f;
			amplitude = persistance;

			for (int i = 0; i < n - 1; i++)
			{
				frequency *= 2.0f;
				amplitude *= persistance;

				result += interpolated(x * frequency, y * frequency) * amplitude;
			}
		}

		return result;
	}

	public float perlin(float x)
	{
		float result = 0.0f;
		float amplitude, frequency;

		float value;

		for (int n = 0; n < octaves; n++)
		{
			frequency = 2.0f;
			amplitude = persistance;

			for (int i = 0; i < n - 1; i++)
			{
				frequency *= 2.0f;
				amplitude *= persistance;
			}

			value = x * frequency;
		
			float a = fnoise((int)value);
			float b = fnoise((int)(value) + 1);


			result += hermiteInterpolate(a, b, value - (float)((int)value)) * amplitude;
		}

		return result;
	}

	public void draw(PixelBuffer16 pb)
	{
		float xoffset = 0.0f;
		float yoffset = 0.0f;

		int len = pb.width * pb.height;
		float buffer[] = new float[len];

		for (int x = 0; x < pb.width; x++)
		{
			yoffset = 0.0f;
			for (int y = 0; y < pb.height; y++)
			{
				float value = perlin(xoffset,yoffset);
				buffer[y * pb.width + x] = value;
			
				yoffset += 0.01f;
			}
			xoffset += 0.01f;
		}


		float min = 0.0f, max = 0.0f;

		for (int i = 0; i < len; i++)
		{
			if (buffer[i] > max) max = buffer[i];
			if (buffer[i] < min) min = buffer[i]; 
		}

		float range = max - min;
		float factor = 255.0f / range;

		if (min < 0.0f) min *= -1.0f;		

		for (int x = 0; x < pb.width; x++)
		{

			for (int y = 0; y < pb.height; y++)
			{
				float value = buffer[y * pb.width + x];
				value = (value + min);
				
				value *= factor;
				
				pb.pixel((int)value, (int)value, (int)value, x, y);
			}
		}
	}
}
