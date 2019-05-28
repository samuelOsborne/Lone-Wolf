package com.gdx.halo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Array;
import com.gdx.halo.Player.Player;

public class Halo extends ApplicationAdapter {
	class MyContactListener extends ContactListener {
		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
			if (userValue0 == 0 || userValue1 == 0)
			{
			}
			return true;
		}
	}
	
	private PerspectiveCamera camera;
	private ModelBatch modelBatch;
	
	/**
	 * Decals
	 */
	private DecalManager                    decalManager;
	
	/**
	 * Bullet physics
	 */
	private Array<GameObject> instances;
	private btCollisionWorld collisionWorld;
	private btCollisionConfiguration collisionConfig;
	private btDispatcher dispatcher;
	private btBroadphaseInterface broadphase;
	private Halo.MyContactListener contactListener;
	
	/**
	 * Bullet debugger
	 */
	private DebugDrawer debugDrawer;
	
	final static short WALL_FLAG = 1<<8;
	final static short PLAYER_FLAG = 1<<9;
	final static short ALL_FLAG = -1;
	
	
	/**
	 * Sprite test
	 */
	private Enemy enemy;
	private Player  player;
	
	private void CreateModels() {
		enemy = new Enemy();
		enemy.create(camera);
		modelBatch = new ModelBatch();
		ModelBuilder modelBuilder = new ModelBuilder();
		
		/**
		 * Decals
		 */
		decalManager = new DecalManager(camera);
		
		decalManager.AddTextureRegions("walls/stone_wall_01.png");
		decalManager.AddTextureRegions("walls/stone_wall_02.png");
		decalManager.AddTextureRegions("walls/stone_wall_03.png");
		decalManager.AddTextureRegions("walls/brain.png");
		
		Wall wall = new Wall(5 ,5, new Vector3(7.5f, 0, -2.5f),"walls/brain.png");
		wall.setRotationY(90f);
		decalManager.addWall(wall);
		collisionWorld.addCollisionObject(wall.getGameObject().body);
		instances.add(wall.getGameObject());
		
		
		Wall wallRot = new Wall(5 ,5, new Vector3(7.5f, 0, -7.5f),"walls/brain.png");
		wallRot.setRotationY(90f);
		decalManager.addWall(wallRot);
		collisionWorld.addCollisionObject(wallRot.getGameObject().body);
		instances.add(wallRot.getGameObject());
		
		
		
		Wall wall_01 = new Wall(5 ,5, new Vector3(0, 0, 0),"walls/stone_wall_03.png");
		decalManager.addWall(wall_01);
		instances.add(wall_01.getGameObject());
		collisionWorld.addCollisionObject(wall_01.getGameObject().body);
		
		Wall wall_02 = new Wall(5 ,5, new Vector3(5, 0,0),"walls/stone_wall_01.png");
		decalManager.addWall(wall_02);
		instances.add(wall_02.getGameObject());
		collisionWorld.addCollisionObject(wall_02.getGameObject().body);
		
		
		Wall wall_03 = new Wall(5 ,5, new Vector3(-5, 0,0),"walls/stone_wall_02.png");
		decalManager.addWall(wall_03);
		instances.add(wall_03.getGameObject());
		collisionWorld.addCollisionObject(wall_03.getGameObject().body);
		
		Wall wall_05 = new Wall(5 ,5, new Vector3(-10, 0,0),"walls/stone_wall_02.png");
		decalManager.addWall(wall_05);
		instances.add(wall_05.getGameObject());
		collisionWorld.addCollisionObject(wall_05.getGameObject().body);
		
		
		Wall wall_06 = new Wall(5 ,5, new Vector3(-15, 0,0),"walls/stone_wall_02.png");
		decalManager.addWall(wall_06);
		instances.add(wall_06.getGameObject());
		collisionWorld.addCollisionObject(wall_06.getGameObject().body);
	}
	
	@Override
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
		instances = new Array<GameObject>();
		contactListener = new Halo.MyContactListener();
		
		/**
		 * Player
		 */
		player = new Player(camera, collisionWorld);
		
		/**
		 * Bullet debug
		 */
		debugDrawer = new DebugDrawer();
		collisionWorld.setDebugDrawer(debugDrawer);
		debugDrawer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
		Gdx.input.setInputProcessor(player.getFpsCameraController());
		player.setVelocity(50f);
		instances.add(player.getFpsCameraController().getGameObject());
		collisionWorld.addCollisionObject(player.getFpsCameraController().getGameObject().body);
		
		/**
		 * Create the wall models
		 */
		CreateModels();
	}
	
	private void addGameObject(GameObject _gameObject) {
		instances.add(_gameObject);
		collisionWorld.addCollisionObject(_gameObject.body);
	}
	
	@Override
	public void render () {
		/**
		 * Physics
		 */
		collisionWorld.performDiscreteCollisionDetection();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		
		/**
		 * Decals
		 */
		decalManager.renderDecals();
		
		
		
		Gdx.gl20.glDepthMask(false);
		enemy.render();
		Gdx.gl20.glDepthMask(true);
		
		player.update();
		player.render();

//		debugDrawer.begin(camera);
//		collisionWorld.debugDrawWorld();
//		debugDrawer.drawLine(camera.position, camera.direction, new Vector3(155, 223, 123));
//		debugDrawer.end();
	}
	
	@Override
	public void dispose () {
		modelBatch.dispose();
		for (GameObject obj : instances)
			obj.dispose();
		instances.clear();
		
		collisionWorld.dispose();
		broadphase.dispose();
		dispatcher.dispose();
		collisionConfig.dispose();

//		wall.dispose();
//		decalBatch.dispose();
	}
	
	@Override
	public void resume () {
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
