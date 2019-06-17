package com.gdx.halo.Utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.gdx.halo.DecalManager;
import com.gdx.halo.Enemies.Elite;
import com.gdx.halo.Enemies.Grunt;
import com.gdx.halo.Halo;
import com.gdx.halo.Items.HealthPack;
import com.gdx.halo.Items.MaIcon;
import com.gdx.halo.Items.PistolPickup;
import com.gdx.halo.Map.Floor;
import com.gdx.halo.Map.Wall;

import java.io.BufferedReader;
import java.io.FileReader;

import static com.gdx.halo.Halo.WALL_FLAG;

public class MapReader {
	public FileHandle           mapFile;
	private Halo                gameManager;
	private btCollisionWorld    collisionWorld;
	private DecalManager        decalManager;
	
	public MapReader(FileHandle fileHandle, Halo gameManager, btCollisionWorld collisionWorld, DecalManager decalManager) {
		this.mapFile = fileHandle;
		this.gameManager = gameManager;
		this.collisionWorld = collisionWorld;
		this.decalManager = decalManager;
	}
	
	public void loadEnemies(FileHandle fileHandle) throws Exception
	{
		float x = 0;
		float z = 0;
		
		if (fileHandle == null)
			throw new Exception("Map file path is null!");
		BufferedReader br = new BufferedReader(new FileReader(fileHandle.file()));
		
		String st;
		while ((st = br.readLine()) != null)
		{
			for (int i = 0; i < st.length(); i++)
			{
				if (st.charAt(i) == 'g')
				{
					Grunt grunt = new Grunt(new Vector3(x, 0, z), gameManager.getPlayer(), collisionWorld);
					gameManager.getEnemyManager().addEnemy(grunt);
				}
				else if (st.charAt(i) == 'e')
				{
					Elite elite = new Elite(new Vector3(x, 0, z), gameManager.getPlayer(), collisionWorld);
					gameManager.getEnemyManager().addEnemy(elite);
				}
				x += 5;
			}
			z += 5;
			x = 0;
		}
	}
	
	public void createMap() throws Exception
	{
		float x = 0;
		float z = 0;
		
		if (mapFile == null)
			throw new Exception("Map file path is null!");
		BufferedReader br = new BufferedReader(new FileReader(mapFile.file()));
		
		String st;
		while ((st = br.readLine()) != null)
		{
			for (int i = 0; i < st.length(); i++)
			{
				if (st.charAt(i) == 'x')
				{
					Wall wall = new Wall(5 ,5, new Vector3(x, 0, z),"walls/stone_wall_02.png");
					
					decalManager.addWall(wall);
					collisionWorld.addCollisionObject(wall.getGameObject().body, WALL_FLAG);
				}
				else if (st.charAt(i) == 'g')
				{
					Grunt grunt = new Grunt(new Vector3(x, 0, z), gameManager.getPlayer(), collisionWorld);
					gameManager.getEnemyManager().addEnemy(grunt);
				}
				else if (st.charAt(i) == 'e')
				{
					Elite elite = new Elite(new Vector3(x, 0, z), gameManager.getPlayer(), collisionWorld);
					gameManager.getEnemyManager().addEnemy(elite);
				}
				else if (st.charAt(i) == 'h')
				{
					HealthPack healthPack = new HealthPack(new Vector3(x, -2f, z), collisionWorld);
					gameManager.getDecalManager().addObject(healthPack);
				}
				else if (st.charAt(i) == 'm')
				{
					MaIcon ar = new MaIcon(new Vector3(x, -2f, z), collisionWorld);
					gameManager.getDecalManager().addObject(ar);
				}
				else if (st.charAt(i) == 'p')
				{
					PistolPickup pistol = new PistolPickup(new Vector3(x, -2f, z), collisionWorld);
					gameManager.getDecalManager().addObject(pistol);
				}
				Floor floor = new Floor(5 ,5, new Vector3(x, -5, z),"walls/stone_wall_01.png");
				decalManager.addFloor(floor);
				x += 5;
			}
			z += 5;
			x = 0;
		}
	}
}
