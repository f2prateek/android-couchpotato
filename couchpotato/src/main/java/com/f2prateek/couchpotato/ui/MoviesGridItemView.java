package com.f2prateek.couchpotato.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.moviedb.model.TMDbMovieMinified;
import com.squareup.picasso.Picasso;

public class MoviesGridItemView extends FrameLayout {
  @InjectView(R.id.gallery_item_image) ImageView image;
  @InjectView(R.id.gallery_item_title) TextView title;

  TMDbMovieMinified movie;

  public MoviesGridItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(TMDbMovieMinified movie, Picasso picasso) {
    this.movie = movie;
    picasso.load(movie.poster).fit().centerCrop().into(image);
    title.setText(movie.title);
  }

  @OnClick(R.id.gallery_item_image) public void onImageClicked() {
    getContext().startActivity(MovieActivity.createIntent(getContext(), movie));
  }
}
