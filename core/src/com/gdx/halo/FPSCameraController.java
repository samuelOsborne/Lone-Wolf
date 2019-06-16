package com.gdx.halo;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.PointLightsAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.IntIntMap;
import com.gdx.halo.Player.Player;

import static com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags.CF_STATIC_OBJECT;
import static com.gdx.halo.Utils.RayTest.rayTest;

public class FPSCameraController extends InputAdapter {
	class MyContactListener extends ContactListener {
		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
			if (userValue0 == 3 || userValue1 == 3)
			{
				System.out.println("contact with bullet");
				attachedPlayer.damagePlayer();
			}
			return true;
		}
	}
	
	private final Camera camera;
	private final IntIntMap keys = new IntIntMap();
	private int STRAFE_LEFT = Input.Keys.A;
	private int STRAFE_RIGHT = Input.Keys.D;
	private int FORWARD = Input.Keys.W;
	private int BACKWARD = Input.Keys.S;
	private int UP = Input.Keys.Q;
	private int DOWN = Input.Keys.E;
	private int SPACE = Input.Keys.SPACE;
	private float velocity = 25f;
	private float degreesPerPixel = 0.5f;
	private final Vector3 tmp = new Vector3();
	private final Vector3 tmp2 = new Vector3();
	private final Vector3 tmp3 = new Vector3();
	private Player  attachedPlayer;
	
	/**
	 * Collider
	 */
	private GameObject      gameObject;
	private Model           wireFrameCubeModel;
	private ModelInstance   wireFrameModelInstance;
	private ModelBuilder    modelBuilder;
	private btCollisionWorld    _btCollisionWorld;
	//private MyContactListener   myContactListener;
	
	private Ray ray;
	private Ray ray2;
	private Ray ray3;
	
	private Ray fwdRay;
	private Ray fwdRay2;
	private Ray fwdRay3;
	
	private Vector3 forward = new Vector3(0, 0, 1);
	private Vector3 halfForward = new Vector3(0, 0, 0.5f);
	
	public FPSCameraController(Camera camera, btCollisionWorld btCollisionWorld, Player player) {
		this.attachedPlayer = player;
		this._btCollisionWorld = btCollisionWorld;
		//this.myContactListener = new MyContactListener();
		this.camera = camera;
		Gdx.input.setCursorCatched(true);
		modelBuilder = new ModelBuilder();
		addCollider();
		gameObject.moving = true;
		ray = new Ray(camera.position, camera.direction);
		ray2 = new Ray(camera.position, camera.direction);
		ray3 = new Ray(camera.position, camera.direction);
		
		fwdRay = new Ray(camera.position, forward);
		fwdRay2 = new Ray(camera.position, camera.direction);
		fwdRay3 = new Ray(camera.position, new Vector3(0, 0, 0.5f));
	}
	
	public Camera getCamera()
	{
		return (camera);
	}
	
	@Override
	public boolean keyDown (int keycode) {
		keys.put(keycode, keycode);
		return true;
	}
	
	@Override
	public boolean keyUp (int keycode) {
		keys.remove(keycode, 0);
		return true;
	}
	
	public void setVelocity (float velocity) {
		this.velocity = velocity;
	}
	
	public void setDegreesPerPixel (float degreesPerPixel) {
		this.degreesPerPixel = degreesPerPixel;
	}
	
