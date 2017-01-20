package com.example.kazu.myapplication;

import android.os.Build;

import com.example.kazu.myapplication.api.ApiService;
import com.example.kazu.myapplication.api.RestClient;
import com.example.kazu.myapplication.model.CreatedItem;
import com.example.kazu.myapplication.model.Item;
import com.example.kazu.myapplication.model.Judgement;
import com.example.kazu.myapplication.model.UpdatedItem;
import com.example.kazu.myapplication.test.Fixture;
import com.example.kazu.myapplication.validation.Common;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import java.io.IOException;
import java.util.List;

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
    private MockResponse mMockResponse = new MockResponse();

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
    public void パスワード認証の結果を返す() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("auth/valid_post_auth.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        Judgement auth = service.apiAuth("1234").execute().body();
        assertThat(auth.getResult()).isEqualTo(true);

        System.out.println("TEST OK");
    }


    @Test(timeout = 500)
    public void アイテム一覧を取得する() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("items/valid_get_items.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        List<Item> itemList = service.apiGetItems().execute().body();
        assertThat(itemList.get(1).getBody()).isEqualTo("test_ok@gmail.com");

        System.out.println("TEST OK");
    }


    @Test(timeout = 500)
    public void アイテムを投稿する() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("item/valid_post_item.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        CreatedItem item = service.apiPostItem("kubox@328w.co.jp").execute().body();
        assertThat(item.getBody()).isEqualTo("kubox@328w.co.jp");

        System.out.println("TEST OK");
    }


    @Test(timeout = 500)
    public void すべてのアイテムを削除する() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("items/valid_delete_items.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        Judgement judge = service.apiDeleteItems().execute().body();
        assertThat(judge.getResult()).isEqualTo(true);

        System.out.println("TEST OK");
    }

    @Test(timeout = 500)
    public void 指定したアイテムを削除する() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("item/valid_delete_item.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        Judgement judge = service.apiDeleteItem(1).execute().body();
        assertThat(judge.getResult()).isTrue();

        System.out.println("TEST OK");
    }

    @Test(timeout = 500)
    public void 指定したアイテムを更新する() throws Exception {

        mBuilder.client(mClient.getHttpClientBuilder().addInterceptor(getStubClient("item/valid_put_item.json")).build());
        mMockServer.enqueue(mMockResponse);

        Retrofit retrofit = mBuilder.build();
        final ApiService service = retrofit.create(ApiService.class);

        UpdatedItem item = service.apiPutItem(1, "put_item@gmail.com").execute().body();
        assertThat(item.getBody()).isEqualTo("put_item@gmail.com");

        System.out.println("TEST OK");
    }


    // ToDo: 別のクラスに持ってこーぜ
    @Test(timeout = 500)
    public void メールアドレス形式でないのでFalseが返る() throws Exception {
        boolean isMailAddress = Common.isMailAddress("test");
        assertThat(isMailAddress).isFalse();
    }

    @Test(timeout = 500)
    public void パスワードの文字長が許容範囲外のためFalseが返る() throws Exception {
        boolean isRequiredLength = Common.isRequiredLength("123");
        assertThat(isRequiredLength).isFalse();
    }

    @Test(timeout = 500)
    public void パスワードの文字長が許容範囲内のためTrueが返る() throws Exception {
        boolean isRequiredLength = Common.isRequiredLength("12345");
        assertThat(isRequiredLength).isTrue();
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
