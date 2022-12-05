
public class polygon
{
	public point points[] = null;
	public int indices[] = null;

	public map mappings[] = null;
	public int textureIndices[] = null;

	public vector normals[] = null;

	public texture _textures[] = null;

	//public boolean edge[] = null;

	public boolean backface = false;

	public vector _normal = new vector(0.0f,0.0f,0.0f);

	public colour lighting[] = null;

	public float tonalMap[][] = null;

	public polygon() { }

	
	public polygon(point p[], vector n[])//, int i[])
	{
		normals = n;
		reset(p, null, null, null);		
	}

	public polygon(point p[], vector n[], colour c[])
	{
		normals = n;
		lighting = c;
		reset(p, null, null, null);
	}

	/*
	public void reset(point[] p,int i[])
	{
		points = p;
		if(i!=null) indices = i.clone();
	}
    */

	public void reset(point[] p, int i[], map[] m, int ti[])
	{
		points = p;
		mappings = m;

		if (i != null) indices = i.clone();
		if (ti != null) textureIndices = ti.clone();
	}

	public vector normal()
	{
		point left = points[indices[dec(0)]];
		point right = points[indices[inc(0)]];
		point center = points[indices[0]];

		vector a = new vector(center.x, center.y, center.z, 0.0f);
		vector b = new vector(a.x - right.x, a.y - right.y, a.z - right.z, 0.0f);
		vector c = new vector(a.x - left.x, a.y - left.y, a.z - left.z, 0.0f);

		//b = b.normalise();
		//c = c.normalise();

		_normal = b.cross(c).normalise();
		
		return _normal;
	}

	public boolean isBackface(point cameraPosition)
	{
		point a = points[indices[0]];
		vector n = _normal;//normal();
		
		vector sight = new vector(a.x - cameraPosition.x, a.y - cameraPosition.y, a.z - cameraPosition.z).normalise();

		//float result = n.dot(new vector(points[indices[0]].x - los.x, points[indices[0]].y - los.y, points[indices[0]].z - los.z, 0.0f));
		float result = n.dot(sight);
		if (result > 0.0f) { backface = false; return false; }

		backface = true;

		return true;
	}

	public void copy(polygon src)
	{
		points = src.points;
		indices = src.indices.clone();
	}

	public int inc(int index)
	{
		int result = index + 1;
		if (result >= indices.length) result = 0;

		return result;
	}

	public int dec(int index)
	{
		int result = index - 1;
		if (result < 0) result = indices.length - 1;

		return result;
	}

