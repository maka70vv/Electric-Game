package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Scenes.Hud;
import com.electric.game.Screens.ParallelScreen;
import com.electric.game.Sprites.Items.ItemDef;
import com.electric.game.Sprites.Items.Mushroom;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public static int mushrooms = 0;

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "Collision");
        if (getCell().getTile().getId() == BLANK_COIN) {
            ElectricGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
        } else {
            ElectricGame.manager.get("audio/sounds/coin.wav", Sound.class).play();

            if (Math.random() < 0.3) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16/ElectricGame.PPM),
                        Mushroom.class));
                mushrooms +=1;
            }
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);
    }

    public Coin(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen){
        super(screen, bounds, kanalizatiaScreen);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(ElectricGame.COIN_BIT);
    }
}

