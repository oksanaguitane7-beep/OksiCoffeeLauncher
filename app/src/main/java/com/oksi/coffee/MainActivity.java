package com.oksi.coffee;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class MainActivity extends Activity {

    EditText nickInput;
    EditText ipInput;
    EditText portInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("oksi_data", MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(40, 40, 40, 40);

        GradientDrawable bg = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(65, 35, 75), Color.rgb(25, 18, 35)}
        );
        root.setBackground(bg);

        TextView title = new TextView(this);
        title.setText("☕ Oksi_Coffee ☕");
        title.setTextColor(Color.WHITE);
        title.setTextSize(32);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView sub = new TextView(this);
        sub.setText("SA-MP / Arizona Mobile Launcher");
        sub.setTextColor(Color.LTGRAY);
        sub.setTextSize(16);
        sub.setGravity(Gravity.CENTER);
        root.addView(sub);

        nickInput = makeInput("Введите ник", prefs.getString("nick", "Oksi_Coffee"));
        ipInput = makeInput("IP сервера", prefs.getString("ip", "127.0.0.1"));
        portInput = makeInput("Порт", prefs.getString("port", "7777"));

        root.addView(nickInput);
        root.addView(ipInput);
        root.addView(portInput);

        Button play = makeButton("Играть / Подключиться");
        root.addView(play);

        Button copy = makeButton("Скопировать IP:порт");
        root.addView(copy);

        TextView info = new TextView(this);
        info.setText("Если игра не откроется автоматически, IP:порт будет скопирован.");
        info.setTextColor(Color.LTGRAY);
        info.setTextSize(14);
        info.setGravity(Gravity.CENTER);
        root.addView(info);

        setContentView(root);

        play.setOnClickListener(v -> {
            saveData();
            copyServer();

            String ip = ipInput.getText().toString().trim();
            String port = portInput.getText().toString().trim();

            Toast.makeText(this, "Подключение готово: " + ip + ":" + port, Toast.LENGTH_LONG).show();

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("samp://" + ip + ":" + port));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Игра не открылась. IP:порт скопирован.", Toast.LENGTH_LONG).show();
            }
        });

        copy.setOnClickListener(v -> {
            saveData();
            copyServer();
            Toast.makeText(this, "IP:порт скопирован", Toast.LENGTH_SHORT).show();
        });
    }

    private EditText makeInput(String hint, String value) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setText(value);
        input.setTextColor(Color.WHITE);
        input.setHintTextColor(Color.LTGRAY);
        input.setTextSize(18);
        input.setSingleLine(true);

        GradientDrawable box = new GradientDrawable();
        box.setColor(Color.argb(60, 255, 255, 255));
        box.setCornerRadius(25);
        input.setBackground(box);
        input.setPadding(25, 10, 25, 10);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0, 18, 0, 0);
        input.setLayoutParams(lp);
        return input;
    }

    private Button makeButton(String text) {
        Button btn = new Button(this);
        btn.setText(text);
        btn.setTextSize(18);
        btn.setTextColor(Color.WHITE);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.rgb(139, 92, 246));
        bg.setCornerRadius(30);
        btn.setBackground(bg);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0, 25, 0, 0);
        btn.setLayoutParams(lp);
        return btn;
    }

    private void saveData() {
        getSharedPreferences("oksi_data", MODE_PRIVATE)
                .edit()
                .putString("nick", nickInput.getText().toString().trim())
                .putString("ip", ipInput.getText().toString().trim())
                .putString("port", portInput.getText().toString().trim())
                .apply();
    }

    private void copyServer() {
        String server = ipInput.getText().toString().trim() + ":" + portInput.getText().toString().trim();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("server", server));
    }
                     }
