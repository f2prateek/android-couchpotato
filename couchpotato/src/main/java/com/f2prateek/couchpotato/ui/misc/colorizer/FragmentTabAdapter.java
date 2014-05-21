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

package com.f2prateek.couchpotato.ui.misc.colorizer;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;

public class FragmentTabAdapter extends FragmentPagerAdapter {
  static final class TabInfo {
    private final Class<?> clazz;
    private final Bundle args;
    private final String title;

    TabInfo(Class<?> clazz, Bundle args, String title) {
      this.clazz = clazz;
      this.args = args;
      this.title = title;
    }
  }

  private final ViewPager pager;
  private final ArrayList<TabInfo> tabs;
  private final Context context;

  public FragmentTabAdapter(Activity activity, ViewPager pager) {
    super(activity.getFragmentManager());
    this.pager = pager;
    this.context = activity;
    tabs = new ArrayList<>();
    init();
  }

  private void init() {
    pager.setAdapter(this);
  }

  @Override public Fragment getItem(int position) {
    TabInfo info = tabs.get(position);
    return Fragment.instantiate(context, info.clazz.getName(), info.args);
  }

  @Override public int getCount() {
    return tabs.size();
  }

  @Override public CharSequence getPageTitle(int position) {
    return tabs.get(position).title;
  }

  public void addTab(Class<?> clazz, Bundle args, int titleResourceId) {
    addTab(clazz, args, context.getString(titleResourceId));
  }

  public void addTab(Class<?> clazz, Bundle args, String title) {
    TabInfo info = new TabInfo(clazz, args, title);
    tabs.add(info);
    notifyDataSetChanged();
  }
}