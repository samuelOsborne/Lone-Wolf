package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private Texture reticule;
	
	private Texture bullet;
	
	private Sprite reloadSign;
	
	public static Color hudColor;
	
	private float blinkerAlpha = 0f;
	
	public void init(){
		hudColor = new Color();
		hudColor.set(0.23f, 0.67f, 1f, 1f);
		this.reloadSign = new Sprite(new Texture("HUD/Alerts/reload.png"));
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
	
	public abstract void render(SpriteBatch spriteBatch);
	
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
	
	public void flashReloadIcon(SpriteBatch spriteBatch) {
		blinkerAlpha += Gdx.graphics.getDeltaTime();
		reloadSign.setColor(hudColor);
		reloadSign.setPosition(Gdx.graphics.getWidth() / 2 - 5,
				Gdx.graphics.getHeight() / 2 + this.reloadSign.getHeight() - 30);
		reloadSign.draw(spriteBatch, Math.abs((float)Math.sin(blinkerAlpha * 5)));
	}
	
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
	
	public Texture getReticule() {
		return reticule;
	}
	
	public void setReticule(Texture reticule) {
		this.reticule = reticule;
	}
	
	public Texture getBullet() {
		return bullet;
	}
	
	public void setBullet(Texture bullet) {
		this.bullet = bullet;
	}
}
