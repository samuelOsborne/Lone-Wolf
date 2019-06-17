package com.gdx.halo;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;

public interface ObjectInstance {
	
	public GameObject   getGameobject();
	
	public void     setTransform(Vector3 _position);
	
	public Decal    getDecal();
	
	public boolean  getLookAt();
	
	public void render(DecalBatch decalBatch);
	
	public void onDestroy();
	
	public boolean getRender();
}
