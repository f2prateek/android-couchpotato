package com.f2prateek.couchpotato.ui;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import butterknife.InjectView;
import com.f2prateek.couchpotato.R;

public class MainActivity extends BaseActivity {

  @InjectView(R.id.navigation_drawer) NavigationDrawer navigationDrawer;
  @InjectView(R.id.content) FrameLayout content;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Start in explore
    getLayoutInflater().inflate(R.layout.explore_movies_grid, content);
  }

  @Override protected void inflateLayout(ViewGroup container) {
    getLayoutInflater().inflate(R.layout.activity_main, container);
  }
}
