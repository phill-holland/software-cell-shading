public class quaternion
{
	public float x, y, z, w;

	public quaternion() { reset(); }
	public quaternion(float a, float b, float c, float d) { x = a; y = b; z = c; w = d; }

	public void reset()
	{
		x = y = z = w = 0.0f;
	}

	public float length()
	{
		return (float)Math.sqrt((double)(x * x + y * y + z * z + w * w));
	}

	public void normalise()
	{
		float t = 1.0f / (length() + 0.001f);
		x *= t; y *= t; z *= t; w *= t;
	}

	public quaternion conjugate()
	{
		quaternion t = new quaternion(-x, -y, -z, w);
		return t;
	}

	public quaternion multiply(quaternion src)
	{
		quaternion t = new quaternion();

		t.x = w * src.x + x * src.w + y * src.z - z * src.y;
		t.y = w * src.y - x * src.z + y * src.w + z * src.x;
		t.z = w * src.z + x * src.y - y * src.x + z * src.w;
		t.w = w * src.w - x * src.x - y * src.y - z * src.z;

		return t;
	}

	public void inverse()
	{
		float l = 1.0f / (x * x + y * y + z * z + w * w);
		x *= -l; y *= -l; z *= -1; w *= l;
	}

	public matrix create()
	{
		matrix m = new matrix();

		float xx = x * x;
		float yy = y * y;
		float zz = z * z;
		float ww = w * w;

		m.set(1.0f - 2.0f * yy - 2.0f * zz, 2.0f * x * y - 2.0f * w * z, 2.0f * x * z + 2.0f * w * y, 0.0f, 0);
		m.set(2.0f * x * y + 2.0f * w * z, 1.0f - 2.0f * xx - 2.0f * zz, 2.0f * y * z - 2.0f * w * x, 0.0f, 1);
		m.set(2.0f * x * z - 2.0f * w * y, 2.0f * y * z + 2.0f * w * x, 1.0f - 2.0f * xx - 2.0f * yy, 0.0f, 2);
		m.set(0.0f, 0.0f, 0.0f, 1.0f, 3);

		return m;
	}
	/*
	public basicMatrix createB()
	{
		basicMatrix m = new basicMatrix();

		float xx = x * x;
		float yy = y * y;
		float zz = z * z;
		float ww = w * w;

		m.set(1.0f - 2.0f * yy - 2.0f * zz, 2.0f * x * y - 2.0f * w * z, 2.0f * x * z + 2.0f * w * y, 0.0f, 0);
		m.set(2.0f * x * y + 2.0f * w * z, 1.0f - 2.0f * xx - 2.0f * zz, 2.0f * y * z - 2.0f * w * x, 0.0f, 1);
		m.set(2.0f * x * z - 2.0f * w * y, 2.0f * y * z + 2.0f * w * x, 1.0f - 2.0f * xx - 2.0f * yy, 0.0f, 2);
		m.set(0.0f, 0.0f, 0.0f, 1.0f, 3);

		return m;
	}
	 */
}