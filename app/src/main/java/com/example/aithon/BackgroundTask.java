package com.example.aithon;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundTask extends AsyncTask<String, Void, String> {

    String json_url;
    String JSON_STRING, msg;

    public AsyncResponse delegate = null;

    public BackgroundTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @Override
    protected String doInBackground(String... strings) {

        json_url = "https://vesaithon.000webhostapp.com";
        String havetoDo;
        String getSet = json_url + "/app/getdata.php";
        String flagd = json_url + "/app/updateflag.php";
        String setXyz = json_url + "/circuit/set_xyz.php";
        havetoDo = strings[0];
        switch (havetoDo) {
            case "getsetall":
                try {
                    URL url = new URL(getSet);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine()) != null) {
                        stringBuilder.append(JSON_STRING + "\n");
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    System.out.println("iiiiiiiiinnnnnnnnnnnnccccccccccccccaaaaaaaaassssssssssseeeeeeeeee :" + stringBuilder.toString());
                    return stringBuilder.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "flag": {
                String nm = strings[1];

                try {
                    URL url = new URL(flagd);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String data = URLEncoder.encode("flag", "UTF-8") + "=" + URLEncoder.encode(nm, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    is.close();
                    httpURLConnection.disconnect();
                    JSON_STRING = sb.toString().trim();
                    return JSON_STRING;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "set_xyz": {
                String[] part_xyz;
                part_xyz = strings[1].split(" ");
                System.out.println("tttttttttttttttttttttttttttttttt :"+strings[1]);
                System.out.println("ssssssssssssssssssssssssssssssssssss"+part_xyz[0]+"eeeeeeeeeeeeeeee"+part_xyz[1]+"bbbbbbbbb"+part_xyz[2]);
                try {
                    URL url = new URL(setXyz);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String data = URLEncoder.encode("x1","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[0],"UTF-8")+"&"+
                            URLEncoder.encode("y1","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[1],"UTF-8")+"&"+
                            URLEncoder.encode("z1","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[2],"UTF-8")+"&"+
                            URLEncoder.encode("x2","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[3],"UTF-8")+"&"+
                            URLEncoder.encode("y2","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[4],"UTF-8")+"&"+
                            URLEncoder.encode("z2","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[5],"UTF-8")+"&"+
                            URLEncoder.encode("x3","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[6],"UTF-8")+"&"+
                            URLEncoder.encode("y3","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[7],"UTF-8")+"&"+
                            URLEncoder.encode("z3","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[8],"UTF-8")+"&"+
                            URLEncoder.encode("x4","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[9],"UTF-8")+"&"+
                            URLEncoder.encode("y4","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[10],"UTF-8")+"&"+
                            URLEncoder.encode("z4","UTF-8")+"="+
                            URLEncoder.encode(part_xyz[11],"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    is.close();
                    httpURLConnection.disconnect();
                    JSON_STRING = sb.toString().trim();
                    return JSON_STRING;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return "sejpal";
    }


    @Override
    protected void onPreExecute() {
        json_url = "https://vesaithon.000webhostapp.com/app/getdata.php";
    }


    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
