package com.electric.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class Platform extends InteractiveTileObject {

    public Platform(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen) {
        super(screen, bounds, kanalizatiaScreen);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.OBJECT_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

    }
}