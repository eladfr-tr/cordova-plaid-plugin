package com.etoro.plaidwidget;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import androidx.activity.result.ActivityResultLauncher;

import com.plaid.link.OpenPlaidLink;
import com.plaid.link.Plaid;
import com.plaid.link.configuration.LinkTokenConfiguration;
import com.plaid.link.result.LinkExit;
import com.plaid.link.result.LinkSuccess;


public class PlaidWidget extends CordovaPlugin {

    public CallbackContext _callbackContext;

    private ActivityResultLauncher<LinkTokenConfiguration> linkAccountToPlaid = cordova.getActivity().registerForActivityResult(new OpenPlaidLink(),
    result -> {
        if (result instanceof LinkSuccess) {
        /* handle LinkSuccess */
        _callbackContext.success("hello plaid success");
        } else {
        _callbackContext.success("hello plaid failure");
        /* handle LinkExit */
        }
    }
    );

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        _callbackContext = callbackContext;
        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        } else {
            
            return false;

        }
    }

    public void openLink(JSONArray data, CallbackContext callbackContext) {
        String linkTokenFromServer = data.getString(0);
        LinkTokenConfiguration linkTokenConfiguration = new LinkTokenConfiguration.Builder()
            .token(linkTokenFromServer)
            .build();
        linkAccountToPlaid.launch(linkTokenConfiguration);
    }

    
}