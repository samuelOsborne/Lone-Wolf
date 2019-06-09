package com.gdx.halo.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionWorld;
import com.badlogic.gdx.utils.Array;
import com.gdx.halo.AnimatedDecal;
import com.gdx.halo.GameObject;
import com.gdx.halo.Halo;
import com.gdx.halo.Player.Player;
import com.gdx.halo.Utils.AnimationLoader;
import com.gdx.halo.Utils.ColliderCreator;
import com.gdx.halo.Weapons.Alien.PlasmaBullet;

import static com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT;
import static com.gdx.halo.Halo.*;

public class Elite extends Enemy {
	private static final int FRAME_COLS = 2, FRAME_ROWS = 1;
	private Array<PlasmaBullet> plasmaProjectiles;
	private Player              player;
	private btCollisionWorld    collisionWorld;
	private float               deathTimer = 0f;
	
	public Elite()
	{
		initInformation(new Vector3(0, 0, 0));
	}
	
	public Elite(Vector3 position, Player player, btCollisionWorld collisionWorld)
	{
		this.plasmaProjectiles = new Array<PlasmaBullet>();
		this.collisionWorld = collisionWorld;
		this.player = player;
		this.initInformation(position);
		this.setPosition(position);
	}
	
	private void initInformation(Vector3 position)
	{
		this.state = State.FIRING;
		this.scaleX = 3f;
		this.scaleY = 5f;
		this.health = 100;
		this.position = position;
		this.initDeathAnimation();
		this.initFireAnimation();
		this.initWalkAnimation();
		this.initCollider();
	}
	
	@Override
	public void update(){
		this.stateTime += Gdx.graphics.getDeltaTime();
		//destroy if 0
		if (this.health <= 0) {
			this.state = State.DEAD;
			this.deathTimer += Gdx.graphics.getDeltaTime();
			if (deathTimer >= 5)
			{
				remove = true;
				collisionWorld.removeCollisionObject(this.gameObject.body);
			}
		}
		else {
			//After x amount of seconds fire again
			this.shoot();
			this.move();
		}
		for (PlasmaBullet bullet : plasmaProjectiles)
		{
			if (bullet.collidedWithPlayer)
				player.damagePlayer();
			if (bullet.remove)
			{
				collisionWorld.removeCollisionObject(bullet.getGameObject().body);
				this.plasmaProjectiles.removeValue(bullet, true);
			}
			else
				bullet.update();
		}
		this.updateCollider();
	}
	
	@Override
	public void render(DecalBatch decalBatch, Camera camera) {
		switch (state)
		{
			case IDLE:
				this.idleDecal.setPosition(position);
				this.idleDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.idleDecal);
				break;
			case WALKING:
			{
				this.walkingDecal.setPosition(position);
				this.walkingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.walkingDecal);
			}
			break;
			case FIRING:
				this.firingDecal.setPosition(position);
				this.firingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.firingDecal);
				for (PlasmaBullet bullet : plasmaProjectiles)
				{
					bullet.render(decalBatch, camera);
				}
				break;
			case RELOADING:
				this.reloadingDecal.setPosition(position);
				this.reloadingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.reloadingDecal);
				break;
			case DEAD:
				this.deathDecal.setPosition(position);
				this.deathDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.deathDecal);
		}
	}
	
	@Override
	public void initFireAnimation() {
		Texture shootSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/shooting.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(shootSheet, 2, 1);
		fireAnimation = new Animation<TextureRegion>(0.25f, walkFrames);
		fireAnimation.setPlayMode(Animation.PlayMode.LOOP);
		firingDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, fireAnimation, true);
		firingDecal.setKeepSize(true);
		firingDecal.setScaleX(scaleX);
		firingDecal.setScaleY(scaleY);
		firingDecal.setPlaying(true);
		firingDecal.setPosition(this.position);
		stateTime = 0f;
	}
	
	@Override
	public void initWalkAnimation() {
		Texture walkSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/walking.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(walkSheet, FRAME_COLS, FRAME_ROWS);
		walkAnimation = new Animation<TextureRegion>(0.5f, walkFrames);
		walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		walkingDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, walkAnimation, true);
		walkingDecal.setKeepSize(true);
		walkingDecal.setScaleX(scaleX);
		walkingDecal.setScaleY(scaleY);
		walkingDecal.setPlaying(true);
		walkingDecal.setPosition(this.position);
		stateTime = 0f;
	}
	
	@Override
	public void initDeathAnimation() {
		Texture deathSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/death.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(deathSheet, 3, 1);
		deathAnimation = new Animation<TextureRegion>(0.25f, walkFrames);
		deathAnimation.setPlayMode(Animation.PlayMode.NORMAL);
		deathDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, deathAnimation, true);
		deathDecal.setKeepSize(true);
		deathDecal.setScaleX(scaleX);
		deathDecal.setScaleY(scaleY);
		deathDecal.setPlaying(true);
		deathDecal.setPosition(this.position);
		stateTime = 0f;
	}
	
	@Override
	public void initIdleAnimation() {
	
	}
	
	@Override
	public void initReloadAnimation() {
	
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	@Override
	public void initCollider() {
		Model model = ColliderCreator.createCollider(this.firingDecal, "elite");
		
		gameObject = new GameObject.Constructor(model, "elite", new btBoxShape(new Vector3(1.5f, 2.5f, 0.5f))).construct();
		gameObject.body.setUserValue(Halo.ELITE_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		gameObject.body.setCollisionFlags(CF_CHARACTER_OBJECT);
		gameObject.transform.set(this.firingDecal.getPosition(), this.gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
	}
	
	@Override
	public void updateCollider() {
		this.gameObject.transform.set(this.currentDecal().getPosition(), this.currentDecal().getRotation());
		this.gameObject.body.setWorldTransform(this.gameObject.transform);
	}
	
	@Override
	public void move() {
	
	}
	
	public void shoot() {
		if (stateTime >= 1.5f)
		{
			PlasmaBullet plasmaBullet = new PlasmaBullet(position,
					this.gameObject.transform.getRotation(new Quaternion()),
					new Vector3(player.getFpsCameraController().getPosition()));
			this.plasmaProjectiles.add(plasmaBullet);
			this.collisionWorld.addCollisionObject(plasmaBullet.getGameObject().body, PLASMA_FLAG, PLAYER_FLAG | WALL_FLAG);
			stateTime = 0;
		}
	}
}
