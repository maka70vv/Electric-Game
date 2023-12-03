package com.electric.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class StoryPlatform extends InteractiveTileObject {

    public StoryPlatform(KanalizatiaScreen screen, Rectangle bounds, MainScreen mainScreen) {
        super(mainScreen, bounds, screen);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.STORY_BIT);
    }

    @Override
    public void onHeadHit(Electic electic) {

    }
}
