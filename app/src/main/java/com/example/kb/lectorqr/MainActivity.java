package com.example.kb.lectorqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.R;

public class MainActivity extends Activity {
    Switch sFacturacion;
    TextView tvResult;
    EditText etFactura;
    EditText etCantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        sFacturacion = (Switch) findViewById(R.id.swFacturacion);

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
                    Toast.makeText(getApplicationContext(), "ERROR:" + e,
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }
    //In the same activity you’ll need the following to retrieve the results:
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) if (resultCode == RESULT_OK)
            tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
        else if (resultCode == RESULT_CANCELED) tvResult.setText("Scan cancelled.");
    }

}