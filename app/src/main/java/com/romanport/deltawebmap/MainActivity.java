package com.romanport.deltawebmap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.romanport.deltawebmap.Framework.Search.SearchAction;
import com.romanport.deltawebmap.Framework.Search.SearchAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchRequest;
import com.romanport.deltawebmap.Framework.Session.Actions.TestingAction;
import com.romanport.deltawebmap.Framework.Session.DeltaServerCallback;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSessionAction;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSessionQueue;
import com.romanport.deltawebmap.Framework.Session.SessionActivityConnection;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.DeltaMapNetworkImageLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.DeltaMapTextLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.DeltaMapContainer;

import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SessionActivityConnection {

    public RecyclerView searchListView;
    public SearchAdapter searchListViewAdapter;
    public EditText searchBox;
    public ImageView searchBackBtn;
    public ImageView profileIcon;

    public DeltaServerSession session;
    public int searchToken;
    public Boolean isSearchOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set up vars
        searchToken = 0;
        isSearchOpen = false;

        //Set up recycler view
        searchListView = (RecyclerView)findViewById(R.id.searchView);
        searchListView.setHasFixedSize(true);
        searchListView.setLayoutManager(new LinearLayoutManager(this));
        searchListViewAdapter = new SearchAdapter();
        searchListView.setAdapter(searchListViewAdapter);

        //Set up header
        profileIcon = (ImageView)findViewById(R.id.serverIcon);
        searchBackBtn = (ImageView)findViewById(R.id.searchBack);
        searchBox = (EditText)findViewById(R.id.tribeSearchQuery);
        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean hasfocus) {
                if (hasfocus) {
                    OpenSearchDrawer();
                }
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Search(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        //Set up map
        SetMap();

        //Set up session
        session = new DeltaServerSession(this,"5de0a83d67bca6989c01b822");
        session.GetQueue().StartThread();

        //Search default
        Search("");
    }

    /*void SetMap(MapView m) {
        //Caching
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(getCacheDir().getAbsolutePath(), "osmdroid-delta");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tiles");
        osmConf.setOsmdroidTileCache(tileCache);

        //Set
        XYTileSource src = new XYTileSource("Delta",0, 6, 256, ".png", new String[] {
                "https://tile-assets.deltamap.net/extinction/v1/0/"
        });
        Log.d("SetMap", src.getTileURLString(0));
        Log.d("SetMap", src.getTileURLString(1));
        m.setTileSource(src);
        m.setMultiTouchControls(true);
        m.setBackgroundResource(R.color.colorPrimary);
        m.setVerticalMapRepetitionEnabled(false);
        m.setHorizontalMapRepetitionEnabled(false);
        m.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        //Test
        Marker startMarker = new Marker(m);
        startMarker.setPosition(new GeoPoint(0, 0));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getDrawable(R.drawable.img_failed));
        m.getOverlays().add(startMarker);
    }*/

    void SetMap() {
        DeltaMapContainer map = (DeltaMapContainer)findViewById(R.id.map);
        DeltaMapConfig cfg = new DeltaMapConfig();
        cfg.layers = new DeltaMapLayer[] {
                new DeltaMapNetworkImageLayer() {
                    @Override
                    public String GetImageURL(DeltaMapConfig config, int zoom, int x, int y) {
                        return "https://tile-assets.deltamap.net/extinction/v1/0/"+zoom+"/"+x+"/"+y+".png";
                    }

                    @Override
                    public int GetMaxZoom(DeltaMapConfig config) {
                        return 6;
                    }
                },
        };
        cfg.maxNativeZoom = 150;
        map.LoadConfig(cfg);
    }

    @Override
    protected void onPause() {
        super.onPause();
        session.GetQueue().EndThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        session.GetQueue().StartThread();
    }

    public void OpenDrawer(View v) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpen) {
            CloseSearchDrawer();
            return;
        }

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
        //Show
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
        View hiddenPanel = findViewById(R.id.search_drawer);
        hiddenPanel.startAnimation(bottomUp);
        hiddenPanel.setVisibility(VISIBLE);
        isSearchOpen = true;

        //Activate text
        searchBox.requestFocus();

        //Fade in back button
        searchBackBtn.setVisibility(VISIBLE);
        searchBackBtn.animate().alpha(1f).setDuration(100);

        //Fade out old icon
        profileIcon.setVisibility(GONE);
        profileIcon.animate().alpha(0f).setDuration(100);
    }

    public void CloseSearchDrawer(View v) {
        CloseSearchDrawer();
    }

    public void CloseSearchDrawer() {
        //Deactivate text
        searchBox.clearFocus();

        //Hide virtual keyboard (gross)
        InputMethodManager imm = (InputMethodManager)searchBox.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

        //Hide
        Animation bottomUp = AnimationUtils.loadAnimation(this, R.anim.bottom_down);
        final View hiddenPanel = findViewById(R.id.search_drawer);
        bottomUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hiddenPanel.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hiddenPanel.startAnimation(bottomUp);
        isSearchOpen = false;

        //Clear
        searchBox.setText("");

        //Fade out back button
        searchBackBtn.setVisibility(GONE);
        searchBackBtn.animate().alpha(0f).setDuration(100);

        //Fade in old icon
        profileIcon.setVisibility(VISIBLE);
        profileIcon.animate().alpha(1f).setDuration(100);
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
