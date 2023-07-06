package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class EntryActivity extends AppCompatActivity {

    TextView userCals;
    Button submitCals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        userCals = findViewById(R.id.userCals);
        submitCals = findViewById(R.id.submitCals);

        submitCals.setOnClickListener(view -> {
            String userCalsInputText = userCals.getText().toString();
            int userCalsInput = Integer.parseInt(userCalsInputText);

            SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
            int previousCals = sharedPreferences.getInt("totalCals", 0);

            int newCals = previousCals + userCalsInput;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("totalCals", newCals);
            editor.apply();

            Intent switchActivityIntent = new Intent(EntryActivity.this, MainActivity.class);
            //switchActivityIntent.putExtra("userCalsInput", userCalsInput);
            startActivity(switchActivityIntent);
        });
    }
}