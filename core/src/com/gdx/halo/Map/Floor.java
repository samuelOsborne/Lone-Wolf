package com.gdx.halo.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gdx.halo.GameObject;
import com.gdx.halo.ObjectInstance;

public class Floor implements ObjectInstance {
	private Decal           decal;
	private Vector3         position;
	
	public Floor(float width, float height, Vector3 _position, String _texturePath) {
		position = new Vector3();

		TextureRegion textureRegion = new TextureRegion(new Texture(Gdx.files.internal(_texturePath)));
		decal = Decal.newDecal(width, height, textureRegion);
		position = new Vector3(_position);
		position.y += height/2;
		decal.setPosition(position);
		decal.setRotationX(90f);
	}
	
	@Override
	public GameObject getGameobject() {
		return null;
	}
	
	@Override
	public void setTransform(Vector3 _position) {
		this.position = _position;
		this.decal.setPosition(position);
	}
	
	@Override
	public Decal getDecal() {
		return null;
	}
	
	@Override
	public boolean getLookAt() {
		return false;
	}
	
	@Override
	public void render(DecalBatch decalBatch) {
		decalBatch.add(decal);
	}
	
	@Override
	public void onDestroy() {
	
	}
	
	@Override
	public boolean getRender() {
		return true;
	}
}
