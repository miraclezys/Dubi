package cc.wo_mo.dubi.activities;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.TweetAdapter;
import cc.wo_mo.dubi.data.Model.Tweet;

/**
 * Created by shushu on 2016/12/13.
 */

public class UserInfoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    int lastVisiblePosition;
    TweetAdapter mAdapter;
    List<Tweet> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information);
        Toolbar mToolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        mToolbar.setTitle("");
        Resources resource=(Resources)getBaseContext().getResources();
        ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.colorToolBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        return true;
    }
}
