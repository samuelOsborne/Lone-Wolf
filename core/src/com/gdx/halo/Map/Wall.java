package com.gdx.halo.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.GameObject;
import com.gdx.halo.Halo;
import com.gdx.halo.ObjectInstance;
import com.gdx.halo.Utils.ColliderCreator;

import static com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags.CF_STATIC_OBJECT;

/**
 * Create an interface and use it in decal manager to get the model
 */

public class Wall implements Disposable, ObjectInstance {
	private Decal           frontDecal;
	private Decal           leftDecal;
	private Decal           rightDecal;
	private Decal           backDecal;
	private Array<Decal>    wallDecals;
	private Vector3         position;
	private Vector3         rotation;
	
	/**
	 * Bullet
	 */
	private GameObject gameObject;
	private Model           model;
	
	public Wall(float width, float height, Vector3 _position, String _texturePath) {
		wallDecals = new Array<Decal>(4);
		position = new Vector3();
		rotation = new Vector3();
		
		TextureRegion   textureRegion = new TextureRegion(new Texture(Gdx.files.internal(_texturePath)));
		
		frontDecal = Decal.newDecal(width, height, textureRegion);
		position = new Vector3(_position);
		frontDecal.setPosition(position);
		wallDecals.add(frontDecal);
		
		backDecal = Decal.newDecal(width, height, textureRegion);
		position = new Vector3(_position);
		position.z -= 5f;
		backDecal.setPosition(position);
		wallDecals.add(backDecal);
		
		leftDecal = Decal.newDecal(width, height, textureRegion);
		position = new Vector3(_position);
		position.x -= 2.5f;
		position.z -= 2.5f;
		leftDecal.setPosition(position);
		leftDecal.setRotationY(90f);
		wallDecals.add(leftDecal);

		rightDecal = Decal.newDecal(width, height, textureRegion);
		position = new Vector3(_position);
		position.x += 2.5f;
		position.z -= 2.5f;
		rightDecal.setPosition(position);
		rightDecal.setRotationY(90f);
		wallDecals.add(rightDecal);
		
		addCollider();
	}
	
	public Wall(float width, float height, Vector3 _position, Decal _decal) {
		position = new Vector3();
		rotation = new Vector3();
		frontDecal = _decal;
		frontDecal.setPosition(position);
		addCollider();
	}
	
	public void render(DecalBatch decalBatch)
	{
		for (Decal decal : wallDecals)
			decalBatch.add(decal);
	}

	public void     setTransform(Vector3 _position)
	{
		position = _position;
		this.frontDecal.setPosition(_position);
		updateCollider();
	}
	
	public void     setRotationX(float angle)
	{
		rotation.x = angle;
		this.frontDecal.setRotationX(angle);
		this.updateCollider();
	}
	
	public boolean  getLookAt()
	{
		return (false);
	}
	
	public Decal    getDecal()
	{
		return (frontDecal);
	}
	
	public void     setDecal(Decal _decal)
	{
		frontDecal = _decal;
	}
	
	public GameObject   getGameObject() { return (gameObject); }
	
	public void addCollider()
	{
		if (frontDecal == null)
			return ;
		/**
		 * Collider code
		 */
		model = ColliderCreator.createCollider(this.frontDecal, "wall");
		gameObject = new GameObject.Constructor(model, "wall", new btBoxShape(new Vector3(2.5f, 2.5f, 2.5f))).construct();
		gameObject.body.setUserValue(Halo.WALL_USER_VALUE);
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags());
		Vector3 colliderPos = new Vector3(frontDecal.getPosition());
		colliderPos.z -= 2.5;
		gameObject.transform.set(colliderPos, frontDecal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
		gameObject.body.setCollisionFlags(CF_STATIC_OBJECT);
	}
	
	public void updateCollider()
	{
 		if (model == null)
		{
			addCollider();
			return ;
		}
		gameObject.transform.set(frontDecal.getPosition(), frontDecal.getRotation());
		gameObject.body.setWorldTransform(gameObject.transform);
	}
	
	public Model getModel(){ return (model); }
	
	@Override
	public void dispose() {
	}
	
}
