package com.gdx.halo.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.gdx.halo.AnimatedDecal;
import com.gdx.halo.Utils.AnimationLoader;

public class Elite extends Enemy {
	private static final int FRAME_COLS = 2, FRAME_ROWS = 1;
	
	public Elite()
	{
		initInformation();
	}
	
	public Elite(Vector3 position)
	{
		this.position = position;
		this.initInformation();
	}
	
	private void initInformation()
	{
		this.state = State.WALKING;
		this.scaleX = 3f;
		this.scaleY = 5f;
		this.initDeathAnimation();
		this.initFireAnimation();
		this.initWalkAnimation();
	}
	
	@Override
	public void render(DecalBatch decalBatch, Camera camera) {
		switch (state)
		{
			case IDLE:
				decalBatch.add(this.idleDecal);
				break;
			case WALKING:
			{
				this.walkingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.walkingDecal);
			}
			break;
			case FIRING:
				decalBatch.add(this.firingDecal);
				break;
			case RELOADING:
				decalBatch.add(this.reloadingDecal);
				break;
			case DEAD:
				decalBatch.add(this.deathDecal);
		}
	}
	
	@Override
	public void initFireAnimation() {
	
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
		walkingDecal.setPosition(0, 0, 10f);
		stateTime = 0f;
	}
	
	@Override
	public void initDeathAnimation() {
	
	}
	
	@Override
	public void initIdleAnimation() {
	
	}
	
	@Override
	public void initReloadAnimation() {
	
	}
}
