package com.shadyworld.shadyhockey;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ShadyHockeyGame extends ApplicationAdapter {
    static class Palet {
        static float RADIUS;
        static float MAX_VELOCITY = 10f;
        static float DAMPING = 0.999f;
        static final Vector2 position = new Vector2();
        static final Vector2 velocity = new Vector2();
    }

    private static final FPSLogger FPS_LOGGER = new FPSLogger();
    private BitmapFont font;

    private SpriteBatch batch;
    private Sprite palet;
    private Sprite player1;
    private Sprite player2;

    @Override
    public void create() {
        font = new BitmapFont();
        batch = new SpriteBatch();

        palet = new Sprite(new Texture(Gdx.files.internal("palet.png")));
        palet.setPosition((Gdx.graphics.getWidth() - palet.getWidth()) / 2, (Gdx.graphics.getHeight() - palet.getHeight()) / 2);
        Palet.velocity.x = Palet.velocity.y = 5f;

        player1 = new Sprite(new Texture(Gdx.files.internal("palet.png")));
        player1.setColor(Color.BLUE);
        player1.setPosition(10, (Gdx.graphics.getHeight() - player1.getHeight()) / 2);

        player2 = new Sprite(new Texture(Gdx.files.internal("palet.png")));
        player2.setColor(Color.RED);
        player2.setPosition(Gdx.graphics.getWidth() - player2.getWidth() - 10, (Gdx.graphics.getHeight() - player2.getHeight()) / 2);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update players state
        if (Gdx.input.isTouched()) {
            Vector2 tp = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            if (player1.getBoundingRectangle().contains(tp)) {
                System.out.println("P1 set!");
            }
            if (player2.getBoundingRectangle().contains(tp)) {
                System.out.println("P2 set!");
            }
        }

        // update palet state
        palet.translate(Palet.velocity.x, Palet.velocity.y);
        if (palet.getX() < 0 || palet.getX() + palet.getWidth() > Gdx.graphics.getWidth()) {
            Palet.velocity.x = -Palet.velocity.x;
        } else if (palet.getY() < 0 || palet.getY() + palet.getHeight() > Gdx.graphics.getHeight()) {
            Palet.velocity.y = -Palet.velocity.y;
        } else if (palet.getBoundingRectangle().overlaps(player1.getBoundingRectangle()) // P1 collision
                || palet.getBoundingRectangle().overlaps(player2.getBoundingRectangle())) { // P2 collision
            Palet.velocity.y = -Palet.velocity.y;
            Palet.velocity.x = -Palet.velocity.x;
        }
        Palet.velocity.x *= Palet.DAMPING;
        Palet.velocity.y *= Palet.DAMPING;

        // render players and palet
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        batch.begin();

        palet.draw(batch);
        // font.draw(batch, "Palet", 10, h - (font.getLineHeight() * 1));
        // font.draw(batch, palet.getBoundingRectangle().toString(), 10, h - (font.getLineHeight() * 2));
        // font.draw(batch, "" + palet.getX(), 10, h - (font.getLineHeight() * 3));

        player1.draw(batch);
        font.draw(batch, "P1", 10, h - (font.getLineHeight() * 1));
        font.draw(batch, player1.getBoundingRectangle().toString(), 10, h - (font.getLineHeight() * 2));
        font.draw(batch, "" + player1.getX(), 10, h - (font.getLineHeight() * 3));

        player2.draw(batch);
        font.draw(batch, "P2", w / 2, h - (font.getLineHeight() * 1));
        font.draw(batch, player2.getBoundingRectangle().toString(), w / 2, h - (font.getLineHeight() * 2));
        font.draw(batch, "" + player2.getX(), w / 2, h - (font.getLineHeight() * 3));

        batch.end();

        FPS_LOGGER.log();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
