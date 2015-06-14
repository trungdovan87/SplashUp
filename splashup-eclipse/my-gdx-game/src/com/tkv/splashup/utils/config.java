package com.tkv.splashup.utils;
import java.util.Random;

public class config {

    //key de nap cac sound vao mang va lay sound ra de play
    public static String SoundExpose = "expose";
    public static String SoundScore = "score";    
    public static String SoundGrow = "grow";

    
    //chieu cao va chieu dai thiet ke cua hinh land.png
    public static int kLandHeight = 112;
   
    
    //chieu cao va thoi gian khi bay len do "tap"
   // public static int kjumpHeight = 75;
    //public static float kjumpDura = 0.28f;
    public static int kjumpHeight = 56;
    public static float kjumpDura = 0.224f;
    //khoang thoi gian them ong nuoc vao man hinh
    public static float kTimeAddPipe = 1.5f;
    
    //khoang ho giua hai ong nuoc
    public static float kHoleBetweenPipes = 90;
    
    public static int sizeMode = 1;
   public static float kLandWidth = 336  ;//* sizeMode;
    
    public static float kmoveLeftDura = 1.5f;
    //ham tinh random mot so trong pham vi tu min den max
    public static int random(int min, int max)
    {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}