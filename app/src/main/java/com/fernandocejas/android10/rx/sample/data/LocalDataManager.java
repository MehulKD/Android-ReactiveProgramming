/**
 * Copyright (C) 2015 android10.org Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.rx.sample.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LocalDataManager {

  private final RandomStringGenerator randomStringGenerator;
  private final NumberGenerator numberGenerator;
  private final Executor jobExecutor;

  private final List<Integer> numbers;
  private final List<String> elements;

  private static final Func1<String, String> TO_RANDOM_ITEM = new Func1<String, String>() {
    @Override public String call(String string) {
      return "RandomItem" + string;
    }
  };

  public LocalDataManager(RandomStringGenerator randomStringGenerator,
      NumberGenerator numberGenerator,
      Executor jobExecutor) {
    this.numbers = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10));
    this.elements = new ArrayList<>();
    this.randomStringGenerator = randomStringGenerator;
    this.numberGenerator = numberGenerator;
    this.jobExecutor = jobExecutor;
    populateElementsList();
  }

  public Observable<Integer> numbers() {
    return Observable.from(numbers);
  }

  public Observable<Integer> squareOfAsync(int number) {
    return Observable.just(number * number).subscribeOn(Schedulers.from(this.jobExecutor));
  }

  public Observable<String> elements() {
    return Observable.from(elements);
  }

  public Observable<String> newElement() {
    return Observable.just(randomStringGenerator.nextString()).map(TO_RANDOM_ITEM);
  }

  public List<Integer> getNumbersSynchronously() {
    return this.numbers;
  }

  private void populateElementsList() {
    for (int i = 0; i < 10; i++) {
      this.elements.add(randomStringGenerator.nextString());
    }
  }
}
