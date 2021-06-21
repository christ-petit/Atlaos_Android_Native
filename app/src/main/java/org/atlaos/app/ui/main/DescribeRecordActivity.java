package org.atlaos.app.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;


import org.atlaos.app.R;
import org.atlaos.app.databinding.DescribeRecordActivityBinding;
import org.atlaos.app.model.DescribeRecordModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DescribeRecordActivity extends AppCompatActivity implements View.OnClickListener {

    public static int RC_SIGN_IN = 999;
    private static final String TAG = "DescribeActivity";
    private GoogleSignInAccount googleSignInAccount;
    public DescribeRecordActivityBinding binding;

    private final Fragment pictureFragment = new PictureFragment();
    private final Fragment metaFragment = new MetaDataFragment();
    private final Fragment locationPickerFragment = new LocationPickerFragment();
    private final ChooseRecordFragment chooseRecordFragment = new ChooseRecordFragment(this);
    private final RecordPlayFragment recordPlayFragment = new RecordPlayFragment(this);
    private final UploadFragment uploadFragment = new UploadFragment();
    private DescribeRecordModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DescribeRecordActivityBinding.inflate(
                getLayoutInflater());

        setContentView(binding.container);
        binding.floatingActionButtonBottom.setOnClickListener(this);
        setSupportActionBar(findViewById(R.id.bottom_app_bar));
        model = new ViewModelProvider(this).get(DescribeRecordModel.class);
        if (model.getUuid() == null) model.setUuid(UUID.randomUUID());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottomappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_rec:
                switchToAudio();
                break;
            case R.id.app_bar_photo:
                switchToPhoto();
                break;
            case R.id.app_bar_loc:
                switchToFragment(locationPickerFragment);
                break;
            case R.id.app_bar_meta:
                switchToFragment(metaFragment);
                break;

        }
        return true;
    }

    private void switchToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.fragment_container_view, fragment)
                .commit();
    }

    public void switchToAudio() {
        if (model.getRecordUri() == null) {
            switchToFragment(chooseRecordFragment);
        } else {
            switchToFragment(recordPlayFragment);
        }
    }
    public void switchToPhoto() {
        switchToFragment(pictureFragment);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.floating_action_button_bottom:
                switchToFragment(uploadFragment);
                uploadFragment.submit();
                break;

        }
    }




    /*
        private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
            try {
             googleSignInAccount = completedTask.getResult(ApiException.class);

                // Signed in successfully, show authenticated UI.
                updateUI();
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    */
    private void updateUI() {
        Toast.makeText(this, "selected account :" + googleSignInAccount != null ? googleSignInAccount.getEmail() : "", Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}