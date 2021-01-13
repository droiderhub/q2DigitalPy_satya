package com.tarang.dpq2.base.terminal_sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tarang.dpq2.base.AppManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CheckConnection extends AsyncTask<Void, Void, Boolean> {

    private CheckConnectionInterface mListener;
    Context mContext;


    public CheckConnection(Context context, CheckConnectionInterface mListener) {
        mContext = context;
        this.mListener  = mListener;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Boolean doInBackground(Void... params) {
       /* Boolean  result = null;
        try {
            if (isOnline(AppManager.getContext())){

            }
              result= InetAddress.getByName(AppManager.getInstance().getIP()).isReachable(Integer.parseInt(AppManager.getInstance().getPort()));
            Logger.v("conn_results---"+result);
            Logger.v("conn_results---"+AppManager.getInstance().getIP());
            Logger.v("conn_results---"+Integer.parseInt(AppManager.getInstance().getPort()));
              return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.v("conn_resultsout---"+result);
        return result;*/


        try (final Socket socket = new Socket()) {
            final InetAddress inetAddress = InetAddress.getByName(AppManager.getInstance().getIP());
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, Integer.parseInt(AppManager.getInstance().getPort()));
            socket.connect(inetSocketAddress, 5000);
            return true;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    protected void onPostExecute(Boolean result) {
        if (mListener != null)
            mListener.myMethod(result);
    }
}
