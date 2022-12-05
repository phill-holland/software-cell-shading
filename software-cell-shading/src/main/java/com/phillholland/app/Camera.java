package com.phillholland.app;

public class Camera
{
	public Point center;
	public Vector looking;	
	public float orientation;

	public Matrix c;

	public int width,height;

	public float fov,fardistance,focus;

	public Matrix translation,xrotate,yrotate,zrotate;
	
	public Vector planes[],tempPlanes[];

	public Camera(int w,int h)
	{
		c = new Matrix(); width = w; height = h;

		orientation = 0.0f;

		planes = new Vector[6]; tempPlanes = new Vector[6];

		fov = 1.57f; fardistance = 6000.0f;

		translation = new Matrix();

		xrotate = new Matrix();
		yrotate = new Matrix();		
		zrotate = new Matrix();

		center = new Point();
		looking = new Vector();

		frustum();
	}

	public void set(Point c, Vector l, float direction)
	{
		looking.copy(l);

		orientation = direction;

		center.copy(c);
	}

	public void calculateNew()
	{
		for(int x=0;x<3;x++) translation.add(x,x,1.0f);
		translation.row(center.x,center.y,center.z,1.0f,3);	
		
		Vector ta = new Vector(0.0f,0.0f,1.0f,0.0f);
		Vector tb = new Vector();

		tb.copy(looking);
		ta = ta.normalise();  tb = tb.normalise();

		Vector axis = tb.cross(ta);
		float angle = ta.dot(tb);

		float s = (float)Math.sqrt((1.0f + angle) * 2.0f);
		Quaternion q = new Quaternion(axis.x / s,axis.y / s,axis.z / s,s / 2.0f);
		Matrix tm = q.create();
		
		Point a = new Point();
		Point b = new Point();

		Vector position = new Vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++)
		{
			a.x = planes[i].x; a.y = planes[i].y; a.z = planes[i].z;
			Matrix m = a.get();
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

		yrotate.add(0,0,(float)Math.cos(t));
        yrotate.add(2,0,-(float)Math.sin(t));
		yrotate.add(0,2,(float)Math.sin(t));
		yrotate.add(2,2,(float)Math.cos(t));
		
		yrotate.add(1,1,1.0f);
		yrotate.add(3,3,1.0f);		
		
		t = 0.0f;
	
		xrotate.add(1,1,(float)Math.cos(t));
		xrotate.add(2,1,(float)Math.sin(t));
		xrotate.add(1,2,-(float)Math.sin(t));
		xrotate.add(2,2,(float)Math.cos(t));
		xrotate.add(0,0,1.0f);
		xrotate.add(3,3,1.0f);

		zrotate.add(0,0,(float)Math.cos(orientation));
		zrotate.add(1,0,(float)Math.sin(orientation));
		zrotate.add(0,1,-(float)Math.sin(orientation));
		zrotate.add(1,1,(float)Math.cos(orientation));
		zrotate.add(2,2,1.0f);
		zrotate.add(3,3,1.0f);

		c = zrotate.multiply(xrotate.multiply(yrotate));					

		Point a = new Point();
		Point b = new Point();

		Vector position = new Vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++)
		{
			a.x = planes[i].x; a.y = planes[i].y; a.z = planes[i].z;
			Matrix m = a.get();
			b.set(m.multiply(c));			

			tempPlanes[i].x = b.x; tempPlanes[i].y = b.y; tempPlanes[i].z = b.z;
			tempPlanes[i].w = tempPlanes[i].dot(position);
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

		planes[0] = new Vector(c,0.0f,s,0.0f); // left
		planes[1] = new Vector(-c,0.0f,s,0.0f); // right

		s = (float)Math.sin(fovv);
		c = (float)Math.cos(fovv);

		planes[2] = new Vector(0.0f,c,s,0.0f); // bottom
		planes[3] = new Vector(0.0f,-c,s,0.0f); // top

		planes[4] = new Vector(0.0f,0.0f,1.0f,0.0f);
		planes[5] = new Vector(0.0f,0.0f,fardistance,0.0f);

		Vector position = new Vector(center.x,center.y,center.z,0.0f);

		for(int i=0;i<6;i++) 
		{ 
			tempPlanes[i] = new Vector(planes[i]); 
			tempPlanes[i].w = tempPlanes[i].dot(position);
		}
	}

	public Vector getIntersection(Vector n1, Vector n2, Vector n3)
	{
		Matrix m = new Matrix(),mi = new Matrix();
	
		m.row(n1.x,n1.x,n1.z,0.0f,0);
		m.row(n2.x,n2.x,n2.z,0.0f,1);
		m.row(n3.x,n3.x,n3.z,0.0f,2);
		m.row(0.0f,0.0f,0.0f,0.0f,3);

		mi = m.inverse();
		Point temp = new Point(-n1.w,-n2.w,-n3.w);
		Matrix j = temp.get();
		Matrix e = mi.multiply(j);
		temp.set(e);
		
		return new Vector(temp.x,temp.y,temp.z,1.0f);
	}
}