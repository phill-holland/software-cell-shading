package com.phillholland.app;

public class Line
{
	public float noiseIncX, noiseIncY;

	public float intensityX, intensityY;

	public int width;

	public Colour _colour = new Colour(0.0f, 0.0f, 0.0f);

	public Line()
	{
		noiseIncX = noiseIncY = 0.0f;
		intensityX = intensityY = 2.0f;

		width = 1;
	}

	public void draw(PixelBuffer16 pb,Point p0,Point p1)
	{
		Noise n = new Noise();

		Vector origin = new Vector(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);

		float length = (float)Math.sqrt((double)(origin.x * origin.x + origin.y * origin.y));
		
		origin = origin.normalise();

		int dummyPoints = 4;
		int numberOfPoints = 12;
		float offset = length / (float)(numberOfPoints - 1 - dummyPoints);

		Point points[] = new Point[numberOfPoints];

		for (int i = 0; i < numberOfPoints; i++)
		{
			points[i] = new Point();

			float noisex = 0.0f, noisey = 0.0f;
			
			if((i>0)&&(i<numberOfPoints-1-dummyPoints))
			{
				noisex = n.perlin(noiseIncX) * intensityX;
				noiseIncX += 1.01f;
				noisey = n.perlin(noiseIncY) * intensityY;
				noiseIncY += 1.01f;
			}
			
			points[i].x = noisex;
			points[i].y = (offset * (float)i) + noisey;
		}

		int a = (int)(p0.x * 10.0f);
		int b = (int)(p1.x * 10.0f);

		if (a != b)
		{
			Vector default_orientation = new Vector(0.0f, 1.0f, 0.0f);

			Vector axis = origin.cross(default_orientation);
			float d = -default_orientation.dot(origin);

			float s = (float)Math.sqrt((double)((1.0f + d) * 2.0f));
			Quaternion q = new Quaternion(axis.x / s, axis.y / s, axis.z / s, s / 2.0f);

			Matrix m = q.create();

			for (int i = 0; i < numberOfPoints; i++)
			{
				Vector t = m.multiply(new Vector(points[i].x, points[i].y, points[i].z));

				points[i].x = t.x + p0.x;
				points[i].y = t.y + p0.y;
				points[i].z = t.z + p0.z;
			}
		}
		Catmull cm = new Catmull(points,points[points.length - dummyPoints -1]);
		cm.width = width;
		cm._colour = _colour;

		cm.render(pb);
	}
}