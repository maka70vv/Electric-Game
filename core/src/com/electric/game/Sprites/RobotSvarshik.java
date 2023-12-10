package com.electric.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.MainScreen;


public class RobotSvarshik extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private boolean setToDestroy;
    private boolean destroyed;
    private boolean electricAtack;

    public RobotSvarshik(MainScreen screen, float x, float y) {
        super(screen, x, y);
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик идет"), i * 16, 0, 16, 11));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16 / ElectricGame.PPM, 16 / ElectricGame.PPM);
        setToDestroy = false;
        destroyed = false;
        defineEnemy();
    }

    public void update(float dt){
        stateTime += dt;
        float distanceToPlayer = Math.abs(screen.getPlayer().b2body.getPosition().x - b2body.getPosition().x);

        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик поломанный"), 0, 0, 16, 11));
            stateTime = 0;
        }
        else if(!destroyed) {
            if (distanceToPlayer<1) {
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
            } else if (distanceToPlayer <= 0.3f) {
                electricAtack = true;
            } else {
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

                setRegion(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик стоит"), 0, 0, 16, 11));
            }
        }
    }

    public void onHit(Electic electic) {
        electic.decreaseHealth();
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
                ElectricGame.SVARSHIK_PLACE_BIT |
                ElectricGame.ENEMY_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.ELECTRIC_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / ElectricGame.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / ElectricGame.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / ElectricGame.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / ElectricGame.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ElectricGame.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

}