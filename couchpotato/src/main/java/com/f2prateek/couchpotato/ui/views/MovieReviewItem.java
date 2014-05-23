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

package com.f2prateek.couchpotato.ui.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.data.api.tmdb.model.MovieReview;

public class MovieReviewItem extends LinearLayout {
  @InjectView(R.id.review_content) TextView content;
  @InjectView(R.id.review_author) TextView author;

  private MovieReview review;

  public MovieReviewItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(MovieReview review) {
    this.review = review;
    content.setText(review.getContent());
    author.setText(review.getAuthor());
  }

  @OnClick(R.id.review_author) public void onAuthorClicked() {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(review.getUrl()));
    getContext().startActivity(i);
  }
}
