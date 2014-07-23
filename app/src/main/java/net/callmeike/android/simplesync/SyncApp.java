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
package net.callmeike.android.simplesync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;

import net.callmeike.android.simplesync.data.SyncContract;


public class SyncApp extends Application {
    private static final String TAG = "APP";

    public static final String CANONICAL_ACCOUNT = "SimpleSync";

    public static final String KEY_URI = "SyncAdapter.URI";


    private AccountManager mgr;
    private String acctType;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "up");

        mgr = AccountManager.get(this);
        acctType = getString(R.string.account_type);

        if (getAccount() == null) { createAccount(); }
    }

    public Account getAccount() {
        Account[] accounts = mgr.getAccountsByType(acctType);
        int accountNum = accounts.length;
        if (accountNum > 1) { Log.w(TAG, "Unexpected accounts: " + accountNum); }
        return (accountNum < 1) ? null : accounts[0];
    }

    private void createAccount() {
        Account account = new Account(CANONICAL_ACCOUNT, acctType);

        mgr.addAccountExplicitly(account, null, null);
        mgr.setUserData(account, KEY_URI, getString(R.string.server_uri));

        ContentResolver.setIsSyncable(account, SyncContract.AUTHORITY, 1);
        ContentResolver.setSyncAutomatically(account, SyncContract.AUTHORITY, true);
        ContentResolver.addPeriodicSync(
            account,
            SyncContract.AUTHORITY,
            new Bundle(),
            getResources().getInteger(R.integer.sync_interval_secs));

        Log.d(TAG, "create account: " + account);
    }
}
