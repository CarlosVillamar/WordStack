/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.wordstack;

import android.content.ClipData;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.service.quicksettings.Tile;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2, dragWord1 = " ", dragWord2 = " ";
    ViewGroup word1Layout, word2Layout;
    Stack<LetterTile> placedTiles = new Stack<>();
    View word1LinearLayout, word2LinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();

        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = in.readLine()) != null) {
                String word = line.trim();
                if (word.length() == WORD_LENGTH) {
                    words.add(word);
                }

            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        word1LinearLayout = findViewById(R.id.word1);
        //word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());

        word2LinearLayout = findViewById(R.id.word2);
        //word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());


    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
                LetterTile tile = (LetterTile) stackedLayout.peek();
                tile.moveToViewGroup((ViewGroup) v);

                if (stackedLayout.empty()) {
                    TextView messageBox = (TextView) findViewById(R.id.message_box);
                    messageBox.setText(word1 + " " + word2);
                }
                placedTiles.push(tile);
                return true;


            }


            return false;
        }
    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //when tile is moved
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    //when tile enters the layout area
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    //when tile exits the layout area
                    v.setBackgroundColor(LIGHT_BLUE);
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

                    //loop added to a copy of the word to be used to compare them in winConditon method
                    if (v == word1LinearLayout) {
                        dragWord1 += word1;
                    } else dragWord2 += word2;


                    if (stackedLayout.empty()) {
                        /*TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);*/
                        winCondition();

                    }
                    placedTiles.push(tile);

                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {
          /*create an algorithm to shuffle the character in
        the string without changed the order.*/

        //setting up required attributes for this button
        word1Layout = findViewById(R.id.word1);
        word2Layout = findViewById(R.id.word2);
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        String wordScrambled = "";

        //clearing out the previous game with each start press
        word1Layout.removeAllViews();
        word2Layout.removeAllViews();
        stackedLayout.clear();

        //Get words at random and create a do-while loop to ensure words are the same size,
        //while being more time efficient
        word1 = words.get(random.nextInt(words.size()));
        word2 = "";

        do {
            word2 = words.get(random.nextInt(words.size()));
        } while (word1 == word2);

        //counters are needed for each word to compare word length
        int word1Counter = 0;
        int word2Counter = 0;

      /*we need to make sure if either counter when compared to the deafult word length match, that
      depending on the condition met that the word that meets the condition is in position to become scrambled
      */

        while (word1Counter < WORD_LENGTH || word1Counter < WORD_LENGTH) {
            if (random.nextInt(2) == 0 && word1Counter < word1.length()) {
                wordScrambled += word1.charAt(word1Counter);
                word1Counter++;
            } else if (word2Counter < word2.length()) {
                wordScrambled += word2.charAt(word2Counter);
                word2Counter++;
            }
        }

        //Toast.makeText(getBaseContext(), "Scrambled Word", Toast.LENGTH_SHORT).show();
        //messageBox.setText(wordScrambled);

        for (int i = wordScrambled.length() - 1; i > 0.; i--) {
            //Allows for each character with in the scrambled word to be placed in a stack one
            // character at a time
            stackedLayout.push(new LetterTile(this, wordScrambled.charAt(i)));
            Log.d("StartGame", String.valueOf(wordScrambled.charAt(i)));
        }
        Toast.makeText(getBaseContext(), "Scrambled Word Selected", Toast.LENGTH_SHORT).show();

        return true;
    }

    public boolean onUndo(View view) {
        //allows to go back one placed tile at a time
        if (!placedTiles.empty()) {
            /*we needed to add a condition to make sure the button wouldn't
             crash the app once all of the letters returned to the stack, making sure the stack
            was not empty was the easiest fix for this issue*/
            placedTiles.pop().moveToViewGroup(stackedLayout);
            //pop off from top of stack LIFO

           // tile.moveToViewGroup(stackedLayout);
            //would like to return this to change the conditons to check the view groups
            // for each word instead of the stack

        }
        return true;
    }

    public void winCondition() {
        //extension milestone
        //once all of the tiles are placed check if player won
        TextView messageBox = findViewById(R.id.message_box);
        if (word1 == dragWord1 && word2 == dragWord2) {
            //if both words match there original selfs
            messageBox.setText("You Win!!!" + word1 + "" + word2);

        } else if (words.contains(dragWord1) || words.contains(dragWord2)) {
            //if either word are in the list
            messageBox.setText("look you found different words" + word1 + "" + word2);
        } else {
            messageBox.setText("Can you find the words you're looking forrrrrrr");
        }
    }
}
