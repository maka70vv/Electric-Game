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
    private Array<Cores> coresArray;
    private Array<Aptechka> aptechkas;
    private Array<Boss> boss;
    private Array<RobotTable> robotTables;
    private Array<NadpisNext> nadpisNexts;
    private Array<StoryPlatform> stories;

    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(robots);
        enemies.addAll(robotTables);
        enemies.addAll(svarshiks);
        enemies.addAll(coresArray);
        enemies.addAll(nadpisNexts);
        enemies.addAll(aptechkas);
        enemies.addAll(boss);
        enemies.addAll(stories);
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

        for(MapObject object:map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new EndMap(kanalizatiaScreen, rect, screen);
        }

        for(MapObject object:map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new LestnitsaPlatform(kanalizatiaScreen, rect, screen);
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
        coresArray = new Array<Cores>();
        for(MapObject object:map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            coresArray.add(new Cores(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        //        robot table
        robotTables = new Array<RobotTable>();
        for(MapObject object:map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            robotTables.add(new RobotTable(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        nadpisNexts = new Array<NadpisNext>();
       for(MapObject object:map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            nadpisNexts.add(new NadpisNext(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        boss = new Array<Boss>();
       for(MapObject object:map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            boss.add(new Boss(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        aptechkas = new Array<Aptechka>();
        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            aptechkas.add(new Aptechka(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
        stories = new Array<StoryPlatform>();
        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            stories.add(new StoryPlatform(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }
    }
}
