package com.example.ResumeAppV1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;

import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Home.OnHomeFragmentInteractionListener,
        Projects.OnProjectsFragmentInteractionListener, Skills.OnSkillsFragmentInteractionListener,
        Contact.OnContactFragmentInteractionListener, MailForm.OnMailFormFragmentInteractionListener,
        WebViewFragment.OnWebViewFragmentInteractionListener {

    private static final String mailFormFragmentTag = "MAILFORM_FRAGMENT";
    private static final String webViewFragmentTag = "WEB_FRAGMENT";
    private boolean mBackPressedOnce;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mBackPressedOnce = false;
        }
    };
    private int delay = 2000;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment fragment = new Home();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        WebViewFragment webViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentByTag(webViewFragmentTag);
        MailForm mailForm = (MailForm) getSupportFragmentManager().findFragmentByTag(mailFormFragmentTag);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (webViewFragment != null && webViewFragment.isVisible()) {
            webViewFragment.webNavigation();
        }
        else if (mailForm != null && mailForm.isVisible())
        {
            mailForm.onBackButtonPressed();
        }
        else {
            if (mBackPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.mBackPressedOnce = true;
            String mMessage = getResources().getString(R.string.pressBackTwice);
            mToast = Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_SHORT);
            mToast.show();

            mHandler.postDelayed(mRunnable, delay);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mToast != null) {
            mToast.cancel();
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_contact:
                fragment = new Contact();
                break;
            case R.id.nav_skills:
                fragment = new Skills();
                break;
            case R.id.nav_project:
                fragment = new Projects();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Fragment not ready.", Toast.LENGTH_SHORT).show();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        showFragment(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("Call", "Permission is Granted!!");
                return true;
            } else {
                Log.v("Call", "Permission is Revoked!!");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.permissionToastCall), Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else {
            Log.v("Call", "Permission is granted!!");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.v("func", "Called");
        switch (requestCode) {
            case 1: //for Call
            {
                Toast.makeText(getApplicationContext(), "Case 1", Toast.LENGTH_SHORT).show();
                Log.v("Permission Result", "Case 1");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission is Granted!!", Toast.LENGTH_LONG).show();
                    //callAction();
                    onCallButtonPressed(Uri.parse("tel:7478755667"));
                } else {
                    Toast.makeText(getApplicationContext(), "Permission is Denied!!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                Log.v("onReqPermissionsRes", "default case reached. requestCode = " + requestCode);
            }
        }
    }


    //Home.OnHomeFragmentInteractionListener
    @Override //same for both Contact Fragment and Home fragment
    public void onCallButtonPressed(Uri uri) {
        if (isCallPermissionGranted()) {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(uri);
            startActivity(call);
        }
    }

    @Override
    public void onMailButtonPressed(Bundle bundle) {
        onSendMailButtonPressed(bundle);
    }

    //Projects.OnProjectsFragmentInteractionListener
    @Override
    public void onProjectsFragmentListViewInteraction(int i, String[] toastArray) {
        Toast.makeText(getApplicationContext(), String.valueOf(toastArray[i]), Toast.LENGTH_SHORT).show();
    }

    //Skills.OnSkillsFragmentInteractionListener
    @Override
    public void onSkillsFragmentGridViewInteraction(int i, String[] toastArray) {
        Toast.makeText(getApplicationContext(), toastArray[i], Toast.LENGTH_SHORT).show();
    }

    //Contact.OnContactFragmentInteractionListener
    @Override
    public void onMailButtonPressed() {
        MailForm fragment = new MailForm();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, mailFormFragmentTag);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onSmsButtonPressed(Bundle bundle) {
        //Toast.makeText(getApplicationContext(), "Button not ready yet.", Toast.LENGTH_SHORT).show();
        String number = bundle.getString(Contact.numberKey);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
        Intent chooser = Intent.createChooser(intent, getResources().getString(R.string.smsPrompt));
        startActivity(chooser);
    }

    @Override
    public void onWebButtonPressed(String url) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, webViewFragmentTag);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //MailForm.OnMailFormFragmentInteractionListener
    @Override
    public void onSendMailButtonPressed(Bundle bundle) {
        String email = bundle.getString(MailForm.emailKey);
        String subject = bundle.getString(MailForm.subjectKey);
        String body = bundle.getString(MailForm.bodyKey);
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        Intent chooser = Intent.createChooser(intent, getResources().getString(R.string.mailPrompt));
        startActivity(chooser);
    }

    //WebViewFragment.OnWebViewFragmentInteractionListener
    @Override
    public void onWebViewFragmentBackButtonPressedFromHomeWebsite() {
        showFragment(R.id.nav_contact);
    }

    @Override
    public void onBackPressedFromMailForm() {
        showFragment(R.id.nav_contact);
    }
}
