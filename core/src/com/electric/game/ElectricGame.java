package com.electric.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.electric.game.Screens.MainScreen;

public class ElectricGame extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short ELECTRIC_BIT = 2;
	public static final short PARALLEL_BIT = 4;
	public static final short SVARSHIK_PLACE_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ROBOT_BIT = 64;
	public static final short SVARSHIK_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short MARIO_HEAD_BIT = 512;
	public static final short LESTNITSA_BIT = 1024;
	public static final short STORY_BIT = 2048;
	public static final short END_MAP_BIT = 4096;


	public SpriteBatch batch;

	public static AssetManager manager;


	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/Komiku_-_32_-_Love_Planet(chosic.com).mp3", Music.class);
		manager.finishLoading();
		setScreen(new MainScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render () {
		super.render();
	}
}
