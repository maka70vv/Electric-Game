package com.electric.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.MainScreen;

public class Platform extends InteractiveTileObject {

    public Platform(MainScreen screen, Rectangle bounds) {
        super(screen, bounds);
//        TiledMapTileSet tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.OBJECT_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

    }
}