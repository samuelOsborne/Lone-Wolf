package com.gdx.halo.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.gdx.halo.AnimatedDecal;
import com.gdx.halo.GameObject;
import com.gdx.halo.Halo;
import com.gdx.halo.Utils.AnimationLoader;
import com.gdx.halo.Utils.ColliderCreator;

public class Elite extends Enemy {
	private static final int FRAME_COLS = 2, FRAME_ROWS = 1;
	
	public Elite()
	{
		initInformation();
	}
	
	public Elite(Vector3 position)
	{
		this.initInformation();
		this.setPosition(position);
	}
	
	private void initInformation()
	{
		this.state = State.FIRING;
		this.scaleX = 3f;
		this.scaleY = 5f;
		this.health = 100;
		this.initDeathAnimation();
		this.initFireAnimation();
		this.initWalkAnimation();
		this.initCollider();
	}
	
	@Override
	public void update(){
		System.out.println("health : " + this.health);
		if (this.health <= 0)
			this.state = State.DEAD;
		this.updateCollider();
	}
	
	@Override
	public void render(DecalBatch decalBatch, Camera camera) {
		switch (state)
		{
			case IDLE:
				this.idleDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.idleDecal);
				break;
			case WALKING:
			{
				this.walkingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.walkingDecal);
			}
			break;
			case FIRING:
				this.firingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.firingDecal);
				break;
			case RELOADING:
				this.reloadingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.reloadingDecal);
				break;
			case DEAD:
				this.deathDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.deathDecal);
		}
	}
	
	@Override
	public void initFireAnimation() {
		Texture shootSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/shooting.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(shootSheet, 2, 1);
		fireAnimation = new Animation<TextureRegion>(0.25f, walkFrames);
		fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
		firingDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, fireAnimation, true);
		firingDecal.setKeepSize(true);
		firingDecal.setScaleX(scaleX);
		firingDecal.setScaleY(scaleY);
		firingDecal.setPlaying(true);
		firingDecal.setPosition((new Vector3(0, 0, 10f)));
		stateTime = 0f;
	}
	
	@Override
	public void initWalkAnimation() {
		Texture walkSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/walking.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(walkSheet, FRAME_COLS, FRAME_ROWS);
		walkAnimation = new Animation<TextureRegion>(0.5f, walkFrames);
		walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		walkingDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, walkAnimation, true);
		walkingDecal.setKeepSize(true);
		walkingDecal.setScaleX(scaleX);
		walkingDecal.setScaleY(scaleY);
		walkingDecal.setPlaying(true);
		walkingDecal.setPosition(new Vector3(0, 0, 10f));
		stateTime = 0f;
	}
	
	@Override
	public void initDeathAnimation() {
		Texture deathSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/death.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(deathSheet, 3, 1);
		deathAnimation = new Animation<TextureRegion>(0.25f, walkFrames);
		deathAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		deathDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, deathAnimation, true);
		deathDecal.setKeepSize(true);
		deathDecal.setScaleX(scaleX);
		deathDecal.setScaleY(scaleY);
		deathDecal.setPlaying(true);
		deathDecal.setPosition((new Vector3(0, 0, 10f)));
		stateTime = 0f;
	}
	
	@Override
	public void initIdleAnimation() {
	
	}
	
	@Override
	public void initReloadAnimation() {
	
	}
	
	@Override
	public void initCollider() {
		Model model = ColliderCreator.createCollider(this.firingDecal, "elite");
		
		gameObject = new GameObject.Constructor(model, "elite", new btBoxShape(new Vector3(1.5f, 2.5f, 0.5f))).construct();
//		gameObject.body.setUserValue(0);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		gameObject.transform.set(this.firingDecal.getPosition(), this.gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
		gameObject.body.setCollisionFlags(Halo.ENEMY_FLAG);
	}
	
	@Override
	public void updateCollider() {
		this.gameObject.transform.set(this.currentDecal().getPosition(), this.currentDecal().getRotation());
		this.gameObject.body.setWorldTransform(this.gameObject.transform);
	}
}
