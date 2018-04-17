package com.example.truongnguyen.pets;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.truongnguyen.pets.data.PetContract.PetEntry;
import com.example.truongnguyen.pets.data.PetDbHelper;

import java.sql.SQLData;

public class MainActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(i);
            }
        });

        mDbHelper = new PetDbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_all_pets:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert the new row
        db.insert(PetEntry.TABLE_NAME, null, values);
    }

    public void displayDatabaseInfo(){
        String[] project = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                PetEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null
        );
        try{
            TextView displayView = findViewById(R.id.text_total);
            displayView.setText("Number of rows in pets database table: " + cursor.getCount());

            while (cursor.moveToNext()) {
                displayView.setText(displayView.getText() + "\n" +
                        cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)) +
                        " - " + cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)) +
                        " - " + cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)) +
                        " - " + cursor.getInt(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)));
            }
        }finally {
            cursor.close();
        }
    }
}
