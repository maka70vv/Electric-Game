package com.electric.game.Sprites;


import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class RedirectPlatform extends InteractiveTileObject {

    public RedirectPlatform(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen) {
        super(screen, bounds, kanalizatiaScreen);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.PARALLEL_BIT);
    }

    @Override
    public void onHeadHit(Electic electic) {

    }
}