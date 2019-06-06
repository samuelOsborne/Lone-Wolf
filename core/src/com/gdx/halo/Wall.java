package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Utils.ColliderCreator;

import static com.gdx.halo.Halo.WALL_FLAG;

/**
 * Create an interface and use it in decal manager to get the model
 */

public class Wall implements Disposable, ObjectInstance {
	private Decal           decal;
	private Vector3         position;
	private Vector3         rotation;
	
	/**
	 * Bullet
	 */
	private GameObject      gameObject;
	private Model           model;
	
	public Wall(float width, float height, Vector3 _position, String _texturePath) {
		position = new Vector3();
		rotation = new Vector3();
		
		TextureRegion   textureRegion = new TextureRegion(new Texture(Gdx.files.internal(_texturePath)));
		
		decal = Decal.newDecal(width, height, textureRegion);
		position = _position;
		decal.setPosition(position);
		addCollider();
	}
	
	public Wall(float width, float height, Vector3 _position, Decal _decal) {
		position = new Vector3();
		rotation = new Vector3();
		decal = _decal;
		decal.setPosition(position);
		addCollider();
	}
	
	@Override
	public ModelInstance getInstance() {
		return null;
	}
	
	@Override
	public void setModelInstance(ModelInstance _modelInstance) {
	
	}
	
	public void     setTransform(Vector3 _position)
	{
		position = _position;
		this.decal.setPosition(_position);
		updateCollider();
	}
	
	public void     setRotationX(float angle)
	{
		rotation.x = angle;
		this.decal.setRotationX(angle);
		this.updateCollider();
	}
	
	public boolean  getLookAt()
	{
		return (false);
	}
	
	public void     setRotationY(float angle)
	{
		rotation.y = angle;
		this.decal.setRotationY(angle);
		this.updateCollider();
	}
	
	public void     setRotationZ(float angle)
	{
		rotation.z = angle;
		this.decal.setRotationZ(angle);
		this.updateCollider();
	}
	
	public Decal    getDecal()
	{
		return (decal);
	}
	
	public void     setDecal(Decal _decal)
	{
		decal = _decal;
	}
	
//	class WallColliderListener extends ContactListener {
//		@Override
//		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
//			System.out.println("contact");
//			return true;
//		}
//	}
	
	public GameObject   getGameObject() { return (gameObject); }
	
	public void addCollider()
	{
		if (decal == null)
			return ;
		/**
		 * Collider code
		 */
		model = ColliderCreator.createCollider(this.decal, "wall");
		gameObject = new GameObject.Constructor(model, "wall", new btBoxShape(new Vector3(2.5f, 2.5f, 0.5f))).construct();
		gameObject.body.setUserValue(Halo.WALL_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		gameObject.transform.set(decal.getPosition(), decal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
		gameObject.body.setCollisionFlags(WALL_FLAG);
	}
	
	public void updateCollider()
	{
 		if (model == null)
		{
			addCollider();
			return ;
		}
		gameObject.transform.set(decal.getPosition(), decal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
	}
	
	public Model getModel(){ return (model); }
	
	@Override
	public void dispose() {
	}
	
}
