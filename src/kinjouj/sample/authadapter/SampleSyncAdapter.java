package kinjouj.sample.authadapter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class SampleSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SampleSyncAdapter.class.getName();

    @SystemService
    public AccountManager mAccountManager;

    @StringRes(R.string.account_type)
    public String mAccountType;

    @Bean
    public Service service;

    public SampleSyncAdapter(Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
        ContentProviderClient provider, SyncResult result) {

        Log.v(TAG, "onPerformSync " + new Date());

        String authToken = getAuthToken(account);
        List<Sample> samples = service.getSamples(authToken);

        SampleContantProviderManager.insert(getContext().getContentResolver(), samples);
    }

    public String getAuthToken(Account account) {
        String authToken = null;

        try {
            authToken = mAccountManager.blockingGetAuthToken(
                account,
                mAccountType,
                true
            );
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (AuthenticatorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mAccountManager.invalidateAuthToken(mAccountType, authToken);
        }

        Log.v(TAG, "token: " + authToken);

        return authToken;
    }
}