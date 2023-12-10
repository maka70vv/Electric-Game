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


public class B2WorldCreator {
    private Array<Robot> robots;
    private Array<RobotSvarshik> svarshiks;
    private Array<RobotTable> robotTables;

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(robots);
        enemies.addAll(robotTables);
        enemies.addAll(svarshiks);
        return enemies;
    }


    public B2WorldCreator(MainScreen screen, KanalizatiaScreen kanalizatiaScreen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

//        cerate ground bodies/fixtures
        for(MapObject object:map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2)/ ElectricGame.PPM, (rect.getY() + rect.getHeight()/2)/ElectricGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/ElectricGame.PPM, (rect.getHeight()/2)/ElectricGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

//        create shop bodies/fixtures
//        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//            new Coin(screen, rect, kanalizatiaScreen);
//        }

//        create platform bodies/fixtures
        for(MapObject object:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Platform(screen, rect, kanalizatiaScreen);
        }

//        create redirect platform bodies/fixtures
        for(MapObject object:map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new RedirectPlatform(screen, rect, kanalizatiaScreen);
        }

//        create platform for robot svarshik
        for(MapObject object:map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new SvarshikPlace(screen, rect, kanalizatiaScreen);
        }

//        robot
        robots = new Array<Robot>();
        for(MapObject object:map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            robots.add(new Robot(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }

//        robot svarshik
        svarshiks = new Array<RobotSvarshik>();
        for(MapObject object:map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            svarshiks.add(new RobotSvarshik(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        //        robot table
        robotTables = new Array<RobotTable>();
        for(MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            robotTables.add(new RobotTable(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
    }
}
