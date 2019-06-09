package com.gdx.halo.Weapons.Alien;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.gdx.halo.GameObject;
import com.gdx.halo.Halo;
import com.gdx.halo.Utils.ColliderCreator;
import com.gdx.halo.Weapons.AProjectile;

public class PlasmaBullet extends AProjectile {
	class MyContactListener extends ContactListener {
		@Override
		public boolean onContactAdded (int userValue0, int partId0, int index0, int userValue1, int partId1, int index1) {
			if (userValue0 == 1 && userValue1 == 2)
			{
				collidedWithPlayer = true;
				remove = true;
			}
			else if (userValue0 == Halo.WALL_USER_VALUE && userValue1 == Halo.PLASMA_USER_VALUE)
			{
				remove = true;
			}
			return true;
		}
	}
	
	public static String bulletPath = "Animations/Weapons/Plasma_Rifle/pp_4.png";
	private static float velocity = .9f;
	public boolean collidedWithPlayer = false;
	private MyContactListener myContactListener;
	
	public PlasmaBullet(Vector3 startPos, Quaternion rotation, Vector3 direction) {
		super(new Texture(bulletPath), velocity, startPos, direction);
		this.myContactListener = new MyContactListener();
		this.projectileDecal.setScale(0.2f, 0.2f);
		this.projectileDecal.setRotation(rotation);
	}
	
	@Override
	public void initCollider() {
		model = ColliderCreator.createCollider(this.projectileDecal, "plasma_bullet");
		gameObject = new GameObject.Constructor(model, "plasma_bullet",
				new btBoxShape(new Vector3(1f, 1f, 1f))).construct();
		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		gameObject.transform.set(this.projectileDecal.getPosition(), this.gameObject.transform.getRotation(new Quaternion()));
		gameObject.body.setWorldTransform(gameObject.transform);
		gameObject.body.setUserValue(Halo.PLASMA_USER_VALUE);
	}
	
	@Override
	public void updateCollider() {
		this.gameObject.transform.set(this.projectileDecal.getPosition(), this.projectileDecal.getRotation());
		this.gameObject.body.setWorldTransform(this.gameObject.transform);
	}
}
