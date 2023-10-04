package com.mygdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.drop.common.Constants;

public class DropGame extends ApplicationAdapter {
	private Texture dropTexture;
	private Texture bucketTexture;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;
	private final int BUCKET_SIZE = 64;

	@Override
	public void create () {
		// load the textures
		dropTexture = new Texture(Gdx.files.internal("drop.png"));
		bucketTexture = new Texture(Gdx.files.internal("bucket.png"));

		// load sounds
		dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music
		rainMusic.setLooping(true);
		rainMusic.play();

		// set up camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// set up backet position on screen
		bucket = new Rectangle();
		bucket.x = Constants.SCREEN_WIDTH / 2 - BUCKET_SIZE / 2; // the latter is the size of the texture, i.e 64*64
		bucket.y = 20;
		bucket.width = BUCKET_SIZE;
		bucket.height = BUCKET_SIZE;
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
	}
	
	@Override
	public void dispose () {
	}
}
