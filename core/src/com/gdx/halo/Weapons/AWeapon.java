package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public abstract class AWeapon implements Disposable {
	/**
	 * Current bullets left in mag
	 */
	private int magSize = 0;
	
	/**
	 * Max mag size
	 */
	private int bulletsLeft = 0;
	
	private float damage = 0;
	
	private Animation<TextureRegion> fireAnimation;
	
	private Animation<TextureRegion> reloadAnimation;
	
	private Sound   shootingSound;
	
	private Sound   reloadSound;
	
	
	public void init(){
	}
	
	public Animation<TextureRegion> initaliseAnimation(String animationPath,
	                                                   Animation<TextureRegion> animation,
	                                                   int frameCols,
	                                                   int frameRows,
	                                                   float animationSpeed)
	{
		if (frameCols == 0 || frameRows == 0 || animationSpeed == 0 || animationPath.equals(""))
		{
			throw new RuntimeException("Please set frameCol, frameRow, AnimationSpeed and AnimationPath " +
					"before initialising an animation.");
		}
		Texture weaponAnimationSheet = new Texture(Gdx.files.internal(animationPath));
		TextureRegion[][] tmpTextures = TextureRegion.split(weaponAnimationSheet,
				weaponAnimationSheet.getWidth() / frameCols,
				weaponAnimationSheet.getHeight() / frameRows);
		TextureRegion[] walkFrames = new TextureRegion[frameCols * frameRows];
		int index = 0;
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameCols; j++) {
				walkFrames[index++] = tmpTextures[i][j];
			}
		}
		return (animation = new Animation<TextureRegion>(animationSpeed, walkFrames));
	}
	
	public Animation<TextureRegion> getFireAnimation() {
		return (fireAnimation);
	}
	
	public abstract void render();
	
	public void setDamage(float dmg)
	{
		this.damage = dmg;
	}
	
	public float getDamage()
	{
		return (damage);
	}
	
	public void setMagSize(int amount) {
		this.magSize = amount;
	}
	
	public void setBulletsLeft(int amount) {
		this.bulletsLeft = amount;
	}
	
	public int getMagSize() {
		return (magSize);
	}
	
	public int getBulletsLeft() {
		return (bulletsLeft);
	}
	
	public abstract void fire();
	
	public abstract void reload();
	
	public abstract void update();
	
	public Sound getShootingSound() {
		return shootingSound;
	}
	
	public void setShootingSound(Sound shootingSound) {
		this.shootingSound = shootingSound;
	}
	
	public Sound getReloadSound() {
		return reloadSound;
	}
	
	public void setReloadSound(Sound reloadSound) {
		this.reloadSound = reloadSound;
	}
	
	public Animation<TextureRegion> getReloadAnimation() {
		return reloadAnimation;
	}
	
	public void setReloadAnimation(Animation<TextureRegion> reloadAnimation) {
		this.reloadAnimation = reloadAnimation;
	}
	
	public void setFireAnimation(Animation<TextureRegion> fireAnimation) {
		this.fireAnimation = fireAnimation;
	}
}
