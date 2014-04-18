package com.f2prateek.couchpotato.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.f2prateek.couchpotato.CouchPotatoApplication;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.TMDbDatabase;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbMovieMinified;
import com.f2prateek.couchpotato.data.rx.EndlessObserver;
import com.f2prateek.couchpotato.ui.misc.BindableAdapter;
import com.f2prateek.couchpotato.ui.widget.BetterViewAnimator;
import com.squareup.picasso.Picasso;
import hugo.weaving.DebugLog;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import rx.Subscription;

public class MoviesGrid extends BetterViewAnimator {

  private static final int LOAD_THRESHOLD = 4;
  private final AtomicInteger page = new AtomicInteger(1);
  private final AtomicBoolean loading = new AtomicBoolean(false);

  @InjectView(R.id.gallery_grid) AbsListView grid;

  @Inject Picasso picasso;
  @Inject TMDbDatabase database;

  private final GalleryAdapter adapter;
  private Subscription request;

  public MoviesGrid(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);

    adapter = new GalleryAdapter(context, picasso);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
    grid.setAdapter(adapter);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    fetch();

    grid.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {
        // ignore
      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        if (adapter.getCount() == 0) return; // No items

        int lastVisibleItem = visibleItemCount + firstVisibleItem;
        if (lastVisibleItem >= totalItemCount - LOAD_THRESHOLD) {
          fetch();
        }
      }
    });
  }

  private void fetch() {
    if (loading.get()) return;
    loading.set(true);
    request = database.getPopularMovies(page.getAndIncrement(),
        new EndlessObserver<List<TMDbMovieMinified>>() {
          @Override public void onNext(List<TMDbMovieMinified> movies) {
            adapter.add(movies);
            setDisplayedChild(grid);
            loading.set(false);
          }
        }
    );
  }

  @Override protected void onDetachedFromWindow() {
    request.unsubscribe();
    super.onDetachedFromWindow();
  }

  static class GalleryAdapter extends BindableAdapter<TMDbMovieMinified> {
    private List<TMDbMovieMinified> movies = new ArrayList<>();

    private final Picasso picasso;

    public GalleryAdapter(Context context, Picasso picasso) {
      super(context);
      this.picasso = picasso;
    }

    @DebugLog
    public void add(List<TMDbMovieMinified> movies) {
      this.movies.addAll(movies);
      notifyDataSetChanged();
    }

    @Override public int getCount() {
      return movies.size();
    }

    @Override public TMDbMovieMinified getItem(int position) {
      return movies.get(position);
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
      return inflater.inflate(R.layout.movies_grid_item, container, false);
    }

    @Override public void bindView(TMDbMovieMinified item, int position, View view) {
      ((MoviesGridItemView) view).bindTo(item, picasso);
    }
  }
}
