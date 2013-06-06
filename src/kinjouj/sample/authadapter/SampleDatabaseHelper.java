package kinjouj.sample.authadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class SampleDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = SampleDatabaseHelper.class.getName();

    protected SampleDatabaseHelper(Context ctx) {
        super(ctx, "sample", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "onCreate");
        db.beginTransaction();

        try {
            db.execSQL("CREATE TABLE samples(_id integer primary key autoincrement, name text not null)");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion) {
        Log.v(TAG, "onUpgrade");

        db.beginTransaction();

        try {
            db.execSQL("DROP TABLE IF EXISTS samples");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}