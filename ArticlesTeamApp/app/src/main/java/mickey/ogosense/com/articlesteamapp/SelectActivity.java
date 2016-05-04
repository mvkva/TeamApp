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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    public ArrayList<String> articleArray;
    public String articleTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        new MyTask().execute();





    }

    public class MyTask extends AsyncTask<Void, Void, String> {

        protected void onPostExecute(String result){

            try {

                JSONArray jsonStrings = new JSONArray(result);
                articleArray = new ArrayList<String>();
                for(int i=0;i<jsonStrings.length();i++) {
                    JSONObject json = jsonStrings.getJSONObject(i);
                    String title = json.getString("title");

                    articleArray.add(title);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected String doInBackground(Void... g) {


            String responseData = "";
            try {
                URL url = new URL("http://android.ogosense.net/interns/ace/articles.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

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

    public void selectArticle(View v){


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
                Intent intent = new Intent(SelectActivity.this, ArticleActivity.class);
                intent.putExtra("title", articleTitle);
                startActivity(intent);
            }
        });

    }
}
