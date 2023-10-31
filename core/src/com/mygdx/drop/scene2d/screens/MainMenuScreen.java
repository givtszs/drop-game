package com.mygdx.drop.scene2d.screens;

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

    private final OrthographicCamera camera;
    private final Stage mainMenuStage;
    private final Skin mainMenuSkin;
    private final ImageButton mainMenuImageButton;
    private final Table mainMenuTable;
    private final Texture mainMenuTexture;
    private final Image mainMenuImage;

    public MainMenuScreen(final Drop game) {
        camera = new OrthographicCamera();

        mainMenuStage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,
                camera));

        mainMenuTexture = new Texture("backgrounds/main_menu_px.jpg");
        mainMenuTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        mainMenuImage = new Image(mainMenuTexture);
        mainMenuImage.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);

        mainMenuSkin = new Skin(Gdx.files.internal("skins/play.json"),
                new TextureAtlas(Gdx.files.internal("skins/buttons.atlas")));
        mainMenuImageButton = new ImageButton(mainMenuSkin);

        mainMenuTable = new Table();
        mainMenuTable.setSize(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        mainMenuTable.bottom().add(mainMenuImageButton).size(200f, 200f).padBottom(20f);

        mainMenuStage.addActor(mainMenuImage);
        mainMenuImage.addAction(Actions.sequence(Actions.alpha(0.0f), Actions.fadeIn(1.0f)));

        mainMenuStage.addActor(mainMenuTable);
        mainMenuTable.addAction(Actions.sequence(Actions.moveBy(0.0F, -250F), Actions.delay(1.0F), Actions.moveBy(0.0F, 250F, 1.0F, Interpolation.swing)));

        mainMenuImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
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
        mainMenuSkin.dispose();
    }
}
