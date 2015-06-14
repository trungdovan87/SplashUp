package com.tkv.splashup.android;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.tkv.splashup.IActivityRequestHandler;

public class AndroidLauncher extends AndroidApplication implements
		GameHelper.GameHelperListener, IActivityRequestHandler {
	GameHelper gameHelper;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	protected AdView adView;
	protected AdView adView1;
	private boolean isRequestingPermission = false;
	Bitmap currentScreen;
	private int scoreSubmiting;
	private int tableWidthSubmiting;
	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_ADS: {
					// adView.pause();
					adView.setVisibility(View.VISIBLE);
					break;
				}
				case HIDE_ADS: {
					adView.setVisibility(View.GONE);
					// adView.resume();
					break;
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the layout
		RelativeLayout layout = new RelativeLayout(this);

		// Do the stuff that initialize() would do for you
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.setMaxAutoSignInAttempts(0);

		gameHelper.setup(this);

		// Create the libgdx View
		View gameView = initializeForView(new MyGdxGame(this), false);

		// Create and setup the AdMob view
		adView = new AdView(this);
		adView.setAdUnitId(getResources().getString(R.string.ad_id));
		adView.setAdSize(AdSize.SMART_BANNER);

		adView.loadAd(new AdRequest.Builder().build());

		// Add the libgdx view
		layout.addView(gameView);

		// Add the AdMob view
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		layout.addView(adView, adParams);
		// Hook it all up
		setContentView(layout);

	}

	// This is the callback that posts a message for the handler
	@Override
	public void showAds(boolean show) {
		try {
			handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
		} catch (Exception ex) {
		}

	}

	public void shareFacebook() {

		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/jpeg");

		try {

			Bitmap icon = captureScreenShot();
			ContentValues values = new ContentValues();
			values.put(Images.Media.TITLE, "title");
			values.put(Images.Media.MIME_TYPE, "image/*");
			Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
					values);
			OutputStream outstream = getContentResolver().openOutputStream(uri);
			icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
			/*
			 * String url = Images.Media.insertImage(getContentResolver(), icon,
			 * "title", null); Uri phototUri = Uri.parse(url);
			 */
			// share.putExtra(Intent.EXTRA_SUBJECT,
			// "Play Now! https://play.google.com/store/apps/details?id=com.tkv.stupidbird");

			share.putExtra(
					Intent.EXTRA_TEXT,
					" I'm playing SPLASH UP. Can you beat my high score?"
							+ "\n\n"
							+ "https://play.google.com/store/apps/details?id=com.tkv.splashup");
			share.putExtra(Intent.EXTRA_STREAM, uri);

			startActivity(Intent.createChooser(share, "Share Image"));
			// outstream.close();
		} catch (Exception e) {
			// System.err.println(e.toString());
			// Log.d("This is the output", e.toString());
		}
	}

	public void shareLink() {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, "SPLASH UP");
		String sAux = "\n"
				+ " I'm playing SPLASH UP. Can you beat my high score?"
				+ "\n\n";
		sAux = sAux
				+ "https://play.google.com/store/apps/details?id=com.tkv.splashup";
		i.putExtra(Intent.EXTRA_TEXT, sAux);
		startActivity(Intent.createChooser(i, "choose one"));
	}

	public Pixmap getScreenshot(int x, int y, int w, int h, boolean flipY) {
		Gdx.gl.glPixelStorei(GL10.GL_PACK_ALIGNMENT, 1);

		final Pixmap pixmap = new Pixmap(w, h, Format.RGBA8888);
		ByteBuffer pixels = pixmap.getPixels();
		Gdx.gl.glReadPixels(x, y, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE,
				pixels);

		final int numBytes = w * h * 4;
		byte[] lines = new byte[numBytes];
		if (flipY) {
			final int numBytesPerLine = w * 4;
			for (int i = 0; i < h; i++) {
				pixels.position((h - i - 1) * numBytesPerLine);
				pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
			}
			pixels.clear();
			pixels.put(lines);
		} else {
			pixels.clear();
			pixels.get(lines);
		}

		return pixmap;
	}

	public Bitmap captureScreenShot() {
		int width = Gdx.graphics.getWidth(); // use your favorite width
		int height = Gdx.graphics.getHeight(); // use your favorite height
		int screenshotSize = width * height;
		ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
		bb.order(ByteOrder.nativeOrder());
		// any opengl context will do

		Gdx.gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA,
				GL10.GL_UNSIGNED_BYTE, bb);
		int pixelsBuffer[] = new int[screenshotSize];
		bb.asIntBuffer().get(pixelsBuffer);
		bb = null;
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		bitmap.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0,
				width, height);
		pixelsBuffer = null;

		short sBuffer[] = new short[screenshotSize];
		ShortBuffer sb = ShortBuffer.wrap(sBuffer);
		bitmap.copyPixelsToBuffer(sb);

		// Making created bitmap (from OpenGL points) compatible with Android
		// bitmap
		for (int i = 0; i < screenshotSize; ++i) {
			short v = sBuffer[i];
			sBuffer[i] = (short) (((v & 0x1f) << 11) | (v & 0x7e0) | ((v & 0xf800) >> 11));
		}
		sb.rewind();
		bitmap.copyPixelsFromBuffer(sb);
		return bitmap;
	}

	private void showToast(final String message) {

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// Log.e("exception", e.toString());
				}

			}
		});

	}

	private void GetKeyHash() {
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo("com.tkv.stupidbird",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());

				String so = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				String something = new String(Base64.encode(md.digest(), 0));
				// String something = new
				// String(Base64.encodeBytes(md.digest()));
				Log.i("hash key", something);
				Log.i("KeyHash:", so);
			}
		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		if (scoreSubmiting > 0) {
			showLeaderBoard(scoreSubmiting, tableWidthSubmiting);
		}
		scoreSubmiting = 0;
	}

	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
		EasyTracker.getInstance(this).activityStart(this); // Add this method.

	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}

	@Override
	public void logon() {
		// TODO Auto-generated method stub

		try {
			gameHelper.beginUserInitiatedSignIn();
		} catch (final Exception ex) {
		}
	}

	@Override
	public void logoff() {
		// TODO Auto-generated method stub
		try {
			gameHelper.signOut();
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScore(int score, int gamemode) {
		if (gameHelper.isSignedIn()) {
			if (gamemode == 1) {
				if (score > 0) {
					String leaderCode = "CgkIqJCt8PsYEAIQBQ";
					Games.Leaderboards.submitScore(gameHelper.getApiClient(),
							leaderCode, score);
				}
			}

		} else {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void showLeaderBoard(int maxScore, int gamemode) {
		// TODO Auto-generated method stub
		if (gameHelper.isSignedIn()) {
			submitScore(maxScore, gamemode);
			String leaderCode = "CgkIqJCt8PsYEAIQBQ";
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							gameHelper.getApiClient(), leaderCode), 100);
		} else {
			// TODO Auto-generated method stub
			scoreSubmiting = maxScore;
			tableWidthSubmiting = gamemode;
			logon();
		}

	}

	@Override
	public boolean unlockAchievement(int type, int value) {
		// TODO Auto-generated method stub
		if (gameHelper.isSignedIn()) {
			String code = "";
			if (type == 1) {
				if (value == 5)
					code = getResources().getString(
							R.string.achievement_reach_level_5);
				if (value == 10)
					code = getResources().getString(
							R.string.achievement_reach_level_10);
				if (value == 11)
					code = getResources().getString(
							R.string.achievement_reach_level_11);
				if (value == 12)
					code = getResources().getString(
							R.string.achievement_reach_level_12);
				if (value == 13)
					code = getResources().getString(
							R.string.achievement_reach_level_13);
				if (value == 14)
					code = getResources().getString(
							R.string.achievement_reach_level_14);
				if (value == 15)
					code = getResources().getString(
							R.string.achievement_reach_level_15);
				if (value == 16)
					code = getResources().getString(
							R.string.achievement_reach_level_16);
				if (value == 17)
					code = getResources().getString(
							R.string.achievement_reach_level_17);
				if (value == 18)
					code = getResources().getString(
							R.string.achievement_reach_level_18);
				if (value == 19)
					code = getResources().getString(
							R.string.achievement_reach_level_19);
				if (value == 20)
					code = getResources().getString(
							R.string.achievement_reach_level_20);
				if (value == 21)
					code = getResources().getString(
							R.string.achievement_reach_level_21);
				if (value == 22)
					code = getResources().getString(
							R.string.achievement_reach_level_22);
				if (value == 23)
					code = getResources().getString(
							R.string.achievement_reach_level_23);
				if (value == 24)
					code = getResources().getString(
							R.string.achievement_reach_level_24);
				if (value == 25)
					code = getResources().getString(
							R.string.achievement_reach_level_25);
				if (value == 30)
					code = getResources().getString(
							R.string.achievement_reach_level_30);

			} else if (type == 2) {
				if (value == 6)
					code = getResources().getString(
							R.string.achievement_explode_6_splash_in_a_row);
				if (value == 9)
					code = getResources().getString(
							R.string.achievement_explode_9_splash_in_a_row);
				if (value == 12)
					code = getResources().getString(
							R.string.achievement_explode_12_splash_in_a_row);
				if (value == 15)
					code = getResources().getString(
							R.string.achievement_explode_15_splash_in_a_row);
				if (value == 18)
					code = getResources().getString(
							R.string.achievement_explode_18_splash_in_a_row);
				if (value == 21)
					code = getResources().getString(
							R.string.achievement_explode_21_splash_in_a_row);
				if (value == 24)
					code = getResources().getString(
							R.string.achievement_explode_24_splash_in_a_row);
				if (value == 27)
					code = getResources().getString(
							R.string.achievement_explode_27_splash_in_a_row);
				if (value == 30)
					code = getResources().getString(
							R.string.achievement_explode_30_splash_in_a_row);
				if (value == 33)
					code = getResources().getString(
							R.string.achievement_explode_33_splash_in_a_row);
				if (value == 36)
					code = getResources().getString(
							R.string.achievement_explode_36_splash_in_a_row);
			} else if (type == 3) {
				code = getResources().getString(
						R.string.achievement_full_20_drops);
			}
			if (code != "") {
				Games.Achievements.unlock(gameHelper.getApiClient(), code);
			}
			return true;

		} else {
			return false;
		}

	}

	@Override
	public void showAchievement() {
		// TODO Auto-generated method stub
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(gameHelper
							.getApiClient()), 100);

		} else {
			logon();
		}

	}

	@Override
	public void showExitMsg() {
		// TODO Auto-generated method stub
		showToast("Press back once more to exit.");
	}

	public static int random(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
}