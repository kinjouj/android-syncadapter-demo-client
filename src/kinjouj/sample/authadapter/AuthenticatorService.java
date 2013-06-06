package kinjouj.sample.authadapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;

import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;

@EService
public class AuthenticatorService extends Service {

    private static final String TAG = AuthenticatorService.class.getName();

    @Bean
    public SampleAccountAuthenticator mAccountAuthenticator;

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");

        if(ACTION_AUTHENTICATOR_INTENT.equals(intent.getAction())) {
            return mAccountAuthenticator.getIBinder();
        }

        return null;
    }
}