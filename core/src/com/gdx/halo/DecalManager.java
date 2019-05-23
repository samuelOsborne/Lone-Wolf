package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class DecalManager implements Disposable {
	private Camera camera;
	
	/**
	 * Decals
	 */
	private ModelBuilder                    modelBuilder;
	private Array<ObjectInstance>           objects = new Array<ObjectInstance>();
	private DecalBatch                      decalBatch;
	private Array<TextureRegion>            textureRegions = new Array<TextureRegion>();
	
	/**
	 * Collider information
	 */
//	private btCollisionShape                groundObject;
	private ModelBatch modelBatch;

	public DecalManager(Camera _camera) {
		camera = _camera;
		modelBuilder = new ModelBuilder();
		modelBatch =  new ModelBatch();
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
	}

	public void AddTextureRegions(String filePath)
	{
		textureRegions.add(new TextureRegion(new Texture(Gdx.files.internal(filePath))));
	}
	
	public Decal getDecal(int width, int height, int textureIndex)
	{
		return (Decal.newDecal(width, height, textureRegions.get(textureIndex)));
	}
	
	public void addWall(Wall wall)
	{
		objects.add(wall);
//		instances.add(wall.getInstance());
	}
	
//	public Array<ModelInstance>     getInstances()
//	{
//		return (instances);
//	}
	
//	public Decal getDecal(int decalIndex)
//	{
//		return (decals.get(decalIndex));
//	}

	public void renderDecals()
	{
		modelBatch.begin(camera);
		for ( int i = 0; i < objects.size; i++)
		{
			ModelInstance tmp = objects.get(i).getInstance();
			
			if (tmp != null)
				modelBatch.render(tmp);
		}
//		modelBatch.render(camera.getWireFrameModelInstance());
		modelBatch.end();
		
		for ( int i = 0; i < objects.size; i++)
		{
			Decal decal = objects.get(i).getDecal();
			if (objects.get(i).getLookAt())
				decal.lookAt(camera.position, camera.up);
			decalBatch.add(decal);
		}
		decalBatch.flush();
	}

	public void addCollider(ObjectInstance objectInstance)
	{
		modelBuilder.begin();
		modelBuilder.node().id = "wall";
		modelBuilder.part("wall", GL20.GL_LINES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN)))
				.box(5f, 5f, 1f);

		Model wireframeCubeModel = modelBuilder.end();
		ModelInstance wireframeCubeInstance = new ModelInstance(wireframeCubeModel, "wall");

		Matrix4 matWireframeTr = new Matrix4();
		matWireframeTr.setToTranslation(objectInstance.getDecal().getPosition());

		Matrix4 matWireframeRot = new Matrix4();
		matWireframeRot.setToRotation(Vector3.X, objectInstance.getDecal().getRotation().getAxisAngle(Vector3.X));
		matWireframeRot.setToRotation(Vector3.Y, objectInstance.getDecal().getRotation().getAxisAngle(Vector3.Y));
		wireframeCubeInstance.transform.set(matWireframeTr.getTranslation(new Vector3()), matWireframeRot.getRotation(new Quaternion()));
		objectInstance.setModelInstance(wireframeCubeInstance);
	}

	@Override
	public void dispose() {
		decalBatch.dispose();
	}
}
