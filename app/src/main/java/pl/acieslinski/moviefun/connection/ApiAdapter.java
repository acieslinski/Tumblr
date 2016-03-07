/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.acieslinski.moviefun.connection;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.acieslinski.moviefun.Application;
import pl.acieslinski.moviefun.BuildConfig;
import pl.acieslinski.moviefun.R;
import pl.acieslinski.moviefun.models.Post;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Subscriber;

/**
 * Provides methods for pull / push data from / to the server.
 *
 * @author Arkadiusz Cieśliński
 *         <acieslinski@gmail.com>
 */

public class ApiAdapter {
    private static final String TAG = ApiAdapter.class.getSimpleName();
    private static final ExecutorService sExecutor = Executors.newFixedThreadPool(5);

    protected Api mApiAdapter;

    public ApiAdapter() {
        String serviceUrl = Application.getInstance().getString(R.string.rest_url);

        Gson gson = new GsonBuilder()
                .setDateFormat(Api.DATE_FORMAT)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(serviceUrl)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL :
                        RestAdapter.LogLevel.NONE)
                .setConverter(new GsonConverter(gson))
                .build();

        mApiAdapter = restAdapter.create(Api.class);
    }

    /**
     * Sends query for provided {@link Post} and returns an observer for the results (posts). It
     * also warms up the cache with poster's images. It calls {Subscriber#onNext} when video's
     * data is fetched with the poster image.
     */
    @WorkerThread
    public Observable<Post> getPosts() {
        return Observable
                .create(new Observable.OnSubscribe<Post>() {
                    @Override
                    public void call(Subscriber<? super Post> subscriber) {
                        try {
                            getPosts(subscriber);
                        } catch (Exception ex) {
                            subscriber.onError(ex);
                        }
                    }
                })
                // items are emitted faster than they can be consumed by the observer
                .onBackpressureBuffer();
    }

    /**
     * Sends query for provided {@link Post}. It also warms up the cache with poster's images.
     * It calls {Subscriber#onNext} when video's data is fetched with the poster image.
     */
    @WorkerThread
    private void getPosts(final Subscriber<? super Post> subscriber) {
        Api.PostsResult postsResult = mApiAdapter.getPosts();

        if (postsResult != null && postsResult.posts != null) {
            int videosCount = postsResult.posts.length;

            for (int i = 0; i < videosCount; i++) {
                final Post post = postsResult.posts[i];
                subscriber.onNext(post);
            }
        }

        subscriber.onCompleted();
    }
}
