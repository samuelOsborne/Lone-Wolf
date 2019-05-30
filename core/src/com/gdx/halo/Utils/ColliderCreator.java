package com.gdx.halo.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class ColliderCreator {
	private static ModelBuilder    modelBuilder = new ModelBuilder();
	
	public static Model    createCollider(Decal decal, String id){
		if (decal == null)
			return (null);
		modelBuilder.begin();
		modelBuilder.node().id = id;
		modelBuilder.part(id, GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.box(5f, 5f, 1f);
		
		Model wireFrameCubeModel = modelBuilder.end();
		ModelInstance wireFrameModelInstance = new ModelInstance(wireFrameCubeModel, id);
		wireFrameModelInstance.transform.set(decal.getPosition(), decal.getRotation());
		/**
		 * Collider code
		 */
//		gameObject = new GameObject.Constructor(wireFrameCubeModel, "wall", new btBoxShape(new Vector3(2.5f, 2.5f, 0.5f))).construct();
//		gameObject.body.setUserValue(0);
//		gameObject.body.setCollisionFlags(gameObject.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
//		gameObject.transform.set(decal.getPosition(), decal.getRotation());
//		gameObject.body.setWorldTransform(gameObject.transform);
//		gameObject.body.setCollisionFlags(WALL_FLAG);
		return (wireFrameCubeModel);
	}
}
