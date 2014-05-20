/*
 * Copyright 2014 Prateek Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.data.rx;

import com.f2prateek.couchpotato.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Collection;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.observables.Assertions;
import rx.functions.Func1;
import rx.operators.OperatorConditionalBinding;

import static rx.android.schedulers.AndroidSchedulers.mainThread;

/**
 * A class to help manage subscriptions. It will automatically unsubscribe any subscription if the
 * predicate does not validate.
 */
public abstract class SubscriptionManager<T> {
  private final T instance;
  private final Collection<Subscription> subscriptions = new ArrayList<>();
  private final Func1<T, Boolean> predicate = new Func1<T, Boolean>() {
    @Override
    public Boolean call(T object) {
      return validate(object);
    }
  };

  public SubscriptionManager(T instance) {
    this.instance = instance;
  }

  public <O> void subscribe(final Observable<O> source, final Observer<O> observer) {
    Assertions.assertUiThread();
    Subscription subscription = source.observeOn(mainThread())
        .lift(new OperatorConditionalBinding<O, T>(instance, predicate))
        .subscribe(observer);
    subscriptions.add(subscription);
  }

  public void unsubscribeAll() {
    if (!CollectionUtils.isNullOrEmpty(subscriptions)) {
      for (Subscription subscription : subscriptions) {
        if (!subscription.isUnsubscribed()) {
          subscription.unsubscribe();
        }
      }
      subscriptions.clear();
    }
  }

  /**
   * Return a {@link rx.functions.Func1} implementation that will be a predicate for {@link
   * OperatorConditionalBinding}. If the predicate fails to validate, the sequence unsubscribes
   * itself and releases the bound reference.
   */
  protected abstract boolean validate(final T object);
}
