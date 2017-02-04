package com.example.kb.lectorqr;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    Switch sFacturacion;
    TextView tvResult;
    EditText etFactura;
    EditText etCantidad;
    Button bSend;

    boolean ventas = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = (TextView) findViewById(R.id.tvResult);
        sFacturacion = (Switch) findViewById(R.id.swFacturacion);
        bSend = (Button) findViewById(R.id.bEnviar);
        etCantidad = (EditText) findViewById(R.id.etCantidad);
        etFactura = (EditText) findViewById(R.id.etCodigo);

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

        sFacturacion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sFacturacion.setText("Ventas");
                    ventas = true;
                }else {
                    sFacturacion.setText("Compras");
                    ventas = false;
                }
            }
        });

        bSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarData();
            }
        });
    }

    private void enviarData() {
        String codigo, producto, cantidad;

        codigo = etFactura.getText().toString();
        cantidad = etCantidad.getText().toString();
        producto = tvResult.getText().toString();
        
        Send(codigo, cantidad, producto);
    }

    //http://angeluz.azurewebsites.net
    private void Send(String _codigo, String _cantidad, String _producto) {
        class SendAsync extends AsyncTask<String,Void,String>{
            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("codigo", params[0]));
                nameValuePairs.add(new BasicNameValuePair("producto", params[1]));
                nameValuePairs.add(new BasicNameValuePair("cantidad", params[3]));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost= null;
                    if (ventas){
                        //envio de ventas
                        httpPost = new HttpPost(
                                "http://angeluz.azurewebsites.net/java/ventas.php");
                    }else{
                        //envio de compras
                        httpPost = new HttpPost(
                                "http://angeluz.azurewebsites.net/java/compras.php");
                    }

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }
    }


    //In the same activity you’ll need the following to retrieve the results:
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) if (resultCode == RESULT_OK)
            tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
        else if (resultCode == RESULT_CANCELED) tvResult.setText("Scan cancelled.");
    }

}