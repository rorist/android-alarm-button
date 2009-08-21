/*
    Alarm Button
    Aubort Jean-Baptiste <aubort.jeanbaptiste@gmail.com> 2009 

    AlarmButton is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package info.lamatricexiste.alarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class Alarm extends Activity
{

    private MediaPlayer mp;
    private Float mp_vol;
    private String mp_rng;
    private View btn;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        btn = findViewById(R.id.start_alarm);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartAlarm();
            }
        });

        setup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(getApplication()).inflate(R.menu.options, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.settings){
            startActivity(new Intent(this, Prefs.class));
            return true;
        }
        else if(item.getItemId()==R.id.credits){
            new AlertDialog.Builder(this)
                .setTitle(R.string.credits_title)
                .setMessage(R.string.credits_msg)
                .setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {}
                })
                .show();
            return true;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void StartAlarm() {
        if(mp.isPlaying()) {
            stop();
        }
        else {
            play();
        }
    }

    private void play() {
        btn.setBackgroundResource(R.drawable.button_on);
        mp.start();
    }

    private void stop() {
        btn.setBackgroundResource(R.drawable.button_off);
        mp.stop();
        mp.release();
        loadClip();
    }

    private void setup() {
        try {
            //FIXME: How to store/retrieve float values in prefs ?
            mp_vol = Float.parseFloat(prefs.getString("volume", "0")); //TODO: AddPrefListener anywhere
            mp_rng = prefs.getString("ring", "alarm_sound");
        }
        catch (ClassCastException e) {
            e.printStackTrace();
        }

        loadClip();
    }

    private void loadClip() {
        try {
            mp=MediaPlayer.create(this, getResources().getIdentifier(mp_rng, "raw", this.getPackageName()));
            mp.setVolume(mp_vol, mp_vol);
            mp.setLooping(true);
        }
        catch (Throwable t) {}
    }
}
