package com.mygdx.drop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.drop.Drop;
import com.mygdx.drop.common.Constants;

public class PauseScreen implements Screen {

    private Stage pauseMenuStage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Skin resumeButtonSkin;
    private ImageButton resumeImageButton;

    public PauseScreen(final Drop game, final GameScreen screen) {
//        screen.pause();
        pauseMenuStage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,
                new OrthographicCamera()));

        backgroundTexture = new Texture("backgrounds/pause_menu_bg.png");
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundImage = new Image(backgroundTexture);

        resumeButtonSkin = new Skin(Gdx.files.internal("skins/play_button.json"),
                new TextureAtlas(Gdx.files.internal("skins/menu_buttons.atlas")));
        resumeImageButton = new ImageButton(resumeButtonSkin);
        resumeImageButton.setPosition(Constants.WORLD_WIDTH / 2 - resumeImageButton.getWidth() / 2,
                Constants.WORLD_HEIGHT / 2 - resumeImageButton.getHeight());
        resumeImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(screen);
            }
        });

        pauseMenuStage.addActor(backgroundImage);
        pauseMenuStage.addActor(resumeImageButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(pauseMenuStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        pauseMenuStage.act();
        pauseMenuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        pauseMenuStage.getViewport().update(width, height);
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
        backgroundTexture.dispose();
        pauseMenuStage.dispose();
        resumeButtonSkin.dispose();
    }
}
