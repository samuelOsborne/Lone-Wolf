package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.gdx.halo.Enemies.Enemy;
import com.gdx.halo.Enemies.EnemyManager;
import com.gdx.halo.Player.Player;
import com.gdx.halo.Utils.MapReader;
import com.gdx.halo.Utils.UtilsContactListener;

public class Halo implements Screen {
	private PerspectiveCamera   camera;
	private MenuManager         menuManager;
	private RoundManager        roundManager;
	private Music               music;
	
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
		this.menuManager = menuManager;
		this.create();
		//menuManager.changeScreen(this);
	}
	
	private void CreateModels() {
		enemyManager = new EnemyManager(camera, collisionWorld);
		
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
		player.setPosition(new Vector3(121,0,87));
		player.setDirection(new Vector3(0.06409252f,-0.07845621f,0.9948513f));
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
		 * Music
		 */
		this.music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Music/devil_daggers.mp3"));
		this.music.setVolume(0.35f);
		this.music.isLooping();
		this.music.play();
		
		CreateModels();


		/**
		 * Create map
		 */
		mapReader = new MapReader(Gdx.files.internal("Map/bigger_map.txt"), this,
				collisionWorld,
				decalManager);
		try {
			mapReader.createMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		roundManager = new RoundManager(enemyManager, player, menuManager, this);
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
	
	public EnemyManager getEnemyManager()
	{
		return (this.enemyManager);
	}
	
	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		roundManager.update();
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
		
		roundManager.render();
		
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
		//player.dispose();
		enemyManager.emptyEnemyContainer();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();
		collisionWorld.dispose();
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
		roundManager.resize();
	}
	
	@Override
	public void pause () {
	}
	
	public DecalManager getDecalManager()
	{
		return this.decalManager;
	}
	
	public MapReader getMapReader()
	{
		return (this.mapReader);
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
