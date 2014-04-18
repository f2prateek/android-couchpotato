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
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

public class ExploreMoviesView extends BetterViewAnimator {

  @InjectView(R.id.gallery_grid) AbsListView galleryView;

  @Inject Picasso picasso;
  @Inject TMDbDatabase database;

  private final GalleryAdapter adapter;
  private Subscription request;

  public ExploreMoviesView(Context context, AttributeSet attrs) {
    super(context, attrs);
    CouchPotatoApplication.get(context).inject(this);

    adapter = new GalleryAdapter(context, picasso);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
    galleryView.setAdapter(adapter);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    request = database.getPopularMovies(new EndlessObserver<List<TMDbMovieMinified>>() {
      @Override public void onNext(List<TMDbMovieMinified> movies) {
        adapter.replaceWith(movies);
        setDisplayedChild(galleryView);
      }
    });
  }

  @Override protected void onDetachedFromWindow() {
    request.unsubscribe();
    super.onDetachedFromWindow();
  }

  static class GalleryAdapter extends BindableAdapter<TMDbMovieMinified> {
    private List<TMDbMovieMinified> movies = Collections.emptyList();

    private final Picasso picasso;

    public GalleryAdapter(Context context, Picasso picasso) {
      super(context);
      this.picasso = picasso;
    }

    public void replaceWith(List<TMDbMovieMinified> movies) {
      this.movies = movies;
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
      return inflater.inflate(R.layout.explore_movies_grid_item, container, false);
    }

    @Override public void bindView(TMDbMovieMinified item, int position, View view) {
      ((ExploreMoviesItemView) view).bindTo(item, picasso);
    }
  }
}
