package com.example.passwordmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.passwordmanager.R;
import com.example.passwordmanager.database.DatabaseHelper;
import com.example.passwordmanager.models.Password;
import com.example.passwordmanager.utils.EncryptionUtils;

public class AddPasswordActivity extends AppCompatActivity {

    private EditText etServiceName, etUsername, etPassword, etNotes;
    private Button btnSave;
    private CheckBox cbShowPassword;
    private String masterUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        masterUsername = getIntent().getStringExtra("USERNAME");

        etServiceName = findViewById(R.id.etServiceName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etNotes = findViewById(R.id.etNotes);
        btnSave = findViewById(R.id.btnSave);
        cbShowPassword = findViewById(R.id.cbShowPassword);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = etServiceName.getText().toString().trim();
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String notes = etNotes.getText().toString().trim();

                if (serviceName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AddPasswordActivity.this, "Заполните обязательные поля", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String encryptedPassword = EncryptionUtils.encrypt(password);
                        Password newPassword = new Password(-1, masterUsername, serviceName, username, encryptedPassword, notes);

                        DatabaseHelper databaseHelper = new DatabaseHelper(AddPasswordActivity.this);
                        long result = databaseHelper.addPassword(newPassword);

                        if (result != -1) {
                            Toast.makeText(AddPasswordActivity.this, "Пароль сохранён", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddPasswordActivity.this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddPasswordActivity.this, "Ошибка шифрования", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}