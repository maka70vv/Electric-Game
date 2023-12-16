package com.electric.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;


public class JadroBlue extends InteractiveTileObject {

    public JadroBlue(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen) {
        super(screen, bounds, kanalizatiaScreen);
        TextureRegion region;

        region = new TextureRegion(kanalizatiaScreen.getAtlasJadro().findRegion("Ядро синее"), 0, 0, 16, 12);
        region.setRegion(region);
    }
    @Override
    public void onHeadHit(Electic electic) {

    }
}