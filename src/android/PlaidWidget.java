package com.etoro.plaidwidget;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;

import com.plaid.link.OpenPlaidLink;
import com.plaid.link.Plaid;
import com.plaid.link.PlaidHandler;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkExit;
import com.plaid.link.result.LinkResultHandler;
import com.plaid.link.result.LinkSuccess;

import kotlin.Unit;


public class PlaidWidget extends CordovaPlugin {

    public CallbackContext _callbackContext;

    private LinkResultHandler linkResultHandler = new LinkResultHandler(
            linkSuccess -> {
                /* handle LinkSuccess */
                _callbackContext.success("hello LinkSuccess");
                return Unit.INSTANCE;
            },
            linkExit -> {
                /* handle LinkExit */
                _callbackContext.success("hello linkexit");
                return Unit.INSTANCE;
            }
    );

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        _callbackContext = callbackContext;
        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            // callbackContext.success(message);
            openLink(data, callbackContext);
            return true;
        } else {
            return false;
        }
    }

    public void openLink(JSONArray data, CallbackContext callbackContext) throws JSONException {
        Log.d("open link", "open link start");
        Context context= this.cordova.getActivity().getApplicationContext();
        Log.d("open link context", context.toString());
        cordova.setActivityResultCallback (this);
        String linkTokenFromServer = data.getString(0);
        LinkTokenConfiguration linkTokenConfiguration = new LinkTokenConfiguration.Builder()
            .token(linkTokenFromServer)
            .build();
        PlaidHandler plaidHandler = Plaid.create((Application) context, linkTokenConfiguration);
        plaidHandler.open(this.cordova.getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("plaid", "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (!linkResultHandler.onActivityResult(requestCode, resultCode, data)) {
            // Not handled by the LinkResultHandler
        }
    }

    
}