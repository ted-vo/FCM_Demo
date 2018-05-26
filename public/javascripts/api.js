/**
 * Created by NXON on 11/12/16.
 */
var server_url = 'http://localhost:3000';
var api = {
    call: function (route, data, method, callback) {
        var httpObj;
        if (window.XMLHttpRequest)
            httpObj = new XMLHttpRequest();
        else if (window.ActiveXObject)
            httpObj = new ActiveXObject("Microsoft.XMLHTTP");

        httpObj.onreadystatechange = function () {
            if (httpObj.readyState == 4) {
                var checkObject = httpObj.responseText.startsWith('{');
                if (checkObject) {
                    if (httpObj.status == 200) {
                        response = JSON.parse(httpObj.responseText);
                        if (response.success) {            
                            if (typeof callback == 'function')
                                callback(true, response.results);
                        } else httpObj.status = response.code;
                    }

                    if (!response.success && (typeof callback == 'function'))
                        callback(false, response);
                } else callback(true, httpObj.responseText);
            }
        };

        var url = route;
        if (!url.startsWith('http'))
            url = server_url + url;

        if (httpObj) {
            httpObj.open(method, url);
            if ((method !== 'GET') && (data !== null) && (typeof data == 'object')) {
                httpObj.setRequestHeader('Content-Type', 'application/json');
                httpObj.send(JSON.stringify(data));
            } else httpObj.send();
        }
    },
    post: function (route, data, callback) {
        api.call(route, data, 'POST', callback);
    },
    get: function (route, data, callback) {
        api.call(route, data, 'GET', callback);
    },
    put: function (route, data, callback) {
        api.call(route, data, 'PUT', callback);
    },
    delete: function (route, data, callback) {
        api.call(route, data, 'DELETE', callback);
    }
};