package com.mygdx.drop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.drop.Drop;
import com.mygdx.drop.common.Constants;

import java.util.Iterator;

public class GameScreen implements Screen {

    private Drop game;

    private Texture raindropTexture, bucketTexture, backgroundTexture;
    private Sound dropSound;
    private Music rainMusic;
    private OrthographicCamera camera;

    private Image bucket, backgroundImage;
    private Label dropsCollected;
    private Skin pauseButtonSkin;
    private ImageButton pauseImageButton;
    private float BUCKET_SIZE = 64f;

    private Array<Image> raindropActors;
    private float RAINDROP_SIZE = 64f;

    private long lastDropTime;
    private String dropsCollectedText = "Drops collected: ";
    int dropsGathered;

    private Stage gameStage;

    public GameScreen(Drop game) {
        this.game = game;


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        gameStage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));

        raindropTexture = new Texture("drop.png");
        raindropTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        bucketTexture = new Texture("bucket.png");
        bucketTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        backgroundTexture = new Texture("backgrounds/game_bg_tint.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        pauseButtonSkin = new Skin(Gdx.files.internal("skins/pause_button.json"),
                new TextureAtlas(Gdx.files.internal("skins/menu_buttons.atlas")));
        pauseImageButton = new ImageButton(pauseButtonSkin);
        pauseImageButton.setSize(80, 80);
        pauseImageButton.setPosition(Constants.WORLD_WIDTH - pauseImageButton.getWidth() - 20,
                Constants.WORLD_HEIGHT - pauseImageButton.getHeight() - 20);
        pauseImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen.this.game.setScreen(new PauseScreen(GameScreen.this.game, GameScreen.this));
            }
        });

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.font, Color.WHITE);
        dropsCollected = new Label(dropsCollectedText + dropsGathered, labelStyle);
        dropsCollected.setPosition(10, Constants.WORLD_HEIGHT - 20);

        dropSound = Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        // Create the bucket actor
        bucket = new Image(bucketTexture);
        bucket.setSize(BUCKET_SIZE, BUCKET_SIZE);
        bucket.setPosition(Constants.WORLD_WIDTH / 2 - BUCKET_SIZE / 2, 20);
        bucket.setTouchable(Touchable.enabled);
        bucket.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                Gdx.app.log("GameScreen", "touchDragged: x: " + x + "; norm: " + (x - BUCKET_SIZE / 2));
                bucket.moveBy(x - BUCKET_SIZE / 2, 0);
            }
        });

        // Add actors to the stage
        gameStage.addActor(backgroundImage);
        gameStage.addActor(bucket);
        gameStage.addActor(dropsCollected);

        raindropActors = new Array<>();
        spawnRainDrop();

        gameStage.addActor(pauseImageButton);
    }

    private void spawnRainDrop() {
        Gdx.app.log("GameScreen", "spawnRaindrop is called");
        Image raindrop = new Image(raindropTexture);
        raindrop.setSize(RAINDROP_SIZE, RAINDROP_SIZE);
        raindrop.setPosition(MathUtils.random(0, 800 - RAINDROP_SIZE), Constants.WORLD_HEIGHT);

        gameStage.addActor(raindrop);
        raindropActors.add(raindrop);

        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameStage);
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        // prevent the bucket from going off the screen
        if (bucket.getX() < 0) {
            bucket.setX(0);
        }

        if (bucket.getX() > 800 - BUCKET_SIZE) {
            bucket.setX(800 - BUCKET_SIZE);
        }

//        float bucketX = MathUtils.clamp(bucket.getX(), 0, 800 - BUCKET_SIZE);
//        bucket.setPosition(bucketX, 20);

        // Spawn new raindrop if > 1s has passed
        if (TimeUtils.nanoTime() - lastDropTime > 1_000_000_000) {
            spawnRainDrop();
        }

        // Handle collision and remove raindrop actors
        Iterator<Image> iterator = raindropActors.iterator();
        Rectangle bucketBounds = new Rectangle(bucket.getX(), bucket.getY(), bucket.getWidth(), bucket.getHeight());
        while (iterator.hasNext()) {
            Image raindrop = iterator.next();
            Rectangle raindropBounds = new Rectangle(raindrop.getX(), raindrop.getY(), raindrop.getWidth(), raindrop.getHeight());

            raindrop.moveBy(0, -200 * Gdx.graphics.getDeltaTime());

            if (raindrop.getY() + RAINDROP_SIZE < 0) {
                iterator.remove();
                gameStage.getActors().removeValue(raindrop, true);
            }

            if (bucketBounds.overlaps(raindropBounds)) {
                dropSound.play();
                dropsGathered++;
                iterator.remove();
                gameStage.getActors().removeValue(raindrop, true);
            }
        }

        dropsCollected.setText(dropsCollectedText + dropsGathered);

        gameStage.act(delta);
        gameStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        Gdx.input.setInputProcessor(null);
        rainMusic.pause();
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "dispose is called");
        bucketTexture.dispose();
        raindropTexture.dispose();
        backgroundTexture.dispose();
        dropSound.dispose();
        rainMusic.dispose();
        gameStage.dispose();
    }
}
