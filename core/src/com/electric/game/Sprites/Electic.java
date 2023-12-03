package com.electric.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Screens.ParallelScreen;

import static com.electric.game.Screens.KanalizatiaScreen.kanalizatia;
import static com.electric.game.Screens.MainScreen.main;
import static com.electric.game.Screens.ParallelScreen.parallel;


public class Electic extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, DEAD}
    public static State currentState;
    public State previousState;
    public World world;
    public static Body b2body;
    public TextureRegion electricStand;
    private Animation<TextureRegion> electricRun;
    private TextureRegion electricJump;
    private TextureRegion electricDie;

    private float stateTimer;
    private boolean runningRight;
    public static boolean electricIsDead;



    public Electic(MainScreen screen, ParallelScreen parallelScreen, KanalizatiaScreen kanalizatiaScreen) {
        if (main && !parallel && !kanalizatia) {
            this.world = screen.getWorld();
        } else if (parallel && !main && !kanalizatia) {
            this.world = parallelScreen.getWorld();
        } else if (kanalizatia && !main && !parallel) {
            this.world = kanalizatiaScreen.getWorld();
        }
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
// in main screen
        if (main && !parallel && !kanalizatia) {
//            run
            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(screen.getAtlasPers().findRegion("персонаж"), i * 16, 0, 16, 32));
            }
            electricRun = new Animation<>(0.1f, frames);

            frames.clear();

            electricJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);

            electricStand = new TextureRegion(screen.getAtlasPers().findRegion("персонаж_стоит"), 0, 0, 17, 32);

            electricDie = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

            definePlayer();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(electricStand);

//        mario grow
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        }
// in parallel screen
        else if (parallel && !main && !kanalizatia) {
//            run
            for (int i = 1; i < 4; i++) {
                frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
            }
            electricRun = new Animation<>(0.1f, frames);

            frames.clear();

            electricJump = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);

            electricStand = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);

            electricDie = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

            definePlayer();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(electricStand);

//        mario grow
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        }

    // in kanalizatia screen
        else if (!parallel && !main && kanalizatia) {
            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж"), i * 16, 0, 16, 32));
            }
            electricRun = new Animation<>(0.1f, frames);

            frames.clear();


            electricStand = new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж_стоит"), 0, 0, 17, 32);

            electricJump = new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж_стоит"), 80, 0, 16, 16);

            electricDie = new TextureRegion(kanalizatiaScreen.getAtlas().findRegion("персонаж_стоит"), 96, 0, 16, 16);


            definePlayer();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(electricStand);
        }
    }

    public void update(float dt){
        setCenter(b2body.getPosition().x, b2body.getPosition().y);

        setRegion(getFrame(dt));

        if (b2body.getPosition().y < 0){
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
            case JUMPING:
                region = electricJump;
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
        }else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y<0 && previousState == State.JUMPING)){
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0){
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

        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.COIN_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.ENEMY_HEAD_BIT |
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
        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public float getStateTimer(){
        return stateTimer;
    }

}