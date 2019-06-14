package com.gdx.halo.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
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

import java.util.Iterator;

import static com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags.CF_CHARACTER_OBJECT;
import static com.gdx.halo.Halo.*;

public class Grunt extends Enemy {
	private static final int    FRAME_COLS = 2, FRAME_ROWS = 1;
	private Array<PlasmaBullet> plasmaProjectiles;
	private Player              player;
	private btCollisionWorld    collisionWorld;
	private float               deathTimer = 0f;
	private Color               pinkPlasma;
	private boolean             flinching = false;
	private float               resetAnimationTimer;
	
	/**
	 * Sounds
	 */
	private Array<Sound>        hitSounds;
	private Array<Sound>        deathSounds;
	private boolean             playingSound = false;
	
	public Grunt(){
		this.initInformation(new Vector3(0, 0, 0));
	}
	
	public Grunt(Vector3 position, Player player, btCollisionWorld collisionWorld)
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
		this.scaleX = 2f;
		this.scaleY = 2f;
		this.health = 100;
		this.position = position;
		this.position.y -= 1.5f;
		this.initDeathAnimation();
		this.initFireAnimation();
		this.initWalkAnimation();
		this.initFlinchDecal();
		this.initCollider();
		this.pinkPlasma = new Color();
		this.pinkPlasma.set(1f, 123/255f, 1f, 1f);
		this.hitSounds = new Array<Sound>();
		this.deathSounds = new Array<Sound>();
		this.initSounds();
	}
	
	private void initSounds()
	{
		String[] paths = {"Sounds/Enemies/Grunt/Flinch/hurt_enemy.1.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.2.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.3.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.4.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.5.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.6.ogg",
				"Sounds/Enemies/Grunt/Flinch/hurt_enemy.15.ogg"};
		String[] deathPaths = {"Sounds/Enemies/Grunt/Death/death_violent.1.ogg",
				"Sounds/Enemies/Grunt/Death/death_violent.2.ogg",
				"Sounds/Enemies/Grunt/Death/death_violent.3.ogg",
				"Sounds/Enemies/Grunt/Death/death_violent.4.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.1.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.2.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.3.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.4.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.5.ogg",
				"Sounds/Enemies/Grunt/Death/pain_body_major.6.ogg"};
		
		
		for (int i = 0; i < 7; i++)
		{
			this.hitSounds.add(Gdx.audio.newSound(Gdx.files.internal(paths[i])));
		}
		for (int i = 0; i < 10; i++)
		{
			this.deathSounds.add(Gdx.audio.newSound(Gdx.files.internal(deathPaths[i])));
		}
	}
	
	@Override
	public void update() {
		this.stateTime += Gdx.graphics.getDeltaTime();
		//destroy if 0
		if (this.health <= 0) {
			this.state = State.DEAD;
			this.deathTimer += Gdx.graphics.getDeltaTime();
			collisionWorld.removeCollisionObject(this.gameObject.body);
			if (!playingSound)
			{
				this.deathSounds.get((int )(Math.random() * 10)).play();
				playingSound = true;
			}
			if (deathTimer >= 5)
				remove = true;
		}
		else {
			//After x amount of seconds fire again
			this.shoot();
			this.move();
		}
		if (flinching && state != State.DEAD)
		{
			resetAnimationTimer += Gdx.graphics.getDeltaTime();
			if (!playingSound)
			{
				this.hitSounds.get((int )(Math.random() * 7)).play();
				playingSound = true;
			}
			if (resetAnimationTimer >= 0.2f)
			{
				flinching = false;
				resetAnimationTimer = 0;
				this.state = State.FIRING;
				playingSound = false;
			}
		}
		
		Iterator<PlasmaBullet> i = plasmaProjectiles.iterator();
		while (i.hasNext())
		{
			PlasmaBullet tmp = i.next();
			if (tmp.collidedWithPlayer)
				player.damagePlayer();
			if (tmp.remove)
			{
				collisionWorld.removeCollisionObject(tmp.getGameObject().body);
				i.remove();
			}
			else
				tmp.update();
		}
		this.updateCollider();
	}
	
	@Override
	public void render(DecalBatch decalBatch, Camera camera) {
		switch (state) {
			case IDLE:
				this.idleDecal.setPosition(position);
				this.idleDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.idleDecal);
				break;
			case WALKING: {
				this.walkingDecal.setPosition(position);
				this.walkingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.walkingDecal);
			}
			break;
			case FIRING:
				this.firingDecal.setPosition(position);
				this.firingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.firingDecal);
				for (PlasmaBullet bullet : plasmaProjectiles) {
					bullet.getProjectileDecal().setColor(pinkPlasma);
					bullet.render(decalBatch, camera);
				}
				break;
			case RELOADING:
				this.reloadingDecal.setPosition(position);
				this.reloadingDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.reloadingDecal);
				break;
			case FLINCH:
				this.flinchDecal.setPosition(position);
				this.flinchDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.flinchDecal);
				this.flinching = true;
				break;
			case DEAD:
				this.deathDecal.setPosition(position);
				this.deathDecal.lookAt(camera.position, camera.up);
				decalBatch.add(this.deathDecal);
		}
	}
	
	@Override
	public void initFireAnimation() {
		Texture shootSheet = new Texture(Gdx.files.internal("Animations/Enemies/Grunt/shooting.png"));
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
		Texture walkSheet = new Texture(Gdx.files.internal("Animations/Enemies/Grunt/walking.png"));
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
		Texture deathSheet = new Texture(Gdx.files.internal("Animations/Enemies/Grunt/dieing.png"));
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
	public void initFlinchDecal() {
		this.flinchDecal = Decal.newDecal(1f, 1f, new TextureRegion(new Texture(Gdx.files.internal("Animations/Enemies/Grunt/flinch.png"))));
		this.flinchDecal.setScaleX(scaleX);
		this.flinchDecal.setScaleY(scaleY);
		this.flinchDecal.setPosition(this.position);
		this.flinchDecal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		stateTime = 0f;
	}
	
	@Override
	public void initCollider() {
		Model model = ColliderCreator.createCollider(this.firingDecal, "grunt");
		
		gameObject = new GameObject.Constructor(model, "grunt", new btBoxShape(new Vector3(1f, 1f, 0.5f))).construct();
		gameObject.body.setUserValue(Halo.GRUNT_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() |
				btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
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
	
	@Override
	public void removeBullet(btCollisionObject btCollisionObject) {
		for (PlasmaBullet plasmaBullet : plasmaProjectiles)
		{
			if (plasmaBullet.getBtCollisionObject() == btCollisionObject)
				plasmaBullet.remove = true;
		}
	}
	
	@Override
	public void shoot() {
		if (stateTime >= 1.5f)
		{
			Vector3 shootingPos = new Vector3(position);
			//shootingPos.y -= 0.3f;
			PlasmaBullet plasmaBullet = new PlasmaBullet(shootingPos,
					this.gameObject.transform.getRotation(new Quaternion()),
					new Vector3(player.getFpsCameraController().getPosition()));
			this.plasmaProjectiles.add(plasmaBullet);
			this.collisionWorld.addCollisionObject(plasmaBullet.getGameObject().body, PLASMA_FLAG, PLAYER_FLAG | WALL_FLAG);
			stateTime = 0;
		}
	}
	
	@Override
	public void takeDamage(int dmg)
	{
		super.takeDamage(dmg);
		if (this.state != State.DEAD)
			this.state = State.FLINCH;
	}
}
