package com.androidcookbook.simplejumper;

import org.flixel.FlxGame;

public class FlixelGame extends FlxGame
{
	public FlixelGame()
	{
		super(400, 240, PlayState.class, 2, 50, 50, false);
	}
}
