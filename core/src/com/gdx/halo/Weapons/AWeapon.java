package com.gdx.halo.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

import javax.swing.text.View;

public abstract class AWeapon implements Disposable {
	/**
	 * Current bullets left in mag
	 */
	private int magSize = 0;
	
	/**
	 * Max mag size
	 */
	private int ammoAmount = 0;
	
	private String  animationPath = "";
	
	private float   animationSpeed = 0;
	
	private int frameRows = 0;
	
	private int frameCols = 0;
	
	private float damage = 0;
	
	private Animation<TextureRegion> fireAnimation;
	
	private float stateTime;
	
	public void init(){
		this.stateTime = 0f;
	}
	
	public void initaliseAnimation()
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
		fireAnimation = new Animation<TextureRegion>(animationSpeed, walkFrames);
		fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
		stateTime = 0;
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
	
	public void setAmmoAmount(int amount) {
		this.ammoAmount = amount;
	}
	
	public int getMagSize() {
		return (magSize);
	}
	
	public int getAmmoAmount() {
		return (ammoAmount);
	}
	
	public abstract void fire();
	
	public abstract void reload();
	
	public void setAnimationSheet(String path) {
		this.animationPath = path;
	}
	
	public String getAnimationSheet() {
		return (this.animationPath);
	}
	
	public void setAnimationSpeed(float speed) {
		this.animationSpeed = speed;
	}
	
	public float getAnimationSpeed() {
		return (animationSpeed);
	}
	
	public void setFrameCols(int frameCols) {
		this.frameCols = frameCols;
	}
	
	public int getFrameCols() {
		return (frameCols);
	}
	
	public void setFrameRows(int frameRows) {
		this.frameRows = frameRows;
	}
	
	public int getFrameRows() {
		return (frameRows);
	}
	
	public abstract void update();
}
