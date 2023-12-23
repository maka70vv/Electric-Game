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
    private Array<Inoi> inoiRobots;
    private Array<JadroBlue> jadros;
    private Array<RobotBlue> robots;

    public Array<EnemyKanalizatia> getEnemiesKanalizatia(){
        Array<EnemyKanalizatia> enemiesKanalizatia = new Array<EnemyKanalizatia>();
        enemiesKanalizatia.addAll(inoiRobots);
        enemiesKanalizatia.addAll(jadros);
        enemiesKanalizatia.addAll(robots);
        return enemiesKanalizatia;
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

        for(MapObject object:map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new EndMap(screen, rect, mainScreen);
        }


        jadros = new Array<JadroBlue>();
        for(MapObject object:map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            jadros.add(new JadroBlue(screen, rect.x/ElectricGame.PPM, rect.y/ElectricGame.PPM));
        }


        robots = new Array<RobotBlue>();
        for(MapObject object:map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            robots.add(new RobotBlue(screen, rect.x/ElectricGame.PPM, rect.y/ElectricGame.PPM));
        }

        //        robot inoi
        inoiRobots = new Array<Inoi>();
        for(MapObject object:map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            inoiRobots.add(new Inoi(screen, rect.x / ElectricGame.PPM, rect.y / ElectricGame.PPM));
        }

    }
}
