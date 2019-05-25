package com.gdx.halo.Player;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.FPSCameraController;

public class Player implements Disposable {
	private PlayerHUD           playerHUD;
	private FPSCameraController fpsCameraController;
	private Camera              camera;
	private btCollisionWorld    collisionWorld;
	
	public Player(Camera _camera, btCollisionWorld _collisionWorld) {
		camera = _camera;
		collisionWorld = _collisionWorld;
		this.fpsCameraController = new FPSCameraController(_camera, _collisionWorld, this);
		this.playerHUD = new PlayerHUD(camera);
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
	
	public void setPlayerHUD(PlayerHUD playerHUD) {
		this.playerHUD = playerHUD;
	}
	
	public void setVelocity(float velocity) {
		this.fpsCameraController.setVelocity(velocity);
	}
	
	public void shoot() {
		this.playerHUD.shoot();
	}
	
	public void reload() {
		this.playerHUD.reload();
	}
	
	public void update()
	{
		fpsCameraController.update();
		playerHUD.update();
	}
}
