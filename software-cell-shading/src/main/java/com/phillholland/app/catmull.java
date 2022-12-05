
public class catmull
{
	private boolean finished;
	private float currentPosition;
	private int positionIndex, count;

	private int endIndex = -1;

	private point[] points = null;

	private point endCondition = null;

	public float speed;

	public colour _colour = new colour(0.0f, 0.0f, 0.0f);

	public int width = 1;

	public catmull(point[] c)
	{
		if (c.length >= 4)
		{
			points = c;
			speed = 0.1f;			
		}

		reset();
	}

	public catmull(point[] c,point stopCondition)
	{
		if (c.length >= 4)
		{
			points = c;
			speed = 0.1f;
		}

		reset();
		//endIndex = endIdx;
		endCondition = new point();
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

	public point get()
	{
		float time2 = currentPosition * currentPosition;
		float time3 = currentPosition * currentPosition * currentPosition;

		point result = new point();

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
			//if (inc(positionIndex, 1) == points.length - 1)
			if (count > endIndex)//points.length / 2)
			{
				result = new point(-1.0f, -1.0f, -1.0f);
				finished = true;

				//System.out.println("fin");
				return result;
			}
			/*else
			{
				int t = points.length / 2;
				if ((((int)result.x) == (int)points[t - 1].x) && (((int)result.y) == (int)points[t - 1].y))
				{
					result = new point(-1.0f, -1.0f, -1.0f);
					finished = true;

					System.out.println("DONE ");
					return result;
				}
			}*/

			currentPosition = 0.0f;
			positionIndex = inc(positionIndex, 1);

			//System.out.println("count " + count);
			++count;
		}
		//System.out.println("RESULT " + result.x + "," + result.y);
		//System.out.println("end point " + points[points.length - 1].x + "," + points[points.length - 1].y);
		/*if ((((int)result.x) == (int)points[points.length - 1].x) && (((int)result.y) == (int)points[points.length - 1].y))
		{
			System.out.println("finished");
			finished = true;
		}*/

		return result;
	}

	protected int inc(int i, int n)
	{
		int result = i + n;
		if (result >= points.length) result = result - points.length;//i - points.Length - 1;

		return result;
	}

	protected int dec(int i, int n)
	{
		int result = i - n;
		if (result < 0) result = result + points.length;

		return result;
	}

	public boolean isFinished() { return finished; }

	public void render(pixelBuffer16 pb)
	{
		//System.out.println("START");
		int previousx, previousy;
		boolean end = false;

		//point t = get();
		previousx = (int)points[0].x;//(int)t.x;
		previousy = (int)points[0].y;//(int)t.y;
	
		do
		{
			point t = get();
			if (!isFinished())
			{
				int x = (int)t.x;
				int y = (int)t.y;


				//pb.line(previousx, previousy, x, y, 255, 255, 0, 0, 2);
				pb.line(previousx, previousy, x, y, 255, (int)_colour.red, (int)_colour.green, (int)_colour.blue, width);

				if (endCondition != null) if ((x == (int)endCondition.x) && (y == (int)endCondition.y)) end = true;
				//System.out.println("line(" + previousx + "," + previousy + "," + x + "," + y + ")");
				previousx = x;
				previousy = y;
			}
		} while ((!isFinished()) && (!end));		
	}
	/*
	public void render(pixelBuffer16 pb)
	{
		//System.out.println("START");
		int previousx, previousy;
		boolean end = false;

		//point t = get();
		previousx = (int)points[0].x;//(int)t.x;
		previousy = (int)points[0].y;//(int)t.y;

		do
		{
			point t = get();
			if (!isFinished())
			{
				int x = (int)t.x;
				int y = (int)t.y;


				pb.line(previousx, previousy, x, y, 255, 0, 0);
				
				if(endCondition!=null) if ((x == (int)endCondition.x) && (y == (int)endCondition.y)) end = true;
				//System.out.println("line(" + previousx + "," + previousy + "," + x + "," + y + ")");
				previousx = x;
				previousy = y;
			}
		} while ((!isFinished())&&(!end));
	}
	 * */
};
