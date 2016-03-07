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

package pl.acieslinski.tumblr.connection;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import pl.acieslinski.tumblr.models.Post;
import retrofit.http.GET;

/**
 * Defines methods for communication with the server.
 *
 * @author Arkadiusz Cieśliński 07.03.16.
 *         <acieslinski@gmail.com>
 */

public interface Api {
    String FIELD_POSTS = "posts";
    String FIELD_TITLE = "link-text";
    String FIELD_DESCRIPTION = "link-description";
    String FIELD_DATE = "date-gmt";

    String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    String QUERY_GET_MOVIES = "/json?debug=1";

    @GET(QUERY_GET_MOVIES)
    PostsResult getPosts();

    class PostsResult {
        @Nullable
        @SerializedName(value = Api.FIELD_POSTS)
        public Post[] posts;
    }
}
