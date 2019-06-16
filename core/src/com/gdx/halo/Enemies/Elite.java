package com.gdx.halo.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
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
import static com.gdx.halo.Halo.*;

public class Elite extends Enemy {
	private static final int FRAME_COLS = 2, FRAME_ROWS = 1;
	private Array<PlasmaBullet> plasmaProjectiles;
	private Player              player;
	private btCollisionWorld    collisionWorld;
	private float               deathTimer = 0f;
	private boolean             flinching = false;
	private float               resetAnimationTimer;
	
	/**
	 * Movement
	 */
	private Vector3             directionToPlayer;
	private float               velocity;
	private Vector3             direction;
	
	/**
	 * Sounds
	 */
	private Array<Sound>        hitSounds;
	private Array<Sound>        deathSounds;
	private boolean             playingSound = false;
	
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
		this.state = State.WALKING;
		this.scaleX = 3f;
		this.scaleY = 5f;
		this.health = 100;
		this.position = position;
		this.initDeathAnimation();
		this.initFireAnimation();
		this.initWalkAnimation();
		this.initFlinchDecal();
		this.initCollider();
		this.hitSounds = new Array<Sound>();
		this.deathSounds = new Array<Sound>();
		this.initSounds();
		
		this.velocity = 0.01f;
		this.direction = new Vector3(player.getFpsCameraController().getPosition());
		this.directionToPlayer = this.direction.sub(this.currentDecal().getPosition().nor());
	}
	
	private void initSounds()
	{
		String[] paths = {"Sounds/Enemies/Elite/Flinch/berserk.1.ogg",
				"Sounds/Enemies/Elite/Flinch/berserk.4.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.1.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.2.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.3.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.4.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.5.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemy.6.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemymelee.3.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtenemymelee.4.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtfriend.1.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtfriend.3.ogg",
				"Sounds/Enemies/Elite/Flinch/hurtfriend.7.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.oof01.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.oof02.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.oof_a.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short01.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short02.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short03.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short_a.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short_b.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short_c.ogg",
				"Sounds/Enemies/Elite/Flinch/painminor.short_d.ogg"};
		
		String[] deathPaths = {
				"Sounds/Enemies/Elite/Death/deathflying.die01.ogg",
				"Sounds/Enemies/Elite/Death/deathflying.die02.ogg",
				"Sounds/Enemies/Elite/Death/deathquiet.short01.ogg",
				"Sounds/Enemies/Elite/Death/deathquiet.short02.ogg",
				"Sounds/Enemies/Elite/Death/deathquiet.short03.ogg",
				"Sounds/Enemies/Elite/Death/deathquiet.short_a.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die01.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die02.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die04.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die08.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die09.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die10.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.die11.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long01.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long02.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long03.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long04.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long_a.ogg",
				"Sounds/Enemies/Elite/Death/deathviolent.long_b.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.die04.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.die05.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.die06.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.huh_a.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.huh_b.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.oof01.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.oof02.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.short01.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.short02.ogg",
				"Sounds/Enemies/Elite/Death/painfalling.short03.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die03.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die05.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die06.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die07.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die08.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die09.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die10.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die11.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die12.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die13.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die_a.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.die_b.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.oof01.ogg",
				"Sounds/Enemies/Elite/Death/painmajor.oof02.ogg",
				"Sounds/Enemies/Elite/Death/painminor.die03.ogg",
				"Sounds/Enemies/Elite/Death/painminor.die05.ogg",
				"Sounds/Enemies/Elite/Death/painminor.die06.ogg",
				"Sounds/Enemies/Elite/Death/painminor.die07.ogg",
				"Sounds/Enemies/Elite/Death/painminor.die08.ogg",
				"Sounds/Enemies/Elite/Death/painminor.huh_a.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die01.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die02.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die06.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die07.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die08.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die11.ogg",
				"Sounds/Enemies/Elite/Death/screampain.die12.ogg",
				"Sounds/Enemies/Elite/Death/screampain.long01.ogg",
				"Sounds/Enemies/Elite/Death/screampain.long02.ogg",
				"Sounds/Enemies/Elite/Death/screampain.long_a.ogg",
				"Sounds/Enemies/Elite/Death/screampain.long_b.ogg"};
		
		for (int i = 0; i < 23; i++)
		{
			this.hitSounds.add(Gdx.audio.newSound(Gdx.files.internal(paths[i])));
		}
		for (int i = 0; i < 60; i++)
		{
			this.deathSounds.add(Gdx.audio.newSound(Gdx.files.internal(deathPaths[i])));
		}
	}
	
	
	@Override
	public void update(){
		this.stateTime += Gdx.graphics.getDeltaTime();
		//destroy if 0
		if (this.health <= 0) {
			this.state = State.DEAD;
			this.deathTimer += Gdx.graphics.getDeltaTime();
			collisionWorld.removeCollisionObject(this.gameObject.body);
			if (deathTimer >= 5)
				remove = true;
			if (!playingSound)
			{
				this.deathSounds.get((int )(Math.random() * 60)).play();
				playingSound = true;
			}
			if (deathTimer >= 5)
				remove = true;
		}
		else {
			//After x amount of seconds fire again
			if (state == State.FIRING)
				this.shoot();
			this.move();
		}
		
		if (flinching && state != State.DEAD)
		{
			resetAnimationTimer += Gdx.graphics.getDeltaTime();
			if (!playingSound)
			{
				this.hitSounds.get((int )(Math.random() * 23)).play();
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
		Texture shootSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/shooting.png"));
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(shootSheet, 2, 1);
		fireAnimation = new Animation<TextureRegion>(0.70f, walkFrames);
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
	public void initFlinchDecal() {
		this.flinchDecal = Decal.newDecal(1f, 1f, new TextureRegion(new Texture(Gdx.files.internal("Animations/Enemies/Elite/flinch.png"))));
		this.flinchDecal.setScaleX(scaleX);
		this.flinchDecal.setScaleY(scaleY);
		this.flinchDecal.setPosition(this.position);
		this.flinchDecal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		stateTime = 0f;
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
		//gameObject.body.setCollisionFlags(CF_CHARACTER_OBJECT);
		gameObject.transform.set(this.firingDecal.getPosition(), this.gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
		//collisionWorld.addCollisionObject(this.gameObject.body, ENEMY_FLAG);
	}
	
	@Override
	public void updateCollider() {
		this.gameObject.transform.set(this.currentDecal().getPosition(), this.currentDecal().getRotation());
		this.gameObject.body.setWorldTransform(this.gameObject.transform);
	}
	
	@Override
	public void move() {
		if (this.getEliteCollide())
		{
			this.eliteCollide = false;
			return ;
		}
		if (this.getWallCollide())
		{
			this.position.z -= 1f;
			this.wallCollide = false;
			return ;
		}
		int xOffset = ((int )(Math.random() * 10));
		int zOffset = ((int )(Math.random() * 10));
		
		this.directionToPlayer = new Vector3(player.getFpsCameraController().getPosition());
		
		double destX = (this.directionToPlayer.x + xOffset) - this.currentDecal().getPosition().x;
		double destZ = (this.directionToPlayer.z + zOffset) - this.currentDecal().getPosition().z;
		
		double dist = Math.sqrt(destX * destX + destZ * destZ);
//		destX = destX / dist;
//		destZ = destZ / dist;
		
		double travelX = destX * velocity;
		double travelZ = destZ * velocity;
		
		double distTravel = Math.sqrt(travelX * travelX + travelZ * travelZ);
		
		if ( distTravel > dist)
		{
			this.currentDecal().setPosition((float)destX, this.currentDecal().getY(), (float)destZ);
		}
		else
		{
			double tmpX = this.currentDecal().getX() + travelX;
			double tmpZ = this.currentDecal().getZ() + travelZ;
			this.currentDecal().setPosition((float)tmpX, this.currentDecal().getY(), (float)tmpZ);
			this.position = this.currentDecal().getPosition();
		}
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
			PlasmaBullet plasmaBullet = new PlasmaBullet(position,
					this.gameObject.transform.getRotation(new Quaternion()),
					new Vector3(player.getFpsCameraController().getPosition()));
			this.plasmaProjectiles.add(plasmaBullet);
			this.collisionWorld.addCollisionObject(plasmaBullet.getGameObject().body, PLASMA_FLAG,
					PLAYER_FLAG | WALL_FLAG);
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
