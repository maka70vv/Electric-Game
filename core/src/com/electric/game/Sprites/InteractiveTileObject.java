package com.electric.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;

import static com.electric.game.Screens.KanalizatiaScreen.kanalizatia;
import static com.electric.game.Screens.MainScreen.main;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected MainScreen screen;
    protected KanalizatiaScreen kanalizatiaScreen;
    protected Fixture fixture;

    public InteractiveTileObject(MainScreen screen, Rectangle bounds, KanalizatiaScreen kanalizatiaScreen) {
        if (main && !kanalizatia) {
            this.screen = screen;
            this.world = screen.getWorld();
            this.map = screen.getMap();
        }else if (!main && kanalizatia) {
            this.kanalizatiaScreen = kanalizatiaScreen;
            this.world = kanalizatiaScreen.getWorld();
            this.map = kanalizatiaScreen.getMap();
        }
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / ElectricGame.PPM, (bounds.getY() + bounds.getHeight() / 2) / ElectricGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth() / 2) / ElectricGame.PPM, (bounds.getHeight() / 2) / ElectricGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public abstract void onHeadHit(Mario mario);

    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * ElectricGame.PPM / 16),
                (int) (body.getPosition().y * ElectricGame.PPM / 16));
    }
}
