package com.electric.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.electric.game.ElectricGame;
import com.electric.game.Sprites.Cores;


public class Hud implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    private static Integer keys;
    private static Integer cores;
    private static Integer coresBlue;

    //Scene2D widgets
    private static Label keysCounter;
    private static Label coresCounter;
    private static Label coresBlueCounter;
    private Label keysLabel;
    private Label coresBlueLabel;
    private Label coresLabel;

    public Hud(SpriteBatch sb){

        keys = 0;
        coresBlue = 1;
        cores = 0;
        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(ElectricGame.V_WIDTH, ElectricGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        keysCounter =new Label(String.format("%06d", keys), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coresCounter =new Label(String.format("%06d", coresBlue), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coresCounter =new Label(String.format("%06d", cores), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coresLabel = new Label("CORES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        keysLabel = new Label("KEYS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coresBlueLabel = new Label("BLUE CORES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(coresLabel).expandX().padTop(10);
        table.add(coresBlueLabel).expandX().padTop(10);
        table.add(keysLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(coresCounter).expandX();
        table.add(coresBlueCounter).expandX();
        table.add(keysCounter).expandX();


        //add our table to the stage
        stage.addActor(table);

    }

    public void update(float dt){
    }

    public static void addKeys(int value){
        keys += value;
        keysCounter.setText(String.format("%06d", keys));
    }

    public static void addCores(int value){
        cores += value;
        coresCounter.setText(String.format("%06d", cores));

    }


    public static void addCoresBlue(int value){
        coresBlue += value;
        coresBlueCounter.setText(String.format("%06d", coresBlue));

    }

    @Override
    public void dispose() { stage.dispose(); }

}