package com.gdx.halo.Enemies;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Player.Player;

public class EnemyManager implements Disposable {
	private Array<Enemy>        enemies;
	private DecalBatch          decalBatch;
	private Camera              camera;
	private btCollisionWorld    collisionWorld;
	
	public EnemyManager(Camera _camera, btCollisionWorld _collisionWorld)
	{
		this.camera = _camera;
		this.collisionWorld = _collisionWorld;
		this.enemies = new Array<Enemy>();
		this.decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
	}
	
	public void update()
	{
		for (Enemy enemy : enemies)
		{
			enemy.update();
			if (enemy.getRemove())
				this.enemies.removeValue(enemy, true);
		}
	}
	
	public void render()
	{
		for (Enemy e : enemies) {
			e.render(decalBatch, camera);
		}
		decalBatch.flush();
	}
	
	public void addElite(Vector3 position, Player player)
	{
		Elite elite = new Elite(position, player, collisionWorld);
		
		this.enemies.add(elite);
	}
	
	public void addEnemy(Enemy enemy)
	{
		this.enemies.add(enemy);
		enemy.gameObject.body.setUserValue(this.enemies.size - 1);
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
