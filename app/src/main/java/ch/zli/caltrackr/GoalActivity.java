package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class GoalActivity extends AppCompatActivity {

    public TextView userGoal;
    public Button submitGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        userGoal = findViewById(R.id.userGoal);
        submitGoal = findViewById(R.id.submitGoal);
    }
}