//	@Override
//	public boolean mouseMoved(int screenX, int screenY)
	
	private void updateView()
	{
		float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel * 0.5f;
		float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel * 0.5f;
		
		camera.direction.rotate(camera.up, deltaX);
		Vector3 oldPitchAxis = tmp.set(camera.direction).crs(camera.up).nor();
		Vector3 newDirection = tmp2.set(camera.direction).rotate(tmp, deltaY);
		Vector3 newPitchAxis = tmp3.set(tmp2).crs(camera.up);
		if (!newPitchAxis.hasOppositeDirection(oldPitchAxis))
			camera.direction.set(newDirection);
	}
	
	public void update () {
		update(Gdx.graphics.getDeltaTime());
	}
	
	public ModelInstance    getWireFrameModelInstance()
	{
		return (wireFrameModelInstance);
	}
	
	public void update (float deltaTime) {
		if (keys.containsKey(FORWARD)) {
			fwdRay.set(camera.position, forward);
			fwdRay2.set(camera.position, camera.direction);
			fwdRay3.set(camera.position, halfForward);
			if (rayTest(_btCollisionWorld, fwdRay, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, fwdRay2, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, fwdRay3, 2.5f, (short)CF_STATIC_OBJECT) == null)
			{
				tmp.set(camera.direction).nor().scl(deltaTime * velocity);
				tmp.y = 0;
				camera.position.add(tmp);
			}
//			else
//			{
//				if (camera.direction.x > 0)
//				{
//					tmp.set(Vector3.X).nor().scl(deltaTime * velocity / 2);
//					camera.position.add(tmp);
//				}
//				else
//				{
//					tmp.set(Vector3.X).nor().scl(-deltaTime * velocity / 2);
//					camera.position.add(tmp);
//				}
//			}
		}
		if (keys.containsKey(BACKWARD)) {
			ray.set(camera.position, camera.direction);
			ray2.set(camera.position, camera.direction);
			ray3.set(camera.position, camera.direction);
			ray.direction.rotate(Vector3.Y, 180);
			ray2.direction.rotate(Vector3.Y, 135);
			ray3.direction.rotate(Vector3.Y, 225);
			if (rayTest(_btCollisionWorld, ray, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, ray2, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, ray3, 2.5f, (short)CF_STATIC_OBJECT) == null )
			{
				tmp.set(camera.direction).nor().scl(-deltaTime * velocity);
				tmp.y = 0;
				camera.position.add(tmp);
			}
		}
		if (keys.containsKey(STRAFE_LEFT)) {
			ray.set(camera.position, camera.direction);
			ray2.set(camera.position, camera.direction);
			ray.direction.rotate(Vector3.Y, 90);
			ray2.direction.rotate(Vector3.Y, 45);
			if (rayTest(_btCollisionWorld, ray, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, ray2, 2.5f, (short)CF_STATIC_OBJECT) == null)
			{
				tmp.set(camera.direction).crs(camera.up).nor().scl(-deltaTime * velocity);
				camera.position.add(tmp);
			}
		}
		if (keys.containsKey(STRAFE_RIGHT)) {
			ray.set(camera.position, camera.direction);
			ray2.set(camera.position, camera.direction);
			ray.direction.rotate(Vector3.Y, -90);
			ray2.direction.rotate(Vector3.Y, -45);
			if (rayTest(_btCollisionWorld, ray, 2.5f, (short)CF_STATIC_OBJECT) == null &&
					rayTest(_btCollisionWorld, ray2, 2.5f, (short)CF_STATIC_OBJECT) == null) {
				tmp.set(camera.direction).crs(camera.up).nor().scl(deltaTime * velocity);
				camera.position.add(tmp);
			}
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
		{
			attachedPlayer.shoot();
		}
		if (Gdx.input.isKeyPressed(Input.Keys.R))
		{
			attachedPlayer.reload();
		}
		updateView();
		updateCollider();
		camera.update(true);
	}
	
	private void addCollider() {
		modelBuilder.begin();
		modelBuilder.node().id = "player";
		modelBuilder.part("player", GL20.GL_LINES, VertexAttributes.Usage.Position |
				VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.box(1f, 1f, 1f);
		
		wireFrameCubeModel = modelBuilder.end();
		wireFrameModelInstance = new ModelInstance(wireFrameCubeModel, "player");
		wireFrameModelInstance.transform.set(camera.position, camera.combined.getRotation(new Quaternion()));
		/**
		 * Collider code
		 */
		gameObject = new GameObject.Constructor(wireFrameCubeModel, "player", new btSphereShape(3f)).construct();
		gameObject.body.setWorldTransform(gameObject.transform);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		//gameObject.body.setCollisionFlags(Halo.PLAYER_FLAG);
		gameObject.body.setContactCallbackFlag(Halo.PLAYER_FLAG);
		gameObject.body.setUserValue(Halo.PLAYER_USER_VALUE);
	}
	
	public void updateCollider()
	{
		if (wireFrameModelInstance == null)
		{
			addCollider();
			return ;
		}
		gameObject.transform.set(camera.position, gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
	}
	
	public GameObject getGameObject()
	{
		return (gameObject);
	}
	
	public Vector3 getPosition()
	{
		return (this.camera.position);
	}
}
