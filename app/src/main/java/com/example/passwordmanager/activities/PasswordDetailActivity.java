package com.example.passwordmanager.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.passwordmanager.R;
import com.example.passwordmanager.database.DatabaseHelper;
import com.example.passwordmanager.models.Password;
import com.example.passwordmanager.utils.EncryptionUtils;

public class PasswordDetailActivity extends AppCompatActivity {

    private TextView tvServiceName, tvUsername, tvPassword, tvNotes;
    private Password password;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);

        tvServiceName = findViewById(R.id.tvServiceName);
        tvUsername = findViewById(R.id.tvUsername);
        tvPassword = findViewById(R.id.tvPassword);
        tvNotes = findViewById(R.id.tvNotes);

        int passwordId = getIntent().getIntExtra("PASSWORD_ID", -1);
        if (passwordId != -1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            password = databaseHelper.getPassword(passwordId);
            if (password != null) {
                tvServiceName.setText(password.getServiceName());
                tvUsername.setText(password.getUsername());
                tvPassword.setText("••••••••");
                tvNotes.setText(password.getNotes().isEmpty() ? "Нет заметок" : password.getNotes());
            } else {
                Toast.makeText(this, "Пароль не найден", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.password_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_password) {
            togglePasswordVisibility();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            deletePassword();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void togglePasswordVisibility() {
        if (password != null) {
            try {
                if (passwordVisible) {
                    tvPassword.setText("••••••••");
                    passwordVisible = false;
                } else {
                    String decryptedPassword = EncryptionUtils.decrypt(password.getPassword());
                    tvPassword.setText(decryptedPassword);
                    passwordVisible = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка дешифрования", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deletePassword() {
        if (password != null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            databaseHelper.getPassword(password.getId());
            Toast.makeText(this, "Пароль удалён", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}