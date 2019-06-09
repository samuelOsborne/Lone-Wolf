package com.gdx.halo.Utils;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.*;

public class RayTest {
	private static final Vector3 rayFrom = new Vector3();
	private static final Vector3 rayTo = new Vector3();
	private static final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);
	
	public static btCollisionObject rayTest(btCollisionWorld collisionWorld, Ray ray, float distance, short flagToCheck) {
		rayFrom.set(ray.origin);
		rayTo.set(ray.direction).scl(distance).add(rayFrom);
		
		callback.setCollisionObject(null);
		callback.setClosestHitFraction(1f);
		Vector3 rayFromWorld = new Vector3();
		rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
		Vector3 rayToWorld   = new Vector3();
		rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);
		collisionWorld.rayTest(rayFrom, rayTo, callback);
		if (callback.hasHit()) {
			if (callback.getCollisionObject().getCollisionFlags() == flagToCheck)
				return callback.getCollisionObject();
		}
		return null;
	}
	
	public static btCollisionObject rayTest(btCollisionWorld collisionWorld, Ray ray, float distance, int flagToCheck) {
		rayFrom.set(ray.origin);
		rayTo.set(ray.direction).scl(distance).add(rayFrom);
		
		callback.setCollisionObject(null);
		callback.setClosestHitFraction(1f);
		Vector3 rayFromWorld = new Vector3();
		rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
		Vector3 rayToWorld   = new Vector3();
		rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);
		collisionWorld.rayTest(rayFrom, rayTo, callback);
		if (callback.hasHit()) {
			if (callback.getCollisionObject().getCollisionFlags() == flagToCheck)
				return callback.getCollisionObject();
		}
		return null;
	}
}
