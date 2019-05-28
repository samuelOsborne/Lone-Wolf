package com.gdx.halo.Utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationLoader {
	public static TextureRegion[] loadAnimation(Texture sheet, int frame_cols, int frame_rows)
	{
		TextureRegion[][] tmp = TextureRegion.split(sheet,
				sheet.getWidth() / frame_cols,
				sheet.getHeight() / frame_rows);
		TextureRegion[] frames = new TextureRegion[frame_cols * frame_rows];
		int index = 0;
		for (int i = 0; i < frame_rows; i++) {
			for (int j = 0; j < frame_cols; j++) {
				frames[index++] = tmp[i][j];
			}
		}
		return (frames);
	}
}
