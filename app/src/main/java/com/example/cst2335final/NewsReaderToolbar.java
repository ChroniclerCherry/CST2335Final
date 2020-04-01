package com.example.cst2335final;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

public class NewsReaderToolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar tbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader_view);

        //toolbar
        tbar = findViewById(R.id.toolbar);
        setSupportActionBar(tbar);

        //NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.news_reader_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Look at your menu XML file. Put a case for every id in that file:
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.home:
                    Intent goHome = new Intent(NewsReaderToolbar.this, MainActivity.class);
                    startActivity(goHome);
                break;
            case R.id.bbc:
                Intent gotoBbc = new Intent(NewsReaderToolbar.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Click a title to view the details. Click Favourite with option note, to save an article to Favourites.");
                alertDialogBuilder.setNegativeButton("Exit", null);
                break;
        }
        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String message2 = null;
        switch(item.getItemId())
        {
            case R.id.bbc:
                Intent gotoBbc = new Intent(NewsReaderToolbar.this, NewsReaderSearch.class);
                startActivity(gotoBbc);
                break;
             case R.id.guardian:
                message2 = "Guardian here";
                break;
            case R.id.earth:
                message2 = "Earth images here";
                break;
            case R.id.space:
                message2 = "Image of the Day here";
                break;
            case R.id.help:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setNegativeButton("Exit", null);
                break;
        }
        Toast.makeText(this, message2, Toast.LENGTH_LONG).show();
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}