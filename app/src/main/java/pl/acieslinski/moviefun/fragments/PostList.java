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

package pl.acieslinski.moviefun.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import pl.acieslinski.moviefun.Application;
import pl.acieslinski.moviefun.R;
import pl.acieslinski.moviefun.connection.ApiAdapter;
import pl.acieslinski.moviefun.models.Post;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Arkadiusz Cieśliński 14.11.15.
 *         <acieslinski@gmail.com>
 */

public class PostList extends Fragment {
    private static final String TAG = PostList.class.getSimpleName();

    @Bind(R.id.rv_searches)
    protected RecyclerView mSearchesRecyclerView;
    protected PostsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog mProgressDialog;

    private ApiAdapter mApiAdapter;
    private boolean isLoadingState;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(getResources().getString(R.string.message_loader));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setIndeterminate(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mProgressDialog.dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        mAdapter = new PostsAdapter();
        mApiAdapter = new ApiAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(view.getContext());
        mSearchesRecyclerView.setLayoutManager(mLayoutManager);
        mSearchesRecyclerView.setAdapter(mAdapter);

        if (isLoadingState) {
            mProgressDialog.show();
        } else if (mAdapter.mPosts.isEmpty()) {
            fetchPosts();
        }
    }

    private void fetchPosts() {
        isLoadingState = true;
        if (null != mProgressDialog) {
            mProgressDialog.show();
        }

        mAdapter.clear();

        mApiAdapter.getPosts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .filter(post -> !post.getTitle().isEmpty())
                .doOnNext(post -> mAdapter.add(post))
                .doOnCompleted(() -> {
                    if (null != mProgressDialog) {
                        mProgressDialog.hide();
                    }
                    isLoadingState = false;
                })
                .subscribe();
    }

    protected class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
        private List<Post> mPosts;

        public PostsAdapter() {
            mPosts = new ArrayList();
        }

        @Override
        public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.fragment_posts_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Post post = mPosts.get(position);
            holder.setPost(post);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        public void add(Post post) {
            mPosts.add(post);

            if (isAdded()) {
                getActivity().runOnUiThread(() -> notifyItemInserted(mPosts.indexOf(post)));
            }
        }

        public void clear() {
            mPosts.clear();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_title)
            protected TextView mTitleTextView;
            @Bind(R.id.tv_details)
            protected TextView mDetailsTextView;
            @Bind(R.id.tv_date)
            protected TextView mDateTextView;

            public ViewHolder(View view) {
                super(view);

                ButterKnife.bind(this, view);
            }

            public void setPost(final Post post) {
                mTitleTextView.setText(post.getTitle());
                mDetailsTextView.setText(Html.fromHtml(post.getDescription()));
                mDateTextView.setText(formatDate(post.getDate()));
            }

            private String formatDate(Date date) {
                String formatted = "";
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    String[] months = getResources().getStringArray(R.array.months);

                    formatted = day + " " + capitalize(months[month]).substring(0, 3);
                } catch (Exception ex) {
                    Log.e(TAG, "can't format date");
                    Log.e(TAG, Log.getStackTraceString(ex));
                }

                return formatted;
            }

            private String capitalize(final String line) {
                return Character.toUpperCase(line.charAt(0)) + line.substring(1);
            }
        }
    }
}
