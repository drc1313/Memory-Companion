package com.example.memorycompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    FilterHandler qfh = FilterHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(WRITE_EXTERNAL_STORAGE, 1);
        loadContent();
    }

    private void loadContent()
    {
        qfh.loadQuestionFilters();

        Button buttonCreate = findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getApplicationContext(), QuestionCreatorActivity.class);
                startActivity(activityIntent);
            }
        });

        Button buttonFilterCreate = findViewById(R.id.buttonFilterCreate);
        buttonFilterCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getApplicationContext(), FilterCreatorActivity.class);
                startActivity(activityIntent);
            }
        });

        Button buttonManage = findViewById(R.id.buttonManage);
        buttonManage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getApplicationContext(), QuestionManagerActivity.class);
                startActivity(activityIntent);
            }
        });

        Button buttonFilterManager = findViewById(R.id.buttonfilterManager);
        buttonFilterManager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent activityIntent = new Intent(getApplicationContext(), FilterManagerActivity.class);
                startActivity(activityIntent);
            }
        });

        Button buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Don't start unless filter is specified
                if(FilterHandler.selectedFilter != null)
                {
                    Intent activityIntent = new Intent(getApplicationContext(), QuestionViewerActivity.class);
                    startActivity(activityIntent);
                }
            }
        });
    }
// Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast
                    .makeText(MainActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

}