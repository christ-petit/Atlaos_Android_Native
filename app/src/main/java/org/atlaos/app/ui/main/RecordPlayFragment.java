package org.atlaos.app.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.atlaos.app.databinding.FragmentRecordPlayBinding;
import org.atlaos.app.model.DescribeRecordModel;
import org.atlaos.app.utils.MediaPlayerController;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class RecordPlayFragment extends Fragment implements View.OnClickListener {


    public  FragmentRecordPlayBinding binding;
    private DescribeRecordModel model;
    private DescribeRecordActivity describeRecordActivity;
    MediaPlayerController controller;

    public RecordPlayFragment() {     }

    public RecordPlayFragment(DescribeRecordActivity describeRecordActivity) {
    this.describeRecordActivity=describeRecordActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRecordPlayBinding.inflate(
                inflater,  container, false);

        model = new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
        controller=new MediaPlayerController(binding.seekBar,binding.play,binding.totalDuration,binding.currentDuration,getContext());
        if(model.getRecordUri()!= null) {
        controller.loadRecord(model.getRecordUri());
        }

        binding.remove.setOnClickListener(this);
        return binding.getRoot();
    }

    private void clearAnswer() {
        model.setRecordUri(null);
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (controller!= null) controller.onDestroy();
    }

    @Override
    public void onClick(View v) {
        clearAnswer();
    }
}