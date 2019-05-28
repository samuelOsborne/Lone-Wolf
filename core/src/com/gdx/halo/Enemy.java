package com.gdx.halo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Utils.AnimationLoader;

public class Enemy implements Disposable {
	private static final int FRAME_COLS = 2, FRAME_ROWS = 1;
	// Objects used
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	DecalBatch decalBatch;
	AnimatedDecal animatedDecal;
	Camera _camera;
	
	float stateTime;
	
	public void create(Camera camera) {
		_camera = camera;
		walkSheet = new Texture(Gdx.files.internal("Animations/Enemies/Elite/walking.png"));
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		
		TextureRegion[] walkFrames = AnimationLoader.loadAnimation(walkSheet, FRAME_COLS, FRAME_ROWS);
		
		walkAnimation = new Animation<TextureRegion>(0.5f, walkFrames);
		walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		animatedDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, walkAnimation, true);
		animatedDecal.setKeepSize(true);
		animatedDecal.setScaleX(3f);
		animatedDecal.setScaleY(5f);
		animatedDecal.setPlaying(true);
		animatedDecal.setPosition(0, 0, 10f);
		stateTime = 0f;
	}
	
	public void render() {
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		animatedDecal.lookAt(_camera.position, _camera.up);
		decalBatch.add(animatedDecal);
		decalBatch.flush();
	}
	
	@Override
	public void dispose() { // SpriteBatches and Textures must always be disposed
		walkSheet.dispose();
	}
}
