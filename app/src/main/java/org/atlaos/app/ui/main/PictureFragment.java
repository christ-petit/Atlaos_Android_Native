package org.atlaos.app.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aminography.choosephotohelper.ChoosePhotoHelper;
import com.aminography.choosephotohelper.callback.ChoosePhotoCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.atlaos.app.R;
import org.atlaos.app.databinding.FragmentPictureBinding;
import org.atlaos.app.model.DescribeRecordModel;


public class PictureFragment extends Fragment implements View.OnClickListener {
    private ChoosePhotoHelper choosePhotoHelper;
    private DescribeRecordModel model;
    private FragmentPictureBinding binding;

    public PictureFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPictureBinding.inflate(
                inflater, container, false);
        model = new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
        binding.fragmentAddPicture.setOnClickListener(this);
        refreshImage();
        return binding.getRoot();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_add_picture:
                takepicture();
                break;
        }
    }

    private void takepicture() {
        choosePhotoHelper = ChoosePhotoHelper.with(this)
                .asUri()
                .build(new ChoosePhotoCallback<Uri>() {
                    @Override
                    public void onChoose(Uri photo) {
                        binding.fragmentAddPicture.setText(R.string.fragment_picture_change_picture);
                        model.setPhotoUri(photo);
                        refreshImage();
                    }
                });
        choosePhotoHelper.showChooser();
    }

    private void refreshImage() {
        if (model.getPhotoUri() != null) {
          //  binding.fragmentAddPicture.setVisibility(View.INVISIBLE);
            binding.fragmentImage.setVisibility(View.VISIBLE);
            binding.fragmentImage.forceLayout();
            binding.fragmentImage.requestLayout();
            RequestOptions myOptions = new RequestOptions()
                    .fitCenter();
            Glide.with(binding.fragmentImage)
                    .load(model.getPhotoUri())
                    .apply(myOptions)
                    .into(binding.fragmentImage);
            // forceLayout
        } else {
          //  binding.fragmentAddPicture.setVisibility(View.VISIBLE);
            binding.fragmentImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        choosePhotoHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        choosePhotoHelper.onActivityResult(requestCode, resultCode, data);

    }
}