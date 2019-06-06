package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.GameObject;

public abstract class AProjectile implements Disposable {
	protected Texture       projectileTexture;
	protected Decal         projectileDecal;
	private float           velocity;
	private Vector3         direction;
	private float           stateTime = 0f;
	private Vector3         directionToPlayer;
	public boolean          remove = false;
	
	/**
	 * Collider
	 */
	protected GameObject    gameObject;
	protected Model         model;
	
	public AProjectile(Texture projectileTexture, float velocity, Vector3 startPos, Vector3 direction)
	{
		this.projectileTexture = projectileTexture;
		this.velocity = velocity;
		this.direction = direction;
		this.projectileDecal = Decal.newDecal(new TextureRegion(this.projectileTexture));
		this.projectileDecal.setPosition(startPos);
		directionToPlayer = this.direction.sub(this.projectileDecal.getPosition()).nor();
		this.initCollider();
	}
	
	public abstract void initCollider();
	
	public abstract void updateCollider();
	
	public void update() {
		this.stateTime += Gdx.graphics.getDeltaTime();
		if (this.stateTime >= 5)
			this.remove = true;
		this.projectileDecal.setPosition(this.projectileDecal.getPosition().x + (directionToPlayer.x * velocity),
				0,
				this.projectileDecal.getPosition().z + (directionToPlayer.z * velocity));
		this.updateCollider();
	}
	
	public void render(DecalBatch decalBatch, Camera camera) {
		decalBatch.add(projectileDecal);
	}
	
	public Texture getProjectileTexture() {
		return projectileTexture;
	}
	
	public void setProjectileTexture(Texture projectileTexture) {
		this.projectileTexture = projectileTexture;
	}
	
	public float getVelocity() {
		return velocity;
	}
	
	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}
	
	public Vector3 getDirection() {
		return direction;
	}
	
	public void setDirection(Vector3 direction) {
		this.direction = direction;
	}
	
	public Decal getProjectileDecal() {
		return projectileDecal;
	}
	
	public void setProjectileDecal(Decal projectileDecal) {
		this.projectileDecal = projectileDecal;
	}
	
	@Override
	public void dispose() {
		this.projectileTexture.dispose();
		this.gameObject.dispose();
		this.model.dispose();
	}
	
	public GameObject getGameObject() { return this.gameObject; }
	
	public void setGameObject(GameObject gameObject) { this.gameObject = gameObject; }
}
