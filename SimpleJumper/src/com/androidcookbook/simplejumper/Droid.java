package com.androidcookbook.simplejumper;

import org.flixel.FlxSprite;

public class Droid extends FlxSprite {

    public Droid(int X, int Y) {
        super(X, Y);
        // loadGraphic("player", true, true);
        maxVelocity.x = 100; // walking speed
        acceleration.y = 10; // gravity
        drag.x = maxVelocity.x * 4; // deceleration (sliding to a stop)

        // tweak the bounding box for better feel
        width = 8;
        height = 10;

        offset.x = 3;
        offset.y = 3;

        addAnimation("idle", new int[] { 0 }, 0, false);
        addAnimation("walk", new int[] { 1, 2, 3, 0 }, 12);
        addAnimation("walk_back", new int[] { 3, 2, 1, 0 }, 10, true);
        addAnimation("flail", new int[] { 1, 2, 3, 0 }, 18, true);
        addAnimation("jump", new int[] { 4 }, 0, false);
      }
    
    @Override
    public void play(String animName) {
    	// System.err.println("Playing " + animName);
    	super.play(animName);
    }
}
