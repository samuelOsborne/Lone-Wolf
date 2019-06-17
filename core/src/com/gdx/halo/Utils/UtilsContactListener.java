package com.gdx.halo.Utils;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.gdx.halo.Halo;
import com.gdx.halo.Weapons.AWeapon;
import com.gdx.halo.Weapons.Human.Ma5b;
import com.gdx.halo.Weapons.Human.Pistol;

public class UtilsContactListener extends ContactListener {
	private Halo    gameManager;
	
	public UtilsContactListener(Halo gameManager)
	{
		this.gameManager = gameManager;
	}
	
	@Override
	public boolean onContactAdded (btCollisionObject userValue0, int partId0, int index0, btCollisionObject userValue1,
	                               int partId1, int index1) {
		if (userValue0.getContactCallbackFlag() == Halo.PLAYER_FLAG && userValue1.getContactCallbackFlag() == Halo.PLASMA_FLAG)
		{
			gameManager.getPlayer().damagePlayer();
			gameManager.removeBullet(userValue1);
		}
		if (userValue0.getContactCallbackFlag() == Halo.PLAYER_FLAG && userValue1.getUserValue() == Halo.HP_USER_VALUE)
		{
			if (gameManager.getPlayer().getHealthBars() > 0 && gameManager.getPlayer().getHealthBars() < 8)
			{
				gameManager.getPlayer().setHealthBars(8);
				gameManager.getDecalManager().removeObject(userValue1.getUserValue());
			}
		}
		if (userValue0.getContactCallbackFlag() == Halo.PLAYER_FLAG && userValue1.getUserValue() == Halo.AR_USER_VALUE)
		{
			if (gameManager.getPlayer().getCurrentWeapon().weaponType.equals(AWeapon.WeaponType.M40))
			{
				gameManager.getPlayer().setCurrentWeapon(new Ma5b());
				gameManager.getDecalManager().removeObject(userValue1.getUserValue());
			}
		}
		if (userValue0.getContactCallbackFlag() == Halo.PLAYER_FLAG && userValue1.getUserValue() == Halo.M40_USER_VALUE)
		{
			if (gameManager.getPlayer().getCurrentWeapon().weaponType.equals(AWeapon.WeaponType.MA5B))
			{
				gameManager.getPlayer().setCurrentWeapon(new Pistol());
				gameManager.getDecalManager().removeObject(userValue1.getUserValue());
			}
		}
		if (userValue0.getUserValue() == Halo.WALL_USER_VALUE && userValue1.getUserValue() == Halo.PLASMA_USER_VALUE)
		{
			gameManager.removeBullet(userValue1);
		}
		if (userValue0.getContactCallbackFlag() == Halo.ENEMY_FLAG && userValue1.getContactCallbackFlag() == Halo.ENEMY_FLAG)
		{
			gameManager.setEliteCollide(userValue0.getUserValue(), userValue1.getUserValue(), true);
		}
		if (userValue0.getContactCallbackFlag() == Halo.ENEMY_FLAG && userValue1.getUserValue() == Halo.WALL_USER_VALUE)
		{
			gameManager.setWallCollide(userValue0.getUserValue(), userValue1.getUserValue(), true);
		}
		if (userValue0.getUserValue() == Halo.WALL_USER_VALUE && userValue1.getContactCallbackFlag() == Halo.ENEMY_FLAG)
		{
			gameManager.setWallCollide(userValue0.getUserValue(), userValue1.getUserValue(), true);
		}
		return true;
	}
}
