package com.androidcookbook.simplejumper;

import org.flixel.FlxG;
import org.flixel.FlxObject;
import org.flixel.FlxSprite;
import org.flixel.FlxState;
import org.flixel.FlxText;
import org.flixel.FlxTilemap;

import com.badlogic.gdx.utils.IntArray;

public class PlayState extends FlxState {
	private FlxSprite mPlayer;
	private FlxTilemap mLevel;
	private FlxSprite mExit;
	
	@Override
	public void create() {
		add(new FlxText(0, 0, 200, "SimpleJumper 0.0"));
		add(mPlayer = new Droid(50, 50));
		
		// Create a new tilemap using our level data
		mLevel = new FlxTilemap();
		mLevel.loadMap(FlxTilemap.arrayToCSV(new IntArray(RoomLayout.DATA), 40), FlxTilemap.ImgAuto, 0, 0, FlxTilemap.AUTO);
		add(mLevel);
		
		//Create the level exit, a dark gray box that is hidden at first
		mExit = new FlxSprite(35*8+1,25*8);
		mExit.makeGraphic(14,16,0xff3f3f3f);
		mExit.exists = false;
		add(mExit);
	}
	
    @Override
	public void update() {
    	if (FlxG.keys.justPressed("Q")) {
    		mPlayer.kill();
    		System.exit(0);
    		return;
    	}
		// Smooth slidey walking controls
    	mPlayer.acceleration.x = 0;
		if (FlxG.keys.LEFT)
			mPlayer.acceleration.x -= mPlayer.drag.x;
		if (FlxG.keys.RIGHT)
			mPlayer.acceleration.x += mPlayer.drag.x;
		if (FlxG.keys.justPressed("SPACE") && mPlayer.isTouching(FlxObject.FLOOR))
			mPlayer.velocity.y = -mPlayer.maxVelocity.y/2;
		
		// Updates all the objects using physics rules
		super.update();

		if (mPlayer.isTouching(FlxObject.FLOOR)) {
			// Jump controls
			if (FlxG.keys.UP) {

				mPlayer.velocity.y = -mPlayer.acceleration.y * 0.51f;
				mPlayer.play("jump");

			} // Animations
			else if (mPlayer.velocity.x > 0) {
				mPlayer.play("walk");
			} else if (mPlayer.velocity.x < 0) {
				mPlayer.play("walk_back");
			} else
				mPlayer.play("idle");
		} else if (mPlayer.velocity.y < 0)
			mPlayer.play("jump");
		else
			mPlayer.play("flail");
      }
    
}