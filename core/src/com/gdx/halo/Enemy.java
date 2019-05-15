package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;

public class Enemy {
	private static final int FRAME_COLS = 6, FRAME_ROWS = 5;
	
	// Objects used
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;
	
	DecalBatch decalBatch;
	AnimatedDecal animatedDecal;
	
	Camera _camera;
	
	
	// A variable for tracking elapsed time for the animation
	float stateTime;
	
	public void create(Camera camera) {
		_camera = camera;
		// Load the sprite sheet as a Texture
		walkSheet = new Texture(Gdx.files.internal("sprite-animation4.png"));
		
		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		
		
		// Use the split utility method to create a 2D array of TextureRegions. This is
		// possible because this sprite sheet contains frames of equal size and they are
		// all aligned.
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);
		
		// Place the regions into a 1D array in the correct order, starting from the top
		// left, going across first. The Animation constructor requires a 1D array.
		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		
		// Initialize the Animation with the frame interval and array of frames
		walkAnimation = new Animation<TextureRegion>(0.025f, walkFrames);
		
		// Instantiate a SpriteBatch for drawing and reset the elapsed animation
		// time to 0
		spriteBatch = new SpriteBatch();
		
		
		//Init the animated decal
		walkAnimation.setPlayMode(Animation.PlayMode.LOOP);
		animatedDecal = AnimatedDecal.newAnimatedDecal(1f, 1f, walkAnimation, true);
		animatedDecal.setKeepSize(true);
		animatedDecal.setScaleX(3f);
		animatedDecal.setScaleY(3f);
		animatedDecal.setPlaying(true);
		animatedDecal.setPosition(0, 0, 10f);
		
		
		stateTime = 0f;
	}
	
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
		stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
		
		// Get current frame of animation for the current stateTime
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, 50, 50); // Draw current frame at (50, 50)
		spriteBatch.end();
		
		animatedDecal.lookAt(_camera.position, _camera.up);
		decalBatch.add(animatedDecal);
		decalBatch.flush();
	}
	
	public void dispose() { // SpriteBatches and Textures must always be disposed
		spriteBatch.dispose();
		walkSheet.dispose();
	}
}
