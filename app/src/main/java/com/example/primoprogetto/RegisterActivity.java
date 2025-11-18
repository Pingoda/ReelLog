package com.example.primoprogetto;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnRegistrati = (Button) findViewById(R.id.btnRegistrati);
        EditText Username = (EditText) findViewById(R.id.Username);
        EditText Password = (EditText) findViewById(R.id.Password);
        EditText Email = (EditText) findViewById(R.id.Email);


        TextView Accedi = findViewById(R.id.Accedi);
        String htmlText = "<font color=" + Color.WHITE + ">Hai già un account? </font>" +
                "<font color=" + Color.parseColor("#FFF647") + "> Accedi!</font>";
        Accedi.setText(Html.fromHtml(htmlText));


        View mainLayout = findViewById(R.id.main);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                View currentFocus = getCurrentFocus();
                if(currentFocus != null){
                    imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
                }
                Username.clearFocus();
                Password.clearFocus();
                Email.clearFocus();

            }

        });
        btnRegistrati.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                String usernameText = Username.getText().toString();
                String passwordText = Password.getText().toString();
                String emailText = Email.getText().toString();
                Log.d("btnRegistrati", "bottone premuto da "+usernameText+", email: "+emailText+ ", password: "+passwordText);

            }
        });

        Accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Accedi", "hai cliccato la scritta Accedi");
            }
        });

    }

}
