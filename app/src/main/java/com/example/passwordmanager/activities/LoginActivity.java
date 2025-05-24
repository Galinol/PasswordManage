package com.example.passwordmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.passwordmanager.R;
import com.example.passwordmanager.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        checkExistingSession();
    }

    private void initViews() {
        databaseHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        setupLoginButton();
        setupRegisterLink();
    }

    private void checkExistingSession() {
    }

    private void setupLoginButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    if (databaseHelper.checkUser(username, password)) {
                        // Переход на MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("USERNAME", username); // Передача имени пользователя
                        startActivity(intent);
                        finish(); // Закрыть текущую активити
                    } else {
                        Toast.makeText(LoginActivity.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInput(username, password)) {
            authenticateUser(username, password);
        }
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showToast("Заполните все поля");
            return false;
        }
        return true;
    }

    private void authenticateUser(String username, String password) {
        new Thread(() -> {
            final boolean isValid = databaseHelper.checkUser(username, password);

            runOnUiThread(() -> {
                if (isValid) {
                    handleSuccessfulLogin(username);
                } else {
                    handleFailedLogin();
                }
            });
        }).start();
    }

    private void handleSuccessfulLogin(String username) {
        Log.d(TAG, "Успешный вход для пользователя: " + username);
        showToast("Вход выполнен успешно");

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
        finishAffinity(); 
    }

    private void handleFailedLogin() {
        Log.w(TAG, "Неудачная попытка входа");
        showToast("Неверные учетные данные");
        etPassword.setText("");
    }

    private void setupRegisterLink() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRegister();
            }
        });
    }

    private void navigateToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}