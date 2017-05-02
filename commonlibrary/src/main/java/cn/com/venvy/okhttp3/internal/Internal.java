/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.venvy.okhttp3.internal;

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocket;

import cn.com.venvy.okhttp3.Address;
import cn.com.venvy.okhttp3.Call;
import cn.com.venvy.okhttp3.Callback;
import cn.com.venvy.okhttp3.ConnectionPool;
import cn.com.venvy.okhttp3.ConnectionSpec;
import cn.com.venvy.okhttp3.Headers;
import cn.com.venvy.okhttp3.HttpUrl;
import cn.com.venvy.okhttp3.OkHttpClient;
import cn.com.venvy.okhttp3.internal.http.StreamAllocation;
import cn.com.venvy.okhttp3.internal.io.RealConnection;

/**
 * Escalate internal APIs in {@code okhttp3} so they can be used from OkHttp's implementation
 * packages. The only implementation of this interface is in {@link OkHttpClient}.
 */
public abstract class Internal {

  public static void initializeInstanceForTests() {
    // Needed in tests to ensure that the instance is actually pointing to something.
    new OkHttpClient();
  }

  public static Internal instance;

  public abstract void addLenient(Headers.Builder builder, String line);

  public abstract void addLenient(Headers.Builder builder, String name, String value);

  public abstract void setCache(OkHttpClient.Builder builder, InternalCache internalCache);

  public abstract InternalCache internalCache(OkHttpClient client);

  public abstract RealConnection get(
      ConnectionPool pool, Address address, StreamAllocation streamAllocation);

  public abstract void put(ConnectionPool pool, RealConnection connection);

  public abstract boolean connectionBecameIdle(ConnectionPool pool, RealConnection connection);

  public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);

  public abstract void apply(ConnectionSpec tlsConfiguration, SSLSocket sslSocket,
      boolean isFallback);

  public abstract HttpUrl getHttpUrlChecked(String url)
      throws MalformedURLException, UnknownHostException;

  // TODO delete the following when web sockets move into the main package.
  public abstract void callEnqueue(Call call, Callback responseCallback, boolean forWebSocket);

  public abstract StreamAllocation callEngineGetStreamAllocation(Call call);
}
