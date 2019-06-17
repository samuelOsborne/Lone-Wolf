package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.gdx.halo.Enemies.EnemyManager;
import com.gdx.halo.Player.Player;
import com.gdx.halo.Utils.MapReader;

public class RoundManager {
	private EnemyManager    enemyManager;
	private Player          player;
	private MenuManager     menuManager;
	private Halo            halo;
	private MapReader       mapReader;
	private float           nextRoundTimer = 0f;
	private int             round = 0;
	private Matrix4         viewMatrix;
	
	/**
	 * Countdowns
	 */
	private SpriteBatch     spriteBatch;
	private Texture         countDown9;
	private Texture         countDown8;
	private Texture         countDown7;
	private Texture         countDown6;
	private Texture         countDown5;
	private Texture         countDown4;
	private Texture         countDown3;
	private Texture         countDown2;
	private Texture         countDown1;
	private Color           hudColor;
	
	
	public RoundManager(EnemyManager enemyManager, Player player, MenuManager menuManager, Halo halo)
	{
		this.viewMatrix = new Matrix4();
		this.enemyManager = enemyManager;
		this.player = player;
		this.menuManager = menuManager;
		this.halo = halo;
		this.spriteBatch = new SpriteBatch();
		this.hudColor = new Color();
		hudColor.set(0.23f, 0.67f, 1f, 1f);
		this.initCountdown();
	}
	
	public void initCountdown()
	{
		this.countDown9 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_9_w.png"));
		this.countDown8 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_8_w.png"));
		this.countDown7 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_7_w.png"));
		this.countDown6 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_6_w.png"));
		this.countDown5 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_5_w.png"));
		this.countDown4 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_4_w.png"));
		this.countDown3 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_3_w.png"));
		this.countDown2 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_2_w.png"));
		this.countDown1 = new Texture(Gdx.files.internal("HUD/Alerts/countdown_1_w.png"));
	}
	
	public void update()
	{
		if (player.getShieldHealth() == 0 && player.getHealthBars() == 0)
		{
			//player death screen
			enemyManager.emptyEnemyContainer();
			//menuManager.changeScreen(new MainMenu(menuManager));
			player.setShieldHealth(4);
			player.setHealthBars(8);
			this.round = 0;
			return ;
		}
		if (enemyManager.allEnemiesDead)
		{
			//load up next round of enemies
			enemyManager.emptyEnemyContainer();
			nextRoundTimer += Gdx.graphics.getDeltaTime();
			if (nextRoundTimer >= 10)
			{
				try {
					if (round == 0)
					{
						halo.getMapReader().loadEnemies(Gdx.files.internal("Map/round_1.txt"));
					}
					if (round == 1)
						halo.getMapReader().loadEnemies(Gdx.files.internal("Map/round_2.txt"));
					if (round == 2)
						halo.getMapReader().loadEnemies(Gdx.files.internal("Map/round_3.txt"));
					if (round == 3)
						halo.getMapReader().loadEnemies(Gdx.files.internal("Map/round_4.txt"));
					round++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				nextRoundTimer = 0;
			}
		}
	}
	
	public void render() {
		if (enemyManager.allEnemiesDead)
		{
			spriteBatch.setColor(hudColor);
			this.spriteBatch.begin();
			if (nextRoundTimer >= 1 && nextRoundTimer < 2)
				spriteBatch.draw(this.countDown9, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 2 && nextRoundTimer < 3)
				spriteBatch.draw(this.countDown8, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 3 && nextRoundTimer < 4)
				spriteBatch.draw(this.countDown7, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 4 && nextRoundTimer < 5)
				spriteBatch.draw(this.countDown6, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 5 && nextRoundTimer < 6)
				spriteBatch.draw(this.countDown5, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 6 && nextRoundTimer < 7)
				spriteBatch.draw(this.countDown4, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 7 && nextRoundTimer < 8)
				spriteBatch.draw(this.countDown3, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 8 && nextRoundTimer < 9)
				spriteBatch.draw(this.countDown2, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			if (nextRoundTimer >= 9 && nextRoundTimer < 10)
				spriteBatch.draw(this.countDown1, Gdx.graphics.getHeight() / 2 - this.countDown9.getWidth() / 2,
						Gdx.graphics.getHeight() / 2 + 15);
			spriteBatch.setColor(Color.WHITE);
			this.spriteBatch.end();
		}
	}
	
	public void resize() {
		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(viewMatrix);
	}
}
