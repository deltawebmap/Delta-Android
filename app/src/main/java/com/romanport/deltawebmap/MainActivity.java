package com.romanport.deltawebmap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.romanport.deltawebmap.Framework.Search.SearchAction;
import com.romanport.deltawebmap.Framework.Search.SearchAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchRequest;
import com.romanport.deltawebmap.Framework.Session.Actions.TestingAction;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public RecyclerView searchListView;
    public SearchAdapter searchListViewAdapter;

    public DeltaServerSession session;
    public int searchToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set up recycler view
        searchListView = (RecyclerView)findViewById(R.id.searchView);
        searchListView.setHasFixedSize(true);
        searchListView.setLayoutManager(new LinearLayoutManager(this));
        searchListViewAdapter = new SearchAdapter();
        searchListView.setAdapter(searchListViewAdapter);

        //Set up session
        session = new DeltaServerSession("5de0a83d67bca6989c01b822");
        session.QueueAction(this, null, new TestingAction());

        //Set up vars
        searchToken = 0;

        Search("");
    }

    public void OpenDrawer(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //TODO
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

    }

    public void OpenSearchDrawer() {
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        View hiddenPanel = findViewById(R.id.search_drawer);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(View.VISIBLE);
    }

    public void CloseSearchDrawer(View v) {

    }

    public void Search(String query) {
        searchToken++;

        //Create request
        SearchRequest request = new SearchRequest();
        request.token = searchToken;
        request.activity = this;
        request.query = query.toLowerCase();

        //Create action
        SearchAction action = new SearchAction();
        session.QueueAction(this, request, action);
    }
}
