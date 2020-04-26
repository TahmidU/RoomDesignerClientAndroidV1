package com.tahmidu.room_designer_client_android.util.network;

import android.content.Context;
import android.util.Log;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.tahmidu.room_designer_client_android.network.NetworkState;
import com.tahmidu.room_designer_client_android.network.NetworkStatus;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class PicassoSetup
{
    private final String TAG = "PICASSO_SETUP";

    private static PicassoSetup instance;
    private static PicassoStatus picassoStatus;
    private static Context applicationContext;

    public static PicassoSetup getInstance(Context applicationContext) {
        if(instance == null)
        {
            picassoStatus = PicassoStatus.NOT_SET;
            PicassoSetup.applicationContext = applicationContext;
            return instance = new PicassoSetup();
        }

        return instance;
    }

    public void configurePicasso()
    {
        if(picassoStatus == PicassoStatus.NOT_SET) {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                //logging
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.level(HttpLoggingInterceptor.Level.BODY);

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.addInterceptor(logging);
                builder.addInterceptor(new BasicAuthInterceptor(applicationContext));
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
                builder.hostnameVerifier((hostname, session) -> true);

                Picasso picasso = new Picasso.Builder(applicationContext)
                        .downloader(new OkHttp3Downloader(builder.build()))
                        .build();

                Picasso.setSingletonInstance(picasso);
                Picasso.get().setLoggingEnabled(true);

                picassoStatus = PicassoStatus.SET;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }

    public static PicassoStatus getPicassoStatus() {
        return picassoStatus;
    }
}
