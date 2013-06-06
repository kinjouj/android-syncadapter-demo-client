package kinjouj.sample.authadapter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EProvider;

@EProvider
public class SampleContentProvider extends ContentProvider {

    private static final String TAG = SampleContentProvider.class.getName();
    public static final String AUTHORITY = "kinjouj.sample.authadapter.provider";
    private static final int REQUEST_SAMPLES = 1;

    @Bean
    public SampleDatabaseHelper mDBHelper;

    private static UriMatcher sMatcher;

    static {
        sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sMatcher.addURI(AUTHORITY, "samples", REQUEST_SAMPLES);
    }

    @Override
    public String getType(Uri uri) {
        Log.v(TAG, "getType");
        return "vnd.android.cursor.dir/" + AUTHORITY;
    }

    @Override
    public boolean onCreate() {
        Log.v(TAG, "onCreate");
        return false;
    }

    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
        String[] selectionArgs, String sortOrder) {

        Log.v(TAG, "query");
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

        Cursor csr = db.query(
            "samples",
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        );
        csr.setNotificationUri(getContext().getContentResolver(), uri);

        return csr;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert");

        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.beginTransaction();

        int cnt = 0;

        try {
            cnt += db.insert("samples", null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Uri returnUri = null;

        if (cnt > 0) {
            returnUri = ContentUris.withAppendedId(uri, cnt);
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
        String[] selectionArgs) {

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }
}