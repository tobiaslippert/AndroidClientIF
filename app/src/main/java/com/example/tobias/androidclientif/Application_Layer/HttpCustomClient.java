package com.example.tobias.androidclientif.Application_Layer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tobias on 06.03.15.
 */
public class HttpCustomClient {

    //Var-declaration
    private Context context;
    public HttpClient client = new DefaultHttpClient();
    public CookieStore store = ((DefaultHttpClient) client).getCookieStore();


    //Constructor
    public HttpCustomClient() {
    }


    //Read and Access the server
    //Receives the URI, from where should be read
    //Returns the string read from the server
    public String readHerokuServer(String uri) {

        //Allow internet access
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //String-Builder for output String
        StringBuilder builder = new StringBuilder();
        //Get the cookie from the Cookiestore
        HttpContext ctx = new BasicHttpContext();
        ctx.setAttribute(ClientContext.COOKIE_STORE, store);
        //set the Url for the Get-request
        HttpGet httpGet = new HttpGet("https://inspection-framework.herokuapp.com/"+uri);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            System.out.println(statusCode);
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ParseJSON.class.toString(), "Download not possible!");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }

    //Postmethod
    //Receives the uri, where the String should be posted to and the String
    //Should be used than a new object is send to the server, that is not already stored in the server database
    public boolean postToHerokuServer(String uri, String username, String password){
        //Var declaration
        boolean status = false;
        StringBuilder builder = new StringBuilder();
        //Allow internet connection
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Set URL for post-request
        HttpPost httpPost = new HttpPost("https://inspection-framework.herokuapp.com/"+uri);

        try {
            List<NameValuePair> params = new LinkedList<NameValuePair>();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            System.out.println(statusCode);
            System.out.println(response);
            if (statusCode == 200) {
                status = true;
                //CookieStore store = ((DefaultHttpClient) client).getCookieStore();
                List<Cookie> cookies = store.getCookies();

                if (cookies != null) {
                    for (Cookie c : cookies) {
                        store.addCookie(c);
                        System.out.println(c);
                    }
                }

                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ParseJSON.class.toString(), "Download not possible!");
                status = false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    return status;
    }

    //Putmethod
    //Receives the URI where the object should be put at and the String
    //Should be used than an existing object of the server database should be updated
    public void putToHerokuServer(String uri, String jsonObject){
        JSONObject jO;

        //Allow internet connection
        StrictMode.ThreadPolicy policy = new StrictMode.
        ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //set URL for Put-request
        HttpPut httpPut = new HttpPut("https://inspection-framework.herokuapp.com/"+uri);

        //Create a new JSONObject from the given String
        try {
            jO = new JSONObject(jsonObject);
            //passes the results to a string builder/entity
            try {
                StringEntity se = new StringEntity(jO.toString());
                httpPut.setEntity(se);
                //sets a request header so the page receving the request
                //will know what to do with it
                httpPut.setHeader("Accept", "application/json");
                httpPut.setHeader("Content-type", "application/json");
                try {
                    client.execute(httpPut);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Check whether a internet connection is available
    //Returns true, when a connection is available
    //Returns false, when no connection is available
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
