package com.gdx.halo.Enemies;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.AnimatedDecal;
import com.gdx.halo.GameObject;

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
	protected Animation<TextureRegion>      deathAnimation;
	protected Animation<TextureRegion>      reloadingAnimation;
	protected Animation<TextureRegion>      idleAnimation;
	protected AnimatedDecal                 walkingDecal;
	protected AnimatedDecal                 firingDecal;
	protected AnimatedDecal                 deathDecal;
	protected AnimatedDecal                 reloadingDecal;
	protected AnimatedDecal                 idleDecal;
	
	
	/**
	 * Collider
	 */
	protected ModelBuilder                  modelBuilder;
	protected Model                         wireFrameCubeModel;
	protected ModelInstance                 wireFrameModelInstance;
	
	/**
	 * Enemy information
	 */
	protected GameObject                    gameObject;
	protected float                         scaleX;
	protected float                         scaleY;
	protected float                         stateTime;
	protected State                         state;
	protected int                           health;
	
	public Enemy()
	{
		this.modelBuilder = new ModelBuilder();
	}
	
	protected AnimatedDecal currentDecal(){
		switch (state)
		{
			case IDLE:
				return (this.idleDecal);
			case WALKING:
				return (this.walkingDecal);
			case FIRING:
				return (this.firingDecal);
			case RELOADING:
				return (this.reloadingDecal);
			case DEAD:
				return (this.deathDecal);
		}
		return (null);
	}
	
	public abstract void render(DecalBatch decalBatch, Camera camera);
	
	public abstract void initFireAnimation();
	
	public abstract void initWalkAnimation();
	
	public abstract void initDeathAnimation();
	
	public abstract void initIdleAnimation();
	
	public abstract void initReloadAnimation();
	
	public abstract void initCollider();
	
	public abstract void updateCollider();
	
	public Animation<TextureRegion> getDeathAnimation() {
		return deathAnimation;
	}
	
	public void setDeathAnimation(Animation<TextureRegion> deathAnimation) {
		this.deathAnimation = deathAnimation;
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
	
	public GameObject getGameObject() {
		return gameObject;
	}
	
	public void setPosition(Vector3 position) {
		this.gameObject.transform.set(position, gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
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
	
	public int getHealth() {
		return (this.health);
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
	}
	
	public abstract void update();
	
	public void takeDamage(int dmg) {
		this.health -= dmg;
	}
}
