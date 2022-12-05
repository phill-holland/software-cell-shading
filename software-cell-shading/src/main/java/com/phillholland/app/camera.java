import java.awt.*;
import java.applet.*;
import java.awt.image.*;

public class camera
{
	public point center;
	public vector looking;	
	public float orientation;

	public matrix c;

	public int width,height;

	public float fov,fardistance,focus;

	public matrix translation,xrotate,yrotate,zrotate;
	
	public vector planes[],tempPlanes[];

	public camera(int w,int h)
	{
		c = new matrix(); width = w; height = h;

		orientation = 0.0f;

		planes = new vector[6]; tempPlanes = new vector[6];

		fov = 1.57f; fardistance = 6000.0f;

		translation = new matrix();

		xrotate = new matrix();
		yrotate = new matrix();		
		zrotate = new matrix();

		center = new point();
		looking = new vector();

		frustum();
	}

	public void set(point c,vector l,float direction)
	{
		looking.copy(l);
		//looking.x += 0.001f;
		//looking.y += 0.001f;

		//System.out.println("MOO" + direction);
		orientation = direction;

		center.copy(c);
	}

	public void calculateNew()
	{
		for(int x=0;x<3;x++) translation.add(x,x,1.0f);
		translation.row(center.x,center.y,center.z,1.0f,3);	

		//point rest = new point(0.0f,0.0f,1.0f);
		
		vector ta = new vector(0.0f,0.0f,1.0f,0.0f);

		//vector ta = new vector(center.minus(rest));
		//vector tb = new vector(center.x - looking.x,center.y - looking.y,center.z - looking.z,0.0f);
		vector tb = new vector();
		tb.copy(looking);
		ta = ta.normalise();  tb = tb.normalise();

		vector axis = tb.cross(ta);
		float angle = ta.dot(tb);

		//System.out.println("ANGLE " + angle);
		// ****
		float s = (float)Math.sqrt((1.0f + angle) * 2.0f);
		quaternion q = new quaternion(axis.x / s,axis.y / s,axis.z / s,s / 2.0f);
		matrix tm = q.create();
		// ****

		//tm.display();

		//c.copy(tm);

		//c.copy(translation);
		//c.display();

		point a = new point();
		point b = new point();

		vector position = new vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++)
		{
			a.x = planes[i].x; a.y = planes[i].y; a.z = planes[i].z;
			matrix m = a.get();
			b.set(m.multiply(c));			

			tempPlanes[i].x = b.x; tempPlanes[i].y = b.y; tempPlanes[i].z = b.z;
			tempPlanes[i].w = tempPlanes[i].dot(position);
		}

