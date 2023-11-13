package com.electric.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Screens.ParallelScreen;

import static com.electric.game.Screens.MainScreen.main;
import static com.electric.game.Screens.ParallelScreen.*;


public class Mario extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD}
    public static State currentState;
    public State previousState;
    public World world;
    public static Body b2body;
    public TextureRegion marioStand;
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioDead;
    private Animation<TextureRegion> bigMarioRun;
    private Animation<TextureRegion> growMario;
    private float stateTimer;
    private boolean runningRight;
    public static boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    public static boolean marioIsDead;



    public Mario(MainScreen screen, ParallelScreen parallelScreen) {
        if (main && !parallel) {
            this.world = screen.getWorld();
        } else if (parallel && !main) {
            this.world = parallelScreen.getWorld();
        }
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
// in main screen
        if (main && !parallel) {
//            run
            for (int i = 0; i < 8; i++) {
                frames.add(new TextureRegion(screen.getAtlasPers().findRegion("персонаж"), i * 16, 0, 16, 32));
            }
            marioRun = new Animation<TextureRegion>(0.1f, frames);

            frames.clear();

            for (int i = 1; i < 4; i++) {
                frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
            }
            bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

            frames.clear();

            marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
            bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);


            marioStand = new TextureRegion(screen.getAtlasPers().findRegion("персонаж_стоит"), 0, 0, 17, 32);
            bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

            marioDead = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

            defineMario();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(marioStand);

//        mario grow
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            growMario = new Animation<>(0.2f, frames);
        }
// in parallel screen
        else if (parallel && !main) {
//            run
            for (int i = 1; i < 4; i++) {
                frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), i * 16, 0, 16, 16));
            }
            marioRun = new Animation<TextureRegion>(0.1f, frames);

            frames.clear();

            for (int i = 1; i < 4; i++) {
                frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), i * 16, 0, 16, 32));
            }
            bigMarioRun = new Animation<TextureRegion>(0.1f, frames);

            frames.clear();

            marioJump = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 80, 0, 16, 16);
            bigMarioJump = new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 80, 0, 16, 32);


            marioStand = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 0, 0, 16, 16);
            bigMarioStand = new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32);

            marioDead = new TextureRegion(parallelScreen.getAtlas().findRegion("little_mario"), 96, 0, 16, 16);

            defineMario();

            setBounds(0, 0, 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
            setRegion(marioStand);

//        mario grow
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
            frames.add(new TextureRegion(parallelScreen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
            growMario = new Animation<>(0.2f, frames);
        }
    }

    public void update(float dt){
        if (marioIsBig){
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6/ElectricGame.PPM);
        } else {
            setCenter(b2body.getPosition().x, b2body.getPosition().y);
        }

        setRegion(getFrame(dt));
//        if (timeToDefineBigMario){
//            defineBigMario();
//        } else if (timeToRedefineMario) {
//            redefineMario();
//        }
        if (b2body.getPosition().y < 0){
            marioIsDead = true;
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = marioDead;
                break;
            case GROWING:
                region = (TextureRegion) growMario.getKeyFrame(stateTimer);
                if (growMario.isAnimationFinished(stateTimer)){
                    runGrowAnimation = false;
                }
                break;
            case JUMPING:
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? (TextureRegion) bigMarioRun.getKeyFrame(stateTimer, true) : (TextureRegion) marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                setSize(0.16f, 0.32f);
                region = marioIsBig ? bigMarioStand : marioStand;
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
        if (marioIsDead) {
            return State.DEAD;
        }else if (runGrowAnimation){
            return State.GROWING;
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



//    public void defineBigMario(){
//        Vector2 currentPosition = b2body.getPosition();
//        world.destroyBody(b2body);
//
//        BodyDef bdef = new BodyDef();
//        bdef.position.set(currentPosition.add(0, 10/ElectricGame.PPM));
//        bdef.type = BodyDef.BodyType.DynamicBody;
//        b2body = world.createBody(bdef);
//
//        FixtureDef fdef = new FixtureDef();
//
//        CircleShape shape = new CircleShape();
//        shape.setRadius(32 / ElectricGame.PPM);
//        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
//        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
//                ElectricGame.COIN_BIT |
//                ElectricGame.ENEMY_BIT |
//                ElectricGame.OBJECT_BIT |
//                ElectricGame.ENEMY_HEAD_BIT |
//                ElectricGame.ITEM_BIT |
//                ElectricGame.PARALLEL_BIT
//        ;
//
//        fdef.shape = shape;
//        b2body.createFixture(fdef).setUserData(this);
//        shape.setPosition(new Vector2(0, -14/ElectricGame.PPM));
//        b2body.createFixture(fdef).setUserData(this);
//
//        EdgeShape head = new EdgeShape();
//        head.set(new Vector2(-2/ElectricGame.PPM, 6/ElectricGame.PPM), new Vector2(2/ElectricGame.PPM, 6/ElectricGame.PPM));
//        fdef.filter.categoryBits = ElectricGame.MARIO_HEAD_BIT;
//        fdef.shape = head;
//        fdef.isSensor = true;
//
//        b2body.createFixture(fdef).setUserData(this);
//        timeToDefineBigMario = false;
//    }

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ElectricGame.PPM, 30/ElectricGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
//
//        CircleShape shape = new CircleShape();
//        shape.setRadius(1 / ElectricGame.PPM);
        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.COIN_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.ENEMY_HEAD_BIT |
                ElectricGame.ITEM_BIT |
                ElectricGame.PARALLEL_BIT
        ;

//        fdef.shape = shape;
//        b2body.createFixture(fdef).setUserData(this);
//        shape.setPosition(new Vector2(0, -14/ElectricGame.PPM));
//        b2body.createFixture(fdef).setUserData(this);

        PolygonShape body = new PolygonShape();
        Vector2[] vertice = new Vector2[4];

        vertice[0] = new Vector2(-8.5f, -16).scl(1 / ElectricGame.PPM);
        vertice[1] = new Vector2(8.5f, -16).scl(1 / ElectricGame.PPM);
        vertice[2] = new Vector2(-8.5f, 16).scl(1 / ElectricGame.PPM);
        vertice[3] = new Vector2(8.5f, 16).scl(1 / ElectricGame.PPM);
        body.set(vertice);
        fdef.shape = body;
        b2body.createFixture(fdef).setUserData(this);
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }
    public boolean isBig() {
        return marioIsBig;
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void hit(Enemy enemy){
            if (marioIsBig) {
                marioIsBig = false;
                timeToRedefineMario = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
                ElectricGame.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
            } else {
                ElectricGame.manager.get("audio/music/mario_music.ogg", Music.class).play();
                ElectricGame.manager.get("audio/sounds/mariodie.wav", Sound.class).play();
                marioIsDead = true;
                Filter filter = new Filter();
                filter.maskBits = ElectricGame.NOTHING_BIT;
                for (Fixture fixture : b2body.getFixtureList()) {
                    fixture.setFilterData(filter);
                }
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }
        }


    public void redefineMario() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ElectricGame.PPM);
        fdef.filter.categoryBits = ElectricGame.MARIO_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.COIN_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.ENEMY_HEAD_BIT |
                ElectricGame.ITEM_BIT |
                ElectricGame.PARALLEL_BIT
        ;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ElectricGame.PPM, 6 / ElectricGame.PPM), new Vector2(2 / ElectricGame.PPM, 6 / ElectricGame.PPM));
        fdef.filter.categoryBits = ElectricGame.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;
    }
}