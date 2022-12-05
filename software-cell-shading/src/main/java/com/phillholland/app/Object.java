package com.phillholland.app;

public class Object
{
	public Point points[] = null;
	public Point temp[] = null;

	public Vector normals[] = null;

	public Polygon polygons[] = null;
	
	public int[][] sharedByPolygon = null;
	public int[][] sharedByVertex = null;

	public Edge edges[] = null;

	public Colour lighting[] = null;

	public int totalEdges = 0;
	
	public Object(int total_points,int total_polygons)
	{
		points = new Point[total_points];
		temp = new Point[total_points];
		normals = new Vector[total_points];
		lighting = new Colour[total_points];

		for (int i = 0; i < total_points; i++)
		{
			points[i] = new Point();
			temp[i] = new Point();
			normals[i] = new Vector();
			lighting[i] = new Colour();
		}

		polygons = new Polygon[total_polygons];

		for (int i = 0; i < polygons.length; i++)
		{
			polygons[i] = new Polygon(points,normals,lighting);
		}

		sharedByPolygon = new int[total_polygons][total_polygons];
		for (int x = 0; x < total_polygons; x++)
		{
			for (int y = 0; y < total_polygons; y++)
			{
				sharedByPolygon[x][y] = -1;
			}
		}

		// ***

		sharedByVertex = new int[total_points][total_polygons];
		for (int x = 0; x < total_points; x++)
		{
			for (int y = 0; y < total_polygons; y++)
			{
				sharedByVertex[x][y] = -1;
			}
		}

	}

	public void compute()
	{
		for (int i = 0; i < polygons.length; i++)
		{
			int position = 0;
			for (int j = 0; j < polygons.length; j++)
			{
				if(i!=j)
				{
					boolean inserted = false;

					for (int idx_i = 0; idx_i < polygons[i].indices.length; idx_i++)
					{
						for (int idx_j = 0; idx_j < polygons[j].indices.length; idx_j++)
						{
							if (polygons[i].indices[idx_i] == polygons[j].indices[idx_j])
							{
								if (!inserted)
								{
									sharedByPolygon[i][position] = j;
									++position;
									inserted = true;
								}
							}
						}
					}
				}
			}
		}

		// ***

		for (int i = 0; i < points.length; i++)
		{
			int position = 0;

			for (int j = 0; j < polygons.length; j++)
			{
				boolean inserted = false;

				for (int idx = 0; idx < polygons[j].indices.length; idx++)
				{
					if (polygons[j].indices[idx] == i)
					{
						if (!inserted)
						{
							sharedByVertex[i][position] = j;
							++position;
							inserted = true;
						}
					}
				}
			}
		}

		int count = 0;

		for (int i = 0; i < polygons.length; i++)
		{
			count += polygons[i].indices.length;
		}

		edges = new Edge[count];
		totalEdges = 0;

		for (int i = 0; i < polygons.length; i++)
		{
			int a = polygons[i].indices[0];

			for (int idx = 0; idx < polygons[i].indices.length; idx++)
			{
				int b = polygons[i].indices[polygons[i].inc(idx)];
				boolean found = false;

				int j = 0;
				while ((j < totalEdges) && (!found)) 
				{
					if ((a == edges[j].a) && (b == edges[j].b))
					{
						edges[j].add(i);
						found = true;
					}
					else if ((b == edges[j].a) && (a == edges[j].b))
					{
						edges[j].add(i);
						found = true;
					}
					
					++j;
				};


				if (!found)
				{
					edges[totalEdges] = new Edge(a, b, polygons.length);
					edges[totalEdges].add(i);

					++totalEdges;
				}				

				a = b;
			}
		}
	}

	
	public void normals()
	{
		for (int j = 0; j < polygons.length; j++) 
		{ 
			polygons[j].normal();
		}

		for (int i = 0; i < temp.length; i++)
		{
			Vector normal = new Vector(0.0f, 0.0f, 0.0f);
			float total = 0.0f;
			for (int j = 0; j < polygons.length; j++)
			{
				if (sharedByVertex[i][j] != -1)
				{
					int index = sharedByVertex[i][j];

					normal.x += polygons[index]._normal.x;
					normal.y += polygons[index]._normal.y;
					normal.z += polygons[index]._normal.z;
					total += 1.0f;
				}
			}
			normal.x = normal.x / total;
			normal.y = normal.y / total;
			normal.z = normal.z / total;

			normals[i].copy(normal);
		}
	}

	public void compute(Light _light)
	{
		for (int i = 0; i < normals.length; i++)
		{
			float x = temp[i].x - _light.position.x;
			float y = temp[i].y - _light.position.y;
			float z = temp[i].z - _light.position.z;

			float distance = (float)Math.sqrt((double)(x * x + y * y + z * z));

			float intensity = (_light.radius - distance) / _light.radius;
			float v = 0.0f;

			if (intensity >= 0.0f)
			{
				float t = _light.direction.dot(normals[i]);
				t = (t + 3.0f) / 6.0f;
				v = t * intensity;
			}

			normals[i].w = v;

			lighting[i].red = _light._colour.red * v;
			lighting[i].green = _light._colour.green * v;
			lighting[i].blue = _light._colour.blue * v;			
		}
	}

	public void backfaces(Point cameraPosition)
	{
		for (int i = 0; i < polygons.length; i++)
		{
			polygons[i].isBackface(cameraPosition);
		}
	}

	public void copy()
	{
		for (int i = 0; i < points.length; i++)
		{
			temp[i].copy(points[i]);
		}

		for (int i = 0; i < polygons.length; i++)
		{
			polygons[i].points = temp;
		}
	}

	public void transform(Matrix m)
	{
		for (int i = 0; i < temp.length; i++)
		{
			Matrix mp = temp[i].get();
			Matrix result = mp.multiply(m);
			temp[i].set(result);
		}
	}

	public void transform(Camera c)
	{
		for (int i = 0; i < temp.length; i++)
		{
			temp[i].copy(temp[i].transform(c));
		}
	}

	public void paint(PixelBuffer16 pb, Line n)
	{
		for (int i = 0; i < polygons.length; i++)
		{
			if (!polygons[i].backface) polygons[i].paint(pb);
		}

		for (int i = 0; i < totalEdges; i++)
		{
			Edge e = edges[i];
			Point a = temp[e.a]; Point b = temp[e.b];

			int count = 0;
			for (int j = 0; j < e.length; j++)
			{
				if (polygons[e.polygons[j]].backface) count++;
			}

			if (count < e.length)
			{
				n.noiseIncX = e.seedX;
				n.noiseIncY = e.seedY;

				e.seedX += 0.001f;
				e.seedY += 0.001f;

				n.draw(pb, a, b);
			}
		}
	}
}
