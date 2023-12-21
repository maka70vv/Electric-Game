package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class Inoi extends EnemyKanalizatia {
    private float stateTime;
    private final Animation<TextureRegion> flying;
    private Array<TextureRegion> frames = new Array<TextureRegion>();




    public Inoi(KanalizatiaScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasInoi().findRegion("иной"), i * 16, 0, 16, 32));
        flying = new Animation<TextureRegion>(0.2f, frames);
        setBounds(getX(), getY(), 16 / ElectricGame.PPM, 24 / ElectricGame.PPM);

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
        fdef.filter.categoryBits = ElectricGame.ROBOT_BIT;
        fdef.filter.maskBits = ElectricGame.GROUND_BIT |
                ElectricGame.SVARSHIK_PLACE_BIT |
                ElectricGame.ROBOT_BIT |
                ElectricGame.OBJECT_BIT |
                ElectricGame.PARALLEL_BIT |
                ElectricGame.ELECTRIC_BIT;

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
        fdef.filter.categoryBits = ElectricGame.ROBOT_BIT;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setGravityScale(0);
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = flying.getKeyFrame(stateTime, true);

        stateTime = stateTime + dt;
        return region;
    }

    @Override
    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));
    }


//    @Override
//    public void hitOnHead(Electic electic) {
//        setToBroke = true;
//        ElectricGame.manager.get("audio/sounds/stomp.wav", Sound.class).play();
//    }

    public void draw(Batch batch) {
        super.draw(batch);
    }
}