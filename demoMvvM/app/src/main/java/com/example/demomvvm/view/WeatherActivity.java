package com.example.demomvvm.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.demomvvm.R;
import com.example.demomvvm.databinding.ActivityWeatherBinding;
import com.example.demomvvm.model.WeatherModel.WeatherDigital;
import com.example.demomvvm.remote.database.WeatherModel;
import com.example.demomvvm.remote.retrofit.ApiUtils;
import com.example.demomvvm.remote.retrofit.SOService;
import com.example.demomvvm.viewmodel.WeatherViewModel;

public class WeatherActivity extends AppCompatActivity {
    ActivityWeatherBinding binding;
    private SOService mService;
    private WeatherViewModel weatherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = ApiUtils.getSOService();
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        binding.setLifecycleOwner(this);
        binding.setViewmodel(weatherViewModel);

    }

}
