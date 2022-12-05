import java.awt.*;
import java.applet.*;
import java.awt.image.*;

/*
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

	public vector multiply(vector src)
	{
		vector t = new vector(src.x * m[0][0] + src.y * m[0][1] + src.z * m[0][2] + src.w * m[0][3], src.x * m[1][0] + src.y * m[1][1] + src.z * m[1][2] + src.w * m[1][3], src.x * m[2][0] + src.y * m[2][1] + src.z * m[2][2] + src.w * m[2][3], src.x * m[3][0] + src.y * m[3][1] + src.z * m[3][2] + src.w * m[3][3]);
		return t;
	}
}
*/

public class oldMatrix
{
	public float m[];
	public static final int elements = 16;
	
	public oldMatrix() 
	{ 
		m = new float[elements];

		reset();
	}

	public void reset()
	{
		for(int i=0;i<elements;i++) m[i] = 0.0f;
	}

	public void set(float a, float b, float c, float d, int row)
	{
		row(a, b, c, d, row);
	}

	public void row(float a,float b,float c,float d,int row)
	{		
		m[row] = a;
		m[4 + row] = b;
		m[8 + row] = c;
		m[12 + row] = d;
	}

	public void column(float a,float b,float c,float d,int col)
	{
		
		int ofs = (col * 4);

		m[ofs] = a;
		m[ofs + 1] = b;
		m[ofs + 2] = c;
		m[ofs + 3] = d;				
	}

	public void add(int x,int y,float v)
	{
		m[(x * 4) + y] = v;
	}

	public float get(int x,int y)
	{
		return m[(x * 4) + y];
	}

	public oldMatrix multiply(oldMatrix r)
	{
		oldMatrix result = new oldMatrix();

		for(int y=0;y<4;y++)
		{
			//int offset = y * 4;

			for(int x=0;x<4;x++)
			{	
				//int offset = y * 4;
				int offset = (x * 4) + y;				

				result.m[offset] += m[y] * r.m[(x * 4)];
				result.m[offset] += m[4 + y] * r.m[(x * 4) + 1];
				result.m[offset] += m[8 + y] * r.m[(x * 4) + 2];
				result.m[offset] += m[12 + y] * r.m[(x * 4) + 3];
			}	
		}
		

		return result;
	}

	public vector multiply(vector r)
	{
		float x = r.x * get(0,0) + r.y * get(0,1) + r.z * get(0,2) + r.w * get(0,3);
		float y = r.x * get(1,0) + r.y * get(1,1) + r.z * get(1,2) + r.w * get(1,3);
		float z = r.x * get(2,0) + r.y * get(2,1) + r.z * get(2,2) + r.w * get(2,3);
		float w = r.x * get(3,0) + r.y * get(3,1) + r.z * get(3,2) + r.w * get(3,3);

		return new vector(x,y,z,w);
	}

	public void copy(oldMatrix r)
	{
		for(int i=0;i<elements;i++)
		{
			m[i] = r.m[i];
		}
	}


	public oldMatrix inverse()
	{
		int j,i,row,column;
		float M = 0.0f,tempA[],tempB[];
		oldMatrix result= new oldMatrix(); 
		
		tempA = new float[4]; tempB = new float[4];
				
		for(j=0;j<4;j++) { result.m[(j * 4) + j] = 1; }	

		j=0;
		do
		{
			i=0;
			do 
			{ 
			} while((m[(i++ * 4) + j]==0.0f)&&(i<4));

	
			if(m[(--i * 4) + j]==0.0f) { return result; }
		
			if(i!=j) 	
			{ 
				for(column=0;column<4;column++)
				{						
					tempA[column] = m[(column * 4) + i];
					tempB[column] = result.m[(column * 4) + i];
	
					m[(column * 4) + i] = m[(column * 4) + j];
					result.m[(column * 4) + i] = m[(column * 4) + j];

					m[(column * 4) + j] = tempA[column];
					result.m[(column * 4) + j] = tempB[column];
				}	
			} 
	
			M = 1.0f / m[(j * 4) + j];
	
			for(column=0;column<4;column++)	
			{
				m[(column * 4) + j] = m[(column * 4) + j] * M;
				result.m[(column * 4) + j] = result.m[(column * 4) + j] * M;
			}
	
			for(row=0;row<4;row++)
			{
				if(row!=j)
				{	
					M = -m[(j * 4) + row]; 
		
					for(column=0;column<4;column++)
					{		
						int offset = (column * 4) + row;
						m[offset] = (m[(column * 4) + j] * M) + m[offset];
						result.m[offset] = (result.m[(column * 4) + j]* M) + result.m[offset];
					}
				}
				
			}			
		} while(++j<4);
	
		return result;
	}

	public void display()
	{
	        int x,y;

	        for(y=0;y<4;y++)
        	{
                	System.out.print("(");
	                for(x=0;x<4;x++)
        	        {
                	         System.out.print(m[(x * 4) + y]);
                        	 if (x<3) System.out.print(",");
	                }
        	        System.out.println(")");
	        }
	}
}
