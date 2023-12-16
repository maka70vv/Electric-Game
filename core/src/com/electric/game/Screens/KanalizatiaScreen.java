package com.electric.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.electric.game.ElectricGame;
import com.electric.game.Sprites.Electic;
import com.electric.game.Sprites.Enemy;
import com.electric.game.Sprites.EnemyKanalizatia;
import com.electric.game.Tools.KanalizationWorldCreator;
import com.electric.game.Tools.WorldContactListener;

public class KanalizatiaScreen implements Screen {
    private final ElectricGame game;
    private final TextureAtlas atlas;
    private final TextureAtlas atlasInoi;
    private final TextureAtlas atlasJadro;
    private final OrthographicCamera gameCam;
    private final Viewport gameport;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final KanalizationWorldCreator creator;
    private final Electic player;
    private final Music music;

    public static boolean kanalizatia;
    private MainScreen mainScreen;
    private ParallelScreen parallelScreen;
    public static boolean wasDead;


    public KanalizatiaScreen(ElectricGame game){
        atlas = new TextureAtlas("pers.pack");
        atlasInoi = new TextureAtlas("inoi.pack");
        atlasJadro = new TextureAtlas("yadro.pack");

        ParallelScreen.parallel = false;
        MainScreen.main = false;
        kanalizatia=true;

        this.game = game;
        gameCam = new OrthographicCamera();
        gameport = new FitViewport(ElectricGame.V_WIDTH/ElectricGame.PPM, ElectricGame.V_HEIGHT/ElectricGame.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("kanalizatia.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1 / ElectricGame.PPM);
        gameCam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new KanalizationWorldCreator(this, mainScreen);

        player = new Electic(mainScreen, parallelScreen, this);

        world.setContactListener(new WorldContactListener());

        music = ElectricGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();


    }

    public TextureAtlas getAtlas(){
        return atlas;
    }
    public TextureAtlas getAtlasInoi(){
        return atlasInoi;
    }
    public TextureAtlas getAtlasJadro(){
        return atlasJadro;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (player.currentState != Electic.State.DEAD) {
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.05f, 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0), player.b2body.getWorldCenter(), true);
            if (WorldContactListener.climb && (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W))) {
                float climbSpeed = 0.4f;

                player.b2body.setLinearVelocity(0, climbSpeed);
            }
        }

            float playerHeight = Electic.b2body.getPosition().y;
            if (playerHeight >= 160) {
                WorldContactListener.climb = false;

        }
    }

    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);
        player.update(dt);

        for (EnemyKanalizatia enemy : creator.getEnemiesKanalizatia()) {
            enemy.update(dt);
            if(enemy.getX() < gameCam.position.x + 224 / ElectricGame.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        if (player.currentState != Electic.State.DEAD) {
            if (gameCam.position.x < player.b2body.getPosition().x) {
                gameCam.position.x = player.b2body.getPosition().x;
            } else if (player.b2body.getPosition().x > 2) {
                gameCam.position.x = player.b2body.getPosition().x;
            } else if (player.b2body.getPosition().x - player.b2body.getFixtureList().get(0).getShape().getRadius()
                    <= gameCam.position.x - gameCam.viewportWidth / 2) {
                player.b2body.setLinearVelocity(0, player.b2body.getLinearVelocity().y);
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getLocalCenter(), true);
            }
            gameCam.update();
            renderer.setView(gameCam);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        render map
        renderer.render();
//        render B2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);

        game.batch.end();

        if (WorldContactListener.redirectMain){
            game.setScreen(new MainScreen(game));
        }

        if (gameOver()){
            game.setScreen(new GameOverScreen(game));
            wasDead = true;
            dispose();
        }
    }

    public boolean gameOver(){
        return player.currentState == Electic.State.DEAD && player.getStateTimer() > 3;
    }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }

    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}