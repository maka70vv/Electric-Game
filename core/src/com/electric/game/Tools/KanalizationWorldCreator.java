package com.electric.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.KanalizatiaScreen;
import com.electric.game.Screens.MainScreen;
import com.electric.game.Sprites.*;


public class KanalizationWorldCreator {
    private Array<Robot> robots;
    private Array<RobotTable> robotTables;

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(robots);
        enemies.addAll(robotTables);
        return enemies;
    }


    public KanalizationWorldCreator(KanalizatiaScreen screen, MainScreen mainScreen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

//        cerate ground bodies/fixtures
        for (MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / ElectricGame.PPM, (rect.getY() + rect.getHeight() / 2) / ElectricGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / ElectricGame.PPM, (rect.getHeight() / 2) / ElectricGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new LestnitsaPlatform(screen, rect, mainScreen);
        }

        for(MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new StoryPlatform(screen, rect, mainScreen);
        }

    }
}
