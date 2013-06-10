package kinjouj.sample.authadapter;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.googlecode.androidannotations.annotations.SystemService;

import static kinjouj.sample.authadapter.AuthenticatorActivity.ACTION_LOGIN;

@EBean
class SampleAccountAuthenticator extends AbstractAccountAuthenticator {

    private static final String TAG = SampleAccountAuthenticator.class.getName();

    @RootContext
    public Context mContext;

    @Bean
    public Service service;

    @SystemService
    public AccountManager mAccountManager;

    public SampleAccountAuthenticator(Context context) {
        super(context);
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
        String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        Log.v(TAG, "addAcount");

        Intent intent = new Intent();
        intent.setAction(ACTION_LOGIN);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        Bundle b = new Bundle();
        b.putParcelable(AccountManager.KEY_INTENT, intent);

        return b;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
        Account account, Bundle options) throws NetworkErrorException {

        Log.v(TAG, "confirmCredentials");
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.v(TAG, "editProperties");
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
        String authTokenType, Bundle options) {

        Log.v(TAG, "getAuthToken");

        if(account == null) {
            return null;
        }

        String password = mAccountManager.getPassword(account);
        String token = service.getAccessToken(account.name, password);

        Bundle b = new Bundle();
        b.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
        b.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
        b.putString(AccountManager.KEY_AUTHTOKEN, token);

        return b;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.v(TAG, "getAuthTokenLabel");
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
        String[] features) throws NetworkErrorException {

        Log.v(TAG, "hasFeatures");
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response,
        Account account, String authTokenType, Bundle options) throws NetworkErrorException {

        Log.v(TAG, "updateCredentials");
        return null;
    }
}