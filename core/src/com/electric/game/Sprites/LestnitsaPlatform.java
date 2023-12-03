package com.electric.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class LestnitsaPlatform extends InteractiveTileObject {

    public LestnitsaPlatform(KanalizatiaScreen screen, Rectangle bounds, MainScreen mainScreen) {
        super(mainScreen, bounds, screen);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.LESTNITSA_BIT);
    }

    @Override
    public void onHeadHit(Electic electic) {

    }
}
