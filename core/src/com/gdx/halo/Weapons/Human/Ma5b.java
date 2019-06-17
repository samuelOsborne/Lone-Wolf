package com.gdx.halo.Weapons.Human;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;
import com.gdx.halo.Halo;
import com.gdx.halo.Weapons.AWeapon;

import static com.gdx.halo.Utils.RayTest.rayTest;

public class Ma5b extends AWeapon {
	private float stateTime = 0;
	private boolean shooting = false;
	private boolean reloading = false;
	private boolean shootingSound = false;
	private boolean reloadingSound = false;
	private Ray shootRay;
	private Array<Sound> shootingSounds;
	
	/**
	 * Textures
	 */
	private Texture ma5bTexture;
	private Texture ma5bBulletsTexture;
	
	
	/**
	 * Sounds
	 */
	private static String firstShootingSoundPath = "Sounds/Weapons/MA5B/shot_1.wav";
	private static String secondShootingSoundPath = "Sounds/Weapons/MA5B/shot_2.wav";
	private static String reloadSoundPath = "Sounds/Weapons/MA5B/MA5B_reload.mp3";
	
	/**
	 * Animations
	 */
	private static String shootingAnimationPath = "Animations/Weapons/MA5B/MA5B_animation.png";
	private static String reloadAnimationPath = "Animations/Weapons/MA5B/MA5B_reload.png";
	
	/**
	 * Sprites
	 */
	private static String reticulePath = "HUD/Weapons/MA5B/MA5B_reticule.png";
	private static String ma5bTexturePath = "Animations/Weapons/MA5B/MA5B_base.png";
	private static String ma5bBulletsPath = "HUD/Weapons/MA5B/MA5B_single_bullet.png";
	
	/**
	 * Sizes and speeds
	 */
	private static float weaponWidth = 300f;
	private static float weaponHeight = 250f;
	private static float spacing = -10f;
	private static float bulletDistance = 80f;
	
	public Ma5b()
	{
		super.init();
		this.weaponType = WeaponType.MA5B;
		this.shootRay = new Ray();
		this.stateTime = 0f;
		this.ma5bTexture = new Texture(ma5bTexturePath);
		this.ma5bBulletsTexture = new Texture(ma5bBulletsPath);
		this.setMagSize(60);
		this.setBulletsLeft(60);
		this.setFireAnimation(initaliseAnimation(shootingAnimationPath, this.getFireAnimation(), 2, 1, 0.05f));
		this.setReloadAnimation(this.initaliseAnimation(reloadAnimationPath, this.getReloadAnimation(), 2, 2, 0.40f));
		
		this.shootingSounds = new Array<Sound>();
		this.shootingSounds.add(Gdx.audio.newSound(Gdx.files.internal(firstShootingSoundPath)));
		this.shootingSounds.add(Gdx.audio.newSound(Gdx.files.internal(secondShootingSoundPath)));
		this.setReloadSound(Gdx.audio.newSound(Gdx.files.internal(reloadSoundPath)));
		this.setReticule(new Texture(Gdx.files.internal(reticulePath)));
	}
	
	@Override
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
		int y = 15;
		int a = 0;
		int offset = 10;
		for (int i = 0; i < this.getBulletsLeft(); i++)
		{
			if (i % 20 == 0)
			{
				y += 15;
				offset -= 3;
			}
			if (a == 20)
				a = 0;
			spriteBatch.draw(this.ma5bBulletsTexture, a * this.ma5bBulletsTexture.getWidth() + offset,
					Gdx.graphics.getHeight() - this.ma5bBulletsTexture.getHeight() - y);
			a++;
		}
		spriteBatch.setColor(Color.WHITE);
		
		if (shooting && getBulletsLeft() > 0) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getFireAnimation().isAnimationFinished(stateTime)) {
				reloading = false;
				shooting = false;
				shootingSound = false;
				stateTime = 0;
				this.setBulletsLeft(this.getBulletsLeft() - 1);
			} else {
				TextureRegion currentFrame = this.getFireAnimation().getKeyFrame(stateTime, false);
				spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2f - spacing, 0, weaponWidth, weaponHeight);
				if (!this.shootingSound)
				{
					this.shootingSounds.get((int )(Math.random() * 2)).play();
					this.shootingSound = true;
				}
			}
		} else if (reloading && this.getBulletsLeft() < this.getMagSize()) {
			this.stateTime += Gdx.graphics.getDeltaTime();
			if (this.getReloadAnimation().isAnimationFinished(stateTime)) {
				reloading = false;
				shooting = false;
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
			spriteBatch.draw(ma5bTexture, Gdx.graphics.getWidth() / 2f - spacing, 0, weaponWidth, weaponHeight);
			if (this.shooting && this.getBulletsLeft() == 0)
				this.flashReloadIcon(spriteBatch);
			shooting = false;
			shootingSound = false;
			reloadingSound = false;
			reloading = false;
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
		this.getShootingSound().dispose();
	}
}
