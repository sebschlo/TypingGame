package lab3.cis542.upenn.edu.typinggame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final int EASY = 4;
    private static final int MEDIUM = 7;
    private static final int HARD = 10;

    Button playButton;

    ListView hardList;
    ListView mediumList;
    ListView easyList;
    String hardArray;
    String mediumArray;
    String easyArray;

    private static final String PREFS = "prefs";

    String userName;
    TextView userNameView;



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

        // set up hard list


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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
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
    }

    private void startGameWithDifficulty(int diff) {
        // Create game intent
        final Intent gameIntent = new Intent(this, GameActivity.class);

        // store game difficulty in next activity
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
                userNameView.setText(String.format(getResources().getString(R.string.userTextView),
                        userName));
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

}
