package it.pingoda.reellogapp;

import android.content.Context;
import android.content.Intent;
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
        Button btnRegistrati = findViewById(R.id.btnRegistrati);
        EditText Username = findViewById(R.id.Username);
        EditText Password = findViewById(R.id.Password);
        EditText Email = findViewById(R.id.Email);


        TextView Accedi = findViewById(R.id.Accedi);
        String htmlText = "<font color=" + Color.WHITE + ">Hai già un account? </font>" +
                "<font color=" + Color.parseColor("#FFF647") + "> Accedi!</font>";
        Accedi.setText(Html.fromHtml(htmlText));


        View mainLayout = findViewById(R.id.main);

        mainLayout.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocus = getCurrentFocus();
            if(currentFocus != null){
                imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
            Username.clearFocus();
            Password.clearFocus();
            Email.clearFocus();

        });

        btnRegistrati.setOnClickListener(new View.OnClickListener()  {
            final TextView usernameError = findViewById(R.id.UsernameError);
            final TextView emailError = findViewById(R.id.EmailError);
            final TextView passwordError = findViewById(R.id.PasswordError);

            @Override
            public void onClick(View v) {
                String usernameText = Username.getText().toString();
                String emailText = Email.getText().toString();
                String passwordText = Password.getText().toString();

                if (usernameText.isEmpty()) {
                    String textError = "Username non può essere vuoto";
                    usernameError.setText(textError);
                } else {
                    usernameError.setText("");
                }

                if (emailText.isEmpty()) {
                    String textError = "Email non può essere vuota";
                    emailError.setText(textError);
                } else {
                    emailError.setText("");
                }

                if (passwordText.isEmpty()) {
                    String textError = "Password non può essere vuota";
                    passwordError.setText(textError);
                } else {
                    passwordError.setText("");
                }
                Log.d("btnRegistrati", "bottone premuto da "+usernameText+", email: "+emailText+ ", password: "+passwordText);

            }
        });

        Accedi.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

}
