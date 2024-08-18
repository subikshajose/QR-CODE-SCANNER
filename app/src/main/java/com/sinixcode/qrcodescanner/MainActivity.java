package com.sinixcode.qrcodescanner;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button scanbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanbtn = findViewById(R.id.scanbtn);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanneroption();
            }
        });
    }

    private void scanneroption() {
        ScanOptions so = new ScanOptions();
        so.setPrompt("Volume up to flash on");
        so.setBeepEnabled(true);
        so.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(so);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{
        if (result.getContents()!= null){
            Pattern pattern = Pattern.compile("https?://[^\\s]+");
            Matcher matcher = pattern.matcher(result.getContents());
            SpannableStringBuilder spannableText = new SpannableStringBuilder(result.getContents());
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                spannableText.setSpan(new URLSpan(matcher.group()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


            TextView textView = new TextView(this);
            textView.setText(spannableText);
            textView.setMovementMethod(LinkMovementMethod.getInstance());

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Result");
            builder.setView(textView);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    });
}