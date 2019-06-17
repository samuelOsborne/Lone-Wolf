package com.gdx.halo.Enemies;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Halo;
import com.gdx.halo.Player.Player;

public class EnemyManager implements Disposable {
	private Array<Enemy>        enemies;
	private DecalBatch          decalBatch;
	private Camera              camera;
	private btCollisionWorld    collisionWorld;
	public boolean              allEnemiesDead = false;
	
	public EnemyManager(Camera _camera, btCollisionWorld _collisionWorld)
	{
		this.camera = _camera;
		this.collisionWorld = _collisionWorld;
		this.enemies = new Array<Enemy>();
		this.decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
	}
	
	public void update()
	{
		boolean notAllDeadCheck = false;
		
		for (Enemy enemy : enemies)
		{
			if (!enemy.remove)
			{
				enemy.update();
				notAllDeadCheck = true;
			}
		}
		if (!notAllDeadCheck)
		{
			this.emptyEnemyContainer();
			this.allEnemiesDead = true;
		}
	}
	
	public void setWallCollide(int userValue0, int userValue1, boolean wallCollide)
	{
		for (Enemy e : enemies)
		{
			if (e.getCollisionObject().getUserValue() == userValue0 ||
					e.getCollisionObject().getUserValue() == userValue1)
			{
				e.setWallCollide(wallCollide);
			}
		}
	}
	
	public void setEliteCollide(int userValue0, int userValue1, boolean wallCollide)
	{
		for (Enemy e : enemies)
		{
			if (e.getCollisionObject().getUserValue() == userValue0 ||
					e.getCollisionObject().getUserValue() == userValue1)
			{
				e.setEliteCollide(wallCollide);
				return ;
			}
		}
	}
	
	public void emptyEnemyContainer()
	{
		//System.out.println("all enemies dead ... clearing");
		for (Enemy enemy : enemies)
		{
			if (!enemy.remove)
			{
				collisionWorld.removeCollisionObject(enemy.getCollisionObject());
			}
		}
		
		this.enemies.clear();
		this.allEnemiesDead = true;
	}
	
	public void render()
	{
		for (Enemy e : enemies) {
			if (!e.remove)
				e.render(decalBatch, camera);
		}
		decalBatch.flush();
	}
	
	public void addElite(Vector3 position, Player player)
	{
		Elite elite = new Elite(position, player, collisionWorld);
		
		this.enemies.add(elite);
	}
	
	public void removeBullet(btCollisionObject btCollisionObject)
	{
		for (Enemy e : enemies)
		{
			e.removeBullet(btCollisionObject);
		}
	}
	
	public void addEnemy(Enemy enemy)
	{
		if (enemy.getCollisionObject().getUserValue() == Halo.ELITE_USER_VALUE)
			System.out.println("Adding elite");
		else if (enemy.getCollisionObject().getUserValue() == Halo.GRUNT_USER_VALUE)
			System.out.println("Adding grunt");
		
		this.enemies.add(enemy);
		enemy.gameObject.body.setUserValue(this.enemies.size - 1);
		this.allEnemiesDead = false;
	}
	
	public Array<Enemy> getEnemies() {
		return enemies;
	}
	
	public void setEnemies(Array<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	public DecalBatch getDecalBatch() {
		return decalBatch;
	}
	
	public void setDecalBatch(DecalBatch decalBatch) {
		this.decalBatch = decalBatch;
	}
	
	public btCollisionWorld getCollisionWorld() {
		return collisionWorld;
	}
	
	public void setCollisionWorld(btCollisionWorld collisionWorld) {
		this.collisionWorld = collisionWorld;
	}
	
	@Override
	public void dispose() {
		decalBatch.dispose();
	}
}
