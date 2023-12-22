package com.electric.game.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Sprites.Enemy;
import com.electric.game.Sprites.Electic;

public class WorldContactListener implements ContactListener {
    public static boolean redirectParallel;
    public static boolean redirectMain;
    public static boolean climb;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        redirectParallel = false;
        redirectMain = false;
        climb = false;

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
//            case ElectricGame.ELECTRIC_BIT | ElectricGame.GROUND_BIT:
//                redirectParallel = false;
//                break;
            case ElectricGame.ELECTRIC_BIT | ElectricGame.LESTNITSA_BIT:
                climb = true;
                break;
            case ElectricGame.ELECTRIC_BIT | ElectricGame.END_MAP_BIT:
                if (KanalizatiaScreen.kanalizatia)
                    redirectMain = true;
                break;
            case ElectricGame.ELECTRIC_BIT | ElectricGame.OBJECT_BIT:
                redirectMain = true;
                break;

            case ElectricGame.ELECTRIC_BIT | ElectricGame.PARALLEL_BIT:
                redirectParallel = true;
                break;

            case ElectricGame.SVARSHIK_BIT | ElectricGame.SVARSHIK_PLACE_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ROBOT_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case ElectricGame.ELECTRIC_BIT | ElectricGame.SVARSHIK_BIT:
                if (fixA.getFilterData().categoryBits == ElectricGame.ELECTRIC_BIT)
                    ((Electic) fixA.getUserData()).decreaseHealth();
                else
                    ((Electic) fixB.getUserData()).decreaseHealth();
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