import java.util.Random;

public class line
{
	public float noiseIncX, noiseIncY;

	public float intensityX, intensityY;

	public int width;

	public colour _colour = new colour(0.0f, 0.0f, 0.0f);

	public line()
	{
		//Random r = new Random();

		//noiseIncX = (float)r.nextDouble();
		//noiseIncY = (float)r.nextDouble();

		noiseIncX = noiseIncY = 0.0f;
		intensityX = intensityY = 2.0f;

		width = 1;
	}

	/*
	public void draw(pixelBuffer16 pb, point p0,point p1)
	{
		noise n = new noise();

		int numberOfPoints = 8;
		float offset = 1.0f / (float)(numberOfPoints - 1);

		point points[] = new point[numberOfPoints];

		for (int i = 0; i < numberOfPoints; i++)
		{
			points[i] = new point();

			float position = offset * (float)i;
			float u = 1.0f - position;

			float noisex = 0.0f, noisey = 0.0f;
			if((i>0)&&(i<numberOfPoints-1))
			{
				noisex = n.perlin(noiseIncX) * 2.0f;
				noiseIncX += 0.01f;
				noisey = n.perlin(noiseIncY) * 2.0f;
				noiseIncY += 0.01f;
			}
			points[i].x = ((u * p0.x) + position * p1.x) + noisex; // add noise to x here
			points[i].y = ((u * p0.y) + position * p1.y) + noisey; // add noise to y here ..?


			// add noise with exception of first and last points.

			pb.pixel(255, 0, 0, (int)points[i].x, (int)points[i].y);

			System.out.println(points[i].x + "," + points[i].y + "," + noisex + "," + noisey);
		}

		catmull cm = new catmull(points);

		cm.render(pb);
	}*/

	public void draw(pixelBuffer16 pb,point p0,point p1)
	{
		// need too check if p0.x==p1.x, then no need for rotation, and the sign of Y is the same
		
		noise n = new noise();

		vector origin = new vector(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);

		float length = (float)Math.sqrt((double)(origin.x * origin.x + origin.y * origin.y /*+ origin.z * origin.z*/));
		
		origin = origin.normalise();

		//System.out.println("origin " + origin.x + " " + origin.y + " " + origin.z);

		int dummyPoints = 4;
		int numberOfPoints = 12;//8;//8;
		float offset = length / (float)(numberOfPoints - 1 - dummyPoints);
		//System.out.println("offset " + offset);

		point points[] = new point[numberOfPoints];

		for (int i = 0; i < numberOfPoints; i++)
		{
			points[i] = new point();

			float noisex = 0.0f, noisey = 0.0f; // these can be seeded from a specific start point
			// auto inc. seed will make lines animated ...?
			if((i>0)&&(i<numberOfPoints-1-dummyPoints))
			{
				noisex = n.perlin(noiseIncX) * intensityX;//1.0f;//2.0f;
				noiseIncX += 0.01f;
				noisey = n.perlin(noiseIncY) * intensityY;//1.0f;//2.0f;
				noiseIncY += 0.01f;
			}
			
			points[i].x = noisex;
			points[i].y = (offset * (float)i) + noisey;

			//System.out.println("point " + i + "," + points[i].x + "," + points[i].y);
			//System.out.println(points[i].x + " " + points[i].y + " " + points[i].z);
		}

		//points[numberOfPoints - 1].x = 0.0f;//p1.x;
		//points[numberOfPoints - 1].y = length - p0.y;//p1.y;
		//System.out.println("len " + length);

		int a = (int)(p0.x * 10.0f);
		int b = (int)(p1.x * 10.0f);

		if (a != b)
		{
			//System.out.println("hello");
			vector default_orientation = new vector(0.0f, 1.0f, 0.0f);

			vector axis = origin.cross(default_orientation);
			float d = -default_orientation.dot(origin);

			float s = (float)Math.sqrt((double)((1.0f + d) * 2.0f));
			quaternion q = new quaternion(axis.x / s, axis.y / s, axis.z / s, s / 2.0f);

			matrix m = q.create();
			//matrix m = q.create();

			for (int i = 0; i < numberOfPoints; i++)
			{
				vector t = m.multiply(new vector(points[i].x, points[i].y, points[i].z));

				//System.out.println("vector (" + t.x + "," + t.y + ")");
				points[i].x = t.x + p0.x;
				points[i].y = t.y + p0.y;
				points[i].z = t.z + p0.z;

				//pb.pixel(255, 0, 0, (int)points[i].x, (int)points[i].y);
				//System.out.println(points[i].x + " " + points[i].y);
			}
		}
		catmull cm = new catmull(points,points[points.length - dummyPoints -1]);
		cm.width = width;
		cm._colour = _colour;

		cm.render(pb);
	}
}


/*
	public matrix rotate(vector a,vector b)
	{
	  // a is super.direction.normalise()
	  // b is vector(x - preview.x,y - preview.y).normalise()
		//vector a = new vector(0.0f,1.0f,0.0f);
		//vector b = new vector(position.x - view.x,position.y - view.y,position.z - view.z);
		
		//b.normalise();
		
		vector axis = b.cross(a);

		float d = -a.dot(b);
		float s = (float)Math.sqrt((double)((1.0f + d) * 2.0f));

		quaternion q = new quaternion(axis.x / s,axis.y / s,axis.z / s,s / 2.0f);
		return q.create(q.w);
		//vector w = m.multiply(new vector(0.0f,15.0f,0.0f));
	}*/