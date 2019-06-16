package com.gdx.halo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.gdx.halo.Enemies.Elite;
import com.gdx.halo.Enemies.Enemy;
import com.gdx.halo.Enemies.EnemyManager;
import com.gdx.halo.Enemies.Grunt;
import com.gdx.halo.Player.Player;
import com.gdx.halo.Utils.MapReader;
import com.gdx.halo.Utils.UtilsContactListener;

public class Halo implements Screen {
	private PerspectiveCamera camera;
	private Game game;
	private MenuManager menuManager;
	
	/**
	 * User values
	 */
	public final static int WALL_USER_VALUE = 0;
	public final static int PLAYER_USER_VALUE = 1;
	public final static int PLASMA_USER_VALUE = 2;
	public final static int ELITE_USER_VALUE = 3;
	public final static int GRUNT_USER_VALUE = 4;
	
	/**
	 * Decals
	 */
	private DecalManager                    decalManager;
	
	/**
	 * Bullet physics
	 */
	private btCollisionWorld            collisionWorld;
	private btCollisionConfiguration    collisionConfig;
	private btDispatcher                dispatcher;
	private btBroadphaseInterface       broadphase;
	private UtilsContactListener        utilsContactListener;
	
	/**
	 * Bullet debugger
	 */
	private DebugDrawer debugDrawer;
	
	public final static short PLAYER_FLAG = 1<<9;
	public final static short WALL_FLAG = 1<<8;
	public final static short ENEMY_FLAG = 1<<7;
	public final static short UNIQUE_ENEMY_FLAG = 1<<5;
	public final static short PLASMA_FLAG = 1<<6;
	public final static short ALL_FLAG = -1;
	
	/**
	 * Sprite test
	 */
	private EnemyManager enemyManager;
	private Player  player;
	
	/**
	 * Map
	 */
	private MapReader mapReader;
	
	public Halo(MenuManager menuManager)
	{
		this.create();
		this.menuManager = menuManager;
		//menuManager.changeScreen(this);
	}
	
	private void CreateModels() {
		enemyManager = new EnemyManager(camera, collisionWorld);
		
		Grunt grunt = new Grunt(new Vector3(0, 0, 55f), this.player, collisionWorld);
		enemyManager.addEnemy(grunt);
		grunt.getGameObject().body.setContactCallbackFlag(ENEMY_FLAG);
		collisionWorld.addCollisionObject(grunt.getGameObject().body, ENEMY_FLAG);

		Elite elite = new Elite(new Vector3(-5, 0, 60f), this.player, collisionWorld);
		enemyManager.addEnemy(elite);
		elite.getGameObject().body.setContactCallbackFlag(ENEMY_FLAG);
		collisionWorld.addCollisionObject(elite.getGameObject().body, ENEMY_FLAG);
		
		Elite elite2 = new Elite(new Vector3(-15, 0, 60f), this.player, collisionWorld);
		enemyManager.addEnemy(elite2);
		elite2.getGameObject().body.setContactCallbackFlag(ENEMY_FLAG);
		collisionWorld.addCollisionObject(elite2.getGameObject().body, ENEMY_FLAG);

		Elite elite3 = new Elite(new Vector3(-25, 0, 60f), this.player, collisionWorld);
		enemyManager.addEnemy(elite3);
		elite3.getGameObject().body.setContactCallbackFlag(ENEMY_FLAG);
		collisionWorld.addCollisionObject(elite3.getGameObject().body, ENEMY_FLAG);

		
		
		/**
		 * Decals
		 */
		decalManager = new DecalManager(camera);
	}
	
	//@Override
	public void create () {
		Bullet.init();
		camera = createCam(640, 480);
		
		/**
		 * Bullet physics
		 */
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		collisionWorld = new btCollisionWorld(dispatcher, broadphase, collisionConfig);
		utilsContactListener = new UtilsContactListener(this);
		
		/**
		 * Player
		 */
		player = new Player(this, camera, collisionWorld);
		player.setVelocity(50f);
		Gdx.input.setInputProcessor(player.getFpsCameraController());
		collisionWorld.addCollisionObject(player.getFpsCameraController().getGameObject().body, PLAYER_FLAG);
		
		/**
		 * Bullet debug
		 */
		debugDrawer = new DebugDrawer();
		collisionWorld.setDebugDrawer(debugDrawer);
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
		
		/**
		 * Create map
		 */
		mapReader = new MapReader(Gdx.files.internal("Map/bigger_map.txt"));
		try {
			CreateModels();
			mapReader.createMap(decalManager, collisionWorld);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeBullet(btCollisionObject btCollisionObject)
	{
		this.enemyManager.removeBullet(btCollisionObject);
	}
	
	public void damageEnemy(int enemyIndex, int dmg)
	{
		Enemy enemy = this.enemyManager.getEnemies().get(enemyIndex);
		if (enemy != null)
			enemy.takeDamage(dmg);
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		
		/**
		 * Decals
		 */
		decalManager.renderDecals();
		
		
		/**
		 Load enemies for alpha before the player
		 */
//		Gdx.gl20.glDepthMask(false);
		enemyManager.render();
//		Gdx.gl20.glDepthMask(true);
		
		enemyManager.update();
		
		player.update();
		player.render();
		
		/**
		 * Physics
		 */
		collisionWorld.performDiscreteCollisionDetection();

//		debugDrawer.begin(camera);
//		collisionWorld.debugDrawWorld();
//		debugDrawer.end();
	}
	
	@Override
	public void dispose () {
		collisionWorld.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
	}
	
	@Override
	public void resume () {
	}
	
	@Override
	public void hide() {
	
	}
	
	@Override
	public void show() {
	
	}
	
	@Override
	public void resize (int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
		camera.update();
		player.update();
	}
	
	@Override
	public void pause () {
	}
	
	public void setEliteCollide(int userValue0, int userValue1, boolean eliteCollide)
	{
		this.enemyManager.setEliteCollide(userValue0, userValue1, eliteCollide);
	}
	
	public void setWallCollide(int userValue0, int userValue1, boolean wallCollide)
	{
		this.enemyManager.setWallCollide(userValue0, userValue1, wallCollide);
	}
	
	public Player getPlayer() { return this.player; }
	
	public void setPlayer(Player player) { this.player = player; }
	
	private PerspectiveCamera createCam(int width, int height) {
		PerspectiveCamera camera = new PerspectiveCamera(90,
				width, height);
		camera.position.set(5f, 0f, 15f);
		camera.lookAt(0,0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		return camera;
	}
}
