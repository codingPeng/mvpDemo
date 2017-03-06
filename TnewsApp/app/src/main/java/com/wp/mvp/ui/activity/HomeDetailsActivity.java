package com.wp.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wp.tnewsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_TITLE = "extra_title";
    private static final String EXTRA_URL = "extra_url";
    private String title, url;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_home_details_img)
    ImageView mDetailsImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_details);
        ButterKnife.bind(this);

        // 接收intent数据
        Intent intent = getIntent();
        if (intent != null){
        title = intent.getStringExtra(EXTRA_TITLE);
        url = intent.getStringExtra(EXTRA_URL);
        }

        // 初始化toolbar
        initToolbar();

        // 初始化imageview
        initImageView();
    }

    private void initToolbar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initImageView() {

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mDetailsImg);

    }

    public static void launch(Activity context, String title, String url) {
        Intent intent = new Intent(context, HomeDetailsActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_URL, url);
            context.startActivity(intent);

    }

}

