package com.electric.game.Sprites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

import static com.electric.game.Screens.KanalizatiaScreen.kanalizatia;
import static com.electric.game.Screens.MainScreen.main;

public class Electic extends Sprite {

    public void decreaseHealth() {
        ElectricGame.hpPlayer-=10;
    }

    public enum State {FALLING, STANDING, RUNNING, DEAD}
    public static State currentState;
    public State previousState;
    public World world;
    public static Body b2body;
    private ShapeRenderer shapeRenderer;

    public TextureRegion electricStand;
    private Animation<TextureRegion> electricRun;
    private TextureRegion electricDie;

    private float stateTimer;
    private boolean runningRight;
    public static boolean electricIsDead;


    Texture blank;



    private static final float HEALTH_BAR_WIDTH = 32 / ElectricGame.PPM;
    private static final float HEALTH_BAR_HEIGHT = 4 / ElectricGame.PPM;
    private static final float HEALTH_BAR_OFFSET_Y = 0.02f;


    public Electic(MainScreen screen, KanalizatiaScreen kanalizatiaScreen) {
        if (main && !kanalizatia) {
            this.world = screen.getWorld();
        } else if (kanalizatia && !main) {
            this.world = kanalizatiaScreen.getWorld();
        }
        currentState = State.STANDING;
        previousState = State.STANDING;
        ElectricGame.hpPlayer = 100;
        stateTimer = 0;
        runningRight = true;
        electricIsDead = false;

        Array<TextureRegion> frames = new Array<>();
// in main screen
        if (main && !kanalizatia) {
//            run
            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(screen.getAtlasPers().findRegion("персонаж"), i * 16, 0, 16, 32));
            }
            electricRun = new Animation<>(0.1f, frames);

            frames.clear();

            electricStand = new TextureRegion(screen.getAtlasPers().findRegion("персонаж_стоит"), 0, 0, 17, 32);

            electricDie = new TextureRegion(screen.getAtlasPers().findRegion("персонаж_стоит"), 0, 0, 17, 32);

            definePlayer();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(electricStand);
        }

    // in kanalizatia screen
        else if (!main && kanalizatia) {
            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж"), i * 16, 0, 16, 32));
            }
            electricRun = new Animation<>(0.1f, frames);

            frames.clear();


            electricStand = new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж_стоит"), 0, 0, 17, 32);

            electricDie = new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж_стоит"), 0, 0, 17, 32);


            definePlayer();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(electricStand);
        }
    }

    public void update(float dt){
        setCenter(b2body.getPosition().x, b2body.getPosition().y);

        setRegion(getFrame(dt));

        if (b2body.getPosition().y < 0 || ElectricGame.hpPlayer <= 0){
            electricIsDead = true;
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = electricDie;
                break;
            case RUNNING:
                region = electricRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                setSize(0.16f, 0.32f);
                region = electricStand;
                break;

        }
        if ((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (electricIsDead) {
            return State.DEAD;
        }else if (b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }else {
            return State.STANDING;
        }
    }

    public void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ElectricGame.PPM, 32/ElectricGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        fdef.filter.categoryBits = ElectricGame.ELECTRIC_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.ROBOT_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.SVARSHIK_BIT |
                ElectricGame.ITEM_BIT |
                ElectricGame.PARALLEL_BIT |
                ElectricGame.LESTNITSA_BIT |
                ElectricGame.STORY_BIT |
                ElectricGame.END_MAP_BIT
        ;

        PolygonShape body = new PolygonShape();
        Vector2[] vertice = new Vector2[6];

        vertice[0] = new Vector2(-4, -14).scl(1 / ElectricGame.PPM);
        vertice[1] = new Vector2(4, -14).scl(1 / ElectricGame.PPM);
        vertice[2] = new Vector2(-8.5f, 0).scl(1 / ElectricGame.PPM);
        vertice[3] = new Vector2(8.5f, 0).scl(1 / ElectricGame.PPM);
        vertice[4] = new Vector2(-2, 14).scl(1 / ElectricGame.PPM);
        vertice[5] = new Vector2(2, 14).scl(1 / ElectricGame.PPM);
        body.set(vertice);
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ElectricGame.ELECTRIC_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public float getStateTimer(){
        return stateTimer;
    }


    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (ElectricGame.hpPlayer > 0) {
            batch.draw(getHealthBarRegion(), b2body.getPosition().x - HEALTH_BAR_WIDTH / 2, b2body.getPosition().y + HEALTH_BAR_OFFSET_Y + 20/ElectricGame.PPM,
                    HEALTH_BAR_WIDTH * (ElectricGame.hpPlayer / 100f), HEALTH_BAR_HEIGHT);
        }
    }

    private TextureRegion getHealthBarRegion() {

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(getColorByHealth());
        pixmap.fill();
        TextureRegion textureRegion = new TextureRegion(new Texture(pixmap));
        pixmap.dispose();
        return textureRegion;
    }

    private Color getColorByHealth() {
        if (ElectricGame.hpPlayer > 70) {
            return Color.GREEN;
        } else if (ElectricGame.hpPlayer > 30) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }
}