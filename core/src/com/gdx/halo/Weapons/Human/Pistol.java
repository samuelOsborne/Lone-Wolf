package com.gdx.halo.Weapons.Human;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.gdx.halo.Halo;
import com.gdx.halo.Weapons.AWeapon;

import static com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT;
import static com.gdx.halo.Utils.RayTest.rayTest;

public class Pistol extends AWeapon {
	private float stateTime;
	private boolean shooting = false;
	private boolean reloading = false;
	private boolean shootingSound = false;
	private boolean reloadingSound = false;
	private Ray shootRay;
	
	/**
	 * Textures
	 */
	private Texture pistolTexture;
	private Texture pistolBulletsTexture;
	
	
	/**
	 * Sounds
	 */
	private static String shootingSoundPath = "Sounds/Weapons/Pistol/pistol_shot.wav";
	private static String reloadSoundPath = "Sounds/Weapons/Pistol/pistol_reload.mp3";
	
	/**
	 * Animations
	 */
	private static String shootingAnimationPath = "Animations/Weapons/Pistol/pistol_animation.png";
	private static String reloadAnimationPath = "Animations/Weapons/Pistol/pistol_reload.png";
	
	/**
	 * Sprites
	 */
	private static String reticulePath = "HUD/Weapons/Pistol/pistol_reticule.png";
	private static String pistolTexturePath = "Animations/Weapons/Pistol/pistol_base.png";
	private static String pistolBulletsPath = "HUD/Weapons/Pistol/single_pistol_bullet.png";
	
	/**
	 * Sizes and speeds
	 */
	private static float weaponWidth = 450f;
	private static float weaponHeight = 450f;
	private static float spacing = 200f;
	private static float bulletDistance = 80f;
	
	
	public Pistol() {
		super.init();
		this.weaponType = WeaponType.M40;
		this.shootRay = new Ray();
		this.stateTime = 0f;
		this.pistolTexture = new Texture(pistolTexturePath);
		this.pistolBulletsTexture = new Texture(pistolBulletsPath);
		this.setMagSize(12);
		this.setBulletsLeft(12);
		this.setFireAnimation(initaliseAnimation(shootingAnimationPath, this.getFireAnimation(), 3, 1, 0.15f));
		this.setReloadAnimation(this.initaliseAnimation(reloadAnimationPath, this.getReloadAnimation(), 5, 1, 0.25f));
		this.setShootingSound(Gdx.audio.newSound(Gdx.files.internal(shootingSoundPath)));
		this.setReloadSound(Gdx.audio.newSound(Gdx.files.internal(reloadSoundPath)));
		this.setReticule(new Texture(Gdx.files.internal(reticulePath)));
	}
	
	public void render(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		
		spriteBatch.setColor(hudColor);
		/**
		 * Reticule
		 */
		spriteBatch.draw(this.getReticule(), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()  / 2,
				30f, 30f);
		/**
		 * Ammo
		 */
		for (int i = 0; i < this.getBulletsLeft(); i++)
		{
			spriteBatch.draw(this.pistolBulletsTexture, i * this.pistolBulletsTexture.getWidth() + 10,
					Gdx.graphics.getHeight() - this.pistolBulletsTexture.getHeight() - 15);
		}
		spriteBatch.setColor(Color.WHITE);
		
		if (shooting && getBulletsLeft() > 0) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getFireAnimation().isAnimationFinished(stateTime)) {
				shooting = false;
				shootingSound = false;
				stateTime = 0;
				this.setBulletsLeft(this.getBulletsLeft() - 1);
			} else {
				TextureRegion currentFrame = this.getFireAnimation().getKeyFrame(stateTime, false);
				spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2f - spacing, 0, weaponWidth, weaponHeight);
				if (!this.shootingSound)
				{
					this.getShootingSound().play(0.10f);
					this.shootingSound = true;
				}
			}
		} else if (reloading && this.getBulletsLeft() < this.getMagSize()) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getReloadAnimation().isAnimationFinished(stateTime)) {
				reloading = false;
				reloadingSound = false;
				stateTime = 0;
				this.setBulletsLeft(this.getMagSize());
			} else {
				TextureRegion currentFrame = this.getReloadAnimation().getKeyFrame(stateTime, false);
				spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2f - spacing, 0, weaponWidth, weaponHeight);
				if (!this.reloadingSound)
				{
					this.getReloadSound().play(0.10f);
					this.reloadingSound = true;
				}
			}
		} else {
			spriteBatch.draw(pistolTexture, Gdx.graphics.getWidth() / 2f - spacing, 0, weaponWidth, weaponHeight);
			if (this.shooting && this.getBulletsLeft() == 0)
				this.flashReloadIcon(spriteBatch);
			shooting = false;
			shootingSound = false;
			reloadingSound = false;
		}
		spriteBatch.end();
	}
	
	@Override
	public int fire(btCollisionWorld collisionWorld, Camera camera) {
		btCollisionObject collidedObj;
		
		if (!reloading)
		{
			if (shooting)
				return (-1);
			shooting = true;
			if (this.getBulletsLeft() != 0)
			{
				shootRay.set(camera.position, camera.direction);
				if ((collidedObj = rayTest(collisionWorld,
						shootRay,
						bulletDistance, Halo.ENEMY_FLAG)) != null)
				{
					return (collidedObj.getUserValue());
				}
			}
		}
		return (-1);
	}
	
	@Override
	public void reload() {
		if (!shooting)
			reloading = true;
	}
	
	@Override
	public void update() {
	}
	
	@Override
	public void dispose() {
		//spriteBatch.dispose();
		this.getShootingSound().dispose();
	}
}
