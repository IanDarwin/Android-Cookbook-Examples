package com.androidcookbook.simplejumper;

import org.flixel.FlxState;
import org.flixel.FlxText;

public class PlayState extends FlxState {
	@Override
	public void create() {
		add(new FlxText(0, 0, 200, "SimpleJumper 0.0"));
		add(new Droid(50, 50));
	}
}