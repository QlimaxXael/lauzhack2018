package ch.pl.com.lauzhack2018;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonQuery {

    private static ExecutorService executorService;

    public JsonQuery () {
        if (executorService == null) executorService = Executors.newFixedThreadPool(4);
    }

    public Future<JSONObject> getJson(String url) {
        return executorService.submit(new JSONCallable(url));
    }

    private class JSONCallable implements Callable<JSONObject> {
        private String url;

        public JSONCallable(String url) {
            this.url = url;
        }

        @Override
        public JSONObject call() throws Exception {
            return readJsonFromUrl(url);
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        private JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONArray json = new JSONArray(jsonText);
                return json.getJSONObject(0);
            } finally {
                is.close();
            }
        }
    }
}
