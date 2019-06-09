package com.gdx.halo.Utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.gdx.halo.DecalManager;
import com.gdx.halo.Map.Floor;
import com.gdx.halo.Map.Wall;

import java.io.BufferedReader;
import java.io.FileReader;

import static com.gdx.halo.Halo.WALL_FLAG;

public class MapReader {
	public FileHandle mapFile;
	
	public MapReader(FileHandle fileHandle) {
		this.mapFile = fileHandle;
	}
	
	public void createMap(DecalManager decalManager, btCollisionWorld collisionWorld) throws Exception
	{
		float x = 0;
		float z = 0;
		
		if (mapFile == null)
			throw new Exception("Map file path is null!");
		//File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(mapFile.file()));
		
		String st;
		while ((st = br.readLine()) != null)
		{
			System.out.println(st);
			for (int i = 0; i < st.length(); i++)
			{
				if (st.charAt(i) == 'x')
				{
					Floor floor = new Floor(5 ,5, new Vector3(x, -5, z),"walls/stone_wall_01.png");
					
					decalManager.addFloor(floor);


					Wall wall = new Wall(5 ,5, new Vector3(x, 0, z),"walls/stone_wall_02.png");
					
					decalManager.addWall(wall);
					collisionWorld.addCollisionObject(wall.getGameObject().body, WALL_FLAG);
				}
				else if (st.charAt(i) == '0')
				{
					Floor floor = new Floor(5 ,5, new Vector3(x, -5, z),"walls/stone_wall_01.png");
					
					decalManager.addFloor(floor);
					//collisionWorld.addCollisionObject(wall.getGameObject().body);
				}
				x += 5;
			}
			z += 5;
			x = 0;
		}
	}
}
