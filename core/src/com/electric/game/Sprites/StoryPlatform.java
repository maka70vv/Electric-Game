package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

public class StoryPlatform extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> flying;
    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private static int currentFrameIndex;

    public StoryPlatform(MainScreen screen, float x, float y) {
        super(screen, x, y);
        currentFrameIndex = 0;
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasRobot().findRegion("робот-надпись1"), i * 116, 0, 116, 48));
        flying = new Animation<TextureRegion>(0.4f, frames);
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ElectricGame.PPM);

        fdef.shape = shape;
        fdef.restitution = 0.5f;

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
        b2body.setGravityScale(0);
    }

    public TextureRegion getFrame (float dt){
        TextureRegion region;
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            currentFrameIndex = (currentFrameIndex + 1) % frames.size;
        }
        region = frames.get(currentFrameIndex);
        stateTime = stateTime + dt;
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        velocity.x = 0;
        velocity.y = 0;
        setSize(1.16f, 0.48f);
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
    }

    public void draw(Batch batch){
        super.draw(batch);
    }

}
