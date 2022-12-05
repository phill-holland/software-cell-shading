import java.awt.image.*;
import java.awt.*;

public class oldPolygon
{
	/*
	public point points[];
	public point temp[];
	public texture t;	

	public colour Colour;

	public boolean backface;

	public int textureIndex;

	public float zOrder;

	public polygon(int elements,texture tx)
	{
		textureIndex = 0;
		t = tx; backface = false;

		points = new point[elements];
		temp = new point[elements];

		for(int i=0;i<elements;i++)
		{
			points[i] = new point();
			temp[i] = new point();
		}
	}

	public polygon(int elements)
	{
		textureIndex = 0;
		backface = false;

		points = new point[elements];
		temp = new point[elements];

		for(int i=0;i<elements;i++)
		{
			points[i] = new point();
			temp[i] = new point();
		}
	}

	public polygon(point p[])
	{
		textureIndex = 0;
		backface = false;

		int elements = p.length;

		points = new point[elements];
		temp = new point[elements];

		for(int i=0;i<elements;i++)
		{
			points[i] = new point();
			points[i].copy(p[i]);
			temp[i] = new point();
		}
	}

	public void set(texture tx) { t = tx; }

	public boolean isInside(camera c)
	{
		for(int i=0;i<6;i++)
		{
			int status = 0;
			int outside = 0;

			for(int j=0;j<temp.length;j++)
			{
				vector a = new vector(temp[j].x,temp[j].y,temp[j].z,0.0f);
				float t = a.dot(c.tempPlanes[i]);
				if(t < c.tempPlanes[i].w)
				{
					status = status |  (1 << j);
				}
				outside = outside | (1 << j);				
			}	

			System.out.println("outside " + outside + " status " + status);
			if(status==outside) return false;
		}

		return true;

	}	

	public boolean isInsideOld(camera c)
	{
		// all points must be outside before returning false
		System.out.println("ISINSIDE");

		for(int i=0;i<4;i++)
		{
			System.out.println("plane " + i);

			for(int j=0;j<temp.length;j++)
			{
				vector a = new vector(temp[j].x,temp[j].y,temp[j].z,0.0f);
				//float t = a.dot(c.tempPlanes[i]) - c.tempPlanes[i].w;
				float t = a.dot(c.tempPlanes[i]);

				a.display();
				c.tempPlanes[i].display();

				float bob = temp[j].x * c.tempPlanes[i].x + temp[j].y * c.tempPlanes[i].y + temp[j].z * c.tempPlanes[i].z;

				System.out.println("T" + t + " W " + c.tempPlanes[i].w + " BOB " + bob);
				//if(t < 0.0f)//c.planes[i].w)
				if(t < c.tempPlanes[i].w)
				{					
					System.out.println("outside " + j);
					temp[j].display();
					System.out.println(" ");
				}
				else
				{
temp[j].display();
					System.out.println(" ");
System.out.println("inside " + j);
				} 
			}

			System.out.println(" ");
		}

		return true;
	}

	public void X(float radians) 
	{ 
		float z,y;
		for(int i=0;i<temp.length;i++)
		{
			z = temp[i].z * globals.Cosine.get(radians) - temp[i].y * globals.Sine.get(radians);
			y = temp[i].z * globals.Sine.get(radians) + temp[i].y * globals.Cosine.get(radians);

			temp[i].z = z;
			temp[i].y = y;
		}

	}

	public void Y(float radians) 
	{ 
		float z,x;
		for(int i=0;i<temp.length;i++)
		{
			x = temp[i].x * globals.Cosine.get(radians) - temp[i].z * globals.Sine.get(radians);
			z = temp[i].x * globals.Sine.get(radians) + temp[i].z * globals.Cosine.get(radians);

			temp[i].x = x;
			temp[i].z = z;
		}

	}

	public void Z(float radians) 
	{ 
		float x,y;
		for(int i=0;i<temp.length;i++)
		{
			x = temp[i].x * globals.Cosine.get(radians) - temp[i].y * globals.Sine.get(radians);
			y = temp[i].x * globals.Sine.get(radians) + temp[i].y * globals.Cosine.get(radians);

			temp[i].x = x;
			temp[i].y = y;
		}
	}

	public void translate(matrix m)
	{
		for(int i=0;i<temp.length;i++)
		{
			matrix t = temp[i].get();
			matrix result = t.multiply(m);
			temp[i].set(result);
		}
	}

	public void translate(camera c)
	{
		//System.out.println("translate ");
		//c.c.display();

		for(int i=0;i<temp.length;i++)
		{
			matrix t = temp[i].get();
			matrix result = t.multiply(c.c);
			//result = result.multiply(c.translation);
			
			temp[i].set(result);
		}
	}

	public float order()
	{
		zOrder = 0.0f;

		for(int i=0;i<temp.length;i++)
		{
			zOrder += temp[0].z;
		}

		zOrder /= (float)temp.length;

		return zOrder;
	}

	public vector normal()
	{
		vector a = new vector(temp[0].x,temp[0].y,temp[0].z,0.0f);		
		vector b = new vector(a.x - temp[inc(0)].x,a.y - temp[inc(0)].y,a.z - temp[inc(0)].z,0.0f);
		vector c = new vector(a.x - temp[dec(0)].x,a.y - temp[dec(0)].y,a.z - temp[dec(0)].z,0.0f);			

		return b.cross(c).normalise();
	}

	public point average()
	{
		point result = new point(0.0f,0.0f,0.0f);

		for(int i=0;i<temp.length;i++)
		{
			result.x += temp[i].x; result.y += temp[i].y; result.z += temp[i].z;
		}

		result.x /= (float)temp.length;
		result.y /= (float)temp.length;
		result.z /= (float)temp.length;

		return result;
	}

	public boolean isBackface(vector los)
	{
		vector n = normal();
		float result = n.dot(new vector(temp[0].x - los.x,temp[0].y - los.y,temp[0].z - los.z,0.0f));

		if(result<=0.0f) { backface = false; return false; }

		backface = true;

		return true;
	}

	public void copy()
	{
		for(int i=0;i<points.length;i++) { temp[i].copy(points[i]); }
	}

	public void transform(camera c)
	{
		for(int i=0;i<points.length;i++)
		{
			//System.out.println(temp[i].x + " " + temp[i].y + " " + temp[i].z);
			temp[i].copy(temp[i].transform(c));			
			//System.out.println(temp[i].x + " " + temp[i].y + " " + temp[i].z);
			//System.out.println(" ");
		}

		//System.out.println("DONE");
	}

	public void translate(point p)
	{
		for(int i=0;i<points.length;i++)	
		{
			temp[i].x += p.x;
			temp[i].y += p.y;			
		}
	}

	public void outline(pixelBuffer16 pb)
	{

		point previous = temp[points.length-1];//.transform();

		for(int i=0;i<points.length;i++)
		{
			point t = temp[i];//.transform();

			pb.line((int)previous.x,(int)previous.y,(int)t.x,(int)t.y,255,0,0);

			previous = t;
		}
	}

	public void scale(float s)
	{
		for(int i=0;i<points.length;i++)
		{
			temp[i].x *= s;
			temp[i].y *= s;
		}
	}

	public void output()
	{
		for(int i=0;i<points.length;i++)
		{
			System.out.println(temp[i].x);
			System.out.println(temp[i].y);
			System.out.println(temp[i].z);
			System.out.println(" ");
		}
	}

	public void paint(Graphics g)
	{
		int previousx,previousy;

		previousx = (int)temp[points.length-1].x;
		previousy = (int)temp[points.length-1].y;

		g.setColor(Color.red);

		for(int i=0;i<points.length;i++)
		{
			int x = (int)temp[i].x;
			int y = (int)temp[i].y;

			g.drawLine(previousx,previousy,x,y);

			previousx = x;
			previousy = y;
		}
	}

	public void paint(pixelBuffer16 pb)
	{
		try
		{
		int topIndex = 0;
		int bottomIndex = 0;

		int leftIndex = 0;
		int rightIndex = 0;

		int topLeftIndex = 0;
		int topRightIndex = 0;

		boolean finished;

		for(int i=0;i<temp.length;i++)
		{
			//System.out.println("i " + i + " " + temp[i].x + " " + temp[i].y);
			//System.out.println("i " + i + " " + (int)temp[i].x + " " + (int)temp[i].y);
			if((int)(temp[topIndex].y) > (int)(temp[i].y)) topIndex = i;
			if((int)(temp[bottomIndex].y) <= (int)(temp[i].y)) bottomIndex = i;
		}

		if(topIndex==bottomIndex) return;

		//System.out.println("topIndex " + topIndex);
		finished = false;
		leftIndex = topIndex;

		int count = 0;

		do
		{
			if((int)(temp[leftIndex].y)==(int)(temp[topIndex].y))
			{
				int t = dec(leftIndex);
				count++;
				if((int)(temp[t].y)==(int)(temp[topIndex].y)) leftIndex = t;
					else finished = true;

				if((count>=temp.length)&&(finished==false)) return;

			} else finished = true;

		}while(!finished);


		finished = false;
		rightIndex = topIndex;
		count = 0;

		do
		{
			if((int)(temp[rightIndex].y)==(int)(temp[topIndex].y))
			{
				int t = inc(rightIndex);
				count++;
				if((int)(temp[t].y)==(int)(temp[topIndex].y)) rightIndex = t;
					else finished = true;

				if((count>=temp.length)&&(finished==false)) return;

			} else finished = true;
		}while(!finished);

		topLeftIndex = leftIndex;
		topRightIndex = rightIndex;

		leftIndex = dec(topLeftIndex);
		rightIndex = inc(topRightIndex);

		//System.out.println("top left " + topLeftIndex + " left " + leftIndex);		
		//System.out.println("top right " + topRightIndex + " right " + rightIndex);
		//System.out.println("start");
		System.out.println("poly A");

		float leftGradient = (temp[topLeftIndex].x - temp[leftIndex].x) / (temp[topLeftIndex].y - temp[leftIndex].y);
		float rightGradient = (temp[topRightIndex].x - temp[rightIndex].x) / (temp[topRightIndex].y - temp[rightIndex].y);
		
		float leftUGradient = (temp[topLeftIndex].u - temp[leftIndex].u) / (temp[topLeftIndex].y - temp[leftIndex].y);
		float leftVGradient = (temp[topLeftIndex].v - temp[leftIndex].v) / (temp[topLeftIndex].y - temp[leftIndex].y);

		float rightUGradient = (temp[topRightIndex].u - temp[rightIndex].u) / (temp[topRightIndex].y - temp[rightIndex].y);
		float rightVGradient = (temp[topRightIndex].v - temp[rightIndex].v) / (temp[topRightIndex].y - temp[rightIndex].y);

		System.out.println("poly B");		
		
		//int yIntTop = (int)temp[topIndex].y;//(int)Math.round(temp[topIndex].y);
		int yInt = (int)temp[topIndex].y;
		int height = ((int)temp[bottomIndex].y - (int)temp[topIndex].y);// - 1;

		System.out.println(temp[bottomIndex].y + " " + temp[topIndex].y);
		System.out.println(bottomIndex + " " + topIndex);
		System.out.println("poly C " + height);
		
		
		finished = false;
		do
		{
			float y = (float)yInt; 
			//System.out.println("poly C " + y);

			//float startx = temp[topLeftIndex].x + ((y - temp[topLeftIndex].y) * leftGradient);
			//float endx = temp[topRightIndex].x + ((y - temp[topRightIndex].y) * rightGradient);

			int startx = (int)(temp[topLeftIndex].x) + (int)(((float)((int)(y) - (int)(temp[topLeftIndex].y)) * leftGradient));
			int endx = (int)(temp[topRightIndex].x) + (int)(((float)((int)(y) - (int)(temp[topRightIndex].y)) * rightGradient));

			float startu = (temp[topLeftIndex].u + ((y - temp[topLeftIndex].y) * leftUGradient));// * t.width;
			float endu = (temp[topRightIndex].u + ((y - temp[topRightIndex].y) * rightUGradient));// * t.width;

			float startv = (temp[topLeftIndex].v + ((y - temp[topLeftIndex].y) * leftVGradient)) ;//* t.height;
			float endv = (temp[topRightIndex].v + ((y - temp[topRightIndex].y) * rightVGradient));// * t.height;

			//System.out.println("poly D");

			// ***

			//float tempx;
			int tempx;
			float ftempx;
			
			if(startx>endx)
		 	{
				tempx = startx;
				startx = endx;
				endx = tempx;

				ftempx = startv;
				startv = endv;
				endv = ftempx;

				ftempx = startu;
				startu = endu;
				endu = ftempx;
			}
			
			
			//System.out.println("poly E");
			int startxint = startx;//Math.round(startx);
			int endxint = endx;//Math.round(endx);

			//System.out.println("start " + startxint + " end " + endxint);
			int lengthint = endxint - startxint;

			float length = endx - startx;			

			float ulength = endu - startu;
			float uInterval = ulength / length;

			float vlength = endv - startv;
			float vInterval = vlength / length;
			
			//System.out.println("y " + yInt + " length " + lengthint);
			

		//System.out.println("poly F");

			if(lengthint>=1)
			{											
				int tempstartx = startx;//Math.round(startx);
				float uoffset = startu;
				float voffset = startv;

				for(int i=0;i<lengthint;i++)
				{
					colour c = t.get(startu + ((float)(i) * uInterval),startv + ((float)(i) * vInterval));				
					//colour c = t.get(uoffset,voffset,true);
					pb.pixel((int)c.red,(int)c.green,(int)c.blue,tempstartx + i,yInt);
		
					uoffset += uInterval; voffset += vInterval;
				}
			
			}

		//System.out.println("poly G");
			yInt += 1;

			if(yInt>=(int)(temp[leftIndex].y))
			{
				topLeftIndex = leftIndex;
				leftIndex = dec(leftIndex);

				//System.out.println("change left " + topLeftIndex + " " + leftIndex);
				//System.out.println("top y " + temp[topLeftIndex].y + " left y " + temp[leftIndex].y);

				leftGradient = (temp[topLeftIndex].x - temp[leftIndex].x) / (temp[topLeftIndex].y - temp[leftIndex].y);
				
				leftUGradient = (temp[topLeftIndex].u - temp[leftIndex].u) / (temp[topLeftIndex].y - temp[leftIndex].y);
				leftVGradient = (temp[topLeftIndex].v - temp[leftIndex].v) / (temp[topLeftIndex].y - temp[leftIndex].y);
		//leftUGradient = ((temp[topLeftIndex].u*t.width) - (temp[leftIndex].u*t.width)) / (temp[topLeftIndex].y - temp[leftIndex].y);
		//leftVGradient = ((temp[topLeftIndex].v*t.height) - (temp[leftIndex].v*t.height)) / (temp[topLeftIndex].y - temp[leftIndex].y);

			}

			if(yInt>=(int)(temp[rightIndex].y))
			{
				topRightIndex = rightIndex;
				rightIndex = inc(rightIndex);

				//System.out.println("change right " + topRightIndex + " " + rightIndex);
				//System.out.println("top y " + temp[topRightIndex].y + " right y " + temp[rightIndex].y);
				
				rightGradient = (temp[topRightIndex].x - temp[rightIndex].x) / (temp[topRightIndex].y - temp[rightIndex].y);
				
				rightUGradient = (temp[topRightIndex].u - temp[rightIndex].u) / (temp[topRightIndex].y - temp[rightIndex].y);
				rightVGradient = (temp[topRightIndex].v - temp[rightIndex].v) / (temp[topRightIndex].y - temp[rightIndex].y);

		//rightUGradient = ((temp[topRightIndex].u*t.width) - (temp[rightIndex].u*t.width)) / (temp[topRightIndex].y - temp[rightIndex].y);
		//rightVGradient = ((temp[topRightIndex].v*t.height) - (temp[rightIndex].v*t.height)) / (temp[topRightIndex].y - temp[rightIndex].y);

			}


			if(yInt>=(int)(temp[bottomIndex].y)) finished = true;
			//if(yInt>=yIntTop+height) finished = true;

		}while(!finished);	

		System.out.println("poly FIN");

		//System.out.println("Bottom " + Math.round(temp[bottomIndex].y) + " " + temp[bottomIndex].y);	
		//System.out.println("BottomIndex " + bottomIndex);
		//System.out.println("height " + height + " finish " + (yIntTop + height));
		}catch(Exception e) { System.out.println(e.getMessage()); }
	}

	public int inc(int index)
	{
		int result = index + 1;
		if(result>=points.length) result = 0;

		return result;
	}

	public int dec(int index)
	{
		int result = index -1;
		if(result<0) result = points.length - 1;
		
		return result;
	}
	 * */
}