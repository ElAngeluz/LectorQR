package com.example.kb.lectorqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.R;

public class MainActivity extends Activity {
    TextView tvStatus;
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvResult = (TextView) findViewById(R.id.tvResult);

        Button scanBtn = (Button) findViewById(R.id.btnScan);

        //in some trigger function e.g. button press within your code you should add:
        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    Intent intent = new Intent(
                            "com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
                    startActivityForResult(intent, 0);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ERROR:" + e,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                newGame();
                return true;
            case R.id.help:
                showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //In the same activity you’ll need the following to retrieve the results:
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                tvResult.setText(intent.getStringExtra("SCAN_RESULT")); //obtiene el codigo
            } else if (resultCode == RESULT_CANCELED) {
                tvStatus.setText("Press a button to start a scan.");
                tvResult.setText("Scan cancelled.");
            }
        }
    }

}