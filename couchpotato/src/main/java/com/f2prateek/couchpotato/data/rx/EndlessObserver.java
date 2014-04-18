package com.f2prateek.couchpotato.data.rx;

import com.f2prateek.ln.Ln;
import rx.Observer;

public abstract class EndlessObserver<T> implements Observer<T> {
  @Override public void onCompleted() {
  }

  @Override public void onError(Throwable throwable) {
    Ln.e(throwable);
  }
}
