package lab3.cis542.upenn.edu.typinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final int EASY = 4;
    private static final int MEDIUM = 7;
    private static final int HARD = 10;

    Button playButton;

    private static final String PREFS = "prefs";
    SharedPreferences mSharedPreferences;

    String userName;
    TextView userNameView;

    TextView hardBestT;
    TextView hardWorstT;
    TextView mediumBestT;
    TextView mediumWorstT;
    TextView easyBestT;
    TextView easyWorstT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set up play button
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(this);

        // set up username view
        userNameView = (TextView) findViewById(R.id.username);

        // obtain the username
        obtainUserName();

        // set up score textViews references
        hardBestT = (TextView) findViewById(R.id.hardBest);
        hardWorstT = (TextView) findViewById(R.id.hardWorst);
        mediumBestT = (TextView) findViewById(R.id.mediumBest);
        mediumWorstT = (TextView) findViewById(R.id.mediumWorst);
        easyBestT = (TextView) findViewById(R.id.easyBest);
        easyWorstT = (TextView) findViewById(R.id.easyWorst);

        // update scores
        updateScores();
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
            obtainUserName();
            return true;
        }

        // clear high scores
        if (id == R.id.action_clear_scores) {
            clearHighScores();
        }

        // exit app
        if (id == R.id.action_quit_app) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (userName != null) {
            // Ask for difficulty level
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(getResources().getString(R.string.difficultyDialogTitle));
            alert.setMessage(getResources().getString(R.string.difficultyDialog));
            alert.setPositiveButton("Hard",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startGameWithDifficulty(HARD);
                        }
                    });

            alert.setNeutralButton("Easy",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startGameWithDifficulty(EASY);
                        }
                    });

            alert.setNegativeButton("Medium",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startGameWithDifficulty(MEDIUM);
                        }
                    });
            alert.show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.notSignedIn), Toast.LENGTH_LONG).show();
        }
    }

    private void startGameWithDifficulty(int diff) {
        // Create game intent
        final Intent gameIntent = new Intent(this, GameActivity.class);

        // store game difficulty in next activity as well as username
        gameIntent.putExtra("userName", userName);
        gameIntent.putExtra("difficulty", diff);

        // Start the activity
        startActivity(gameIntent);
    }


    private void obtainUserName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.userDialogTitle));
        alert.setMessage(getResources().getString(R.string.userDialog));

        // add a text input
        final EditText input = new EditText(this);
        alert.setView(input);

        // ok button to save name
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // save name and update the textview
                userName = input.getText().toString();
                userNameView.setText(getResources().getString(R.string.userTextView, userName));
                Toast.makeText(getApplicationContext(), "Welcome, " + userName + "!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // cancel button
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        //show
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateScores();
    }

    private void updateScores() {
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String best = "BEST:  %d at %.2f seconds by %s.";
        String worst = "WORST:  %d at %.2f seconds by %s.";

        float hardBestTime = mSharedPreferences.getFloat("10Best", -1);
        int hardBestScore = mSharedPreferences.getInt("10BestScore", -1);
        if (hardBestScore < 0) {
            hardBestT.setText(getResources().getString(R.string.noScore));
            hardWorstT.setText(getResources().getString(R.string.noScore));
        } else {
            hardBestT.setText(String.format(best, hardBestScore, hardBestTime,
                    mSharedPreferences.getString("10BestName", "")));
            hardWorstT.setText(String.format(worst,
                    mSharedPreferences.getInt("10WorstScore", -1),
                    mSharedPreferences.getFloat("10Worst", -1),
                    mSharedPreferences.getString("10WorstName", "")));
        }


        float mediumBestTime = mSharedPreferences.getFloat("7Best", -1);
        int mediumBestScore = mSharedPreferences.getInt("7BestScore", -1);
        if (mediumBestScore < 0) {
            mediumBestT.setText(getResources().getString(R.string.noScore));
            mediumWorstT.setText(getResources().getString(R.string.noScore));
        } else {
            mediumBestT.setText(String.format(best, mediumBestScore, mediumBestTime,
                    mSharedPreferences.getString("7BestName", "")));
            mediumWorstT.setText(String.format(worst,
                    mSharedPreferences.getInt("7WorstScore", -1),
                    mSharedPreferences.getFloat("7Worst", -1),
                    mSharedPreferences.getString("7WorstName", "")));
        }


        float easyBestTime = mSharedPreferences.getFloat("4Best", -1);
        int easyBestScore = mSharedPreferences.getInt("4BestScore", -1);
        if (easyBestScore < 0) {
            easyBestT.setText(getResources().getString(R.string.noScore));
            easyWorstT.setText(getResources().getString(R.string.noScore));
        } else {
            easyBestT.setText(String.format(best, easyBestScore, easyBestTime,
                    mSharedPreferences.getString("4BestName", "")));
            easyWorstT.setText(String.format(worst,
                    mSharedPreferences.getInt("4WorstScore", -1),
                    mSharedPreferences.getFloat("4Worst", -1),
                    mSharedPreferences.getString("4WorstName", "")));
        }

    }


    private void clearHighScores() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getResources().getString(R.string.clearScoreAlertTitle));
        alert.setMessage(getResources().getString(R.string.clearScoreMessage));

        // ok button to save name
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // clear all information in shared preferences
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.clear();
                editor.commit();
                updateScores();
            }
        });

        // cancel button
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        //show
        alert.show();
    }

}
