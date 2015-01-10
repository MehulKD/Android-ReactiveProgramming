/**
 * Copyright (C) 2015 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.rx.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.fernandocejas.android10.rx.sample.R;
import com.fernandocejas.android10.rx.sample.data.DataManager;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

public class SampleActivityObservableTransfor extends Activity {

  @InjectView(R.id.btn_flatMap) Button btn_flatMap;
  @InjectView(R.id.btn_concatMap) Button btn_concatMap;

  private DataManager dataManager;

  private Subscription subscription;

  private final Func1<Integer, Observable<Integer>> SQUARE_OF_NUMBER =
      new Func1<Integer, Observable<Integer>>() {
        @Override public Observable<Integer> call(Integer number) {
          return Observable.just(number * number);
        }
      };

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, SampleActivityObservableTransfor.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample_observervable_transform);

    ButterKnife.inject(this);
    initialize();
  }

  @Override
  protected void onDestroy() {
    this.subscription.unsubscribe();
    super.onDestroy();
  }

  private void initialize() {
    this.dataManager = new DataManager();
    this.subscription = Subscriptions.empty();
  }

  @OnClick(R.id.btn_flatMap) void onFlatMapClick() {
    this.buildNumbersObservable().flatMap(SQUARE_OF_NUMBER).subscribe(new Observer<Integer>() {
      @Override public void onNext(Integer number) {
        Log.d("onFlatMapClick()", "Number ---------->>>> " + number);
      }

      @Override public void onCompleted() {
        SampleActivityObservableTransfor.this.showToast("flatMap() Completed!!!");
      }

      @Override public void onError(Throwable e) {
        // handle the exception
      }
    });
  }

  @OnClick(R.id.btn_concatMap) void onConcatMapClick() {
    this.buildNumbersObservable().concatMap(SQUARE_OF_NUMBER).subscribe(new Observer<Integer>() {
      @Override public void onNext(Integer number) {
        Log.d("onConcatMapClick()", "Number ---------->>>> " + number);
      }

      @Override public void onCompleted() {
        SampleActivityObservableTransfor.this.showToast("concatMap() Completed!!!");
      }

      @Override public void onError(Throwable e) {
        // handle the exception
      }
    });
  }

  private Observable<Integer> buildNumbersObservable() {
    return this.dataManager.getNumbers().subscribeOn(AndroidSchedulers.mainThread());
  }

  private void showToast(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
