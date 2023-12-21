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


public class Hud implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    private static Integer keys;
    private static Integer cores;

    //Scene2D widgets
    private static Label keysCounter;
    private static Label coresConter;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb){

        keys = 0;
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

        coresConter =new Label(String.format("%06d", cores), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("CORES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("KEYS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(coresConter).expandX();
        table.add(keysCounter).expandX();


        //add our table to the stage
        stage.addActor(table);

    }

    public void update(float dt){

    }

    public static void addScore(int value){
        keys += value;
        cores += value;
        keysCounter.setText(String.format("%06d", keys));
        coresConter.setText(String.format("%06d", cores));
    }

    @Override
    public void dispose() { stage.dispose(); }

}