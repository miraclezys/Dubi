package cc.wo_mo.dubi.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import cc.wo_mo.dubi.R;
import cc.wo_mo.dubi.data.ApiClient;
import cc.wo_mo.dubi.data.DubiService;
import cc.wo_mo.dubi.data.Model.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    DubiService client = ApiClient.getClient();
    BlankFragment mBlankFragment;
    Blank1Fragment mBlank1Fragment;
    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBlankFragment = new BlankFragment();
        mBlank1Fragment = new Blank1Fragment();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_content, mBlankFragment).commit();
        currentFragment = mBlankFragment;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            if (mBlankFragment == null) {
                mBlankFragment = new BlankFragment();
            }
            switchFragment(currentFragment, mBlankFragment);

        } else if (id == R.id.nav_slideshow) {
            if (mBlank1Fragment == null) {
                mBlank1Fragment = new Blank1Fragment();
            }
            switchFragment(currentFragment, mBlank1Fragment);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            client.logout().enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    if (response.code() == 200) {
                        showToast("注销成功");
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
        currentFragment = to;
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