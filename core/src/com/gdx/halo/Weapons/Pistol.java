package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class Pistol extends AWeapon {
	private float stateTime;
	private SpriteBatch spriteBatch;
	private Matrix4 viewMatrix;
	
	public Pistol() {
		super.init();
		this.viewMatrix = new Matrix4();
		this.spriteBatch = new SpriteBatch();
		this.stateTime = 0f;
		this.setAnimationSheet("Animations/Weapons/Pistol/pistol_animation.png");
		this.setMagSize(12);
		this.setAmmoAmount(12);
		this.setFrameCols(3);
		this.setFrameRows(1);
		this.setAnimationSpeed(0.15f);
		this.initaliseAnimation();
	}
	
	public void render() {
		this.stateTime += Gdx.graphics.getDeltaTime();
		TextureRegion currentFrame = this.getFireAnimation().getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2, 0, 300f, 300f); // Draw current frame at (50, 50)
		spriteBatch.end();
	}
	
	
	@Override
	public void fire() {
	
	}
	
	@Override
	public void reload() {
	
	}
	
	@Override
	public void update() {
		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(viewMatrix);
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
}
