package com.example.truongnguyen.pets;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truongnguyen.pets.data.PetContract.PetEntry;
import com.example.truongnguyen.pets.data.PetDbHelper;

public class EditorActivity extends AppCompatActivity {

    private Spinner mGenderSpiner;
    private int mGender = PetEntry.GENDER_UNKNOWN;
    private PetDbHelper mDbHelper;

    private EditText mNameEditText;
    private EditText mBreedEditText;
    private EditText mWeightEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mGenderSpiner = findViewById(R.id.spiner_gender);
        mDbHelper = new PetDbHelper(this);

        setupSpiner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            if (validation()) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(PetEntry.COLUMN_PET_NAME, mNameEditText.getText().toString().trim());
                values.put(PetEntry.COLUMN_PET_BREED, mBreedEditText.getText().toString().trim());
                values.put(PetEntry.COLUMN_PET_GENDER, mGender);
                values.put(PetEntry.COLUMN_PET_WEIGHT, Integer.parseInt(mWeightEditText.getText().toString()));

                // Insert the new row
                long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

                if (newRowId == -1) {
                    Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Pet saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                }
                finish();
                return true;
            } else
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private boolean validation() {
        mNameEditText = findViewById(R.id.edit_pet_name);
        mBreedEditText = findViewById(R.id.edit_pet_breed);
        mWeightEditText = findViewById(R.id.edit_pet_weight);

        if (TextUtils.isEmpty(mNameEditText.getText())) {
            mNameEditText.setError("Không được để trống");
            mNameEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mBreedEditText.getText())) {
            mBreedEditText.setError("Không được để trống");
            mBreedEditText.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(mWeightEditText.getText())) {
            mWeightEditText.setError("Không được để trống");
            mWeightEditText.requestFocus();
            return false;
        }

        if (Integer.parseInt(mWeightEditText.getText().toString()) <= 0) {
            mWeightEditText.setError("Cân nặng phải lớn hơn 0");
            mWeightEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void setupSpiner() {
        ArrayAdapter genderSpinerAdapter = ArrayAdapter.createFromResource(this,
                R.array.arrays_gender_options, android.R.layout.simple_spinner_item);

        //Specify dropdown layout style - simple list view with 1 item per line
        genderSpinerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Apply the adapter to the spiner
        mGenderSpiner.setAdapter(genderSpinerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Male"))
                        mGender = PetEntry.GENDER_MALE;
                    else if (selection.equals("Female"))
                        mGender = PetEntry.GENDER_FEMALE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN;
            }
        });


    }

}
