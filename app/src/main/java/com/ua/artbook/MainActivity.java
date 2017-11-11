package com.ua.artbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static Bitmap chooseImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.listView);

        ArrayList<String> artNames = new ArrayList<>();
        final ArrayList<Bitmap> artImages = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artNames);
        listView.setAdapter(adapter);

        try {
            DetailActivity.database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            DetailActivity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, image BLOB)");
            Cursor cursor = DetailActivity.database.rawQuery("SELECT * FROM Arts",null);
            int nameIndex = cursor.getColumnIndex("name");
            int imageIndex = cursor.getColumnIndex("image");
            cursor.moveToFirst();
            while (cursor != null){
                artNames.add(cursor.getString(nameIndex));
                byte[] byteArray = cursor.getBlob(imageIndex);

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                artImages.add(bitmap);
                cursor.moveToNext();
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
                intent.putExtra("info","old");
                chooseImage = artImages.get(position);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_art) {
            Intent intent = new Intent(getApplicationContext(),DetailActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
