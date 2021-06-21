package org.atlaos.app.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;

import org.atlaos.app.R;
import org.atlaos.app.databinding.FragmentMetaDataBinding;
import org.atlaos.app.model.DescribeRecordModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetaDataFragment extends Fragment implements TextWatcher {
    private FragmentMetaDataBinding binding;
    private DescribeRecordModel model;
    Map<String,String> provinceMap;
    Map<String,String> districtMap;
    Map<String,String> typeMap;
    public MetaDataFragment() {
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMetaDataBinding.inflate(
                inflater,  container, false);


        return binding.getRoot();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {


String province=  binding.metaRecordProvinceSpinner.getText().toString();
if (provinceMap.containsKey(province)) {
           province=provinceMap.get(province);
}      //refreshing all values
if (! model.getProvince().equals(province)) {
         model.setProvince(province);

      int ressourceId=-1;
    switch(model.getProvince()) {
        case "Attapeu":
            ressourceId = R.array.form_district_list_attapeu_values;
            break;
        case "Bokeo":
            ressourceId = R.array.form_district_list_bokeo_values;
            break;
        case "Borikhamxay":
            ressourceId = R.array.form_district_list_borikhamxay_values;
            break;
        case "Champasack":
            ressourceId = R.array.form_district_list_champasack_values;
            break;
        case "Huaphanh":
            ressourceId = R.array.form_district_list_huaphanh_values;
            break;
        case "Khammuane":
            ressourceId = R.array.form_district_list_khammuane_values;
            break;
        case "Luangnamtha":
            ressourceId = R.array.form_district_list_luangnamtha_values;
            break;
        case "Luangprabang":
            ressourceId = R.array.form_district_list_luangprabang_values;
            break;
        case "Oudomxay":
            ressourceId = R.array.form_district_list_oudomxay_values;
            break;
        case "Phongsaly":
            ressourceId = R.array.form_district_list_phongsaly_values;
            break;
        case "Saravane":
            ressourceId = R.array.form_district_list_saravane_values;
            break;
        case "Savanakhet":
            ressourceId = R.array.form_district_list_savanakhet_values;
            break;
        case "Sekong":
            ressourceId = R.array.form_district_list_sekong_values;
            break;
        case "Vientiane Capital":
            ressourceId = R.array.form_district_list_vientianecapital_values;
            break;
        case " Vientiane Province":
            ressourceId = R.array.form_district_list_vientianeprovince_values;
            break;
        case "Xayaboury":
            ressourceId = R.array.form_district_list_xayaboury_values;
            break;
        case "Xaysomboune":
            ressourceId = R.array.form_district_list_xaysomboune_values;
            break;
        case "Xienkhuang":
            ressourceId = R.array.form_district_list_xienkhuang_values;
            break;
    }
    if (ressourceId>0) {
        List<String>districtList = Arrays.asList( getResources().getStringArray(ressourceId));
       ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, districtList);
        binding.metaRecordDistrictSpinner.setAdapter(adapter);
        binding.metaRecordDistrictSpinner.setText("");
    }
        }

    model.setStoryName(binding.metaRecordNameEdit.getText().toString());
    model.setArtist(binding.metaRecordArtistEdit.getText().toString());
    model.setType_other(binding.metaRecordOtherEdit.getText().toString());
    model.setVillage(binding.metaRecordVillageEdit.getText().toString());
    String district=binding.metaRecordDistrictSpinner.getText().toString();
    if (districtMap.containsKey(district)) {
        model.setDistrict(districtMap.get(district));
       }
    String type=binding.metaRecordTypeSpinner.getText().toString();
    if (typeMap.containsKey(type)) {
            model.setType(typeMap.get(type));
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
        binding.metaRecordArtistEdit.setText(model.getStoryName());
        binding.metaRecordNameEdit.setText(model.getArtist());
        binding.metaRecordVillageEdit.setText(model.getVillage());
        binding.metaRecordOtherEdit.setText(model.getType_other());
    //    binding.metaRecordProvinceSpinner.setSelection(((ArrayAdapter<String>) binding.metaRecordProvinceSpinner.getAdapter()).getPosition(model.getProvince()));
       // binding.metaRecordDistrictSpinner.setSelection(((ArrayAdapter<String>) binding.metaRecordDistrictSpinner.getAdapter()).getPosition(model.getDistrict()));
      //  binding.metaRecordTypeSpinner.setSelection(((ArrayAdapter<String>)  binding.metaRecordTypeSpinner.getAdapter()).getPosition(model.getType()));
        binding.metaRecordArtistEdit.addTextChangedListener(this);
        binding.metaRecordNameEdit.addTextChangedListener(this);
        binding.metaRecordOtherEdit.addTextChangedListener(this);
        binding.metaRecordProvinceSpinner.addTextChangedListener(this);
        binding.metaRecordDistrictSpinner.addTextChangedListener(this);
        binding.metaRecordTypeSpinner.addTextChangedListener(this);

        List<String> provinceListList = Arrays.asList( getResources().getStringArray(R.array.form_province_list_values));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, provinceListList);
        binding.metaRecordProvinceSpinner.setAdapter(adapter);
        List<String>districtList = Arrays.asList( getResources().getStringArray(R.array.form_district_list_attapeu_values));
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, districtList);
        binding.metaRecordDistrictSpinner.setAdapter(adapter);
        List<String> typeList = Arrays.asList( getResources().getStringArray(R.array.form_type_list_values));
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, typeList);
        binding.metaRecordTypeSpinner.setAdapter(adapter);
       //building a map of localized text to code to normalize form submission

        String[] localizedProvinces=getResources().getStringArray(R.array.form_province_list_values);
        String[] provincescode=getResources().getStringArray(R.array.form_province_list_code);
      provinceMap=new HashMap<String,String>(localizedProvinces.length);
        int i=0;
        for (String province : localizedProvinces) {
            provinceMap.put(province,provincescode[i++]);
        }
        String[] localizedDistricts=getResources().getStringArray(R.array.form_district_list_districts_values);
        String[] districtscode=getResources().getStringArray(R.array.form_district_list_districts_codes);

      districtMap =new HashMap<String,String>(localizedDistricts.length);
        i=0;
        for (String district : localizedDistricts) {
            districtMap.put(district,districtscode[i++]);
        }
        String[] localizedTypes=getResources().getStringArray(R.array.form_type_list_values);
        String[] typescode=getResources().getStringArray(R.array.form_type_list_code);

        typeMap =new HashMap<String,String>(localizedTypes.length);
        i=0;
        for (String type : localizedTypes) {
            typeMap.put(type,typescode[i++]);
        }

    }

}