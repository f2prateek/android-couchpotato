/*
 * Copyright 2013 Prateek Srivastava (@f2prateek)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.f2prateek.couchpotato.ui.base;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.f2prateek.couchpotato.R;

/** https://github.com/johnkil/Android-BaseProgressFragment/blob/master/progressfragment/src/com/devspark/progressfragment/GridFragment.java */
public abstract class BaseProgressGridFragment extends BaseFragment {

  final private Handler handler = new Handler();
  final private Runnable requestFocus = new Runnable() {
    public void run() {
      gridView.focusableViewAvailable(gridView);
    }
  };
  final private AdapterView.OnItemClickListener onItemClickListener =
      new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
          onGridItemClick((GridView) parent, v, position, id);
        }
      };
  private ListAdapter gridAdapter;
  private GridView gridView;
  private View emptyView;
  private TextView standardEmptyView;
  private View progressContainer;
  private View gridContainer;
  private CharSequence emptyText;
  private boolean isGridShown;

  /** Check if the given view is visible. */
  private static boolean isViewVisible(View view) {
    return view.getVisibility() == View.VISIBLE;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.base_progress_grid_fragment, container, false);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    // Skip this call since we don't want view injection
    // super.onViewCreated(view, savedInstanceState);
    ensureList();
  }

  @Override
  public void onDestroyView() {
    handler.removeCallbacks(requestFocus);
    gridView = null;
    isGridShown = false;
    emptyView = progressContainer = gridContainer = null;
    standardEmptyView = null;
    super.onDestroyView();
  }

  public abstract void onGridItemClick(GridView gridView, View v, int position, long id);

  public void setSelection(int position) {
    ensureList();
    gridView.setSelection(position);
  }

  public int getSelectedItemPosition() {
    ensureList();
    return gridView.getSelectedItemPosition();
  }

  public long getSelectedItemId() {
    ensureList();
    return gridView.getSelectedItemId();
  }

  public GridView getGridView() {
    ensureList();
    return gridView;
  }

  public void setEmptyText(CharSequence text) {
    ensureList();
    if (standardEmptyView == null) {
      throw new IllegalStateException("Can't be used with a custom content view");
    }
    standardEmptyView.setText(text);
    if (emptyText == null) {
      gridView.setEmptyView(standardEmptyView);
    }
    emptyText = text;
  }

  public void setGridShown(boolean shown) {
    setGridShown(shown, true);
  }

  public void setGridShownNoAnimation(boolean shown) {
    setGridShown(shown, false);
  }

  private void setGridShown(boolean shown, boolean animate) {
    ensureList();
    if (progressContainer == null) {
      throw new IllegalStateException("Can't be used with a custom content view");
    }
    if (isGridShown == shown) {
      return;
    }
    isGridShown = shown;
    if (shown) {
      if (animate) {
        progressContainer.startAnimation(
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
        gridContainer.startAnimation(
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
      } else {
        progressContainer.clearAnimation();
        gridContainer.clearAnimation();
      }
      progressContainer.setVisibility(View.GONE);
      gridContainer.setVisibility(View.VISIBLE);
    } else {
      if (animate) {
        progressContainer.startAnimation(
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
        gridContainer.startAnimation(
            AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
      } else {
        progressContainer.clearAnimation();
        gridContainer.clearAnimation();
      }
      progressContainer.setVisibility(View.VISIBLE);
      gridContainer.setVisibility(View.GONE);
    }
  }

  public ListAdapter getGridAdapter() {
    return gridAdapter;
  }

  public void setGridAdapter(ListAdapter adapter) {
    boolean hadAdapter = gridAdapter != null;
    gridAdapter = adapter;
    if (gridView != null) {
      gridView.setAdapter(adapter);
      if (!isGridShown && !hadAdapter) {
        // The grid was hidden, and previously didn't have an
        // adapter.  It is now time to show it.
        setGridShown(true, getView().getWindowToken() != null);
      }
    }
  }

  private void ensureList() {
    if (gridView != null) {
      return;
    }
    View root = getView();
    if (root == null) {
      throw new IllegalStateException("Content view not yet created");
    }
    if (root instanceof GridView) {
      gridView = (GridView) root;
    } else {
      View emptyViewFromResource = root.findViewById(android.R.id.empty);
      if (emptyViewFromResource != null) {
        if (emptyViewFromResource instanceof TextView) {
          standardEmptyView = (TextView) emptyViewFromResource;
        } else {
          emptyView = emptyViewFromResource;
        }
      } else {
        standardEmptyView.setVisibility(View.GONE);
      }
      progressContainer = root.findViewById(R.id.progress_container);
      gridContainer = root.findViewById(R.id.grid_container);
      View rawGridView = root.findViewById(R.id.grid);
      if (!(rawGridView instanceof GridView)) {
        throw new RuntimeException(
            "Content has view with id attribute 'R.id.grid' " + "that is not a GridView class");
      }
      gridView = (GridView) rawGridView;
      if (gridView == null) {
        throw new RuntimeException(
            "Your content must have a GridView whose id attribute is " + "'R.id.grid'");
      }
      if (emptyView != null) {
        gridView.setEmptyView(emptyView);
      } else if (emptyText != null) {
        standardEmptyView.setText(emptyText);
        gridView.setEmptyView(standardEmptyView);
      }
    }
    isGridShown = true;
    gridView.setOnItemClickListener(onItemClickListener);
    if (gridAdapter != null) {
      ListAdapter adapter = gridAdapter;
      gridAdapter = null;
      setGridAdapter(adapter);
    } else {
      // We are starting without an adapter, so assume we won't
      // have our data right away and start with the progress indicator.
      if (progressContainer != null) {
        setGridShown(false, false);
      }
    }
    handler.post(requestFocus);
  }
}