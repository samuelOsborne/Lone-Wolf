package com.gdx.halo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class MenuManager extends Game {
	
	@Override
	public void create() {
		changeScreen(new MainMenu(this));
	}
	
	public void changeScreen(Screen newScreen){
		Screen oldScreen = getScreen();
		setScreen(newScreen);
		//Dispose the old screen to release resources
		if (oldScreen != null)
			oldScreen.dispose();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void pause() {
	
	}
	
	@Override
	public void resume() {
	
	}
	
	@Override
	public void render() {
		//Render the current Screen
		super.render();
	}
	
	@Override
	public void dispose() {
	
	}
}
