package com.example.demomvvm.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.example.demomvvm.model.WeatherModel.WeatherDigital;
import com.example.demomvvm.remote.database.WeatherModel;
import com.example.demomvvm.remote.retrofit.ApiUtils;
import com.example.demomvvm.remote.retrofit.SOService;
import com.example.demomvvm.utility.WeatherRepository;
import com.example.demomvvm.view.WeatherActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
        this.name.setValue("Home");
        weatherRepository = new WeatherRepository(application);
        weatherModelLiveData = weatherRepository.getData();

        this.pressure.setValue(0);
        this.windr.setValue(0);
        this.humidity.setValue(0);
        this.temp.setValue(0.00);
        this.cloud.setValue(0);
        this.winsp.setValue(0.00);
        mService = ApiUtils.getSOService();


    }

    public LiveData<WeatherModel> getWeatherModelLiveData() {
        return weatherModelLiveData;
    }

    public void loadData() {
        mService.EXAMPLE_CALL().enqueue(new Callback<WeatherDigital>() {
            @Override
            public void onResponse(Call<WeatherDigital> call, Response<WeatherDigital> response) {

                if (response.isSuccessful()) {
                    WeatherDigital resultApi;
                    resultApi = response.body();
                    String names = resultApi.getName();
                    double tempC = Math.round(resultApi.getMain().getTemp() / 1.8);
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
                                    //

                    insertdata(weatherModel);


                    temp.setValue(tempC);
                    name.setValue(names);
                    winsp.setValue(winspe);
                    cloud.setValue(clouds);
                    humidity.setValue(Humidity);
                    pressure.setValue(Presssure);
                    windr.setValue(windrect);
                    Log.d("Log", "posts loaded from API");
                    Toast.makeText(getApplication(), "ok", Toast.LENGTH_SHORT).show();

                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code

                    Log.d("Log", "posts loaded from API error" + statusCode);
                }
            }

            @Override
            public void onFailure(Call<WeatherDigital> call, Throwable t) {
                Toast.makeText(getApplication(), "offline", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void insertdata(WeatherModel weatherModel) {
        weatherRepository.insert(weatherModel);
        Log.d("insert", "ok");

    }


}
