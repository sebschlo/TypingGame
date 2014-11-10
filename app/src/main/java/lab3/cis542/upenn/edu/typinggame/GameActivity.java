package lab3.cis542.upenn.edu.typinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private double bestTime = -1;

    private static final int READY_DIALOG = 1;
    private static final int CORRECT_DIALOG = 2;
    private static final int INCORRECT_DIALOG = 3;

    private int DIFFICULTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        // set up up button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // set up difficulty
        DIFFICULTY = this.getIntent().getExtras().getInt("difficulty");

        // Set up the button event listeners
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        // set up text view
        mainTextView = (TextView) findViewById(R.id.target);
        mainTextView.setText(senGen.generate(DIFFICULTY));

        // Set up edit text
        mainEditText = (EditText) findViewById(R.id.userInput);

        // show ready dialog
        showAlertDialog(READY_DIALOG);


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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case READY_DIALOG:
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
                        // this will hide the dialog
                        dialog.cancel();
                    }
                });
                break;

            case CORRECT_DIALOG:
                // time how long it took to type the sentence
                long timeDiff = System.currentTimeMillis() - startTime;
                Float timeDiffSec = new Float(timeDiff/1000.0);

                // test for high score
                String dialogString =
                        String.format(getResources().getString(R.string.sentenceTime),timeDiffSec);
                if (bestTime < 0) {
                    // there is no previous high-score information
                    bestTime = timeDiffSec;
                } else if (timeDiffSec < bestTime) {
                    dialogString = getResources().getString(R.string.sentenceHighScore) + dialogString;
                    bestTime = timeDiffSec;
                } else {
                    dialogString = dialogString +
                            String.format(getResources().getString(R.string.noHighScore), bestTime);
                }

                // build the dialog
                builder.setTitle("Well played!");
                builder.setMessage(dialogString);
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // the user indicated they want to play again. reset everything
                        startTime = System.currentTimeMillis();
                        mainEditText.setText("");
                        mainTextView.setText(senGen.generate(DIFFICULTY));
                        dialog.cancel();
                    }
                });
                break;

            case INCORRECT_DIALOG:
                // build the dialog
                builder.setTitle("Oh no!");
                builder.setMessage(getResources().getString(R.string.incorrect));
                builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // the user indicated they want to play again. reset everything
                        startTime = System.currentTimeMillis();
                        mainEditText.setText("");
                        mainTextView.setText(senGen.generate(DIFFICULTY));
                        dialog.cancel();
                    }
                });
                break;

        }

        builder.show();
    }





}
