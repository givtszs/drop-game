package com.mygdx.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.drop.common.Constants;

import java.util.Iterator;

public class DropGame extends ApplicationAdapter {
	private Texture raindropTexture;
	private Texture bucketTexture;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;
	private final int BUCKET_SIZE = 64;

	private Array<Rectangle> raindrops;
	private final int RAINDROP_SIZE = 64;

	private long lastDropTime;

	@Override
	public void create () {
		// load the textures
		raindropTexture = new Texture(Gdx.files.internal("drop.png"));
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
		batch = new SpriteBatch();

		// set up backet position on screen
		bucket = new Rectangle();
		bucket.x = Constants.WORLD_WIDTH / 2 - BUCKET_SIZE / 2; // the latter is the size of the texture, i.e 64*64
		bucket.y = 20;
		bucket.width = BUCKET_SIZE;
		bucket.height = BUCKET_SIZE;

		// init raindrops array
		raindrops = new Array<>();
		spawnRainDrop();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		// draw the bucket
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketTexture, bucket.x, bucket.y);
		batch.end();

		// respond on touch events (Android)
		if (Gdx.input.isTouched()) {
			Vector3 touchPosition = new Vector3();
			touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPosition);
			bucket.x = touchPosition.x - BUCKET_SIZE / 2;
		}

		// respond on left/right keyboard arrow clicked (Desktop)
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// prevent the bucket to flying out of the screen
		if (bucket.x < 0) bucket.x = 0;
		if (bucket.x > 800 - BUCKET_SIZE) bucket.x = 800 - BUCKET_SIZE;


		// spawn new raindrop if > 1s has passed
		if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000) spawnRainDrop();

		// remove raindrop from the array if it's out of the screen
		for (Iterator<Rectangle> iterator = raindrops.iterator(); iterator.hasNext(); ) {
			Rectangle raindrop = iterator.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + RAINDROP_SIZE < 0) iterator.remove();
			if (raindrop.overlaps(bucket)) {
				dropSound.play();
				iterator.remove();
			}
		}

		// render raindrops
		batch.begin();
		batch.draw(bucketTexture, bucket.x, bucket.y);
		for (Rectangle raindrop: raindrops) {
			batch.draw(raindropTexture, raindrop.x, raindrop.y);
		}
		batch.end();
	}

	private void spawnRainDrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 800 - 64);
		raindrop.y = Constants.WORLD_HEIGHT;
		raindrop.height = RAINDROP_SIZE;
		raindrop.width = RAINDROP_SIZE;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void dispose () {
		raindropTexture.dispose();
		bucketTexture.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
