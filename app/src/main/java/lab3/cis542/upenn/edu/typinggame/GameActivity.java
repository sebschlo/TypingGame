package lab3.cis542.upenn.edu.typinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sebastian on 11/10/14.
 *
 * This view is where the game actually happens.
 *
 */

public class GameActivity extends Activity implements View.OnClickListener {

    Button submitButton;
    Button cancelButton;

    TextView mainTextView;
    EditText mainEditText;

    SentenceGenerator senGen = new SentenceGenerator();

    private long startTime;

    private static final int READY_DIALOG = 1;
    private static final int CORRECT_DIALOG = 2;
    private static final int INCORRECT_DIALOG = 3;

    private int DIFFICULTY;
    private String userName;

    private static final String PREFS = "prefs";
    SharedPreferences mSharedPreferences;

    private Handler handler;

    private ImageView imView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        // set up up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // set up difficulty and user name
        DIFFICULTY = this.getIntent().getExtras().getInt("difficulty");
        userName = this.getIntent().getExtras().getString("userName");

        // Set up the button event listeners
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        // set up text view
        mainTextView = (TextView) findViewById(R.id.target);

        // Set up edit text
        mainEditText = (EditText) findViewById(R.id.userInput);

        // show ready dialog
        showAlertDialog(READY_DIALOG);

        // set up handler
        handler = new Handler();

        // set up image view for happy/sad face
        imView = (ImageView) findViewById(R.id.faceView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // Determine which button was clicked
        switch (v.getId()) {
            case R.id.submit_button:
                //get text from edit text
                String userinput = mainEditText.getText().toString().trim();
                String target = mainTextView.getText().toString();

                // test if user entered the sentence correctly
                if (userinput.equals(target)) {
                    showAlertDialog(CORRECT_DIALOG);
                } else {
                    showAlertDialog(INCORRECT_DIALOG);
                }
                break;

            case R.id.cancel_button:
                finish();
                break;
        }
    }



    private void showAlertDialog(int id) {

        // get the best times from shared preferences
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        final float bestTime = mSharedPreferences.getFloat(String.format("%dBest",DIFFICULTY), -1);
        float worstTime = mSharedPreferences.getFloat(String.format("%dWorst",DIFFICULTY), -1);
        SharedPreferences.Editor editor = mSharedPreferences.edit();


        // start building alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (id == READY_DIALOG) {
            // set custom text for dialog
            builder.setTitle(getResources().getString(R.string.readyDialog));
            builder.setMessage(getResources().getString(R.string.readyDialog2));

            // this button actions
            builder.setPositiveButton("Let's go!", new DialogInterface.OnClickListener() {
                // this is the method to call when the button is clicked
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // save the start time
                    startTime = System.currentTimeMillis();
                    changeToMidFaceIn(bestTime);
                    // create the first sentence
                    mainTextView.setText(senGen.generate(DIFFICULTY));
                    // this will hide the dialog
                    dialog.cancel();
                }
            });
        } else {
            // Change the text based on whether the user got the sentence right or not
            if (id == CORRECT_DIALOG) {
                // time how long it took to type the sentence
                long timeDiff = System.currentTimeMillis() - startTime;
                Float timeDiffSec = new Float(timeDiff/1000.0);

                int score = calculateScore(mainEditText.getText().toString(), timeDiffSec);

                // test for high score
                String dialogString = String.format(getResources().getString(R.string.sentenceTime), timeDiffSec);
                if (bestTime < 0) {
                    // there is no previous high-score information  timeDiffSec;
                    editor.putFloat(String.format("%dBest", DIFFICULTY), timeDiffSec);
                    editor.putString(String.format("%dBestName", DIFFICULTY), userName);
                    editor.putFloat(String.format("%dWorst", DIFFICULTY), timeDiffSec);
                    editor.putString(String.format("%dWorstName", DIFFICULTY), userName);
                } else if (timeDiffSec < bestTime) {
                    // This means the high score has been beaten
                    dialogString = getResources().getString(R.string.sentenceHighScore) + dialogString;
                    editor.putFloat(String.format("%dBest", DIFFICULTY), timeDiffSec);
                    editor.putString(String.format("%dBestName", DIFFICULTY), userName);
                } else {
                    // The high score was not beaten
                    dialogString = dialogString +
                            String.format(getResources().getString(R.string.noHighScore), bestTime);
                    // check if it's the worst score
                    if (timeDiffSec > worstTime) {
                        editor.putFloat(String.format("%dWorst", DIFFICULTY), timeDiffSec);
                        editor.putString(String.format("%dWorstName", DIFFICULTY), userName);
                    }
                }
                // commit changes to high score
                editor.commit();

                // build the dialog
                builder.setTitle("Well played!");
                builder.setMessage(dialogString);

                // update score list

            } else {
                builder.setTitle("Oh no!");
                builder.setMessage(getResources().getString(R.string.incorrect));
            }

            // Either way, add buttons to play again or go back.
            builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // the user indicated they want to play again. reset everything
                    startTime = System.currentTimeMillis();
                    mainEditText.setText("");
                    mainTextView.setText(senGen.generate(DIFFICULTY));
                    imView.setImageResource(R.drawable.sun_face);
                    changeToMidFaceIn(mSharedPreferences.getFloat(String.format("%dBest", DIFFICULTY), 0));
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    finish();
                }
            });
        }
        // SHOW THE ALERT
        builder.show();
    }

    private void changeToMidFaceIn(float s) {
        long ms1 = (long)(s*500.0);
        long ms2 = ms1*2;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // update the picture
                imView.setImageResource(R.drawable.mid_face);
            }
        }, ms1);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imView.setImageResource(R.drawable.sad_face);
            }
        }, ms2);
    }

    private int calculateScore(String s, float time) {
        int count = 0;
        for (int i=0; i<s.length(); i++) {
            count += (int) s.charAt(i);
        }
        return count + (int)(time/100);
    }

}
