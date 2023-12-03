package com.electric.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Scenes.Hud;

public class Brick extends InteractiveTileObject {
    public Brick(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen) {
        super(screen, bounds, kanalizatiaScreen);
        fixture.setUserData(this);
//        setCategoryFilter(ElectricGame.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Electic electic) {

        Gdx.app.log("Brick", "Collision");
        setCategoryFilter(ElectricGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        ElectricGame.manager.get("audio/sounds/breakblock.wav", Sound.class).play();

    }
}
