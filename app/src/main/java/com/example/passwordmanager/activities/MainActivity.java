package com.example.passwordmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.passwordmanager.R;
import com.example.passwordmanager.adapters.PasswordAdapter;
import com.example.passwordmanager.database.DatabaseHelper;
import com.example.passwordmanager.models.Password;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PasswordAdapter adapter;
    private List<Password> passwordList;
    private DatabaseHelper databaseHelper;
    private String username;
    private TextView tvNoPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("USERNAME");
        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        tvNoPasswords = findViewById(R.id.tvNoPasswords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPasswordActivity.class);
                intent.putExtra("USERNAME", username);
                startActivity(intent);
            }
        });

        loadPasswords();
    }

    private void loadPasswords() {
        passwordList = databaseHelper.getAllPasswords(username);
        if (passwordList.isEmpty()) {
            tvNoPasswords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoPasswords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new PasswordAdapter(this, passwordList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}