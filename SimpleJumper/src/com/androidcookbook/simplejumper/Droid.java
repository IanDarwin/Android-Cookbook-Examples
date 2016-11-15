package com.androidcookbook.simplejumper;

import org.flixel.FlxG;
import org.flixel.FlxSound;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.ui.FlxInputText;

public class Droid extends FlxSprite {
    private final FlxSound sound = new FlxSound();

    public Droid(int X, int Y) {
        super(X, Y);
        // loadGraphic(R.drawable.player, true, true);
        maxVelocity.x = 100; // walking speed
        acceleration.y = 400; // gravity
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
    public void update() {
        // Smooth slidey walking controls
        acceleration.x = 0;
        if (FlxG.keys.LEFT)
        	acceleration.x -= drag.x;
        if (FlxG.keys.RIGHT)
        	acceleration.x += drag.x;

        if (isTouching(FLOOR)) {
            // Jump controls
            if (FlxG.keys.UP) {
                // sound.loadEmbedded(R.raw.jump);
                // sound.play();

                velocity.y = -acceleration.y * 0.51f;
                play("jump");

              }// Animations
            else if (velocity.x > 0) {
                play("walk");
              }
            else if (velocity.x < 0) {
                play("walk_back");
              }
            else play("idle");
          }
        else if (velocity.y < 0) play("jump");
        else play("flail");

        // Default object physics update
        super.update();
      }
}
