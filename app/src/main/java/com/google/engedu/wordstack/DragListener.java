package com.google.engedu.wordstack;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

class DragListener implements View.OnDragListener {

    private MainActivity mainActivity;

    public DragListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                //when tile is moved
                v.setBackgroundColor(MainActivity.LIGHT_BLUE);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                //when tile enters the layout area
                v.setBackgroundColor(MainActivity.LIGHT_GREEN);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                //when tile exits the layout area
                v.setBackgroundColor(MainActivity.LIGHT_BLUE);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                //when tile is placed
                v.setBackgroundColor(Color.WHITE);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign Tile to the target Layout
                LetterTile tile = (LetterTile) event.getLocalState();
                tile.moveToViewGroup((ViewGroup) v);


//                if (mainActivity.stackedLayout.empty()) {
//                    /*TextView messageBox = (TextView) findViewById(R.id.message_box);
//                    messageBox.setText(word1 + " " + word2);*/
//
//                    GameLogic.winCondition(v);
//                }

                return true;
        }
        return false;
    }
}
