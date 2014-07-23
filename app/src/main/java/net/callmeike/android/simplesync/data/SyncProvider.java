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
package net.callmeike.android.simplesync.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


public class SyncProvider extends ContentProvider {
    private static final String TAG = "CP";

    public static final String SYNC_UPDATE = "fromNet";

    private static final int DATA_ITEM_TYPE = 1;
    private static final int DATA_DIR_TYPE = 2;

    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(
            SyncContract.AUTHORITY,
            SyncContract.TABLE,
            DATA_DIR_TYPE);
        MATCHER.addURI(
            SyncContract.AUTHORITY,
            SyncContract.TABLE + "/#",
            DATA_ITEM_TYPE);
    }


    private SQLiteOpenHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SyncDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "type: " + uri);
        switch (MATCHER.match(uri)) {
            case DATA_ITEM_TYPE:
                return SyncContract.CONTENT_TYPE_ITEM;
            case DATA_DIR_TYPE:
                return SyncContract.CONTENT_TYPE_DIR;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] proj, String sel, String[] selArgs, String sort) {
        Log.d(TAG, "query: " + uri);
        return null;
    }

    public Uri insert(@NonNull Uri uri, ContentValues vals) {
        Log.d(TAG, "insert: " + uri);

        switch (MATCHER.match(uri)) {
            case DATA_DIR_TYPE:
                break;
            default:
                return null;
        }

        long pk = getDb().insert(SyncContract.TABLE, SyncContract.Columns.COL2, vals);
        if (0 > pk) { return null; }

        uri = uri.buildUpon().appendPath(String.valueOf(pk)).build();
        getContext().getContentResolver().notifyChange(uri, null, !fromNet(uri));

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        Log.d(TAG, "update: " + uri);
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings){
        Log.d(TAG, "delete: " + uri);
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        Log.d(TAG, "bulk insert: " + values.length + " @" + uri);

        switch (MATCHER.match(uri)) {
            case DATA_DIR_TYPE:
                break;
            default:
                return 0;
        }

        int count = 0;
        SQLiteDatabase db = getDb();
        try {
            db.beginTransaction();
            for (ContentValues row: values) {
                if (0 < db.insert(SyncContract.TABLE, null, row)) { count++; }
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }

        if (0 < count) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    private SQLiteDatabase getDb() { return dbHelper.getWritableDatabase(); }

    private boolean fromNet(Uri uri) {
        return null != uri.getQueryParameter(SYNC_UPDATE);
    }
}
