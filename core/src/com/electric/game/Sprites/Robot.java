package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.MainScreen;

public class Robot extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> flying;
    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private boolean setToBroke;
    public static boolean broken;
    private float defaultY;
    private boolean timeToRedefineRobot;
    private boolean timeToDefineBrokenRobot;


    public Robot(MainScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasRobot().findRegion("робот-уборщик1"), i * 17, 0, 17, 32));
        flying = new Animation<TextureRegion>(0.2f, frames);
        setBounds(getX(), getY(), 16 / ElectricGame.PPM, 24 / ElectricGame.PPM);
        setToBroke = false;
        if(!ElectricGame.robotBroken)
            broken = false;
        else if (ElectricGame.robotBroken) {
            broken = true;
        }
        defaultY = y;

    }

    private void redefineRobot(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ElectricGame.PPM);
        fdef.filter.categoryBits = ElectricGame.ENEMY_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.COIN_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.PARALLEL_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);

        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ElectricGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setGravityScale(0);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ElectricGame.PPM);
        fdef.filter.categoryBits = ElectricGame.ENEMY_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.COIN_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.PARALLEL_BIT |
                ElectricGame.MARIO_BIT;

        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];

        vertice[0] = new Vector2(-5, 15).scl(1 / ElectricGame.PPM);
        vertice[1] = new Vector2(5, 15).scl(1 / ElectricGame.PPM);
        vertice[2] = new Vector2(-3, 5).scl(1 / ElectricGame.PPM);
        vertice[3] = new Vector2(3, 5).scl(1 / ElectricGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ElectricGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setGravityScale(0);
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = flying.getKeyFrame(stateTime, true);

        stateTime = stateTime + dt;
        return region;
    }

    private static final float MAX_REPAIR_DISTANCE = 0.3f;

    @Override
    public void update(float dt) {
        if (setToBroke && !broken) {
            broken = true;
            timeToDefineBrokenRobot = true;
            ElectricGame.robotBroken = true;

        } else if (Gdx.input.isKeyJustPressed(Input.Keys.R) && broken) {
            world.destroyBody(b2body);
            defineEnemy();
            float distance = Math.abs(screen.getMario().getX() - b2body.getPosition().x);

            if (distance < MAX_REPAIR_DISTANCE) {
                setToBroke = false;
                broken = false;
                Coin.mushrooms--;
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
                setRegion(getFrame(0.001f));
                ElectricGame.robotBroken = false;


                // Устанавливаем начальные значения для интерполяции
                final float startY = b2body.getPosition().y;
                final float endY = defaultY;

                // Задаем количество шагов
                final int steps = 100;

                // Запускаем таймер
                Timer.schedule(new Timer.Task() {
                    int step = 0;

                    @Override
                    public void run() {
                        if (step < steps) {
                            float alpha = (float) step / (float) steps;
                            float newY = Interpolation.linear.apply(startY, endY, alpha);
                            b2body.setTransform(b2body.getPosition().x, newY, 0);
                            step++;
                        } else {
                            this.cancel();
                        }
                    }
                }, 0.1f, 0.008f);
            }
        } else if (!broken) {
            setRegion(getFrame(dt));
            velocity.x = 0;
            velocity.y = 0;
            setSize(0.17f, 0.32f);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
        } else {
            redefineRobot();
            b2body.setLinearVelocity(0, -1);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
            setSize(0.16f, 0.12f);
            setRegion(new TextureRegion(screen.getAtlasRobot().findRegion("робот-уборщик поломанный"), 0, 0, 16, 12));

        }
    }


    @Override
    public void hitOnHead(Electic electic) {
        setToBroke = true;
        ElectricGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    public void draw(Batch batch) {
        super.draw(batch);
    }
}
