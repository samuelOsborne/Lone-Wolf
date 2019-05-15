package com.gdx.halo;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;

public interface ObjectInstance {
	
	public          ModelInstance getInstance();

	public          void setModelInstance(ModelInstance _modelInstance);
	
	public void     setTransform(Vector3 _position);
	
	public void     setRotationX(float angle);
	
	public void     setRotationY(float angle);
	
	public void     setRotationZ(float angle);
	
	public Decal    getDecal();
	
	public boolean  getLookAt();
}
