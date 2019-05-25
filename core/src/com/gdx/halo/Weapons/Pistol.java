package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

public class Pistol extends AWeapon {
	private float stateTime;
	private SpriteBatch spriteBatch;
	private Matrix4 viewMatrix;
	public boolean shooting = false;
	private Texture pistolTexture;
	private boolean shootingSound = false;
	
	/**
	 * Sounds
	 */
	private static String shootingSoundPath = "Sounds/Pistol/pistol_shot.wav";
	
	public Pistol() {
		super.init();
		this.viewMatrix = new Matrix4();
		this.spriteBatch = new SpriteBatch();
		this.stateTime = 0f;
		this.setAnimationSheet("Animations/Weapons/Pistol/pistol_animation.png");
		this.pistolTexture = new Texture("Animations/Weapons/Pistol/pistol_base.png");
		this.setMagSize(12);
		this.setBulletsLeft(12);
		this.setFrameCols(3);
		this.setFrameRows(1);
		this.setAnimationSpeed(0.15f);
		this.initaliseAnimation();
		this.setShootingSound(Gdx.audio.newSound(Gdx.files.internal(shootingSoundPath)));
	}
	
	public void render() {
		spriteBatch.begin();
		if (shooting && getBulletsLeft() > 0) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getFireAnimation().isAnimationFinished(stateTime)) {
				shooting = false;
				shootingSound = false;
				stateTime = 0;
				this.setBulletsLeft(this.getBulletsLeft() - 1);
			} else {
				TextureRegion currentFrame = this.getFireAnimation().getKeyFrame(stateTime, false);
				spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2, 0, 300f, 300f);
				if (!this.shootingSound)
				{
					this.getShootingSound().play(0.05f);
					this.shootingSound = true;
				}
			}
		} else {
			spriteBatch.draw(pistolTexture, Gdx.graphics.getWidth() / 2, 0, 300f, 300f);
			shooting = false;
			shootingSound = false;
		}
		spriteBatch.end();
	}
	
	@Override
	public void fire() {
		shooting = true;
	}
	
	@Override
	public void reload() {
		shooting = false;
		this.setBulletsLeft(12);
	}
	
	@Override
	public void update() {
		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(viewMatrix);
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
		this.getShootingSound().dispose();
	}
}
