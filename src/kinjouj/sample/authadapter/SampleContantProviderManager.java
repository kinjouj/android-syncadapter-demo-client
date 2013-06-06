package kinjouj.sample.authadapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import static kinjouj.sample.authadapter.SampleContentProvider.AUTHORITY;

public class SampleContantProviderManager {

    private static final String TAG = SampleContantProviderManager.class.getName();
    private static ArrayList<ContentProviderOperation> operations;

    public static void insert(ContentResolver resolver, List<Sample> samples) {
        Log.v(TAG, "insert");
        operations = new ArrayList<ContentProviderOperation>();


        try {
            for (Sample sample : samples) {
                addOperation(sample.getName());
            }

            if (operations.size() > 0) {
                resolver.applyBatch(AUTHORITY, operations);
            }
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void addOperation(String name) {
        operations.add(
            ContentProviderOperation
                .newInsert(Uri.parse("content://" + AUTHORITY + "/samples"))
                .withValue("name",  name)
                .build()
        );
    }
}