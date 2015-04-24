package com.example.httptestapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.httptestapp.http.HttpQueue;
import com.example.httptestapp.http.HttpQueueListener;

public class MainActivity extends ActionBarActivity implements HttpQueueListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        HttpQueue httpQueue = new HttpQueue.Builder().contxt(this).listener(this).url("https://api.mongolab.com/api/1/databases?apiKey=4JuESSZoarhWkP3ztl9_k5vNfGUTWVmX")
                .returnCode(0).methodType(HttpQueue.GET).build();

        httpQueue.execute();

    }

    @Override
    public void request_finished(int returnCode, String result) {
        if (returnCode == 0) {
            textView.setText(result);
            Log.i("Main", result);
        }
    }

    @Override
    public void request_failed(String result) {

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

}
