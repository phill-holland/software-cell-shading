package com.phillholland.app;

public class Catmull
{
	private boolean finished;
	private float currentPosition;
	private int positionIndex, count;

	private int endIndex = -1;

	private Point[] points = null;

	private Point endCondition = null;

	public float speed;

	public Colour _colour = new Colour(0.0f, 0.0f, 0.0f);

	public int width = 1;

	public Catmull(Point[] c)
	{
		if (c.length >= 4)
		{
			points = c;
			speed = 0.1f;			
		}

		reset();
	}

	public Catmull(Point[] c,Point stopCondition)
	{
		if (c.length >= 4)
		{
			points = c;
			speed = 0.1f;
		}

		reset();
		
		endCondition = new Point();
		endCondition.copy(stopCondition);
	}

	public void reset()
	{
		currentPosition = 0.0f;
		positionIndex = 0;
		count = 0;
		endIndex = points.length / 2;

		finished = false;
	}

	public Point get()
	{
		float time2 = currentPosition * currentPosition;
		float time3 = currentPosition * currentPosition * currentPosition;

		Point result = new Point();

		result.x = (2.0f * points[inc(positionIndex, 1)].x) + (-points[positionIndex].x + points[inc(positionIndex, 2)].x) * currentPosition;
		result.x += (2.0f * points[positionIndex].x - 5.0f * points[inc(positionIndex, 1)].x + 4.0f * points[inc(positionIndex, 2)].x - points[inc(positionIndex, 3)].x) * time2;
		result.x += (-points[positionIndex].x + 3.0f * points[inc(positionIndex, 1)].x - 3.0f * points[inc(positionIndex, 2)].x + points[inc(positionIndex, 3)].x) * time3;
		result.x *= 0.5f;

		result.y = (2.0f * points[inc(positionIndex, 1)].y) + (-points[positionIndex].y + points[inc(positionIndex, 2)].y) * currentPosition;
		result.y += (2.0f * points[positionIndex].y - 5.0f * points[inc(positionIndex, 1)].y + 4.0f * points[inc(positionIndex, 2)].y - points[inc(positionIndex, 3)].y) * time2;
		result.y += (-points[positionIndex].y + 3.0f * points[inc(positionIndex, 1)].y - 3.0f * points[inc(positionIndex, 2)].y + points[inc(positionIndex, 3)].y) * time3;
		result.y *= 0.5f;

		currentPosition += speed;

		if (currentPosition >= 1.0f)
		{
			if (count > endIndex)
			{
				result = new Point(-1.0f, -1.0f, -1.0f);
				finished = true;

				return result;
			}

			currentPosition = 0.0f;
			positionIndex = inc(positionIndex, 1);

			++count;
		}

		return result;
	}

	protected int inc(int i, int n)
	{
		int result = i + n;
		if (result >= points.length) result = result - points.length;

		return result;
	}

	protected int dec(int i, int n)
	{
		int result = i - n;
		if (result < 0) result = result + points.length;

		return result;
	}

	public boolean isFinished() { return finished; }

	public void render(PixelBuffer16 pb)
	{
		int previousx, previousy;
		boolean end = false;

		previousx = (int)points[0].x;
		previousy = (int)points[0].y;
	
		do
		{
			Point t = get();
			if (!isFinished())
			{
				int x = (int)t.x;
				int y = (int)t.y;


				pb.line(previousx, previousy, x, y, 255, (int)_colour.red, (int)_colour.green, (int)_colour.blue, width);

				if (endCondition != null) if ((x == (int)endCondition.x) && (y == (int)endCondition.y)) end = true;

				previousx = x;
				previousy = y;
			}
		} while ((!isFinished()) && (!end));		
	}
};
