package com.setclientfactoryissue;

import android.util.Log;
import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.ReactCookieJarContainer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * A static client factory implementation.
 *
 * This is a workaround that reads configuration values directly from build settings.
 */
public class CustomClientFactoryStatic implements OkHttpClientFactory {
  class LoggingInterceptor implements Interceptor {
    @Override public Response intercept(Interceptor.Chain chain) throws IOException {

      return chain.proceed(
        chain.request().newBuilder()
            .header("FromJava", "CustomClientFactoryStatic")
            .build()
      );
    }
  }
 
  @Override
  public OkHttpClient createNewNetworkModuleClient() {
    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

    clientBuilder.addNetworkInterceptor(new LoggingInterceptor());

    clientBuilder.cookieJar(new ReactCookieJarContainer());

    return clientBuilder.build();
  }
}
