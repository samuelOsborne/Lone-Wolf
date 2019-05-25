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
	private boolean shooting = false;
	private boolean reloading = false;
	private Texture pistolTexture;
	private boolean shootingSound = false;
	private boolean reloadingSound = false;
	
	/**
	 * Sounds
	 */
	private static String shootingSoundPath = "Sounds/Pistol/pistol_shot.wav";
	private static String reloadSoundPath = "Sounds/Pistol/pistol_reload.mp3";
	
	/**
	 * Animations
	 */
	private static String shootingAnimationPath = "Animations/Weapons/Pistol/pistol_animation.png";
	private static String reloadAnimationPath = "Animations/Weapons/Pistol/pistol_reload.png";
	
	public Pistol() {
		super.init();
		this.viewMatrix = new Matrix4();
		this.spriteBatch = new SpriteBatch();
		this.stateTime = 0f;
		this.pistolTexture = new Texture("Animations/Weapons/Pistol/pistol_base.png");
		this.setMagSize(12);
		this.setBulletsLeft(12);
		this.setFireAnimation(initaliseAnimation(shootingAnimationPath, this.getFireAnimation(), 3, 1, 0.15f));
		this.setReloadAnimation(this.initaliseAnimation(reloadAnimationPath, this.getReloadAnimation(), 5, 1, 0.25f));
		this.setShootingSound(Gdx.audio.newSound(Gdx.files.internal(shootingSoundPath)));
		this.setReloadSound(Gdx.audio.newSound(Gdx.files.internal(reloadSoundPath)));
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
					this.getShootingSound().play(0.10f);
					this.shootingSound = true;
				}
			}
		} else if (reloading && this.getBulletsLeft() < 12) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getReloadAnimation().isAnimationFinished(stateTime)) {
				reloading = false;
				reloadingSound = false;
				stateTime = 0;
				this.setBulletsLeft(12);
			} else {
				TextureRegion currentFrame = this.getReloadAnimation().getKeyFrame(stateTime, false);
				spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2, 0, 300f, 300f);
				if (!this.reloadingSound)
				{
					this.getReloadSound().play(0.10f);
					this.reloadingSound = true;
				}
			}
		} else {
			spriteBatch.draw(pistolTexture, Gdx.graphics.getWidth() / 2, 0, 300f, 300f);
			shooting = false;
			shootingSound = false;
			reloadingSound = false;
		}
		spriteBatch.end();
	}
	
	@Override
	public void fire() {
		if (!reloading)
			shooting = true;
	}
	
	@Override
	public void reload() {
		if (!shooting)
			reloading = true;
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
