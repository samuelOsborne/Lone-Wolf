package com.gdx.halo;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
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
	
	
	public RoundManager(EnemyManager enemyManager, Player player, MenuManager menuManager, Halo halo)
	{
		this.enemyManager = enemyManager;
		this.player = player;
		this.menuManager = menuManager;
		this.halo = halo;
	}
	
	public void update()
	{
		if (player.getShieldHealth() == 0 && player.getHealthBars() == 0)
		{
			//player death screen
			//menuManager.changeScreen(new MainMenu(menuManager));
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
						halo.getMapReader().loadEnemies(Gdx.files.internal("Map/round_2.txt"));
					round++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				nextRoundTimer = 0;
			}
		}
	}
}
