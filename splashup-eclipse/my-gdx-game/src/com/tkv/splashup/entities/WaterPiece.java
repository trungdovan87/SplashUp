package com.tkv.splashup.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tkv.splashup.screens.PlayScreen;
import com.tkv.splashup.utils.config;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.forever;

public class WaterPiece extends Image {
	protected PlayScreen playScreen;
	public int direction;
	public int movable;
	public boolean moving;
	private RepeatAction currentAction;
	Animation animation;
	TextureRegion curFrame;
	float dura;

	public WaterPiece(PlayScreen screen, int _direction) {
		super(screen.atlas1.findRegion((_direction == 1 ? "pieceup"
				: _direction == 2 ? "pieceright"
						: _direction == 3 ? "piecedown" : "pieceleft") + 2));
		this.playScreen = screen;
		this.direction = _direction;
		String name = (_direction == 1 ? "pieceup"
				: _direction == 2 ? "pieceright"
						: _direction == 3 ? "piecedown" : "pieceleft");
		TextureRegion[] regions = new TextureRegion[] {
				screen.atlas1.findRegion(name + 1),
				screen.atlas1.findRegion(name + 2),
				screen.atlas1.findRegion(name + 3),
				screen.atlas1.findRegion(name + 2) };
		animation = new Animation(0.03f, regions);
		dura = 0;
		actionMove();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		// ra khoi man hinh remove...
		if (getX() < 7 || getY() < 153 || getX() + getWidth() > 473
				|| getY() + getHeight() > 619) {
			playScreen.pieces.remove(this);
			remove();
		}
		checkCollision();
		dura += delta;
		curFrame = animation.getKeyFrame(dura, true);
		setDrawable(new TextureRegionDrawable(curFrame));
	}

	private void checkCollision() {
		for (int i = 0; i < playScreen.tableWith; i++) {
			for (int j = 0; j < playScreen.tableWith; j++) {
				if (playScreen.cards[i][j].getValue() > 0
						&& !playScreen.cards[i][j].disappearing
						&& isCollision(playScreen.cards[i][j])) {
					playScreen.cards[i][j].plusPiece();
					playScreen.pieces.remove(this);
					remove();
					// if(playScreen.getSoundOnOff())
					// MyGdxGame.sounds.get(config.SoundHit).play();
					break;
				}
			}
		}

	}

	private boolean isCollision(Card bird) {
		float d = 10* config.sizeMode; // gia giam de chim dung vao pipe nhieu hon....

		float maxx1 = getX() + getWidth() - d;
		float minx1 = getX() + d;
		float maxy1 = getY() + getHeight() - d;
		float miny1 = getY() + d;

		float maxx2 = bird.getX() + bird.getWidth() - d;
		float minx2 = bird.getX() + d;
		float maxy2 = bird.getY() + bird.getHeight() - d;
		float miny2 = bird.getY() + d;

		return (maxx1 >= minx2 && maxx2 >= minx1 && maxy1 >= miny2 && maxy2 >= miny1);

	}

	protected void actionMove() {
		removeAction(currentAction);
		MoveByAction move = new MoveByAction();
		float dura = config.kmoveLeftDura;
		dura = playScreen.speedMode == 1 ? dura
				: playScreen.speedMode == 2 ? dura / 1.5f
						: playScreen.speedMode == 3 ? dura / 2f : dura / 3f;
		move.setDuration(dura);
		if (direction == 1) {
			move.setAmountY(config.kLandWidth);
		} else if (direction == 2) {
			move.setAmountX(config.kLandWidth);
		} else if (direction == 3) {
			move.setAmountY(-config.kLandWidth);
		} else if (direction == 4) {
			move.setAmountX(-config.kLandWidth);
		}
		currentAction = forever(move);
		addAction(currentAction);

	}
}
