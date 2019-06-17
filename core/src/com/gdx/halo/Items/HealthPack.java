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

public class HealthPack implements ObjectInstance {
	public Decal                frontDecal;
	private GameObject          gameObject;
	private Model               model;
	private btCollisionWorld    btCollisionWorld;
	private boolean             render = true;
	private Sound healingSound;
	
	public HealthPack(Vector3 position, btCollisionWorld btCollisionWorld) {
		this.frontDecal = Decal.newDecal(new TextureRegion(new
				Texture(Gdx.files.internal("Objects/health_pack.png"))), true);
		this.frontDecal.setPosition(position);
		this.frontDecal.setScale(0.05f, 0.05f);
		this.healingSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Objects/health_pack.mp3"));
		this.btCollisionWorld = btCollisionWorld;
		this.addCollider();
	}
	
	@Override
	public GameObject getGameobject() {
		return (this.gameObject);
	}
	
	@Override
	public void setTransform(Vector3 _position) {
		this.frontDecal.setPosition(_position);
	}
	
	@Override
	public Decal getDecal() {
		return this.frontDecal;
	}
	
	@Override
	public boolean getLookAt() {
		return true;
	}
	
	public void render(DecalBatch decalBatch)
	{
		decalBatch.add(frontDecal);
	}
	
	@Override
	public void onDestroy() {
		this.removeHealthPack();
	}
	
	@Override
	public boolean getRender() {
		return render;
	}
	
	public void update()
	{
	
	}
	
	public void addCollider() {
		if (frontDecal == null)
			return;
		/**
		 * Collider code
		 */
		model = ColliderCreator.createCollider(this.frontDecal, "health_pack");
		gameObject = new GameObject.Constructor(model, "health_pack", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f))).construct();
		gameObject.body.setUserValue(Halo.HP_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags());
		Vector3 colliderPos = new Vector3(frontDecal.getPosition());
		gameObject.transform.set(colliderPos, frontDecal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
		btCollisionWorld.addCollisionObject(gameObject.body);
	}
	
	public void removeHealthPack()
	{
		this.healingSound.play();
		btCollisionWorld.removeCollisionObject(gameObject.body);
		this.render = false;
	}
}
