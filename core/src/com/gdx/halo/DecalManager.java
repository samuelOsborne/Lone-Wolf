package com.gdx.halo;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Map.Floor;
import com.gdx.halo.Map.Wall;

public class DecalManager implements Disposable {
	private Camera camera;
	
	/**
	 * Decals
	 */
	private Array<ObjectInstance>           objects = new Array<ObjectInstance>();
	private DecalBatch                      decalBatch;
	private Array<Wall>                     walls;
	private Array<Floor>                    floors = new Array<Floor>();
	
	public DecalManager(Camera _camera) {
		walls = new Array<Wall>();
		camera = _camera;
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
	}
	
	public void removeObject(int userValue)
	{
		for (ObjectInstance objectInstance : objects)
		{
			if (objectInstance.getGameobject() != null &&
					objectInstance.getGameobject().body != null &&
					objectInstance.getGameobject().body.getUserValue() == userValue)
			{
				objectInstance.onDestroy();
				objects.removeValue(objectInstance, true);
			}
		}
	}
	
	public void addFloor(Floor floor)
	{
		floors.add(floor);
	}
	
	public void addWall(Wall wall)
	{
		walls.add(wall);
	}
	
	public void addObject(ObjectInstance obj)
	{
		objects.add(obj);
	}
	
	public void renderDecals()
	{
		for ( int i = 0; i < objects.size; i++)
		{
			if (objects.get(i).getRender())
			{
				Decal decal = objects.get(i).getDecal();
				if (objects.get(i).getLookAt())
					decal.lookAt(camera.position, camera.up);
				decalBatch.add(decal);
			}
		}
		
		for (Wall wall : walls)
		{
			wall.render(decalBatch);
		}
		
		for (Floor floor : floors)
		{
			floor.render(decalBatch);
		}
		
		decalBatch.flush();
	}
	
	@Override
	public void dispose() {
		decalBatch.dispose();
	}
}