		c = tm.multiply(translation);

	}

	public void calculate()
	{
		float t = 0.0f;

		for(int x=0;x<3;x++) translation.add(x,x,1.0f);

		translation.row(-center.x,-center.y,-center.z,1.0f,3);	

		//if(Math.round((double)looking.x)!=0)
		//	t = looking.x / (float)Math.sqrt((looking.x * looking.x) + (looking.z * looking.z));
		//else t = 0.0f;
		//t = 1.57f;

		//System.out.println((looking.x * looking.x) + (looking.z * looking.z));
		//System.out.println("CAMERA X ANGLE " + t);

		yrotate.add(0,0,(float)Math.cos(t));
	        yrotate.add(2,0,-(float)Math.sin(t));
		yrotate.add(0,2,(float)Math.sin(t));
		yrotate.add(2,2,(float)Math.cos(t));
		
        	yrotate.add(1,1,1.0f);
	        yrotate.add(3,3,1.0f);		
		
		//if(Math.round((double)looking.y)!=0)
		//	t = looking.y / (float)Math.sqrt((looking.y * looking.y) + (looking.z * looking.z));	
		//else t = 0.0f;
		t = 0.0f;

		//System.out.println("CAMERA Y ANGLE " + t);
		xrotate.add(1,1,(float)Math.cos(t));
	        xrotate.add(2,1,(float)Math.sin(t));
        	xrotate.add(1,2,-(float)Math.sin(t));
	        xrotate.add(2,2,(float)Math.cos(t));
        	xrotate.add(0,0,1.0f);
	        xrotate.add(3,3,1.0f);

		//System.out.println("UP " + orientation);
		//orientation = 3.0f;
	        zrotate.add(0,0,(float)Math.cos(orientation));
	        zrotate.add(1,0,(float)Math.sin(orientation));
	        zrotate.add(0,1,-(float)Math.sin(orientation));
        	zrotate.add(1,1,(float)Math.cos(orientation));
        	zrotate.add(2,2,1.0f);
	        zrotate.add(3,3,1.0f);

		c = zrotate.multiply(xrotate.multiply(yrotate));					

		

		//c.copy(translation);

		point a = new point();
		point b = new point();

		vector position = new vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++)
		{
			a.x = planes[i].x; a.y = planes[i].y; a.z = planes[i].z;
			matrix m = a.get();
			b.set(m.multiply(c));			

			tempPlanes[i].x = b.x; tempPlanes[i].y = b.y; tempPlanes[i].z = b.z;

			//System.out.println("distance " + i + " " + tempPlanes[i].w);

			tempPlanes[i].w = tempPlanes[i].dot(position);

			//tempPlanes[i].display();
			//System.out.println("distance " + i + " " + tempPlanes[i].w);
			//System.out.println(" ");
		}

		c.copy(c.multiply(translation));
		
	}		

	public void frustum()
	{
		float fovh,fovv;
		float s,c;

		float fwidth = (float)width;
		float fheight = (float)height;

		System.out.println("fov " + fov + " fwidth " + fwidth + " fheight " + fheight);
		fovh = fov * 0.5f;
		focus = (fwidth / 2.0f) / (float)Math.tan(fovh);
		fovv = (float)Math.atan((double)(((fwidth / 2.0f) * (fwidth / fheight)) / focus));

		System.out.println("focus " + focus + " fov " + fov);
		fovh += 0.005f;
		fovv += 0.005f;

		System.out.println("FOV H " + fovh);
		System.out.println("FOV V " + fovv);

		s = (float)Math.sin(fovh);
		c = (float)Math.cos(fovh);

		planes[0] = new vector(c,0.0f,s,0.0f); // left
		planes[1] = new vector(-c,0.0f,s,0.0f); // right

		s = (float)Math.sin(fovv);
		c = (float)Math.cos(fovv);

		planes[2] = new vector(0.0f,c,s,0.0f); // bottom
		planes[3] = new vector(0.0f,-c,s,0.0f); // top

		planes[4] = new vector(0.0f,0.0f,1.0f,0.0f);
		planes[5] = new vector(0.0f,0.0f,fardistance,0.0f);

		vector position = new vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++) 
		{ 
			tempPlanes[i] = new vector(planes[i]); 
			tempPlanes[i].w = tempPlanes[i].dot(position);
		}
	}
	/*
	public polygon[] intersections()
	{
		polygon polygons[] = new polygon[2];

		point temp[] = new point[4];

		vector t = getIntersection(tempPlanes[0],tempPlanes[3],tempPlanes[5]);
		temp[0] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[0],tempPlanes[2],tempPlanes[5]);
		temp[1] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[1],tempPlanes[3],tempPlanes[5]);
		temp[2] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[1],tempPlanes[2],tempPlanes[5]);
		temp[3] = new point(t.x,t.y,t.z);

		polygons[0] = new polygon(temp);				


		t = getIntersection(tempPlanes[0],tempPlanes[3],tempPlanes[4]);
		temp[0] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[0],tempPlanes[2],tempPlanes[4]);
		temp[1] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[1],tempPlanes[3],tempPlanes[4]);
		temp[2] = new point(t.x,t.y,t.z);

		t = getIntersection(tempPlanes[1],tempPlanes[2],tempPlanes[4]);
		temp[3] = new point(t.x,t.y,t.z);

		polygons[1] = new polygon(temp);

		return polygons;
	}
	*/
	public vector getIntersection(vector n1,vector n2,vector n3)
	{
		matrix m = new matrix(),mi = new matrix();
	
		m.row(n1.x,n1.x,n1.z,0.0f,0);
		m.row(n2.x,n2.x,n2.z,0.0f,1);
		m.row(n3.x,n3.x,n3.z,0.0f,2);
		m.row(0.0f,0.0f,0.0f,0.0f,3);

		mi = m.inverse();
		point temp = new point(-n1.w,-n2.w,-n3.w);
		matrix j = temp.get();
		matrix e = mi.multiply(j);
		temp.set(e);
		
		return new vector(temp.x,temp.y,temp.z,1.0f);

		//return (mi * vector(-n1.w,-n2.w,-n3.w,1.0f));	
	}
}