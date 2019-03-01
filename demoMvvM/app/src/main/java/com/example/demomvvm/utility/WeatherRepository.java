package com.example.demomvvm.utility;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.demomvvm.remote.database.WeatherDao;
import com.example.demomvvm.remote.database.WeatherDatabase;
import com.example.demomvvm.remote.database.WeatherModel;

import javax.xml.transform.Result;

import androidx.lifecycle.LiveData;

public class WeatherRepository {
    private WeatherDao weatherDao;
    public LiveData<WeatherModel> weatherModel;


    public WeatherRepository(Application application) {
        WeatherDatabase db = WeatherDatabase.getDatabase(application);
        weatherDao = db.weatherDao();
        weatherModel = weatherDao.getdata();

    }


    public void insert(WeatherModel weatherModel) {
        new insertAsyncTask(weatherDao).execute(weatherModel);
    }

    private static class insertAsyncTask extends AsyncTask<WeatherModel, Void, Void> {
        WeatherDao aSyncweatherDao;

        public insertAsyncTask(WeatherDao aSyncweatherDao) {
            this.aSyncweatherDao = aSyncweatherDao;
        }

        @Override
        protected Void doInBackground(WeatherModel... weatherModels) {
            aSyncweatherDao.deleteAll();
            aSyncweatherDao.insert(weatherModels[0]);
            return null;

        }
    }

    public LiveData<WeatherModel> getData() {
        getdataAsynctask mgetdataAsynctask = new getdataAsynctask(weatherDao);
        mgetdataAsynctask.execute();

        return weatherModel;
    }

    //    private static class getdataAsynctask extends AsyncTask<Void, Void, WeatherModel> {
//        WeatherDao aSyncweatherDao;
//        WeatherModel weatherModelresult;
//
//
//        public getdataAsynctask(WeatherDao aSyncweatherDao) {
//            this.aSyncweatherDao = aSyncweatherDao;
//        }
//
//
//        @Override
//        protected WeatherModel doInBackground(Void... voids) {
//            aSyncweatherDao.getdata();
//
//            return weatherModelresult;
//        }
//
//
//        @Override
//        protected void onPostExecute(WeatherModel weatherModel) {
//            Log.d("postexcute", "" + weatherModel.name);
//            super.onPostExecute(weatherModel);
//        }
//    }
    private static class getdataAsynctask extends AsyncTask<Void, Void, WeatherModel> {
        WeatherDao aSyncweatherDao;
        WeatherModel weatherModeresult;
        public getdataAsynctask(WeatherDao aSyncweatherDao) {
            this.aSyncweatherDao = aSyncweatherDao;
        }


        @Override
        protected WeatherModel doInBackground(Void... voids) {
            aSyncweatherDao.getdata();
            Log.d("asynctask", "background");

            return weatherModeresult;
        }

        @Override
        protected void onPostExecute(WeatherModel weatherModel) {
            super.onPostExecute(weatherModel);
            Log.d("asynctask", "result"+weatherModeresult);
        }


    }


}
