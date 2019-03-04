package com.example.demomvvm.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.demomvvm.model.WeatherModel.WeatherDigital;
import com.example.demomvvm.remote.database.WeatherDao;
import com.example.demomvvm.remote.database.WeatherModel;
import com.example.demomvvm.remote.retrofit.ApiUtils;
import com.example.demomvvm.remote.retrofit.SOService;
import com.example.demomvvm.utility.WeatherRepository;
import com.example.demomvvm.view.WeatherActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<Integer> pressure = new MutableLiveData<>();
    public MutableLiveData<Integer> windr = new MutableLiveData<>();
    public MutableLiveData<Integer> humidity = new MutableLiveData<>();
    public MutableLiveData<Double> temp = new MutableLiveData<>();
    public MutableLiveData<Integer> cloud = new MutableLiveData<>();
    public MutableLiveData<Double> winsp = new MutableLiveData<>();
    private SOService mService;


    WeatherRepository weatherRepository;
    LiveData<WeatherModel> weatherModelLiveData;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        weatherRepository = new WeatherRepository(application);
        weatherModelLiveData = weatherRepository.getData();
        mService = ApiUtils.getSOService();
        setdata("home", 0, 0, 0, 0.0, 0, 0.0);
        loadData();

    }


    public void setdata(String name, int pressure, int windr, int humidity, double temp, int cloud, double winsp) {
        this.name.setValue(name);
        this.pressure.setValue(pressure);
        this.windr.setValue(windr);
        this.humidity.setValue(humidity);
        this.temp.setValue(temp);
        this.cloud.setValue(cloud);
        this.winsp.setValue(winsp);
    }


    public void loadData() {

        mService.EXAMPLE_CALL().enqueue(new Callback<WeatherDigital>() {
            @Override
            public void onResponse(Call<WeatherDigital> call, Response<WeatherDigital> response) {

                if (response.isSuccessful()) {
                    WeatherDigital resultApi;
                    resultApi = response.body();


                    String names = resultApi.getName();
                    double tempC = Math.round(resultApi.getMain().getTemp() - 275.15);
                    int Presssure = resultApi.getMain().getPressure();
                    int Humidity = resultApi.getMain().getHumidity();
                    int clouds = resultApi.getClouds().getAll();
                    double winspe = resultApi.getWind().getSpeed();
                    int windrect = resultApi.getWind().getDeg();


                    WeatherModel weatherModel = new WeatherModel(names,
                            tempC,
                            Presssure,
                            Humidity,
                            clouds,
                            winspe,
                            windrect);

                    insertdata(weatherModel);
                    setdata(names, Presssure, windrect, Humidity, tempC, clouds, winspe);
                    Toast.makeText(getApplication(), "ok", Toast.LENGTH_SHORT).show();

                } else {
                    int statusCode = response.code();
                    Toast.makeText(getApplication(), "" + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherDigital> call, Throwable t) {
                Toast.makeText(getApplication(), "offline", Toast.LENGTH_SHORT).show();
                Log.d("log", "khong ket noi");
                weatherModelLiveData.observe((LifecycleOwner) getApplication(), new Observer<WeatherModel>() {
                    @Override
                    public void onChanged(WeatherModel weatherModel) {
                        getdatabase();
                    }
                });
            }
        });


    }


    public void insertdata(WeatherModel weatherModel) {
        weatherRepository.insert(weatherModel);
        Log.d("insert", "ok");

    }


    public void getdatabase() {

        setdata(weatherModelLiveData.getValue().name,
                weatherModelLiveData.getValue().pressure,
                weatherModelLiveData.getValue().windr,
                weatherModelLiveData.getValue().humidity,
                weatherModelLiveData.getValue().temp,
                weatherModelLiveData.getValue().cloud,
                weatherModelLiveData.getValue().winsp);
    }


}
