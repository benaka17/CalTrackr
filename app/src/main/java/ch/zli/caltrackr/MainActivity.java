package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public TextView progress, steps, usedCals;
    public Button entry, goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findIDs();

        addListeners();

        progress();

        Intent resetIntent = new Intent(this, ResetService.class);
        startService(resetIntent);

    }

    public void findIDs(){
        progress = findViewById(R.id.progress);
        steps = findViewById(R.id.steps);
        usedCals = findViewById(R.id.usedCals);

        entry = findViewById(R.id.entry);
        goal = findViewById(R.id.goal);
    }

    public void addListeners(){
        goal.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(MainActivity.this, GoalActivity.class);
            startActivity(switchActivityIntent);
        });

        entry.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(MainActivity.this, EntryActivity.class);
            startActivity(switchActivityIntent);
        });
    }

    public void progress(){
        Intent intent = getIntent();
        int getUserGoal = intent.getIntExtra("userGoalInput", 0);
        int getUserCals = intent.getIntExtra("userCalsInput", 0);

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        int savedUserGoalInput = sharedPreferences.getInt("userGoalInput", 0);

        int savedTotalCals = sharedPreferences.getInt("totalCals", 0);
        getUserCals = savedTotalCals;

        if (savedUserGoalInput != 0) {
            getUserGoal = savedUserGoalInput;
        }

        //Code from: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate-with-different-frequency
        if (getUserCals == getUserGoal){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }
        }

        progress.setText(getUserCals + " / " + getUserGoal);
    }





}