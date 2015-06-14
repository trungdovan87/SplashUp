package com.tkv.splashup.screens;

import java.io.Console;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.tkv.splashup.MyGdxGame;
import com.tkv.splashup.entities.Card;
import com.tkv.splashup.entities.WaterPiece;
import com.tkv.splashup.helper.ChangeMode;
import com.tkv.splashup.helper.ResultNote;
import com.tkv.splashup.helper.SettingNote;
import com.tkv.splashup.utils.MyStage;
import com.tkv.splashup.utils.config;

public class PlayScreen implements Screen {
	public int tableWith = 6;
	public int gamemode = 1;
	public int speedMode = 2;

	public boolean isSoundOff = false;
	public Card[][] cards = new Card[tableWith][tableWith];
	private int[] counts = new int[5];
	public ArrayList<WaterPiece> pieces;
	public boolean isDoing;
	public boolean isRunning;
	private MyStage stage;
	public TextureAtlas atlas1;
	public TextureAtlas atlas2;
	private ResultNote resultNote;
	private SettingNote settingNode;
	private ChangeMode changeMode;
	private Image tablebg;
	private Image ready;
	private Label labelScore;
	private Label labelBest;
	private Image panelBest;
	private Image panelScore;
	private Image panelLevel;
	private Label labelLevel;
	private Image labelHeath;
	private int score;
	private int level;
	public int heath;
	private int currentExposedSplash;
	protected MyGdxGame game;
	public boolean dancingMode;
	public int step;

	private Image rateicon;;
	private Image achiveicon;
	private Image setting;
	private Group lvlClearGroup;
	Label lvlClearLostDrop;
	Label lvlClearGotDrop;
	Label lvlClearMaxCombo;
	Label lvlClearScore;
	private Image lvlClear;
	private Image lvlClearbg;
	private int curLostDrop = 0;
	private int curGotDrop = 0;
	private int curMaxCombo = 0;
	private int curScore = 0;

	public boolean isGameOver = false;
	public boolean isLevelup = false;
	int undoRemain;
	Preferences prefs = Gdx.app.getPreferences("mypreferences");
	ArrayList<Integer> availablePos = new ArrayList<Integer>();
	ArrayList<Action> pendingAction = new ArrayList<Action>();

	public PlayScreen() {

		//stage = new MyStage(0, 0, true);
		stage = new MyStage();
		stage.setPlayScreen(this);
		game = (MyGdxGame) ScreenManager.getInstance().getGame();
		game.manager.load("data/mymota.txt", TextureAtlas.class);
		game.manager.load("data/mymota1.txt", TextureAtlas.class);
		game.manager.finishLoading();

		atlas1 = game.manager.get("data/mymota.txt", TextureAtlas.class);
		atlas2 = game.manager.get("data/mymota1.txt", TextureAtlas.class);
		dancingMode = true;

		goToStep(1);

	}

