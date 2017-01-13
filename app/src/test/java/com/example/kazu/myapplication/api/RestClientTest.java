package com.example.kazu.myapplication.api;

import android.os.Build;
import android.support.compat.BuildConfig;

import com.example.kazu.myapplication.api.test.Fixture;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.setting.Common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by kbx on 2017/01/13.
 */

@Config(sdk = Build.VERSION_CODES.LOLLIPOP, constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class RestClientTest {

    private MockWebServer mMockServer;
    private MockResponse mMockResponse;

    Retrofit.Builder mBuilder;
    RestClient mClient;

    @Before
    public void Setup() throws Exception{

        mMockServer = new MockWebServer();
        mMockServer.start();
        mClient = new RestClient();
        mBuilder = mClient.getServiceBuilder();

        System.out.println("SETUP OK");

    }

    @Test(timeout = 500)
    public void ユーザ情報とペア情報が返る() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("auth/valid_post_auth.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);
        Judgement auth = service.apiAuth("1234").execute().body();

        System.out.println("opdksgdz");

        assertThat(auth.getResult()).isEqualTo(true);
    }

    private Interceptor getStubClient(final String fixture) {
        return new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response response = null;
                response = new Response.Builder()
                        .code(200)
                        .message("")
                        .request(chain.request())
                        .protocol(Protocol.HTTP_1_0)
                        .body(ResponseBody.create(MediaType.parse("application/json"), Fixture.load(fixture).toString()))
                        .addHeader("content-type", "application/json")
                        .build();
                return response;
            }
        };
    }

}
