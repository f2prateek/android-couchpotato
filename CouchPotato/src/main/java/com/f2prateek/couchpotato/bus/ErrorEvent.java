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

package com.f2prateek.couchpotato.bus;

/**
 * Similar to {@link com.f2prateek.couchpotato.bus.DataEvent}, but wraps an error instead.
 * Only activities, should be listening for this, to display an error message to the user.
 */
public class ErrorEvent {
  public final Exception exception;

  public ErrorEvent(Exception exception) {
    this.exception = exception;
  }
}