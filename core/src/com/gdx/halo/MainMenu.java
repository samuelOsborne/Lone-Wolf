package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntIntMap;

public class MainMenu extends InputAdapter implements Screen {
	private final MenuManager   game;
	
	/**
	 * Main menu music
	 */
	private final String        mainMenuMusicPath = "Sounds/Music/main_menu.mp3";
	private Sound               bgMusic;
	
	/**
	 * Sprites
	 */
	private SpriteBatch         spriteBatch;
	private Sprite              backgroundImage;
	private Sprite              logo;
	private Sprite              campaign_hi;
	private Sprite              quit_hi;
	private Sprite              campaign_low;
	private Sprite              quit_low;
	
	/**
	 * Camera & Viewport
	 */
	private OrthographicCamera  camera;
	
	/**
	 * Input
	 */
	private final IntIntMap keys = new IntIntMap();
	private int UP = Input.Keys.UP;
	private int DOWN = Input.Keys.DOWN;
	private int ENTER = Input.Keys.ENTER;
	private int pos = 0;
	
	public MainMenu(final MenuManager game) {
		this.camera = createCam(640, 480);
		this.game = game;
		this.initSprites();
		this.spriteBatch = new SpriteBatch();
		Gdx.input.setInputProcessor(this);
		this.bgMusic = Gdx.audio.newSound(Gdx.files.internal(mainMenuMusicPath));
		this.bgMusic.loop();
		this.bgMusic.play();
	}
	
	private void initSprites()
	{
		this.backgroundImage = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/menu_background.jpg")),
				0, 0,1920, 1080));
		
		this.logo = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/halo_logo.png")),
				0, 0,795, 110));
		
		this.logo.setSize(370, 70);
		
		this.campaign_hi = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/campaign_hi.png")),
				0, 0,309, 36));
		this.campaign_hi.setSize(250, 25);
		
		this.quit_hi = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/quit_hi.png")),
				0, 0,138, 34));
		this.quit_hi.setSize(100, 30);
		
		this.campaign_low = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/campaign_low.png")),
				0, 0,309, 36));
		this.campaign_low.setSize(250, 25);
		
		this.quit_low = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("Menu/quit_low.png")),
				0, 0,138, 34));
		this.quit_low.setSize(100, 30);
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		camera.update();
		
		if (keys.containsKey(UP))
			if (pos == 1)
				pos--;
		if (keys.containsKey(DOWN))
			if (pos == 0)
				pos++;
		if (keys.containsKey(ENTER))
		{
			if (pos == 0)
			{
				//bgMusic.pause();
				game.changeScreen(new Halo(game));
			}
			if (pos == 1)
				Gdx.app.exit();
		}
		spriteBatch.begin();
		spriteBatch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.draw(logo, (float)Gdx.graphics.getWidth() / 2 - logo.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 50,
				this.logo.getWidth(), this.logo.getHeight());
		if (pos == 0)
		{
			spriteBatch.draw(campaign_hi, (float)Gdx.graphics.getWidth() / 2 - this.campaign_hi.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 30,
					this.campaign_hi.getWidth(), this.campaign_hi.getHeight());
			spriteBatch.draw(quit_low, (float)Gdx.graphics.getWidth() / 2 - this.quit_low.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 80,
					this.quit_low.getWidth(), this.quit_low.getHeight());
		}
		if (pos == 1)
		{
			spriteBatch.draw(campaign_low, (float)Gdx.graphics.getWidth() / 2 - this.campaign_low.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 30,
					this.campaign_low.getWidth(), this.campaign_low.getHeight());
			spriteBatch.draw(quit_hi, (float)Gdx.graphics.getWidth() / 2 - this.quit_hi.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 80,
					this.quit_hi.getWidth(), this.quit_hi.getHeight());
		}
		spriteBatch.end();
	}
	
	private OrthographicCamera createCam(int width, int height) {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		return camera;
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		spriteBatch.setProjectionMatrix(camera.combined);
		camera.update();
	}
	
	@Override
	public void pause() {
	
	}
	
	@Override
	public void resume() {
	
	}
	
	@Override
	public void hide() {
	
	}
	
	@Override
	public void dispose() {
		bgMusic.dispose();
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
}
