package ch.zli.caltrackr;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ResetService extends IntentService {

    public ResetService() {
        super("ResetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        checkReset();
    }

    public void checkReset(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lastResetDate = preferences.getString("lastResetDate", "");

        String today = getCurrentDate();

        if (!lastResetDate.equals(today)){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("lastResetDate", today);
            editor.putInt("totalCals", 0);
            editor.apply();
            Toast.makeText(this, "Calories resetted", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        return format.format(today);
    }
}
