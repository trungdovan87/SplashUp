package com.tkv.splashup.helper;

/**
 * Truoc khi run game, phai kiem tra platform thuoc loai android hay desktop.
 *
 */
public class Constant {
	private static int PLATFORM = 0;
	
	private static final int DESKTOP = 0;
	private static final int ANDROID = 1;
	
	
	public Constant() {
		super();
		
	}
	
	public static final int TOUCH = 0;
	public static final int ACCELEROMETER = 1;
	public static int GAME_STYLE = Constant.TOUCH;
	
	public static float STAGE_WIDTH = 800;
	public static float STAGE_HEIGHT = 480;
}
