/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.f2prateek.couchpotato.util;

import android.app.Activity;
import com.f2prateek.couchpotato.R;
import com.f2prateek.ln.Ln;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit.RetrofitError;

/**
 * A utility class to handle and log different types of errors produced by Retrofit.
 */
public class RetrofitErrorHandler {

  /**
   * Checks the error we have, and shows a message to the user.
   * Types :
   * 1. Could not contact server - show user the ip we're trying to connect to
   * 2. Invalid credentials - ask user to update these
   * 3. Other error - OOPS!
   *
   * // TODO : return an error code that clients calling this can use to provide better messages.
   */
  public static void showError(Activity activity, RetrofitError retrofitError) {
    if (retrofitError.getCause() instanceof SocketTimeoutException) {
      Crouton.makeText(activity, activity.getString(R.string.network_timeout_error,
          parseAddressFromUrl(retrofitError.getUrl())), Style.ALERT).show();
    } else if (retrofitError.getCause() instanceof UnknownHostException) {
      // This should never happen! Host should be verified during the setup screen.
      Crouton.makeText(activity,
          activity.getString(R.string.invalid_host, parseAddressFromUrl(retrofitError.getUrl())),
          Style.ALERT).show();
    }

    Ln.e("Error Url " + retrofitError.getUrl());
    Ln.e(retrofitError.getCause().toString());
    if (retrofitError.getResponse() != null) {
      Ln.e("Status %d", retrofitError.getResponse().getStatus());
      Ln.e("Reason %s", retrofitError.getResponse().getReason());
    }
  }

  /**
   * Filter only the address from the given url
   */
  private static String parseAddressFromUrl(String url) {
    return url;
  }
}
