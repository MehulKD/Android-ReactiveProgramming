package com.fernandocejas.android10.rx.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;

public class MainActivity extends Activity {

  @InjectView(android.R.id.list) RecyclerView rv_elements;
  @InjectView(android.R.id.button1) Button btn_AddElement;

  private DataManager dataManager;
  private ElementsAdapter adapter;
  private ElementsLayoutManager layoutManager;

  private Subscription subscription;
  private Observable<String> observable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ButterKnife.inject(this);
    initialize();
    fillData();
  }

  @Override
  protected void onDestroy() {
    if (this.subscription != null) { this.subscription.unsubscribe(); }
    super.onDestroy();
  }

  private void initialize() {
    this.dataManager = new DataManager();
    this.layoutManager = new ElementsLayoutManager(this);
    this.adapter = new ElementsAdapter(this);

    this.rv_elements.setLayoutManager(this.layoutManager);
    this.rv_elements.setAdapter(this.adapter);
  }

  private void fillData() {
    this.observable = this.dataManager.getElements();
    this.subscription = this.observable.subscribe(this.adapter);
  }

  @OnClick(android.R.id.button1) void onAddElementClick() {
    dataManager.addRandomString();
  }
}
