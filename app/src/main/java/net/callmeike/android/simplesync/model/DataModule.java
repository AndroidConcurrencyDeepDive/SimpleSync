/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.callmeike.android.simplesync.model;

import android.content.ContentValues;

import javax.inject.Singleton;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.callmeike.android.simplesync.data.SyncContract;

import dagger.Module;
import dagger.Provides;


/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
@Module
public class DataModule {
    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
            .registerTypeAdapter(
                ContentValues.class,
                new ContentValuesConverter(SyncContract.Columns.COL1, SyncContract.Columns.COL2))
            .create();
    }
}
