package com.electric.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.*;
import com.electric.game.ElectricGame;
import com.electric.game.Sprites.Enemy;
import com.electric.game.Sprites.InteractiveTileObject;
import com.electric.game.Sprites.Items.Item;
import com.electric.game.Sprites.Mario;

public class WorldContactListener implements ContactListener {
    public static boolean redirectParallel;
    public static boolean redirectMain;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        redirectParallel = false;
        redirectMain = false;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {

            case ElectricGame.MARIO_HEAD_BIT | ElectricGame.COIN_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case ElectricGame.MARIO_BIT | ElectricGame.COIN_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.MARIO_BIT) {
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                }
                break;
            case ElectricGame.MARIO_BIT | ElectricGame.OBJECT_BIT:
                redirectMain = true;
                break;

            case ElectricGame.MARIO_BIT | ElectricGame.PARALLEL_BIT:
                redirectParallel = true;
                break;

//            case ElectricGame.MARIO_BIT | ElectricGame.GROUND_BIT:
//
//                break;
            case ElectricGame.ENEMY_HEAD_BIT | ElectricGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                break;
            case ElectricGame.ENEMY_BIT | ElectricGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
//            case ElectricGame.ENEMY_BIT | ElectricGame.ENEMY_BIT:
//                ((Enemy) fixA.getUserData()).onEnemyHit((Enemy) fixB.getUserData());
//                ((Enemy) fixB.getUserData()).onEnemyHit((Enemy) fixA.getUserData());
//                break;
//            case ElectricGame.MARIO_BIT | ElectricGame.ENEMY_BIT:
//                if (fixA.getFilterData().categoryBits == ElectricGame.MARIO_BIT)
//                    ((Mario) fixA.getUserData()).hit((Enemy) fixB.getUserData());
//                else
//                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
//                break;
            case ElectricGame.ITEM_BIT | ElectricGame.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ElectricGame.ITEM_BIT | ElectricGame.MARIO_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ITEM_BIT)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
