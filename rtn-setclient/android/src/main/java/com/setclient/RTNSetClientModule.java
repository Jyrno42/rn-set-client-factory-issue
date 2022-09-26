package com.setclient;

import android.content.Context;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.network.ReactCookieJarContainer;
import com.facebook.react.modules.network.OkHttpClientFactory;
import com.facebook.react.modules.network.OkHttpClientProvider;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class RTNSetClientModule extends NativeSetClientSpec {

    public static String NAME = "RTNSetClient";

    private Context mContext;

    RTNSetClientModule(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


    boolean isApplied = false;
    String headerValue = "CustomClientFactoryRN::default";

    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            return clazz.getField(fieldName).get(null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFactory() {
        return getBuildConfigValue(mContext, "STATIC_FACTORY") == "1" ? "static" : "dynamic";
    }

    @Override
    public void setValue(String value, Promise promise) {
        if (isApplied) {
            promise.reject("already applied");
            return;
        }

        headerValue = value;

        promise.resolve(true);
    }

    @Override
    public void applyConfig(Promise promise) {
        if (isApplied) {
            promise.reject("already applied");
            return;
        }

        try {
            // Attempt to create the client immediately to detect any errors
            CustomClientFactory dummyFactory = new CustomClientFactory();
            dummyFactory.createNewNetworkModuleClient();

            // Apply the factory to OkHttp
            OkHttpClientProvider.setOkHttpClientFactory(new CustomClientFactory());

            promise.resolve(true);
        } catch (Exception e) {
            promise.reject("Failed to apply: " + e.getMessage());
        }
    }

    class LoggingInterceptor implements Interceptor {
        String myValue = "default";

        LoggingInterceptor(String value) {
            myValue = value;
        }

        @Override public Response intercept(Interceptor.Chain chain) throws IOException {
            return chain.proceed(
                chain.request().newBuilder()
                    .header("FromJava", myValue)
                    .build()
            );
        }
    }

    public class CustomClientFactory implements OkHttpClientFactory {
        @Override
        public OkHttpClient createNewNetworkModuleClient() {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            clientBuilder.addNetworkInterceptor(new LoggingInterceptor(headerValue));

            clientBuilder.cookieJar(new ReactCookieJarContainer());

            return clientBuilder.build();
        }
    }
}
