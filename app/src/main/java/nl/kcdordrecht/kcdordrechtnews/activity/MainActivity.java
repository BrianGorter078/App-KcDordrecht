package nl.kcdordrecht.kcdordrechtnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.brian.woonkamer.clubblad.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static final String TWITTER_KEY = "cdCR5DyYwSx6mUxNfMTQ3vyLM";
    private static final String TWITTER_SECRET = "CRXIFipPtErs3hBkti118IEwMNGDpua8PHwE8YU4rukPI7G6oq";

    private Fragment clubbladFragment = null;
    private ListFragment twitterListfragment = null;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authenticateTwitter();
        setContentView(R.layout.activity_main);


        setupToolbar();
        instantiateFragment(0);
        displayFragment();

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        clubbladFragment = null;
        twitterListfragment = null;
        instantiateFragment(position);
        displayFragment();
    }

    private void authenticateTwitter(){
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }

    private void setupToolbar(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
    }

    private void instantiateFragment(int clickedItem){
        title = getString(R.string.app_name);
        switch (clickedItem) {
            case 0:
                clubbladFragment = new Clubblad();
                title = getString(R.string.title_clubblad);
                break;
            case 1:
                twitterListfragment = new Tweets();
                title = getString(R.string.title_zaalboek);
                break;
        }
    }
    private void displayFragment() {
        if (twitterListfragment != null || clubbladFragment != null) {

            FragmentTransaction fragmentTransaction = setupFragmentTransaction();
            fragmentTransaction.replace(R.id.container_body,getInstantiatedFragment());
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }

    }

    private FragmentTransaction  setupFragmentTransaction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return fragmentManager.beginTransaction();
    }


    private Fragment getInstantiatedFragment(){
        Fragment instantiatedFragment;
        if (twitterListfragment != null)
        {
            instantiatedFragment = twitterListfragment;
        }

        else
        {
            instantiatedFragment = clubbladFragment;
        }

        return instantiatedFragment;
    }
}