package com.f2prateek.couchpotato.ui;

import android.view.ViewGroup;
import com.f2prateek.couchpotato.R;

public class MainActivity extends BaseActivity {

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.discover_gallery, container);
  }
}
