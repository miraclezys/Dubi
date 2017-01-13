package cc.wo_mo.dubi.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.Model.BaseResponse;
import cc.wo_mo.dubi.Model.User;
import cc.wo_mo.dubi.utils.ImageUtils;
import cc.wo_mo.dubi.data.MSharePreferences;
import cc.wo_mo.dubi.utils.ProcessBitmap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    DubiService client;
    BlankFragment mBlankFragment;
    Blank1Fragment mBlank1Fragment;
    BlankFragment currentFragment;
    FloatingActionButton mFab;
    User user;
    ImageView userPhoto;
    TextView username;
    Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Dubi");
        client = ApiClient.getClient(this);
        mBlankFragment = new BlankFragment();
        mFab = (FloatingActionButton) findViewById(R.id.edit_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_content, mBlankFragment).commit();
        currentFragment = mBlankFragment;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Resources resource=(Resources)getBaseContext().getResources();
        ColorStateList csl=(ColorStateList)resource.getColorStateList(R.color.item_color);
        navigationView.setItemTextColor(csl);
        navigationView.setItemIconTintList(csl);
        navigationView.getMenu().getItem(0).setChecked(true);
        View headerView = navigationView.getHeaderView(0);
        userPhoto = (ImageView) headerView.findViewById(R.id.user_pic_img);
        username = (TextView) headerView.findViewById(R.id.user_name);
        searchButton = (Button)findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        refresh();
    }

    private void refresh() {
        ApiClient.getClient(this).getUserInfo(ApiClient.user_id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                    username.setText(user.username);
                    if (user.photo_url != null) {
                        ImageUtils.with(MainActivity.this)
                                .load(ApiClient.BASE_URL+user.photo_url)
                                .transform(new ProcessBitmap(ProcessBitmap.MODE_CIRCLE, 200, null))
                                .into(userPhoto);
                    }
                } else {
                    try {
                        Log.d("error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            currentFragment.getData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        refresh();
        if (id == R.id.all_tweets) {
            if (mBlankFragment == null) {
                mBlankFragment = new BlankFragment();
            }
            switchFragment(currentFragment, mBlankFragment);

        } else if (id == R.id.friend_tweets) {
            if (mBlank1Fragment == null) {
                mBlank1Fragment = new Blank1Fragment();
            }
            switchFragment(currentFragment, mBlank1Fragment);
        } else if (id == R.id.my_tweets) {
            Intent intent = new Intent(this, UserInfoActivity.class);
            intent.putExtra("user", ApiClient.gson.toJson(user));
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            client.logout().enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.code() == 200) {
                        showToast("注销成功");
                        MSharePreferences.getInstance(MainActivity.this)
                                .putBoolean("isLogin", false);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.this.finish();
                    } else {
                        showToast("注销失败");
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void switchFragment(Fragment from, Fragment to) {
        currentFragment = (BlankFragment) to;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (!to.isAdded()) {
            transaction.add(R.id.main_content, to);
        }
        transaction.hide(from).show(to).commit();
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
