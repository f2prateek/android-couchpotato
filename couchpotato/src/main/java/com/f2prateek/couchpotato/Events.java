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

package com.f2prateek.couchpotato;

import android.view.View;
import com.f2prateek.couchpotato.data.api.Movie;

public class Events {
  public static class OnMovieClickedEvent {
    // View Attributes for Animation
    public final int height;
    public final int width;
    public final int left;
    public final int top;

    public final Movie movie;

    public OnMovieClickedEvent(Movie movie, int height, int width, int left, int top) {
      this.movie = movie;
      this.height = height;
      this.width = width;
      this.left = left;
      this.top = top;
    }

    /** Factory method for figuring out required dimensions from clicked view. g*/
    public static OnMovieClickedEvent fromSource(Movie movie, View source) {
      int[] screenLocation = new int[2];
      source.getLocationOnScreen(screenLocation);

      int width = source.getWidth();
      int height = source.getHeight();
      return new Events.OnMovieClickedEvent(movie, height, width, screenLocation[0],
          screenLocation[1]);
    }
  }
}