	/*
	public void paint(pixelBuffer16 pb)
	{
		point a = points[indices[0]];

		for (int i = 0; i < indices.length; i++)
		{
			point b = points[indices[inc(i)]];

			pb.line((int)a.x, (int)a.y, (int)b.x, (int)b.y, 255, 0, 0);

			//System.out.println("line (" + a.x + "," + a.y + ")(" + b.x + "," + b.y + ")");
			a = b;
		}
	}

	public void paint(pixelBuffer16 pb, line n)
	{
		point a = points[indices[0]];

		for (int i = 0; i < indices.length; i++)
		{
			point b = points[indices[inc(i)]];
			
			n.noiseIncX = 0.0f; n.noiseIncY = 0.0f;
			//n.draw(pb, new point(a.x, a.y), new point(b.x, b.y));
			n.draw(pb, a, b);
						
			a = b;
		}
	}
	*/
	public void paint(pixelBuffer16 pb)
	{
		float lightOffset = 0.0f;

		if (_textures == null) return;

		try
		{
			int topIndex = 0;
			int bottomIndex = 0;

			int leftIndex = 0;
			int rightIndex = 0;

			int topLeftIndex = 0;
			int topRightIndex = 0;

			boolean finished;

			point top = null, bottom = null, current = null, left = null, right = null, topLeft = null, topRight = null;
			map mapTopLeft = null, mapTopRight = null, mapLeft = null, mapRight = null;

			for (int i = 0; i < indices.length; i++)
			{
				top = points[indices[topIndex]];
				bottom = points[indices[bottomIndex]];
				current = points[indices[i]];

				if ((int)(top.y) > (int)(current.y)) topIndex = i;
				if ((int)(bottom.y) <= (int)(current.y)) bottomIndex = i;
			}

			if (topIndex == bottomIndex) return;
			
			finished = false;
			leftIndex = topIndex;

			int count = 0;

			top = points[indices[topIndex]];
			bottom = points[indices[bottomIndex]];

			do
			{
				left = points[indices[leftIndex]];
				if ((int)(left.y) == (int)(top.y))
				{
					int t = dec(leftIndex);
					point tp = points[indices[t]];

					count++;
					if ((int)(tp.y) == (int)(top.y)) leftIndex = t;
					else finished = true;

					if ((count >= indices.length) && (finished == false)) return;

				}
				else finished = true;

			} while (!finished);


			finished = false;
			rightIndex = topIndex;
			count = 0;

			do
			{
				right = points[indices[rightIndex]];
				if ((int)(right.y) == (int)(top.y))
				{
					int t = inc(rightIndex);
					point tp = points[indices[t]];

					count++;
					if ((int)(tp.y) == (int)(top.y)) rightIndex = t;
					else finished = true;

					if ((count >= indices.length) && (finished == false)) return;

				}
				else finished = true;
			} while (!finished);

			topLeftIndex = leftIndex;
			topRightIndex = rightIndex;

			topLeft = points[indices[topLeftIndex]];
			topRight = points[indices[topRightIndex]];

			leftIndex = dec(topLeftIndex);
			rightIndex = inc(topRightIndex);

			left = points[indices[leftIndex]];
			right = points[indices[rightIndex]];

			//System.out.println("(tl,tr,l,r) (" + topLeftIndex + "," + topRightIndex + "," + leftIndex + "," + rightIndex + ")");

			mapTopLeft = mappings[textureIndices[topLeftIndex]];
			mapTopRight = mappings[textureIndices[topRightIndex]];
			mapLeft = mappings[textureIndices[leftIndex]];
			mapRight = mappings[textureIndices[rightIndex]];

			// ***
			colour topLeftLight = lighting[indices[topLeftIndex]];
			colour leftLight = lighting[indices[leftIndex]];

			colour topRightLight = lighting[indices[topRightIndex]];
			colour rightLight = lighting[indices[rightIndex]];
	
			float topLeftLight_W = normals[indices[topLeftIndex]].w;
			float leftLight_W = normals[indices[leftIndex]].w;

			float topRightLight_W = normals[indices[topRightIndex]].w;
			float rightLight_W = normals[indices[rightIndex]].w;

			// ***

			colour leftLightGradient = new colour(), rightLightGradient = new colour();

			leftLightGradient.red = (topLeftLight.red - leftLight.red) / (topLeft.y - left.y);
			leftLightGradient.green = (topLeftLight.green - leftLight.green) / (topLeft.y - left.y);
			leftLightGradient.blue = (topLeftLight.blue - leftLight.blue) / (topLeft.y - left.y);

			rightLightGradient.red = (topRightLight.red - rightLight.red) / (topRight.y - right.y);
			rightLightGradient.green = (topRightLight.green - rightLight.green) / (topRight.y - right.y);
			rightLightGradient.blue = (topRightLight.blue - rightLight.blue) / (topRight.y - right.y);

			float leftLightGradient_W = (topLeftLight_W - leftLight_W) / (topLeft.y - left.y);
			float rightLightGradient_W = (topRightLight_W - rightLight_W) / (topRight.y - right.y);

			//System.out.println("tl(" + mapTopLeft.u + "," + mapTopLeft.v + ") tr(" + mapTopRight.u + "," + mapTopRight.v + ")");
			//System.out.println("l(" + mapLeft.u + "," + mapLeft.v + ") r(" + mapRight.u + "," + mapRight.v + ")");
						
			float leftGradient = (topLeft.x - left.x) / (topLeft.y - left.y);
			float rightGradient = (topRight.x - right.x) / (topRight.y - right.y);

			float leftUGradient = (mapTopLeft.u - mapLeft.u) / (topLeft.y - left.y);
			float leftVGradient = (mapTopLeft.v - mapLeft.v) / (topLeft.y - left.y);

			float rightUGradient = (mapTopRight.u - mapRight.u) / (topRight.y - right.y);
			float rightVGradient = (mapTopRight.v - mapRight.v) / (topRight.y - right.y);
			
			int yInt = (int)top.y;
			int height = ((int)bottom.y - (int)top.y);// - 1;

			finished = false;
			do
			{
				float y = (float)yInt;
			

				int startx = (int)(topLeft.x) + (int)(((float)((int)(y) - (int)(topLeft.y)) * leftGradient));
				int endx = (int)(topRight.x) + (int)(((float)((int)(y) - (int)(topRight.y)) * rightGradient));

				float startu = (mapTopLeft.u + ((y - topLeft.y) * leftUGradient));// * t.width;
				float endu = (mapTopRight.u + ((y - topRight.y) * rightUGradient));// * t.width;

				float startv = (mapTopLeft.v + ((y - topLeft.y) * leftVGradient));//* t.height;
				float endv = (mapTopRight.v + ((y - topRight.y) * rightVGradient));// * t.height;

				float startLight_W = (topLeftLight_W + ((y - topLeft.y) * leftLightGradient_W));
				float endLight_W = (topRightLight_W + ((y - topRight.y) * rightLightGradient_W));

				// ***
				colour startLight = new colour(), endLight = new colour();

				startLight.red = (topLeftLight.red + ((y - topLeft.y) * leftLightGradient.red));
				startLight.green = (topLeftLight.green + ((y - topLeft.y) * leftLightGradient.green));
				startLight.blue = (topLeftLight.blue + ((y - topLeft.y) * leftLightGradient.blue));

				endLight.red = (topRightLight.red + ((y - topRight.y) * rightLightGradient.red));
				endLight.green = (topRightLight.green + ((y - topRight.y) * rightLightGradient.green));
				endLight.blue = (topRightLight.blue + ((y - topRight.y) * rightLightGradient.blue));
			
				// ***

				int tempx;
				float ftempx;

				if (startx > endx)
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

					ftempx = startLight_W;
					startLight_W = endLight_W;
					endLight_W = ftempx;

					// ***
					ftempx = startLight.red;
					startLight.red = endLight.red;
					endLight.red = ftempx;

					ftempx = startLight.green;
					startLight.green = endLight.green;
					endLight.green = ftempx;

					ftempx = startLight.blue;
					startLight.blue = endLight.blue;
					endLight.blue = ftempx;
					// ***
				}

			
				int startxint = startx;//Math.round(startx);
				int endxint = endx;//Math.round(endx);
				
				int lengthint = endxint - startxint;

				float length = endx - startx;

				float ulength = endu - startu;
				float uInterval = ulength / length;

				float vlength = endv - startv;
				float vInterval = vlength / length;

				float lightLength = endLight_W - startLight_W;
				float lightInterval = lightLength / length;

				colour lightLengthC = new colour(endLight.red - startLight.red, endLight.green - startLight.green, endLight.blue - startLight.blue);
				colour lightIntervalC = new colour(lightLengthC.red / length, lightLengthC.green / length, lightLengthC.blue / length);
				
				if (lengthint >= 1)
				{
					int tempstartx = startx;//Math.round(startx);
					float uoffset = startu;
					float voffset = startv;
					lightOffset = startLight_W;

					colour lightOffsetC = new colour(startLight.red,startLight.green,startLight.blue);

					for (int i = 0; i < lengthint; i++)
					{
						// *** Texture picker
						/*
						texture _current = _textures[0];
						float tInterval = 1.0f / (float)_textures.length;
						float _c = 0.0f;
						for (int w = 0; w < _textures.length; w++)
						{
							if ((lightOffset > _c) && (lightOffset <= _c + tInterval))
							{
								_current = _textures[w];
							}
							_c += tInterval;
						}
						 colour c = _current.get(uoffset, voffset);
						 */
						colour c = new colour(0.0f, 0.0f, 0.0f);
						int idx = (int)(lightOffset * 100.0f);
						if (idx < 0) idx = 0;
						if (idx >= 100) idx = 99;

						for (int w = 0; w < _textures.length; w++)
						{
							colour tc = _textures[w].get(uoffset, voffset);
							tc.red *= ((lightOffsetC.red - 1.0f) * -1.0f);
							tc.green *= ((lightOffsetC.green - 1.0f) * -1.0f);
							tc.blue *= ((lightOffsetC.blue - 1.0f) * -1.0f);

							float value = tonalMap[w][idx];

							c.red += tc.red * value;
							c.green += tc.green * value;
							c.blue += tc.blue * value;
						}

						/*
						c.red = (c.red + lightOffsetC.red) / 2.0f;
						c.green = (c.green + lightOffsetC.green) / 2.0f;
						c.blue = (c.blue + lightOffsetC.blue) / 2.0f;
						 */
						//c.red *= lightOffsetC.red;
						//c.green *= lightOffsetC.green;
						//c.blue *= lightOffsetC.blue;

						//System.out.println("r,g,b " + c.red + "," + c.green + "," + c.blue);
						// ***
						//colour c = _texture.get(startu + ((float)(i) * uInterval), startv + ((float)(i) * vInterval));
						//colour c = _current.get(uoffset, voffset);//_texture.get(uoffset, voffset);

						/*
						c.red = 255.0f;
						c.green = 255.0f;
						c.blue = 255.0f;
						*/
						//float _l = (float)(i) * lightInterval;
						//System.out.println(lightOffset);
						pb.pixel((int)(c.red), (int)(c.green), (int)(c.blue), tempstartx + i, yInt);
						//pb.pixel((int)(c.red * lightOffset), (int)(c.green * lightOffset), (int)(c.blue * lightOffset), tempstartx + i, yInt);


						uoffset += uInterval; voffset += vInterval;
						lightOffset += lightInterval;

						lightOffsetC.red += lightIntervalC.red;
						lightOffsetC.green += lightIntervalC.green;
						lightOffsetC.blue += lightIntervalC.blue;
					}

				}

				yInt += 1;

				if (yInt >= (int)(left.y))
				{
					topLeftIndex = leftIndex;
					leftIndex = dec(leftIndex);

					topLeft = points[indices[topLeftIndex]];
					left = points[indices[leftIndex]];

					mapLeft = mappings[textureIndices[leftIndex]];
					mapTopLeft = mappings[textureIndices[topLeftIndex]];
										
					leftGradient = (topLeft.x - left.x) / (topLeft.y - left.y);

					leftUGradient = (mapTopLeft.u - mapLeft.u) / (topLeft.y - left.y);
					leftVGradient = (mapTopLeft.v - mapLeft.v) / (topLeft.y - left.y);

					// ****

					topLeftLight_W = normals[indices[topLeftIndex]].w;
					leftLight_W = normals[indices[leftIndex]].w;

					leftLightGradient_W = (topLeftLight_W - leftLight_W) / (topLeft.y - left.y);

					topLeftLight = lighting[indices[topLeftIndex]];
					leftLight = lighting[indices[leftIndex]];

					leftLightGradient.red = (topLeftLight.red - leftLight.red) / (topLeft.y - left.y);
					leftLightGradient.green = (topLeftLight.green - leftLight.green) / (topLeft.y - left.y);
					leftLightGradient.blue = (topLeftLight.blue - leftLight.blue) / (topLeft.y - left.y);

					// ***
				}

				if (yInt >= (int)(right.y))
				{
					topRightIndex = rightIndex;
					rightIndex = inc(rightIndex);

					topRight = points[indices[topRightIndex]];
					right = points[indices[rightIndex]];
					
					mapRight = mappings[textureIndices[rightIndex]];
					mapTopRight = mappings[textureIndices[topRightIndex]];
			
					rightGradient = (topRight.x - right.x) / (topRight.y - right.y);

					rightUGradient = (mapTopRight.u - mapRight.u) / (topRight.y - right.y);
					rightVGradient = (mapTopRight.v - mapRight.v) / (topRight.y - right.y);			

					// ***

					topRightLight_W = normals[indices[topRightIndex]].w;
					rightLight_W = normals[indices[rightIndex]].w;

					rightLightGradient_W = (topRightLight_W - rightLight_W) / (topRight.y - right.y);

					topRightLight = lighting[indices[topRightIndex]];
					rightLight = lighting[indices[rightIndex]];

					rightLightGradient.red = (topRightLight.red - rightLight.red) / (topRight.y - right.y);
					rightLightGradient.green = (topRightLight.green - rightLight.green) / (topRight.y - right.y);
					rightLightGradient.blue = (topRightLight.blue - rightLight.blue) / (topRight.y - right.y);


					// ***
				}


				if (yInt >= (int)(bottom.y)) finished = true;

			} while (!finished);
		}
		catch (Exception e) 
		{ 
			System.out.println("Exception " + e.getMessage());
			System.out.println("lightOffset " + lightOffset);
		}
	}

}

