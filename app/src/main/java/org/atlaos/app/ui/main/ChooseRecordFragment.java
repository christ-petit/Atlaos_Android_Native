package org.atlaos.app.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaos.app.R;
import org.atlaos.app.model.DescribeRecordModel;
import org.jetbrains.annotations.NotNull;


public class ChooseRecordFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = ChooseRecordFragment.class.getName();
    private   ActivityResultLauncher<String> pickRecordLauncher;
    private   ActivityResultLauncher<Intent> recordLauncher;
    private DescribeRecordActivity describeRecordActivity;

    public ChooseRecordFragment() {
     }
    public ChooseRecordFragment(DescribeRecordActivity describeRecordActivity) {
   this.describeRecordActivity=describeRecordActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_choose_record, container, false);
        view.findViewById(R.id.fragment_choose_choose).setOnClickListener(this);
        view.findViewById(R.id.fragment_choose_record).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_choose_choose:choose();
                    break;
            case R.id.fragment_choose_record:
                                            record();
                                            break;
        }
    }

    private void choose(){
        pickRecordLauncher.launch("audio/*");
    }
    private void record(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        recordLauncher.launch(intent);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
       pickRecordLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {

                          Log.w(TAG,"got result :"+uri);
                          goToPlayFragment(uri);
                        }
                    });
        recordLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            Log.w(TAG, "got uri :" + uri);
                            goToPlayFragment(uri);
                        }
                    }
                });

    }

    private void goToPlayFragment(Uri uri) {
        if (uri != null) {
            DescribeRecordModel model = new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
            model.setRecordUri(uri);
            describeRecordActivity.switchToAudio();
        }
    }


}