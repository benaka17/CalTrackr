package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView progress, steps, usedCals;
    public Button entry, goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.progress);
        steps = findViewById(R.id.steps);
        usedCals = findViewById(R.id.usedCals);

        entry = findViewById(R.id.entry);
        goal = findViewById(R.id.goal);
    }
}