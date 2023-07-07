package ch.zli.caltrackr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public TextView progress, steps, usedCals;
    public Button entry, goal;

    public SensorManager sensorManager;
    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findIDs();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        addListeners();

        checkReset();

        progress();

    }

    public void findIDs() {
        progress = findViewById(R.id.progress);
        steps = findViewById(R.id.steps);
        usedCals = findViewById(R.id.usedCals);

        entry = findViewById(R.id.entry);
        goal = findViewById(R.id.goal);

    }

    public void addListeners() {
        goal.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(MainActivity.this, GoalActivity.class);
            startActivity(switchActivityIntent);
        });

        entry.setOnClickListener(view -> {
            Intent switchActivityIntent = new Intent(MainActivity.this, EntryActivity.class);
            startActivity(switchActivityIntent);
        });
    }

    public void progress() {
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

        goalAchieved(getUserCals, getUserGoal);

        progress.setText(getUserCals + " / " + getUserGoal);
    }

    public void goalAchieved(int cals, int goal) {
        if (cals >= goal) {
            //Code from: https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate-with-different-frequency
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                Toast.makeText(this, "I'm vibing.", Toast.LENGTH_SHORT).show();
            } else {
                //deprecated in API 26
                v.vibrate(500);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My Notification");
            builder.setContentTitle("Goal achieved!");
            builder.setContentText("CalTrackr here. You just achieved your calorie goal!");
            builder.setSmallIcon(R.drawable.ic_launcher_background);
            builder.setAutoCancel(true);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            managerCompat.notify(1, builder.build());

        }
    }

    //Code from ChatGPT for this method
    public void checkReset() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lastSavedDate = preferences.getString("lastSavedDate", "");

        String today = getCurrentDate();
        if (!lastSavedDate.equals(today)) {
            // Reset calorie count to zero
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lastSavedDate", today);
            editor.putInt("totalCals", 0);
            editor.apply();
        }
    }

    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        return sdf.format(today);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (running){
            steps.setText(String.valueOf(sensorEvent.values[0]));
            usedCals.setText(String.valueOf(sensorEvent.values[0]*0.4));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Sensor not found.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        sensorManager.unregisterListener(this);
    }
}