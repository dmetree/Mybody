package com.dvb.mybody;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dvb.mybody.data.BodyContract;
import com.dvb.mybody.data.BodyDbHelper;

import java.sql.RowId;
import java.util.List;

public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int BODY_LOADER = 0;
    BodyCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_activity);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        final ListView bodyListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        bodyListView.setEmptyView(emptyView);


        mCursorAdapter = new BodyCursorAdapter(this, null);
        bodyListView.setAdapter(mCursorAdapter);



        // Editing body entries
        bodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);

                Uri currentPetUri = ContentUris.withAppendedId(BodyContract.BodyEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(BODY_LOADER, null, this);
    }






    private void insertBody() {

        ContentValues values = new ContentValues();
        values.put(BodyContract.BodyEntry.COLUMN_BODY_DAY, "19 September 2017");
        values.put(BodyContract.BodyEntry.COLUMN_BODY_HEIGHT, 191);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_SHOULDERS, 110);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_CHEST, 100);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_ARMS, 35);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_BELLY, 90);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_HIP, 55);
        values.put(BodyContract.BodyEntry.COLUMN_BODY_WEIGHT, 60);

       Uri newUri = getContentResolver().insert(BodyContract.BodyEntry.CONTENT_URI, values);
    }

    private void deleteAllBodies() {
        int rowsDeleted = getContentResolver().delete(
                BodyContract.BodyEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from body database");
    }





    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String[] projection = {
                BodyContract.BodyEntry._ID,
                BodyContract.BodyEntry.COLUMN_BODY_DAY,
                BodyContract.BodyEntry.COLUMN_BODY_HEIGHT,
                BodyContract.BodyEntry.COLUMN_BODY_SHOULDERS,
                BodyContract.BodyEntry.COLUMN_BODY_CHEST,
                BodyContract.BodyEntry.COLUMN_BODY_ARMS,
                BodyContract.BodyEntry.COLUMN_BODY_BELLY,
                BodyContract.BodyEntry.COLUMN_BODY_HIP,
                BodyContract.BodyEntry.COLUMN_BODY_WEIGHT
        };
        return new CursorLoader(this,
                BodyContract.BodyEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertBody();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllBodies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
