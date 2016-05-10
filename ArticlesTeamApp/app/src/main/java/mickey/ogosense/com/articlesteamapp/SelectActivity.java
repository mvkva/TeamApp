package mickey.ogosense.com.articlesteamapp;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    public ArrayList<String> articleArray;
    public ArrayList<String> articleIds;
    public ArrayList<Integer> features;
    public String articleTitle = "";
    public String articleId = "";
    public int uid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        Bundle extras = getIntent().getExtras();
        uid = extras.getInt("uid");

        new MyTask().execute();
    }

    public class MyTask extends AsyncTask<Void, Void, String> {

        protected void onPostExecute(String result){

            try {

                JSONArray jsonStrings = new JSONArray(result);
                articleArray = new ArrayList<String>();
                articleIds = new ArrayList<String>();
                features = new ArrayList<>();
                for(int i=0;i<jsonStrings.length();i++) {
                    JSONObject json = jsonStrings.getJSONObject(i);
                    String title = json.getString("title");
                    String id = json.getString("id");
                    int feature = json.getInt("featured");
                    articleArray.add(title);
                    articleIds.add(id);
                    features.add(feature);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... g) {

            String responseData = "";
            try {
                JSONObject jsonUid = new JSONObject();
                jsonUid.put("uid", uid);

                URL url = new URL("http://android.ogosense.net/interns/ace/articles.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                dos.writeBytes(jsonUid.toString());
                dos.flush();
                dos.close();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String data = "";
                while((data = reader.readLine()) != null){

                    responseData = responseData + data;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return responseData;
        }
    }


    public void getArticles(View v){
        ListView articlesList = (ListView)findViewById(R.id.listViewArticles);
        String[] articles = new String[articleArray.size()];
        for (int i = 0; i < articleArray.size(); i ++) {
            articles[i] = articleArray.get(i);

        }
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, articles);


        articlesList.setAdapter(adapter);
        articlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                articleTitle = articleArray.get(position);
                articleId = articleIds.get(position);
                Intent intent = new Intent(SelectActivity.this, ArticleActivity.class);
                intent.putExtra("title", articleTitle);
                intent.putExtra("id", articleId);
                startActivity(intent);
            }
        });

    }
}
