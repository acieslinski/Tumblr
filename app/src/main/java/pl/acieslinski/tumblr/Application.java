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

package pl.acieslinski.tumblr;

import android.util.Log;


/**
 * @author Arkadiusz Cieśliński 07.03.16.
 *         <acieslinski@gmail.com>
 */
public class Application extends android.app.Application {
    private static final String TAG = Application.class.getSimpleName();

    protected static Application sInstance;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate()");
        super.onCreate();

        sInstance = this;
    }

    public static Application getInstance() {
        return sInstance;
    }
}
