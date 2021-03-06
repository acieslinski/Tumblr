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

package pl.acieslinski.tumblr.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import pl.acieslinski.tumblr.connection.Api;

/**
 * @author Arkadiusz Cieśliński 07.03.16.
 *         <acieslinski@gmail.com>
 */
public class Post {
    @Nullable
    @SerializedName(value = Api.FIELD_TITLE)
    private String mTitle;
    @Nullable
    @SerializedName(value = Api.FIELD_DESCRIPTION)
    private String mDescription;
    @Nullable
    @SerializedName(value = Api.FIELD_DATE)
    private Date mDate;

    public Post() {
        mTitle = "";
        mDescription = "";
        mDate = new Date();
    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription == null ? "" : mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate == null ? new Date() : mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
