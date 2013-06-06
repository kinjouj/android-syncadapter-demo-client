package kinjouj.sample.authadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;

@EService
public class SampleSyncService extends Service {

    private static final String TAG = SampleSyncService.class.getName();

    @Bean
    public SampleSyncAdapter mSyncAdadter;

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return mSyncAdadter.getSyncAdapterBinder();
    }
}