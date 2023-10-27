package com.electric.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.electric.game.ElectricGame;
import com.electric.game.Screens.ParallelScreen;

public class ParallelWorldCreator {
//    private Array<Robot> robots;
//
//
//    public Array<Enemy> getEnemies(){
//        Array<Enemy> enemies = new Array<Enemy>();
//        enemies.addAll(robots);
//        return enemies;
//    }


    public ParallelWorldCreator(ParallelScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

//        cerate ground bodies/fixtures
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / ElectricGame.PPM, (rect.getY() + rect.getHeight() / 2) / ElectricGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / ElectricGame.PPM, (rect.getHeight() / 2) / ElectricGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

//        create pipe bodies/fixtures
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / ElectricGame.PPM, (rect.getY() + rect.getHeight() / 2) / ElectricGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / ElectricGame.PPM, (rect.getHeight() / 2) / ElectricGame.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = ElectricGame.OBJECT_BIT;
            body.createFixture(fdef);
        }

////        create coin bodies/fixtures
//        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//            new Coin(screen, rect);
//        }
//
//        create brick bodies/fixtures

//        robot
//        robots = new Array<Robot>();
//        for(MapObject object:map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//            robots.add(new Robot(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
//        }
//    }


    }
}
