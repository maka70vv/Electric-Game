package com.electric.game.Sprites;


import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.MainScreen;

public class RedirectPlatform extends InteractiveTileObject {

    public RedirectPlatform(MainScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.PARALLEL_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

    }
}