package com.gdx.halo.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Weapons.AWeapon;

public class PlayerHUD implements Disposable {
	private SpriteBatch spriteBatch;
	private AWeapon weapon;
	private Matrix4 viewMatrix;
	private Player  attachedPlayer;
	private Camera camera;
	
	/**
	 * Health
	 */
	private Texture healthBar;
	private Texture healthCross;
	private Texture shieldBar;
	private Texture shieldBarHit1;
	private Texture shieldBarHit2;
	private Texture shieldBarHit3;
	private Texture shieldBarHit4;
	private Texture shieldIcon;
	private Color   hudColor;
	
	/**
	 * Paths
	 */
	private static String shieldBarPath = "HUD/Health/shield_bar_no_icon.png";
	private static String shieldBarPath1 = "HUD/Health/shield_bar_no_icon_hit_1.png";
	private static String shieldBarPath2 = "HUD/Health/shield_bar_no_icon_hit_2.png";
	private static String shieldBarPath3 = "HUD/Health/shield_bar_no_icon_hit_3.png";
	private static String shieldBarPath4 = "HUD/Health/shield_bar_no_icon_hit_4.png";
	private static String shieldIconPath = "HUD/Health/shield_icon.png";
	private static String healthBarPath = "HUD/Health/health_bar_no_bg.png";
	private static String healthCrossPath = "HUD/Health/health_cross.png";
	
	public PlayerHUD(Camera _camera, Player player) {
		this.attachedPlayer = player;
		this.camera = _camera;
		this.spriteBatch = new SpriteBatch();
		this.hudColor = new Color();
		hudColor.set(0.23f, 0.67f, 1f, 1f);
		this.viewMatrix = new Matrix4();
		
		this.healthBar = new Texture(Gdx.files.internal(healthBarPath));
		this.shieldBar = new Texture(Gdx.files.internal(shieldBarPath));
		this.shieldBarHit1 = new Texture(Gdx.files.internal(shieldBarPath1));
		this.shieldBarHit2 = new Texture(Gdx.files.internal(shieldBarPath2));
		this.shieldBarHit3 = new Texture(Gdx.files.internal(shieldBarPath3));
		this.shieldBarHit4 = new Texture(Gdx.files.internal(shieldBarPath4));
		this.shieldIcon = new Texture(Gdx.files.internal(shieldIconPath));
		this.healthCross = new Texture(Gdx.files.internal(healthCrossPath));
	}
	
	public void render() {
		spriteBatch.setColor(hudColor);
		spriteBatch.begin();
		
		/*
		  Shield bar
		 */
		if (this.attachedPlayer.getShieldHealth() == 4)
		{
			spriteBatch.draw(this.shieldBar, Gdx.graphics.getWidth() - this.shieldBar.getWidth() - 15,
					Gdx.graphics.getHeight() - this.shieldBar.getHeight() - 15);
			spriteBatch.draw(this.shieldIcon, Gdx.graphics.getWidth() - this.shieldBar.getWidth() - 40,
					Gdx.graphics.getHeight() - this.shieldIcon.getHeight() - 17);
		}
		else if (this.attachedPlayer.getShieldHealth() == 3)
		{
			spriteBatch.draw(this.shieldBarHit1, Gdx.graphics.getWidth() - this.shieldBarHit1.getWidth() - 15,
					Gdx.graphics.getHeight() - this.shieldBarHit1.getHeight() - 15);
			spriteBatch.draw(this.shieldIcon, Gdx.graphics.getWidth() - this.shieldBarHit1.getWidth() - 40,
					Gdx.graphics.getHeight() - this.shieldIcon.getHeight() - 17);
		}
		else if (this.attachedPlayer.getShieldHealth() == 2)
		{
			spriteBatch.draw(this.shieldBarHit2, Gdx.graphics.getWidth() - this.shieldBarHit2.getWidth() - 15,
					Gdx.graphics.getHeight() - this.shieldBarHit2.getHeight() - 15);
			spriteBatch.draw(this.shieldIcon, Gdx.graphics.getWidth() - this.shieldBarHit2.getWidth() - 40,
					Gdx.graphics.getHeight() - this.shieldIcon.getHeight() - 17);
		}
		else if (this.attachedPlayer.getShieldHealth() == 1)
		{
			spriteBatch.draw(this.shieldBarHit3, Gdx.graphics.getWidth() - this.shieldBarHit3.getWidth() - 15,
					Gdx.graphics.getHeight() - this.shieldBarHit3.getHeight() - 15);
			spriteBatch.draw(this.shieldIcon, Gdx.graphics.getWidth() - this.shieldBarHit3.getWidth() - 40,
					Gdx.graphics.getHeight() - this.shieldIcon.getHeight() - 17);
		}
		else if (this.attachedPlayer.getShieldHealth() == 0)
		{
			spriteBatch.draw(this.shieldBarHit4, Gdx.graphics.getWidth() - this.shieldBarHit4.getWidth() - 15,
					Gdx.graphics.getHeight() - this.shieldBarHit4.getHeight() - 15);
			spriteBatch.draw(this.shieldIcon, Gdx.graphics.getWidth() - this.shieldBarHit4.getWidth() - 40,
					Gdx.graphics.getHeight() - this.shieldIcon.getHeight() - 17);
		}
		
		/*
		  Health bars
		 */
		for (int i = 0; i < this.attachedPlayer.getHealthBars(); i++)
		{
			if (this.attachedPlayer.getHealthBars() <= 1)
			{
				this.spriteBatch.setColor(Color.RED);
				spriteBatch.draw(this.healthBar, i * -this.healthBar.getWidth() + Gdx.graphics.getWidth() - 90,
						Gdx.graphics.getHeight() - this.healthBar.getHeight() - this.shieldBar.getHeight() + 5);
				this.spriteBatch.setColor(hudColor);
			}
			else if (this.attachedPlayer.getHealthBars() <= 3)
			{
				this.spriteBatch.setColor(Color.YELLOW);
				spriteBatch.draw(this.healthBar, i * -this.healthBar.getWidth() + Gdx.graphics.getWidth() - 90,
						Gdx.graphics.getHeight() - this.healthBar.getHeight() - this.shieldBar.getHeight() + 5);
				this.spriteBatch.setColor(hudColor);
			}
			else
				spriteBatch.draw(this.healthBar, i * -this.healthBar.getWidth() + Gdx.graphics.getWidth() - 90,
						Gdx.graphics.getHeight() - this.healthBar.getHeight() - this.shieldBar.getHeight() + 5);
			
		}
		spriteBatch.draw(this.healthCross, Gdx.graphics.getWidth() - this.shieldBar.getWidth() - this.shieldIcon.getWidth() - 5,
				Gdx.graphics.getHeight() - this.healthBar.getHeight() - this.shieldBar.getHeight() + 5);
		
		spriteBatch.setColor(Color.WHITE);
		spriteBatch.end();
		if (weapon != null)
			weapon.render(spriteBatch);
	}
	
	public int shoot(btCollisionWorld collisionWorld) {
		if (this.weapon != null)
			return (this.weapon.fire(collisionWorld, camera));
		return (-1);
	}
	
	public void reload() {
		if (this.weapon != null)
			this.weapon.reload();
	}
	
	public void update()
	{
		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(viewMatrix);
		if (this.weapon != null)
			weapon.update();
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
	}
	
	public AWeapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(AWeapon weapon) {
		this.weapon = weapon;
	}
}
