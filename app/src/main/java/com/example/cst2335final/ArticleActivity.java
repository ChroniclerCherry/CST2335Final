package com.example.cst2335final;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ArticleActivity extends Object {

    private  String article;

        public ArticleActivity (String article) {
            this.article = article;
        }

        public String getArticle(){
            return article;
        }
    }
