/*
 * Copyright 2014 Prateek Srivastava (@f2prateek)
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

package com.f2prateek.couchpotato.ui;

import com.squareup.otto.Bus;
import java.util.HashSet;
import java.util.Set;

/**
 * Scoped event bus which automatically registers and unregisters with the lifecycle.
 */
public class ScopedBus {
  private final Bus bus;
  private final Set<Object> objects = new HashSet<>();
  private boolean active;

  public ScopedBus(Bus bus) {
    this.bus = bus;
  }

  public void register(Object obj) {
    objects.add(obj);
    if (active) {
      bus.register(obj);
    }
  }

  public void unregister(Object obj) {
    objects.remove(obj);
    if (active) {
      bus.unregister(obj);
    }
  }

  public void post(Object event) {
    bus.post(event);
  }

  public void paused() {
    active = false;
    for (Object obj : objects) {
      bus.unregister(obj);
    }
  }

  public void resumed() {
    active = true;
    for (Object obj : objects) {
      bus.register(obj);
    }
  }
}
