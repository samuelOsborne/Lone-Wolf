package com.gdx.halo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;

public class AnimatedDecal extends Decal {
	
	/** the {@link Animation} to display */
	private Animation animation;
	
	/** the current time of the {@link Animation} */
	private float time;
	
	/** if the animation is playing */
	private boolean playing = true;
	
	/** if the size of the previous frame should be kept by the following frame */
	private boolean keepSize;
	
	@Override
	protected void update() {
		update(Gdx.graphics.getDeltaTime());
		super.update();
	}
	
	public void update(float delta) {
		if(playing) {
			setTextureRegion((TextureRegion) animation.getKeyFrame(time += delta));
			if(!keepSize) {
				TextureRegion region = getTextureRegion();
				setDimensions(region.getRegionWidth(), region.getRegionHeight());
			}
		}
	}
	
	public void setTime(float time) {
		this.time = time;
	}
	
	public float getTime() {
		return time;
	}
	
	public void setPlaying(boolean playing) {
		this.playing = true;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	
	public Animation getAnimated() {
		return animation;
	}
	
	/** @param animation the {@link #animation} to set */
	public void setAnimated(Animation animation) {
		this.animation = animation;
	}
	
	/** @return the {@link #keepSize} */
	public boolean isKeepSize() {
		return keepSize;
	}
	
	/** @param keepSize the {@link #keepSize} to set */
	public void setKeepSize(boolean keepSize) {
		this.keepSize = keepSize;
	}
	
	/** @see Decal#newDecal(TextureRegion) */
	public static AnimatedDecal newAnimatedDecal(Animation animation) {
		return newAnimatedDecal(((TextureRegion) animation.getKeyFrame(0)).getRegionWidth(), ((TextureRegion) animation.getKeyFrame(0)).getRegionHeight(), animation, DecalMaterial.NO_BLEND, DecalMaterial.NO_BLEND);
	}
	
	/** @see Decal#newDecal(TextureRegion, boolean) */
	public static AnimatedDecal newDecal(Animation animation, boolean hasTransparency) {
		return newAnimatedDecal(((TextureRegion) animation.getKeyFrame(0)).getRegionWidth(), ((TextureRegion) animation.getKeyFrame(0)).getRegionHeight(), animation, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND, hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
	}
	
	/** @see Decal#newDecal(float, float, TextureRegion) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, Animation animation) {
		return newAnimatedDecal(width, height, animation, DecalMaterial.NO_BLEND, DecalMaterial.NO_BLEND);
	}
	
	/** @see Decal#newDecal(float, float, TextureRegion, boolean) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, Animation animation, boolean hasTransparency) {
		return newAnimatedDecal(width, height, animation, hasTransparency ? GL20.GL_SRC_ALPHA : DecalMaterial.NO_BLEND, hasTransparency ? GL20.GL_ONE_MINUS_SRC_ALPHA : DecalMaterial.NO_BLEND);
	}
	
	/** @see Decal#newDecal(float, float, TextureRegion, int, int) */
	public static AnimatedDecal newAnimatedDecal(float width, float height, Animation animation, int srcBlendFactor, int dstBlendFactor) {
		AnimatedDecal decal = new AnimatedDecal();
		decal.setAnimated(animation);
		decal.setTextureRegion((TextureRegion)animation.getKeyFrame(0));
		decal.setBlending(srcBlendFactor, dstBlendFactor);
		decal.dimensions.x = width;
		decal.dimensions.y = height;
		decal.setColor(1, 1, 1, 1);
		return decal;
	}
	
}