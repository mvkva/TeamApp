package mickey.ogosense.com.articlesteamapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        TextView articleTextView = (TextView)findViewById(R.id.textView);
        Bundle extras = getIntent().getExtras();
        String titleOfArticle = extras.getString("title");
        articleTextView.setText(titleOfArticle.toString());

    }
}
