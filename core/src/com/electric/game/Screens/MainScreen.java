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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.electric.game.ElectricGame;
import com.electric.game.Scenes.Hud;
import com.electric.game.Sprites.Enemy;
import com.electric.game.Sprites.Items.Item;
import com.electric.game.Sprites.Items.ItemDef;
import com.electric.game.Sprites.Items.Mushroom;
import com.electric.game.Sprites.Mario;
import com.electric.game.Tools.B2WorldCreator;
import com.electric.game.Tools.WorldContactListener;


import java.util.concurrent.LinkedBlockingQueue;

public class MainScreen implements Screen {
    private final ElectricGame game;
    private final TextureAtlas atlas;
    private final TextureAtlas atlasRobot;
    private final TextureAtlas atlasPers;
    private final OrthographicCamera gameCam;
    private final Viewport gameport;
    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final B2WorldCreator creator;
    private final Mario player;
    private final Music music;
    private final Array<Item> items;
    private final LinkedBlockingQueue<ItemDef> itemsToSpawn;
    public static boolean main;
    private ParallelScreen parallelScreen;
    private KanalizatiaScreen kanalizatiaScreen;
    private static float playerX;
    private static float playerY;



    public MainScreen(ElectricGame game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        atlasRobot = new TextureAtlas("robot.pack");
        atlasPers = new TextureAtlas("pers.pack");

        main = true;
        ParallelScreen.parallel = false;

        this.game = game;
        gameCam = new OrthographicCamera();
        gameport = new FitViewport(ElectricGame.V_WIDTH/ElectricGame.PPM, ElectricGame.V_HEIGHT/ElectricGame.PPM, gameCam);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ElectricGame.PPM);
        gameCam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this, kanalizatiaScreen);

        player = new Mario(this, parallelScreen, kanalizatiaScreen);
        if (playerX > 0 && !ParallelScreen.wasDead)
            player.b2body.setTransform(playerX, playerY, 0);
        else
            player.b2body.setTransform(0, 1, 0);

        world.setContactListener(new WorldContactListener());

        music = ElectricGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public TextureAtlas getAtlasRobot(){
        return atlasRobot;
    }

    public TextureAtlas getAtlasPers(){
        return atlasPers;
    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if (!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if (idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (player.currentState != Mario.State.DEAD) {
            if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W) ))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.05f, 0), player.b2body.getWorldCenter(), true);
            if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();


        world.step(1 / 60f, 6, 2);
        player.update(dt);
        for (Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if(enemy.getX() < gameCam.position.x + 224 / ElectricGame.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (Item item : items) {
            item.update(dt);
        }


        if (player.currentState != Mario.State.DEAD) {
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
        game.batch.enableBlending();

        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items){
            item.draw(game.batch);
        }
        game.batch.end();



        if (Gdx.input.isKeyJustPressed(Input.Keys.E)){
            playerX = player.b2body.getPosition().x;
            playerY = player.b2body.getPosition().y;
            game.setScreen(new ParallelScreen(game));
        }

        if (gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver(){
        return player.currentState == Mario.State.DEAD && player.getStateTimer() > 2;
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

    public Mario getMario() {
        return player;
    }
}
