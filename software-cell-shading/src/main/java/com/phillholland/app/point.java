
public class point
{
	public float x, y, z;
	//public float u, v;

	public colour _colour = new colour();
	
	public point()
	{
		x = y = z = 0.0f;
		//u = v = 0.0f;
	}

	public point(float x1, float y1) { set(x1, y1, 0.0f); }//, 0.0f, 0.0f); }

	public point(float x1, float y1, float z1) { set(x1, y1, z1); }//,0.0f,0.0f); }

	//public point(float x1, float y1, float z1, float u1, float v1) { set(x1, y1, z1, u1, v1); }

	public void copy(point p)
	{
		x = p.x;
		y = p.y;
		z = p.z;

		//u = p.u;
		//v = p.v;
	}

	public void set(float x1, float y1,float z1)//,float u1,float v1)
	{
		x = x1; y = y1; z = z1;
	}

	public point transform(camera c)
	{
		//float t = 1.0f / (z + 0.2f);
		//return new point((((scale * x) * t) + 1.0f),(((scale * y) * t) + 1.0f),z,u,v);
		return new point(c.focus * (x / z), c.focus * (y / z), z);//, u, v);
	}

	public point minus(point r)
	{
		return new point(x - r.x, y - r.y, z - r.z);
	}

	public point add(point r)
	{
		return new point(x + r.x, y + r.y, z + r.z);
	}

	public matrix get()
	{
		matrix m = new matrix();
		m.row(x, y, z, 1.0f, 0);
		return m;
	}

	public void set(matrix m)
	{
		x = m.get(0, 0);
		y = m.get(0, 1);
		z = m.get(0, 2);
	}

	public void display()
	{
		System.out.println(x + " " + y + " " + z);
	}
}
