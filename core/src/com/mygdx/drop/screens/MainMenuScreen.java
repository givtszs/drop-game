package com.mygdx.drop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.drop.Drop;
import com.mygdx.drop.common.Constants;

public class MainMenuScreen implements Screen {

    private OrthographicCamera camera;
    private Stage mainMenuStage;
    private Skin playButtonSkin;
    private ImageButton playImageButton;
    private Table mainMenuTable;
    private Texture mainMenuTexture, logoTexture;
    private Image mainMenuImage, logoImage;

    public MainMenuScreen(final Drop game) {
        camera = new OrthographicCamera();

        mainMenuStage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,
                camera));

        mainMenuTexture = new Texture("backgrounds/main_menu_bg.png");
        mainMenuTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        mainMenuImage = new Image(mainMenuTexture);
        mainMenuImage.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        mainMenuImage.addAction(Actions.sequence(Actions.alpha(0.0f), Actions.fadeIn(1.0f)));

        logoTexture = new Texture("drop_logo.png");
        logoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        logoImage = new Image(logoTexture);
        logoImage.setPosition(Constants.WORLD_WIDTH / 2 - logoImage.getWidth() / 2, Constants.WORLD_HEIGHT - 200);

        playButtonSkin = new Skin(Gdx.files.internal("skins/play_button.json"),
                new TextureAtlas(Gdx.files.internal("skins/menu_buttons.atlas")));
        playImageButton = new ImageButton(playButtonSkin);
        playImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        mainMenuTable = new Table();
        mainMenuTable.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        mainMenuTable.bottom().add(playImageButton).size(200f, 200f).padBottom(20f);
        mainMenuTable.addAction(Actions.sequence(Actions.moveBy(0.0F, -250F), Actions.delay(1.0F), Actions.moveBy(0.0F, 250F, 1.0F, Interpolation.swing)));

        mainMenuStage.addActor(mainMenuImage);
        mainMenuStage.addActor(logoImage);
        mainMenuStage.addActor(mainMenuTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(mainMenuStage);
    }

    @Override
    public void render(float delta) {
//        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainMenuStage.act();
        mainMenuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainMenuStage.getViewport().update(width, height, true);
//        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        mainMenuTable.invalidateHierarchy();
        mainMenuTable.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
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
        mainMenuStage.dispose();
        mainMenuTexture.dispose();
        logoTexture.dispose();
        playButtonSkin.dispose();
    }
}
