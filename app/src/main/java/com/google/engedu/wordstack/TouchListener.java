package com.google.engedu.wordstack;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TouchListener implements View.OnTouchListener {
    private MainActivity mainActivity;

    public TouchListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * Class not currently in use
     * */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !mainActivity.stackedLayout.empty()) {
            LetterTile tile = (LetterTile) mainActivity.stackedLayout.peek();
            tile.moveToViewGroup((ViewGroup) v);

            if (mainActivity.stackedLayout.empty()) {
                TextView messageBox = mainActivity.findViewById(R.id.message_box);
                messageBox.setText(mainActivity.word1 + " " + mainActivity.word2);
            }
            mainActivity.placedTiles.push(tile);
            return true;


        }


        return false;
    }
}
