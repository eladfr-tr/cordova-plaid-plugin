/*global cordova, module*/

module.exports = {
    open: function (name) {
        console.log("Plaid Cordova - open plaid widget cordova plugin");
        return new Promise(function (resolve,reject) {
            cordova.exec(
                function successCallback (response) {
                    console.log("Plaid Cordova - success callback response",response);
                    resolve(response)
                },
                function errorCallback (response) {
                    console.log("Plaid Cordova - Error callback response",response);
                    reject(response);

                }
                , "PlaidWidget", "open", [name]);
        });
    }
};