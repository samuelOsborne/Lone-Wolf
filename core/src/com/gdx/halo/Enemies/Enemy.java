package com.gdx.halo.Enemies;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.AnimatedDecal;

public abstract class Enemy implements Disposable {
	public enum State{
		IDLE,
		WALKING,
		FIRING,
		RELOADING,
		DEAD
	}
	
	/**
	 * Animations and textures
	 */
	protected Animation<TextureRegion>      walkAnimation;
	protected Animation<TextureRegion>      fireAnimation;
	protected Animation<TextureRegion>      DeathAnimation;
	protected Animation<TextureRegion>      reloadingAnimation;
	protected Animation<TextureRegion>      idleAnimation;
	protected AnimatedDecal                 walkingDecal;
	protected AnimatedDecal                 firingDecal;
	protected AnimatedDecal                 deathDecal;
	protected AnimatedDecal                 reloadingDecal;
	protected AnimatedDecal                 idleDecal;
	
	/**
	 * Enemy information
	 */
	protected Vector3                       position;
	protected float                         scaleX;
	protected float                         scaleY;
	protected float                         stateTime;
	protected State                         state;
	
	public Enemy()
	{
		this.position = new Vector3();
	}
	
	public abstract void render(DecalBatch decalBatch, Camera camera);
	
	public abstract void initFireAnimation();
	
	public abstract void initWalkAnimation();
	
	public abstract void initDeathAnimation();
	
	public abstract void initIdleAnimation();
	
	public abstract void initReloadAnimation();
	
	public Animation<TextureRegion> getDeathAnimation() {
		return DeathAnimation;
	}
	
	public void setDeathAnimation(Animation<TextureRegion> deathAnimation) {
		DeathAnimation = deathAnimation;
	}
	
	public Animation<TextureRegion> getFireAnimation() {
		return fireAnimation;
	}
	
	public void setFireAnimation(Animation<TextureRegion> fireAnimation) {
		this.fireAnimation = fireAnimation;
	}
	
	public Animation<TextureRegion> getReloadingAnimation() {
		return reloadingAnimation;
	}
	
	public void setReloadingAnimation(Animation<TextureRegion> reloadingAnimation) {
		this.reloadingAnimation = reloadingAnimation;
	}
	
	public Animation<TextureRegion> getIdleAnimation() {
		return idleAnimation;
	}
	
	public void setIdleAnimation(Animation<TextureRegion> idleAnimation) {
		this.idleAnimation = idleAnimation;
	}
	
	public AnimatedDecal getWalkingDecal() {
		return walkingDecal;
	}
	
	public void setWalkingDecal(AnimatedDecal walkingDecal) {
		this.walkingDecal = walkingDecal;
	}
	
	public AnimatedDecal getFireingDecal() {
		return firingDecal;
	}
	
	public void setFireingDecal(AnimatedDecal firingDecal) {
		this.firingDecal = firingDecal;
	}
	
	public Vector3 getPosition() {
		return position;
	}
	
	public void setPosition(Vector3 position) {
		this.position = position;
	}
	
	public Animation<TextureRegion> getWalkAnimation() {
		return walkAnimation;
	}
	
	public void setWalkAnimation(Animation<TextureRegion> walkAnimation) {
		this.walkAnimation = walkAnimation;
	}
	
	public AnimatedDecal getDeathDecal() {
		return deathDecal;
	}
	
	public void setDeathDecal(AnimatedDecal deathDecal) {
		this.deathDecal = deathDecal;
	}
	
	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
	}
}
