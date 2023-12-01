package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.electric.game.ElectricGame;
import com.electric.game.Scenes.Hud;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Sprites.Items.ItemDef;
import com.electric.game.Sprites.Items.Mushroom;

public class Pipes extends InteractiveTileObject{

    public Pipes(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen){
        super(screen, bounds, kanalizatiaScreen);
        TiledMapTileSet tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.OBJECT_BIT);
    }

    @Override
    public void onHeadHit(Mario mario) {

    }
}
