package com.gdx.halo.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.gdx.halo.GameObject;
import com.gdx.halo.Halo;
import com.gdx.halo.ObjectInstance;
import com.gdx.halo.Utils.ColliderCreator;

public class MaIcon implements ObjectInstance {
	public Decal                frontDecal;
	private GameObject          gameObject;
	private Model               model;
	private btCollisionWorld    btCollisionWorld;
	private boolean             render;
	
	public MaIcon(Vector3 position, btCollisionWorld btCollisionWorld)
	{
		this.frontDecal = Decal.newDecal(new TextureRegion(new
				Texture(Gdx.files.internal("Objects/ar.png"))), true);
		this.frontDecal.setPosition(position);
		this.frontDecal.setScale(0.05f, 0.05f);
		this.btCollisionWorld = btCollisionWorld;
		this.addCollider();
	}
	
	public void addCollider() {
		if (frontDecal == null)
			return;
		/**
		 * Collider code
		 */
		model = ColliderCreator.createCollider(this.frontDecal, "ar");
		gameObject = new GameObject.Constructor(model, "ar", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))).construct();
		gameObject.body.setUserValue(Halo.AR_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags());
		Vector3 colliderPos = new Vector3(frontDecal.getPosition());
		gameObject.transform.set(colliderPos, frontDecal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
		btCollisionWorld.addCollisionObject(gameObject.body);
	}
	
	@Override
	public GameObject getGameobject() {
		return null;
	}
	
	@Override
	public void setTransform(Vector3 _position) {
	
	}
	
	@Override
	public Decal getDecal() {
		return this.frontDecal;
	}
	
	@Override
	public boolean getLookAt() {
		return false;
	}
	
	@Override
	public void render(DecalBatch decalBatch) {
		decalBatch.add(frontDecal);
	}
	
	@Override
	public void onDestroy() {
	
	}
	
	@Override
	public boolean getRender() {
		return true;
	}
}
