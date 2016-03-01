package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Game implements Screen {

	final Drop game;
	OrthographicCamera cam;
	SpriteBatch batch;
	Texture pltImg;
	Texture doodleImg;
	Rectangle doodle;
	Array<Rectangle> plts;
	Vector3 touchPos;
	long lastPltTime;
	int score;




	public  Game (final Drop gam) {
		this.game = gam;

		batch = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, 272, 408);

		touchPos = new Vector3();

		pltImg = new Texture("droplet.png");
		doodleImg = new Texture("bucket.png");


		doodle = new Rectangle();
		doodle.x = 272/2 - 64/2;
		doodle.y = 20;
		doodle.width = 64;
		doodle.height = 64;

		plts = new Array<Rectangle>();
		spawnPltdrop();

	}

	private void spawnPltdrop(){
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 272 - 64);
		raindrop.y = 408;
		raindrop.width =64;
		raindrop.height = 64;
		plts.add(raindrop);
		lastPltTime = TimeUtils.nanoTime();
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		game.batch.setProjectionMatrix(cam.combined);
		game.batch.begin();
		game.font.draw(game.batch,"Sobrano: " + score, 0,400 );
		game.batch.draw(doodleImg, doodle.x, doodle.y);
		for(Rectangle plt: plts){
			game.batch.draw(pltImg, plt.x, plt.y);

		}
		game.batch.end();

		if(Gdx.input.isTouched()){
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cam.unproject(touchPos);
			doodle.x = (int) (touchPos.x - 64/2);
			//doodle.y = (int) (touchPos.y) - 64/2;
		}

		if(doodle.x < 0) doodle.x = 0;
		if(doodle.x > 272-64) doodle.x = 272-64;
		if(doodle.y > 408-64) doodle.y = 408-64;
		if(doodle.y < 0) doodle.y = 0;

		if(TimeUtils.nanoTime() - lastPltTime > 1000000000) spawnPltdrop();

		Iterator<Rectangle> iterator = plts.iterator();
		while(iterator.hasNext()){
			Rectangle plts = iterator.next();
			plts.y -= 200 * Gdx.graphics.getDeltaTime();
			if(plts.y +64 < 0 ) iterator.remove();
			if(plts.overlaps(doodle)){
				score++;
				iterator.remove();

			}
		}
	}

	@Override
	public void show() {

	}



	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

		pltImg.dispose();
		doodleImg.dispose();
		batch.dispose();
	}
}
