package com.gdx.halo.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.FPSCameraController;
import com.gdx.halo.Halo;
import com.gdx.halo.Weapons.AWeapon;
import com.gdx.halo.Weapons.Human.Ma5b;
import com.gdx.halo.Weapons.Human.Pistol;

public class Player implements Disposable {
	private PlayerHUD           playerHUD;
	private FPSCameraController fpsCameraController;
	private Camera              camera;
	private btCollisionWorld    collisionWorld;
	private AWeapon             currentWeapon;
	private Halo                gameInstance;
	private Sound               shieldSound;
	
	/**
	 * Health
	 */
	private int                 shieldHealth = 4;
	private int                 healthBars = 8;
	private float               timeSinceLastHit = 0f;
	
	
	public Player(Halo _gameInstance, Camera _camera, btCollisionWorld _collisionWorld) {
		this.gameInstance = _gameInstance;
		camera = _camera;
		collisionWorld = _collisionWorld;
		this.fpsCameraController = new FPSCameraController(_camera, _collisionWorld, this);
		this.playerHUD = new PlayerHUD(camera, this);
		//this.currentWeapon = new Pistol();
		this.currentWeapon = new Ma5b();
		this.playerHUD.setWeapon(currentWeapon);
		this.shieldSound = Gdx.audio.newSound(Gdx.files.internal("Sounds/HUD/shield_recharge.mp3"));
	}
	
	public void render()
	{
		this.playerHUD.render();
	}
	
	@Override
	public void dispose() {
	}
	
	public FPSCameraController getFpsCameraController() {
		return fpsCameraController;
	}
	
	public void setFpsCameraController(FPSCameraController fpsCameraController) {
		this.fpsCameraController = fpsCameraController;
	}
	
	public void damagePlayer()
	{
		timeSinceLastHit = 0;
		if (this.shieldHealth > 0)
			this.shieldHealth--;
		else if (this.healthBars > 0)
			this.healthBars--;
		if (this.shieldHealth == 0 && this.healthBars == 0)
			collisionWorld.removeCollisionObject(this.fpsCameraController.getGameObject().body);
	}
	
	public void setPosition(Vector3 position)
	{
		this.camera.position.set(position);
	}
	
	public void setDirection(Vector3 direction)
	{
		this.camera.direction.set(direction);
	}
	
	public void setPlayerHUD(PlayerHUD playerHUD) {
		this.playerHUD = playerHUD;
	}
	
	public void setVelocity(float velocity) {
		this.fpsCameraController.setVelocity(velocity);
	}
	
	public void shoot() {
		int enemyIndex;
		
		if ((enemyIndex = this.playerHUD.shoot(collisionWorld)) != -1)
		{
			switch (this.currentWeapon.weaponType)
			{
				case M40:
					this.gameInstance.damageEnemy(enemyIndex, 25);
					break;
				case MA5B:
					this.gameInstance.damageEnemy(enemyIndex, 20);
					break;
				case SHOTGUN:
					break;
				case SNIPER_RIFLE:
					break;
				case PLASMA_PISTOL:
					break;
				case PLASMA_RIFLE:
					break;
			}
		}
	}
	
	public void reload() {
		this.playerHUD.reload();
	}
	
	public void update()
	{
		timeSinceLastHit += Gdx.graphics.getDeltaTime();
		//System.out.println("time : "  + timeSinceLastHit);
		if (timeSinceLastHit >= 3 && this.healthBars > 0 && this.shieldHealth < 4)
		{
			this.shieldSound.play();
			this.shieldHealth = 4;
			timeSinceLastHit = 0;
		}
		fpsCameraController.update();
		playerHUD.update();
	}
	
	public int getShieldHealth() {
		return shieldHealth;
	}
	
	public void setShieldHealth(int shieldHealth) {
		this.shieldHealth = shieldHealth;
	}
	
	public int getHealthBars() {
		return healthBars;
	}
	
	public void setHealthBars(int healthBars) {
		this.healthBars = healthBars;
	}
}
