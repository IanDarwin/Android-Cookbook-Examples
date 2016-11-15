package com.androidcookbook.flixeldemo;

import org.flixel.FlxGame;
import org.flixel.FlxGameView;
import android.content.Context;

public class GameView extends FlxGameView {
    public GameView(Context context, Class<? extends Object> resource) {
        super(new FlxGame(400, 240, SimpleJumper.class, context, resource), context);
    }
}
