package com.electric.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.electric.game.Screens.MainScreen;

public abstract class Enemy extends Sprite {
    protected World world;
    protected MainScreen screen;
    public Body b2body;
    public Vector2 velocity;
    public Vector2 velocityRobot;

    public Enemy(MainScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);

        defineEnemy();

        velocity = new Vector2(1, 0);
        velocityRobot = new Vector2(0, 1);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void update(float dt);

    public abstract void hitOnHead(Electic electic);

    public void reverseVelocity(boolean x, boolean y) {
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }
}