	@Override
	public void show() {
		// set the stage as the input processor
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true);
		// Preferences prefs = Gdx.app.getPreferences("mypreferences");
		// prefs.putInteger("highScore", 0);
		// prefs.flush();
	}

	public void startGame() {
		game.showAds(false);

		if (ready != null) {
			ready.remove();
		}

	}

	public void goToStep(int step) {
		this.step = step;
		if (step == 1) {
			if (prefs.getInteger("intNewVersion") == 0) {
				prefs.putString("currentGameState" + gamemode,
						prefs.getString("currentGameState"));
				prefs.putInteger("highScore" + gamemode,
						prefs.getInteger("highScore" + tableWith));
				prefs.putInteger("intNewVersion", 1);
				prefs.flush();
			}
			if (prefs.getInteger("gamemode") > 0) {
				gamemode = prefs.getInteger("gamemode");
			}
			// prefs.putInteger("highScore" + tableWith, 7235);
			// prefs.flush();
			isSoundOff = prefs.getBoolean("isSoundOff");
			if (prefs.getInteger("speedMode") > 0) {
				speedMode = prefs.getInteger("speedMode");
			}
			// if (prefs.getInteger("currentMaxLevel") > 0) {
			// currentMaxLevel = prefs.getInteger("currentMaxLevel");
			// }
			// if (prefs.getInteger("currentMaxExplodeRow") > 0) {
			// currentMaxExplodeRow = prefs.getInteger("currentMaxExplodeRow");
			// }
			if (prefs.getString("currentGameState" + gamemode) != "") {
				initGame();
				loadCardTable(prefs.getString("currentGameState" + gamemode));
				if (isLevelup) {
					ShowNextLevel();
				}
			} else {
				resetGame();
			}

			// resetGame();
			// showStartBoard();
		}

	}

	public void stopGame() {
		int highScore = prefs.getInteger("highScore" + gamemode);
		boolean isNew = false;
		if (score > highScore) {
			highScore = score;
			isNew = true;
			prefs.putInteger("highScore" + gamemode, score);
			prefs.flush();
		}
		game.submitScore(score, gamemode);
		game.showAds(true);
		showNoteBoard(score, 0, false);

		step = 3;
	}

	public void shareFacebook() {
		game.shareFacebook();
	}

	public boolean unlockAchievement(int type, int value) {
		return game.unlockAchievement(type, value);
	}

	public void showAchievement() {
		game.showAchievement();
	}

	public void showLeaderBoard() {

		int highScore = prefs.getInteger("highScore" + gamemode);
		game.showLeaderBoard(highScore, gamemode);
	}

	public void shareLink() {
		game.shareLink();
	}

	public void showNoteBoard(int score, int best, boolean isNew) {
		resultNote = new ResultNote(this, score, best, isNew);
		stage.addActor(resultNote);
		resultNote.setZIndex(stage.getActors().size);

	}

	public void showSetting() {
		if (step == 1 || step == 3) {
			if (step == 3) {
				resultNote.remove();
			}
			game.showAds(true);
			settingNode = new SettingNote(this, isSoundOff, speedMode);
			stage.addActor(settingNode);
			settingNode.setZIndex(stage.getActors().size);
			step = 2;
		}

	}

	public void hideSetting() {
		game.showAds(false);
		settingNode.remove();
		step = 1;
	}

	public void UpdateSetting(boolean isMusic, int newSpeedMode) {
		if (speedMode != newSpeedMode) {
			speedMode = newSpeedMode;
			prefs.putInteger("speedMode", speedMode);
			prefs.flush();
		}
		if (isSoundOff != isMusic) {
			isSoundOff = isMusic;
			prefs.putBoolean("isSoundOff", isSoundOff);
			prefs.flush();
		}
	}

	public void UpdateGameMode(int newGameMode) {
		// TODO Auto-generated method stub
		if (gamemode != newGameMode) {
			gamemode = newGameMode;
			prefs.putInteger("gamemode", gamemode);
			prefs.flush();
			goToStep(1);
		}
	}

	public void showChangeGameMode() {
		changeMode = new ChangeMode(this, gamemode);
		stage.addActor(changeMode);
		changeMode.setZIndex(stage.getActors().size);
		step = 4;

	}

	public void hideChangeGameMode() {
		// TODO Auto-generated method stub
		changeMode.remove();
		step = 1;
	}

	public void resetGame() {
		initGame();
		saveGameState();
	}

	public void initGame() {
		game.showAds(false);
		stage.clear();
		score = 0;
		level = 1;
		heath = 10;
		curGotDrop = 0;
		curLostDrop = 0;
		curMaxCombo = 0;
		curScore = 0;
		isLevelup = false;
		currentExposedSplash = 0;
		isGameOver = false;
		undoRemain = tableWith;
		step = 1;
		prefs.putString("prevGameState", "");
		cards = new Card[tableWith][tableWith];

		pieces = new ArrayList<WaterPiece>();
		addBackground();
		addLabelScore();
		addCardTable();
	}

	public void pressBack() {
		if (step == 0) {
			Gdx.app.exit();
		}
		if (step == 1) {
			step = 0;
			game.showExitMsg();
		}
		if (step == 3) {
			resultNote.remove();
			step = 1;
		}
		if (step == 2) {
			hideSetting();
		}
		if (step == 4) {
			hideChangeGameMode();
		}
	}

	public void addWaterPieces(float x, float y) {
		isRunning = true;
		WaterPiece piece1 = new WaterPiece(this, 1);
		piece1.setPosition(x - piece1.getWidth() / 2, y);
		pieces.add(piece1);
		stage.addActor(piece1);

		WaterPiece piece2 = new WaterPiece(this, 2);
		piece2.setPosition(x, y - piece2.getHeight() / 2);
		pieces.add(piece2);
		stage.addActor(piece2);

		WaterPiece piece3 = new WaterPiece(this, 3);
		piece3.setPosition(x - piece3.getWidth() / 2, y - piece3.getHeight());
		pieces.add(piece3);
		stage.addActor(piece3);

		WaterPiece piece4 = new WaterPiece(this, 4);
		piece4.setPosition(x - piece4.getWidth(), y - piece4.getHeight() / 2);
		pieces.add(piece4);
		stage.addActor(piece4);
	}

	public void updateScore(int score) {
		labelScore.setText("" + score);

	}

	public long getCardScore(int value) {
		if (value < 6) {
			return 0;
		}
		int value1 = value / 6;
		int log = (int) (Math.round(Math.log(value1) / Math.log(2)));
		int value2 = (int) Math.pow(3, log);
		return 6 * value2;
	}

	public boolean isMoving() {
		if (!pieces.isEmpty())
			return true;
		for (int i = 0; i < tableWith; i++) {
			for (int j = 0; j < tableWith; j++) {
				if (cards[i][j].moving) {
					return true;
				}
			}
		}
		return false;
	}

	private int getRandomValue() {
		int r = config.random(0, 35);
		if (r >= 0 && r < 6) {
			return 0;
		} else if (r >= 6 && r < 12) {
			return 1;
		} else if (r >= 12 && r < 21) {
			return 2;
		} else if (r >= 21 && r < 31) {
			return 3;
		} else if (r >= 31 && r < 36) {
			return 4;
		}
		return 0;
	}

	private boolean isAddable(int value) {
		if (value == 0) {
			return counts[value] < (8 + level / 5);
		} else if (value == 1) {
			return counts[value] < (6 + level / 5);
		} else if (value == 2) {
			return counts[value] < (10 + level / 5);
		} else if (value == 3) {
			return counts[value] < (10 - level / 5);
		} else if (value == 4) {
			return counts[value] < (6 - level / 5);
		}
		return true;
	}

	private float getPosition(int i) {
		return 6 * config.sizeMode + 15 * config.sizeMode * i + 7
				* config.sizeMode + 2 * config.sizeMode * i + getCardWidth()
				* i;

	}

	private float getYPosition(int i) {
		return 153 * config.sizeMode + 15 * config.sizeMode * i + 7
				* config.sizeMode + 2 * config.sizeMode * i + getCardWidth()
				* i;

	}

	private float getCardWidth() {
		return 61 * config.sizeMode;
	}

	public void addCardTable() {
		counts = new int[5];
		cards = new Card[6][6];
		for (int i = 0; i < tableWith; i++) {
			for (int j = 0; j < tableWith; j++) {
				Card card = new Card(this, 0);
				cards[i][j] = card;
				card.setWidth(getCardWidth());
				card.setHeight(getCardWidth());
				card.setPosition(getPosition(i), getYPosition(j));
				stage.addActor(card);
			}
		}

		for (int i = 0; i < 36; i++) {
			int col = config.random(0, tableWith - 1);
			int row = config.random(0, tableWith - 1);
			while (cards[row][col].getValue() > 0) {
				col = config.random(0, tableWith - 1);
				row = config.random(0, tableWith - 1);
			}
			int value = getRandomValue();
			while (!isAddable(value)) {
				value = getRandomValue();
			}
			cards[row][col].setValue(value);
			counts[value] = counts[value] + 1;
		}

	}

	public void saveGameState() {
		String gameState = "";
		for (int i = 0; i <= tableWith - 1; i++) {
			for (int j = 0; j <= tableWith - 1; j++) {
				gameState += cards[i][j].getValue() + ",";
			}
		}
		gameState += score + ",";
		gameState += heath + ",";
		gameState += level + ",";
		gameState += curLostDrop + ",";
		gameState += curGotDrop + ",";
		gameState += curMaxCombo + ",";
		gameState += curScore + ",";
		gameState += isLevelup ? 1 : 0;
		prefs.putString("currentGameState" + gamemode, gameState);
		prefs.flush();
	}

	public boolean checkEndGame() {
		for (int i = 0; i < tableWith; i++) {
			for (int j = 0; j < tableWith; j++) {
				if (cards[i][j].getValue() > 0) {
					return false;
				}
			}
		}
		return true;

	}

	public boolean checkGameOver() {
		return heath <= 0;

	}

	public void goToNextlevel() {
		isLevelup = false;
		lvlClearGroup.setVisible(false);
		lvlClearbg.setVisible(false);
		level++;
		curGotDrop = 0;
		curLostDrop = 0;
		curMaxCombo = 0;
		curScore = 0;
		updateLabelLevel();

		for (int i = 0; i < tableWith; i++) {
			for (int j = 0; j < tableWith; j++) {
				cards[i][j].remove();
			}
		}
		addCardTable();
		saveGameState();
		if (level == 5 || level >= 10 && level <= 25 || level == 30) {
			if (gamemode == 1)
				unlockAchievement(1, level);
		}
		game.showAds(false);

	}

	public void ShowNextLevel() {
		game.showAds(true);
		if (step == 2) {
			hideSetting();
		}
		if (step == 4) {
			hideChangeGameMode();
		}

		lvlClearLostDrop.setText("" + curLostDrop);
		lvlClearGotDrop.setText("" + curGotDrop + " + 1");
		lvlClearMaxCombo.setText("" + curMaxCombo);
		lvlClearScore.setText("" + curScore + " + 50");
		lvlClearbg.setZIndex(stage.getActors().size);
		lvlClearGroup.setVisible(true);
		lvlClearbg.setVisible(true);
		saveGameState();

	}

	public void changeHeath(int change) {
		if (gamemode == 1) {
			heath += change;
			if (change < 0) {
				curLostDrop -= change;
			} else {
				if (!isLevelup)
					curGotDrop += change;
			}
			if (heath > 20) {
				heath = 20;
				if (!isLevelup)
					curGotDrop -= 1;
			}
			updateLabelHeath();
			if (heath == 20) {
				if (gamemode == 1)
					unlockAchievement(3, 1);
			}
			if (change > 0 && !isSoundOff)
				MyGdxGame.sounds.get(config.SoundScore).play();
		}
	}

	public void exposeSplash() {
		currentExposedSplash++;
		if (currentExposedSplash > curMaxCombo) {
			curMaxCombo = currentExposedSplash;
		}
		score += currentExposedSplash;
		curScore += currentExposedSplash;
		updateLabelScore();
		if (currentExposedSplash >= 3 && currentExposedSplash % 3 == 0) {
			changeHeath(1);
		}

		if (currentExposedSplash >= 6 && currentExposedSplash <= 36
				&& currentExposedSplash % 3 == 0) {
			if (gamemode == 1)
				unlockAchievement(2, currentExposedSplash);
		}

	}

	public void loadCardTable(String gameState) {
		if (cards[0][0] == null) {
			for (int i = 0; i < tableWith; i++) {
				for (int j = 0; j < tableWith; j++) {
					Card card = new Card(this, 0);
					cards[i][j] = card;
					card.setPosition(getPosition(i), getYPosition(j));
					stage.addActor(card);
				}
			}
		}
		String[] list = gameState.split(",");
		for (int i = 0; i < tableWith * tableWith; i++) {
			int col = i % tableWith;
			int row = i / tableWith;
			cards[row][col].setValue(Integer.parseInt(list[i]));
		}

		if (list.length >= tableWith * tableWith + 1)
			score = Integer.parseInt(list[tableWith * tableWith]);
		if (labelScore == null) {
			addLabelScore();
		}
		updateLabelScore();

		if (list.length >= tableWith * tableWith + 2)
			heath = Integer.parseInt(list[tableWith * tableWith + 1]);
		updateLabelHeath();

		if (list.length >= tableWith * tableWith + 3)
			level = Integer.parseInt(list[tableWith * tableWith + 2]);
		updateLabelLevel();

		if (list.length >= tableWith * tableWith + 4)
			curLostDrop = Integer.parseInt(list[tableWith * tableWith + 3]);
		if (list.length >= tableWith * tableWith + 5)
			curGotDrop = Integer.parseInt(list[tableWith * tableWith + 4]);
		if (list.length >= tableWith * tableWith + 6)
			curMaxCombo = Integer.parseInt(list[tableWith * tableWith + 5]);
		if (list.length >= tableWith * tableWith + 7)
			curScore = Integer.parseInt(list[tableWith * tableWith + 6]);
		if (list.length >= tableWith * tableWith + 8)
			isLevelup = Integer.parseInt(list[tableWith * tableWith + 7]) == 1;

	}

	private void addBackground() {
		Image bg = new Image(atlas2.findRegion("background1"));
		bg.setWidth(480 * config.sizeMode);
		bg.setHeight(800 * config.sizeMode);
		bg.setPosition(0, 0);
		bg.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (step == 2) {
					hideSetting();
				}
				if (step == 3) {
					resultNote.remove();
					step = 1;
				}
				if (step == 4) {
					hideChangeGameMode();
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});

		stage.addActor(bg);

		tablebg = new Image(atlas2.findRegion("tablebg"));
		tablebg.setPosition(6 * config.sizeMode, 153 * config.sizeMode);
		stage.addActor(tablebg);

		lvlClear = new Image(atlas2.findRegion("lvlclear"));
		lvlClearGroup = new Group();
		lvlClearGroup.setWidth(lvlClear.getWidth());
		lvlClearGroup.setHeight(lvlClear.getHeight());
		lvlClearGroup.setPosition(
				MyGdxGame.VIEWPORT.x / 2 - lvlClear.getWidth() / 2,
				tablebg.getY() + tablebg.getHeight() / 2 - lvlClear.getHeight()
						/ 2);
		lvlClearGroup.setVisible(false);

		lvlClear = new Image(atlas2.findRegion("lvlclear"));
		lvlClear.setPosition(0, 0);

		LabelStyle textStyle = new LabelStyle();
		textStyle.font = new BitmapFont(
				Gdx.files.internal("data/arial16white.fnt"),
				Gdx.files.internal("data/arial16white.png"), false);
		lvlClearLostDrop = new Label("" + curLostDrop, textStyle);
		lvlClearLostDrop.setPosition(lvlClear.getX() + 183 * config.sizeMode,
				lvlClear.getY() + 144 * config.sizeMode);

		lvlClearGotDrop = new Label("" + curGotDrop + " + 1", textStyle);
		lvlClearGotDrop.setPosition(lvlClear.getX() + 178 * config.sizeMode,
				lvlClear.getY() + 114 * config.sizeMode);

		lvlClearMaxCombo = new Label("" + curMaxCombo, textStyle);
		lvlClearMaxCombo.setPosition(lvlClear.getX() + 356 * config.sizeMode,
				lvlClear.getY() + 143 * config.sizeMode);

		lvlClearScore = new Label("" + curScore + " + 50", textStyle);
		lvlClearScore.setPosition(lvlClear.getX() + 312 * config.sizeMode,
				lvlClear.getY() + 116 * config.sizeMode);

		lvlClearbg = new Image(atlas2.findRegion("lvlclearbg"));
		lvlClearbg.setWidth(lvlClear.getWidth());
		lvlClearbg.setHeight(lvlClear.getHeight());
		lvlClearbg.setPosition(MyGdxGame.VIEWPORT.x / 2 - lvlClear.getWidth()
				/ 2,
				tablebg.getY() + tablebg.getHeight() / 2 - lvlClear.getHeight()
						/ 2);
		lvlClearbg.setVisible(false);
		ClickListener lvlClearClick = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				goToNextlevel();
				super.touchUp(event, x, y, pointer, button);
			}
		};
		lvlClearbg.addListener(lvlClearClick);

		lvlClearGroup.addActor(lvlClear);
		lvlClearGroup.addActor(lvlClearLostDrop);
		lvlClearGroup.addActor(lvlClearGotDrop);
		lvlClearGroup.addActor(lvlClearMaxCombo);
		lvlClearGroup.addActor(lvlClearScore);
		// lvlClearGroup.addActor(lvlClearbg);
		stage.addActor(lvlClearGroup);
		stage.addActor(lvlClearbg);

		setting = new Image(atlas1.findRegion("setting"));
		setting.setPosition(6 * config.sizeMode, tablebg.getY() - 7
				* config.sizeMode - setting.getHeight());
		setting.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				showSetting();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		stage.addActor(setting);

		achiveicon = new Image(atlas1.findRegion("rankicon"));
		achiveicon.setPosition(setting.getX() + setting.getWidth() + 5
				* config.sizeMode, setting.getY());
		achiveicon.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				showAchievement();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		stage.addActor(achiveicon);

		panelBest = new Image(atlas1.findRegion("best"));
		panelBest.setPosition(
				tablebg.getX() + tablebg.getWidth() - panelBest.getWidth(),
				achiveicon.getY());
		panelBest.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				showLeaderBoard();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		stage.addActor(panelBest);

		panelLevel = new Image(atlas1.findRegion("scorebg"));
		panelLevel.setPosition(tablebg.getX(),
				tablebg.getY() + tablebg.getHeight() + 5 * config.sizeMode);
		stage.addActor(panelLevel);

		panelScore = new Image(atlas1.findRegion("scorebg"));
		panelScore.setPosition(tablebg.getX(),
				panelLevel.getY() + panelLevel.getHeight() + 3
						* config.sizeMode);
		stage.addActor(panelScore);

		// panelScore = new Image(atlas1.findRegion("scorepanel"));
		// panelScore.setPosition(panelBest.getX() - 5 - panelScore.getWidth(),
		// panelBest.getY());
		//
		// stage.addActor(panelScore);

		rateicon = new Image(atlas2.findRegion("gametitle"));
		rateicon.setPosition(MyGdxGame.VIEWPORT.x - rateicon.getWidth() - 5
				* config.sizeMode, MyGdxGame.VIEWPORT.y - rateicon.getHeight()
				- 5 * config.sizeMode);

		stage.addActor(rateicon);

	}

	private void addLabelScore() {
		LabelStyle textStyle1 = new LabelStyle();
		textStyle1.font = new BitmapFont(
				Gdx.files.internal("data/gothic24.fnt"),
				Gdx.files.internal("data/gothic24.png"), false);

		labelScore = new Label("SCORE: " + score, textStyle1);
		labelScore.setPosition(panelScore.getX() + 5 * config.sizeMode,
				panelScore.getY() + 2 * config.sizeMode);

		stage.addActor(labelScore);

		labelLevel = new Label("LEVEL: " + level, textStyle1);
		labelLevel.setPosition(panelLevel.getX() + 5 * config.sizeMode,
				panelLevel.getY() + 2 * config.sizeMode);

		stage.addActor(labelLevel);

		labelHeath = new Image(atlas1.findRegion("heath", heath));
		labelHeath.setPosition(tablebg.getX() + tablebg.getWidth() - 18
				* config.sizeMode - labelLevel.getWidth(), tablebg.getY()
				+ tablebg.getHeight() + 5 * config.sizeMode);

		stage.addActor(labelHeath);

		int highScore = prefs.getInteger("highScore" + gamemode);
		labelBest = new Label("" + highScore, textStyle1);
		labelBest.setPosition(panelBest.getX() + 65 * config.sizeMode,
				panelBest.getY() + 15 * config.sizeMode);
		labelBest.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				showLeaderBoard();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		stage.addActor(labelBest);
	}

	public void updateLabelScore() {
		labelScore.setText("SCORE: " + score);
	}

	public void updateLabelLevel() {
		labelLevel.setText("LEVEL: " + level);
	}

	public void updateLabelHeath() {
		labelHeath.setDrawable(new SpriteDrawable(new Sprite(atlas1.findRegion(
				"heath", heath))));
	}

	@Override
	public void resize(int width, int height) {
		// resize the stage
		// stage.setViewport(width, height, false );
		//stage.setViewport(MyGdxGame.VIEWPORT.x, MyGdxGame.VIEWPORT.y, false);
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void render(float delta) {

		if (isRunning && !isMoving()) {
			isRunning = false;
			currentExposedSplash = 0;
			if (checkEndGame()) {
				isLevelup = true;
				score += 50;
				updateLabelScore();
				changeHeath(1);
				ShowNextLevel();

			} else {
				if (checkGameOver()) {
					stopGame();
				}
				saveGameState();
			}

		}

		// update the action of actors
		stage.act(delta);

		// clear the screen with the given RGB color (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// draw the actors
		stage.draw();
	}

	@Override
	public void hide() {
		// dispose the resources by default
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}