package com.gdx.halo.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.gdx.halo.Weapons.Pistol;

public class PlayerHUD implements Disposable {
	private static final int FRAME_COLS = 3, FRAME_ROWS = 1;
	
	private SpriteBatch spriteBatch;
	private Array<Sprite>  textures;
	private Sprite sprite;
	private Camera camera;
	private Matrix4 viewMatrix;
	
	
	/*
		Sprite animation
	 */
	Animation<TextureRegion>  pistolAnimation;
	float stateTime;
	
	private Pistol pistol;
	
	public PlayerHUD(Camera _camera) {
		this.pistol = new Pistol();
//		this.camera = _camera;
//		this.spriteBatch = new SpriteBatch();
//		this.textures = new Array<Sprite>();
		
		//Texture tmp = new Texture(Gdx.files.internal("HUD/pistol_5.png"));
		//sprite = new Sprite(tmp, 0, 0, 64, 64);
		//sprite.setPosition(Gdx.graphics.getWidth() / 2, 60);
		//sprite.setScale(3f);
		
		
		/**
		 * Animated sprite
		 */
//		Texture pistolAnimationSheet = new Texture(Gdx.files.internal("Animations/Weapons/Shotgun/shtg_animation_flash.png"));
//		TextureRegion[][] tmpTextures = TextureRegion.split(pistolAnimationSheet,
//				pistolAnimationSheet.getWidth() / FRAME_COLS,
//				pistolAnimationSheet.getHeight() / FRAME_ROWS);
//		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
//		int index = 0;
//		for (int i = 0; i < FRAME_ROWS; i++) {
//			for (int j = 0; j < FRAME_COLS; j++) {
//				walkFrames[index++] = tmpTextures[i][j];
//			}
//		}
//		pistolAnimation = new Animation<TextureRegion>(0.13f, walkFrames);
//		pistolAnimation.setPlayMode(Animation.PlayMode.LOOP);
//		stateTime = 0;
	}
	
	public void render() {
//		spriteBatch.setTransformMatrix(camera.view);
//		stateTime += Gdx.graphics.getDeltaTime();
//
//		TextureRegion currentFrame = pistolAnimation.getKeyFrame(stateTime, true);
//		spriteBatch.begin();
//		spriteBatch.draw(currentFrame, Gdx.graphics.getWidth() / 2, 0, 300f, 300f); // Draw current frame at (50, 50)
//		spriteBatch.end();
		
		pistol.render();


//		spriteBatch.begin();
//		sprite.draw(spriteBatch);
//		spriteBatch.end();
	}
	
	public void shoot() {
		this.pistol.fire();
	}
	
	public void reload() {
		this.pistol.reload();
	}
	
	public void update()
	{
		pistol.update();
//		viewMatrix = new Matrix4();
//		viewMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//		spriteBatch.setProjectionMatrix(viewMatrix);
//		sprite.setPosition(Gdx.graphics.getWidth() / 2, 60);
//		sprite.setScale(3f);
	}
	
	@Override
	public void dispose() {
	
	}
}
