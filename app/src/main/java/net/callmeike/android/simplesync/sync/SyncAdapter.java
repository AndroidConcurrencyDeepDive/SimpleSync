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
package net.callmeike.android.simplesync.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.content.SyncStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

import net.callmeike.android.simplesync.SyncApp;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SYNC";

    public SyncAdapter(Context ctxt, boolean autoInitialize) {
        super(ctxt, autoInitialize);
    }

    @Override
    public void onPerformSync(
        Account account,
        Bundle extras,
        String authority,
        ContentProviderClient provider,
        SyncResult result)
    {
        SyncStats stats = result.stats;

        String url = AccountManager.get(getContext()).getUserData(account, SyncApp.KEY_URI);
        if (null == url) {
            Log.w(TAG, "Account has no uri: " + account);
            stats.numAuthExceptions++;
            return;
        }

        Context ctxt = getContext();
        Intent intent = new Intent(ctxt, SyncService.class);

        ctxt.startService(intent);
        try { stats.numInserts += new SimpleSync().sync(url, provider); }
        catch (RemoteException e) {
            Log.e(TAG, "sync failed: " + e);
            stats.numParseExceptions++;
        }
        catch (IOException e) {
            Log.e(TAG, "sync failed: " + e);
            stats.numIoExceptions++;
        }
        finally { ctxt.stopService(intent); }
    }
}
