import java.awt.*;
import java.applet.*;
import java.awt.image.*;

public class matrix
{
	public float m[][] = new float[4][4];

	public matrix() { reset(); }

	public void reset()
	{
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++) m[x][y] = 0.0f;
		}

		m[3][3] = 1.0f;
	}

	public void set(float a, float b, float c, float d, int index)
	{
		m[index][0] = a;
		m[index][1] = b;
		m[index][2] = c;
		m[index][3] = d;
	}

	public void row(float a, float b, float c, float d, int row)
	{
		set(a, b, c, d, row);
	}

	public void add(int x, int y, float v)
	{
		m[x][y] = v;
	}

	public float get(int x, int y) { return m[x][y]; }

	public vector multiply(vector src)
	{
		vector t = new vector(src.x * m[0][0] + src.y * m[0][1] + src.z * m[0][2] + src.w * m[0][3], src.x * m[1][0] + src.y * m[1][1] + src.z * m[1][2] + src.w * m[1][3], src.x * m[2][0] + src.y * m[2][1] + src.z * m[2][2] + src.w * m[2][3], src.x * m[3][0] + src.y * m[3][1] + src.z * m[3][2] + src.w * m[3][3]);
		return t;
	}

	public matrix multiply(matrix r)
	{
		matrix result = new matrix();

		for (int y = 0; y < 4; y++)
		{
			for (int x = 0; x < 4; x++)
			{								
				result.m[x][y] += m[x][0] * r.m[0][y];
				result.m[x][y] += m[x][1] * r.m[1][y];
				result.m[x][y] += m[x][2] * r.m[2][y];
				result.m[x][y] += m[x][3] * r.m[3][y];

				/*
				 * 		result.m[offset] += m[y] * r.m[(x * 4)];
				result.m[offset] += m[4 + y] * r.m[(x * 4) + 1];
				result.m[offset] += m[8 + y] * r.m[(x * 4) + 2];
				result.m[offset] += m[12 + y] * r.m[(x * 4) + 3];
				 * */
			}
		}


		return result;
	}

	public void display()
	{
		int x, y;

		for (x = 0; x < 4; x++)
		{
			System.out.print("(");
			for (y = 0; y < 4; y++)
			{
				System.out.print(m[x][y]);
				if (y < 3) System.out.print(",");
			}
			System.out.println(")");
		}
	}

	public matrix inverse() { matrix result = new matrix(); return result; }

	public void copy(matrix r)
	{
		for (int x = 0; x < 4; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				m[x][y] = r.m[x][y];
			}
		}
	}
}
