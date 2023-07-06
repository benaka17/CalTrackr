package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class GoalActivity extends AppCompatActivity {

    public TextView goalInput;
    public Button submitGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalInput = findViewById(R.id.userCals);
        submitGoal = findViewById(R.id.submitCals);

        submitGoal.setOnClickListener(view -> {
            String goalInputText = goalInput.getText().toString();
            int userGoalInput = Integer.parseInt(goalInputText);

            SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userGoalInput", userGoalInput);
            editor.apply();

            Intent switchActivityIntent = new Intent(GoalActivity.this, MainActivity.class);
            switchActivityIntent.putExtra("userGoalInput", userGoalInput);
            startActivity(switchActivityIntent);
        });
    }
}