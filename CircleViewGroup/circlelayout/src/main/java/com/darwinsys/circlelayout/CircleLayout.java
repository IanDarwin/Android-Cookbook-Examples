package com.darwinsys.circlelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * A ViewGroup that lays out its (roughly equal-sized) children in a circle
 * @author Ian Darwin
 */

public class CircleLayout extends ViewGroup {

    /** Required Constructor */
    public CircleLayout(Context ctx) {
        super(ctx);
    }

    /** Required Constructor */
    public CircleLayout(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    /**
     * Perform layout calculations for children.
     * changed	boolean: This is a new size or position for this view
     * @param left	int: Left position, relative to parent
     * @param top	int: Top position, relative to parent
     * @param right	int: Right position, relative to parent
     * @param bottom	int: Bottom position, relative to parent
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        throw new UnsupportedOperationException("Write code before running!");
    }
}
