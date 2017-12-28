package com.example.jerry.newsapplication;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class FetchingData
{
    public static final String LOG = FetchingData.class.getSimpleName();
    public static ArrayList<Newss>Newss(String requestUrl){
        URL url=createUrl(requestUrl);
        String json=null;
        try{
            json=makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG,"Error",e);
        }
        ArrayList<Newss> news =extractNews(json);
        return news;
    }
    private static URL createUrl(String Url){
        URL url=null;
        try{
            url=new URL(Url);
        }catch (MalformedURLException e){
            Log.e(LOG,"Error with URL",e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url)throws IOException{
        String json=null;
        if(url==null){
            return null;
        }
        HttpURLConnection url11=null;
        InputStream inputStream=null;
        try{
            url11=(HttpURLConnection)url.openConnection();
            url11.setRequestMethod("GET");
            url11.connect();
            if(url11.getResponseCode()==200){
                inputStream=url11.getInputStream();
                json= read(inputStream);
            }
            else
            {
                Log.e(LOG,"Error Response Code:"+url11.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG,"Problem retrieving te news JSON resuls.",e);
        }finally {
            if(url11 !=null)
            {
                url11.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return json;

    }
    private static String read(InputStream inputStream)throws IOException{
        StringBuilder output=new StringBuilder();
        if(inputStream !=null){
            InputStreamReader input=new InputStreamReader(inputStream,Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(input);
            String line=reader.readLine();
            while(line!=null){
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();

    }
    private static ArrayList<Newss>extractNews(String JSONnews){
        if(TextUtils.isEmpty(JSONnews)){
            return null;
        }
        ArrayList<Newss>news=new ArrayList<>();
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
        String titlee=null;
        String descriptionn=null;
        String categoory=null;
        String authors=null;
        String link=null;
        Date publishedOn=null;
        try {
            JSONObject Object = new JSONObject(JSONnews);
            JSONObject response = Object.getJSONObject("response");
            JSONArray result = response.optJSONArray("results");
            for (int i = 0; i < result.length(); i++) {
                JSONObject jsonObject = result.getJSONObject(i);
                titlee = jsonObject.getString("webTitle").toString();
                JSONArray jsonArray = jsonObject.getJSONArray("tags");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject authorJson = jsonArray.getJSONObject(x);
                    authors = authorJson.getString("webTitle");
                    if (x != jsonArray.length() - 1) {
                        authors = authors + ",";
                    }
                }                categoory = jsonObject.getString("sectionName").toString();
                link = jsonObject.getString("webUrl").toString();
                try {
                    publishedOn = formatter.parse(jsonObject.getString("webPublicationDate").toString());

                } catch (java.text.ParseException e) {
                    publishedOn = null;
                }
                news.add(new Newss(titlee, descriptionn, categoory, authors, link, publishedOn));
            }
            return news;
        }catch (JSONException e){
            Log.e(LOG,"Problem parsing the earthquake Json Results",e);
            return null;
            }
        }
    }

