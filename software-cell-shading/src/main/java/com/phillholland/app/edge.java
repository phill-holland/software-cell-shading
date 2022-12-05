import java.util.Random;

public class edge
{
	public int a, b;

	public int polygons[] = null;
	public int length;

	public float seedX, seedY;
	
	public edge(int total_polygons)
	{
		a = b = length = 0;

		polygons = new int[total_polygons];

		//seedX = seedY = 0.0f; // random gen ...
		Random r = new Random();

		seedX = (float)r.nextDouble();
		seedY = (float)r.nextDouble();
	}

	public edge(int x, int y,int total_polygons) 
	{ 
		a = x; 
		b = y;

		length = 0;

		polygons = new int[total_polygons];
	}

	public void add(int index)
	{
		polygons[length] = index;
		++length;
	}
}
