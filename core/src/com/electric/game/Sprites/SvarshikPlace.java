package com.electric.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

public class SvarshikPlace extends InteractiveTileObject{

    public SvarshikPlace(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen){
        super(screen, bounds, kanalizatiaScreen);
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.SVARSHIK_PLACE_BIT);
    }

    @Override
    public void onHeadHit(Electic electic) {

    }
}
