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
package net.callmeike.android.simplesync.ui;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.callmeike.android.simplesync.R;
import net.callmeike.android.simplesync.SyncApp;
import net.callmeike.android.simplesync.data.SyncContract;


public class SyncActivity extends AppCompatActivity {
    private static final String TAG = "ACT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_notify:
                Log.d(TAG, "notify change");
                getContentResolver().notifyChange(SyncContract.URI, null, true);
                return true;
            case R.id.action_force:
                Log.d(TAG, "force sync");
                SyncApp app = (SyncApp) getApplication();
                ContentResolver.requestSync(app.getAccount(), SyncContract.AUTHORITY, new Bundle());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
