package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Scenes.Hud;
import com.electric.game.Screens.MainScreen;

public class Boss extends Enemy {
    private float stateTime;
    private final Animation<TextureRegion> walking;
    private Array<TextureRegion> frames = new Array<TextureRegion>();
    private boolean setToBroke;
    public static boolean broken;
    public static boolean attacking;
    private boolean timeToRedefineRobot;
    private boolean timeToDefineBrokenRobot;
    private static final float HEALTH_BAR_WIDTH = 32 / ElectricGame.PPM;
    private static final float HEALTH_BAR_HEIGHT = 4 / ElectricGame.PPM;
    private static final float HEALTH_BAR_OFFSET_Y = 0.02f;

    public Boss(MainScreen screen, float x, float y) {
        super(screen, x, y);

        frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик идет"), i * 16, 0, 16, 11));
        walking = new Animation<TextureRegion>(0.2f, frames);
        setBounds(getX(), getY(), 16 / ElectricGame.PPM, 24 / ElectricGame.PPM);
        setToBroke = false;
    }

    private void redefineRobot() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
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
                ElectricGame.PARALLEL_BIT;
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        b2body.createFixture(fdef).setUserData(this);
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
        fdef.filter.categoryBits = ElectricGame.SVARSHIK_BIT;
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
        fdef.filter.categoryBits = ElectricGame.SVARSHIK_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public TextureRegion getFrame(float dt) {
        TextureRegion region;
        region = walking.getKeyFrame(stateTime, true);

        stateTime = stateTime + dt;
        return region;
    }

    private static final float MAX_REPAIR_DISTANCE = 0.3f;
    private static final float START_WALKING_DISTANCE = 1.8f;

    @Override
    public void update(float dt) {
        float distance = Math.abs(screen.getPlayer().getX() - b2body.getPosition().x);

        if (setToBroke && !broken) {
            broken = true;
            timeToDefineBrokenRobot = true;
        } else if (!broken) {
            if (distance < START_WALKING_DISTANCE) {
                b2body.setLinearVelocity(velocity);
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
                setRegion(getFrame(dt));
                if (distance <= MAX_REPAIR_DISTANCE)
                    attacking = true;

                if (distance <= MAX_REPAIR_DISTANCE && Gdx.input.isKeyJustPressed(Input.Keys.R) && ElectricGame.keys>0) {
                    ElectricGame.hpBoss -= 10;
                    ElectricGame.keys--;
                    Hud.addKeys(-1);
                    if (ElectricGame.hpBoss==0)
                        setToBroke = true;
                }
            } else {
                setRegion(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик стоит"), 0, 0, 16, 11));
                setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
            }
        }else {
            redefineRobot();
            b2body.setLinearVelocity(0, 0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / ElectricGame.PPM);
            setRegion(new TextureRegion(screen.getAtlasSvarshik().findRegion("робот-сварщик поломанный"), 0, 0, 16, 11));
            if (Gdx.input.isKeyJustPressed(Input.Keys.N) && distance<MAX_REPAIR_DISTANCE){
                ElectricGame.keys++;
                Hud.addKeys(1);
            }
        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        if (ElectricGame.hpBoss > 0) {
            batch.draw(getHealthBarRegion(), b2body.getPosition().x - HEALTH_BAR_WIDTH / 2, b2body.getPosition().y + HEALTH_BAR_OFFSET_Y + 20/ElectricGame.PPM,
                    HEALTH_BAR_WIDTH * (ElectricGame.hpBoss / 100f), HEALTH_BAR_HEIGHT);
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
        if (ElectricGame.hpBoss > 70) {
            return Color.GREEN;
        } else if (ElectricGame.hpBoss > 30) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }
}