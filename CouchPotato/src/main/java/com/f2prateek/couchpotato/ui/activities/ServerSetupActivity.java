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

package com.f2prateek.couchpotato.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.f2prateek.couchpotato.R;
import com.f2prateek.couchpotato.UserConfig;
import com.f2prateek.couchpotato.model.couchpotato.GetKeyResponse;
import com.f2prateek.couchpotato.services.CouchPotatoLoginApi;
import com.f2prateek.couchpotato.util.Ln;
import com.f2prateek.couchpotato.util.RetrofitErrorHandler;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Inject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity to request user credentials, and trade it for an API key.
 */
public class ServerSetupActivity extends BaseActivity implements Callback<GetKeyResponse> {

  @InjectView(R.id.scheme) EditText scheme;
  @InjectView(R.id.host) EditText host;
  @InjectView(R.id.port) EditText port;
  @InjectView(R.id.username) EditText username;
  @InjectView(R.id.password) EditText password;

  @Inject SharedPreferences sharedPreferences;
  @Inject UserConfig userConfig;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server_setup);
    fillViewWithDefaults();
  }

  private void fillViewWithDefaults() {
    scheme.setText(userConfig.getHostScheme());
    host.setText(userConfig.getHostUrl());
    port.setText(String.valueOf(userConfig.getPort()));
  }

  @OnClick(R.id.login) public void validateEntry() {
    boolean hasError = false;
    if (isTextViewEmpty(host)) {
      hasError = true;
    }
    if (isTextViewEmpty(port)) {
      hasError = true;
    }
    if (isTextViewEmpty(scheme)) {
      hasError = true;
    }

    // Username is not required (server could be unprotected)
    boolean hasUsername = true;
    if (TextUtils.isEmpty(username.getText())) {
      hasUsername = false;
    }
    // However if username is provided, password must be provided as well.
    if (hasUsername) {
      if (isTextViewEmpty(password)) {
        hasError = true;
      }
    }

    if (!hasError) {
      userConfig.setHostScheme(getText(scheme));
      userConfig.setHostUrl(getText(host));
      userConfig.setPort(Integer.parseInt(getText(port)));
      String server_url = userConfig.getUnauthenticatedServerUrl();
      getApiKey(server_url, getText(username), getText(password));
    } else {
      // Show a message to the user, the fields themselves
      Crouton.makeText(this, R.string.invalid_entries, Style.ALERT).show();
    }
  }

  /**
   * Check if the given textview is empty. If it is, set an error and log it, if not, clear the
   * error.
   *
   * @return true if TextView is Empty
   */
  private boolean isTextViewEmpty(TextView textView) {
    boolean hasError = false;
    if (TextUtils.isEmpty(textView.getText())) {
      textView.setError(getString(R.string.required));
      hasError = true;
    } else {
      textView.setError(null);
    }
    return hasError;
  }

  // Get the API key from the server
  private void getApiKey(String server_url, String username, String password) {
    showIndeterminateBar(true);
    CouchPotatoLoginApi api =
        new RestAdapter.Builder().setServer(server_url).build().create(CouchPotatoLoginApi.class);
    api.get_key(md5(password), md5(username), this);
  }

  private static String getText(TextView textView) {
    return textView.getText().toString();
  }

  public static String md5(String input) {
    String md5 = null;
    if (null == input) return null;
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      digest.update(input.getBytes(), 0, input.length());
      //Converts message digest value in base 16 (hex)
      md5 = new BigInteger(1, digest.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return md5;
  }

  @Override public void success(GetKeyResponse getKeyResponse, Response response) {
    if (getKeyResponse.success) {
      userConfig.setApiKey(getKeyResponse.api_key);
      userConfig.save(sharedPreferences);
      Intent homeIntent = new Intent(this, MainActivity.class);
      homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(homeIntent);
      finish();
    } else {
      // This means server was available, so likely due to invalid credentials
      Ln.e("Invalid Credentials!");
      Crouton.makeText(this, R.string.invalid_credentials, Style.ALERT).show();
      username.setError(getString(R.string.may_be_incorrect));
      password.setError(getString(R.string.may_be_incorrect));
    }
    showIndeterminateBar(false);
  }

  @Override public void failure(RetrofitError error) {
    Ln.e("Could not get apiKey for user.");
    RetrofitErrorHandler.showError(this, error);
    showIndeterminateBar(false);
  }
}