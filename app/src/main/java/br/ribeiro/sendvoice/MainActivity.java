package br.ribeiro.sendvoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    Button send;
    EditText text;
    EditText ip_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send = (Button) findViewById(R.id.button_send);
        text = (EditText) findViewById(R.id.text_send);
        ip_text = (EditText) findViewById(R.id.ip_text);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor save = preferences.edit();

        String ip = preferences.getString("ip","192.168.1.");
        ip_text.setText(ip);

        send.setOnClickListener(v -> {
            if (ip_text.getText().toString().isEmpty() || text.getText().toString().isEmpty()){
                Toast.makeText(this,"Algo esta errado",Toast.LENGTH_SHORT).show();
            } else {
                save.putString("ip",ip_text.getText().toString());
                save.commit();
                sendMessage(text.getText().toString(), ip_text.getText().toString());
            }
        });
    }

    private void sendMessage(String var_text, String var_ip){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket s = new Socket(var_ip,9090);

                    OutputStreamWriter w = new OutputStreamWriter(s.getOutputStream());

                    w.append(var_text);
                    w.flush();
                    s.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();
    }
}