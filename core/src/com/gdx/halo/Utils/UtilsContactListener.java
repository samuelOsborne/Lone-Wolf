package com.gdx.halo.Utils;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.gdx.halo.Halo;

public class UtilsContactListener extends ContactListener {
	private Halo    gameManager;
	
	public UtilsContactListener(Halo gameManager)
	{
		this.gameManager = gameManager;
	}
	
	@Override
	public boolean onContactAdded (btCollisionObject userValue0, int partId0, int index0, btCollisionObject userValue1, int partId1, int index1) {
		if (userValue0.getUserValue() == Halo.PLAYER_USER_VALUE && userValue1.getUserValue() == Halo.PLASMA_USER_VALUE)
		{
			gameManager.getPlayer().damagePlayer();
			gameManager.removeBullet(userValue1);
		}
		else if (userValue0.getUserValue() == Halo.WALL_USER_VALUE && userValue1.getUserValue() == Halo.PLASMA_USER_VALUE)
		{
			gameManager.removeBullet(userValue1);
		}
		return true;
	}
	
}
