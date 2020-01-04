package com.romanport.deltawebmap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.romanport.deltawebmap.Activities.LoginActivity;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.EchoIconData;
import com.romanport.deltawebmap.Framework.API.Echo.Tribes.Icons.IconResponseData;
import com.romanport.deltawebmap.Framework.API.Entities.ArkMapData;
import com.romanport.deltawebmap.Framework.API.Entities.Vector3;
import com.romanport.deltawebmap.Framework.HTTPErrorHandler;
import com.romanport.deltawebmap.Framework.HTTPTool;
import com.romanport.deltawebmap.Framework.Search.SearchAction;
import com.romanport.deltawebmap.Framework.Search.SearchAdapter;
import com.romanport.deltawebmap.Framework.Search.SearchRequest;
import com.romanport.deltawebmap.Framework.Session.Actions.ConfigureMapAction;
import com.romanport.deltawebmap.Framework.Session.DeltaServerSession;
import com.romanport.deltawebmap.Framework.Session.SessionActivityConnection;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapConfig;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.DeltaMapLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Icons.DeltaMapEchoIconData;
import com.romanport.deltawebmap.Framework.Views.Maps.Data.Templates.Layers.DeltaMapNetworkImageLayer;
import com.romanport.deltawebmap.Framework.Views.Maps.DeltaMapContainer;

import java.util.LinkedList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SessionActivityConnection, HTTPErrorHandler {

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

        //Set up session
        session = new DeltaServerSession(this, this,"5dff22fa2c4cf87c38f5b173", this);
        session.GetQueue().StartThread();

        //Search default
        Search("");

        //Set up the map
        session.QueueAction(this, new ConfigureMapAction());
    }

    public void SetMap(IconResponseData iconData, ArkMapData mapData) {
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
        cfg.initialPos = new Vector3(0f, 0f, 2f);
        cfg.icons = new LinkedList<>();
        cfg.iconSize = 150;
        for(EchoIconData ic : iconData.icons) {
            cfg.icons.add(new DeltaMapEchoIconData(ic, mapData));
        }
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
        action.input = request;
        session.QueueAction(this, action);
    }

    public void OnHTTPAuthFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(HTTPTool.GetAccessToken(getApplicationContext()) == null) {
                    //If the token simply isn't set, prompt login
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    //We'll notify the user that they were disconnected
                    NotifyUserSignedOut();
                }
            }
        });
    }

    public void NotifyUserSignedOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.force_signed_out_title);
        builder.setMessage(R.string.force_signed_out_sub);
        builder.setPositiveButton(R.string.force_signed_out_go, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton(R.string.force_signed_out_ignore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimary);
        dialog.show();
    }

    public void OnHTTPErrorBackground(int statusCode){

    }

    public void OnHTTPErrorForeground(final int statusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast t = Toast.makeText(getApplicationContext(), GetHTTPErrorResourceStringFromStatusCode(statusCode), Toast.LENGTH_LONG);
                t.show();
            }
        });
    }

    //Returns a resource ID for a string to be displayed to the user as an error
    private int GetHTTPErrorResourceStringFromStatusCode(int statusCode) {
        if(statusCode == -1)
            return R.string.http_error_net;
        if(statusCode == 404)
            return R.string.http_error_404;
        if(statusCode == 503)
            return R.string.http_error_503;
        return R.string.http_error_500;
    }
}
