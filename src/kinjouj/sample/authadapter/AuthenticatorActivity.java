package kinjouj.sample.authadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.res.StringRes;

import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;

@EActivity
public class AuthenticatorActivity extends Activity {

    private static final String TAG = AuthenticatorActivity.class.getName();
    public static final String ACTION_LOGIN = "kinjouj.sample.authadapter.LOGIN";
    private View mLoginView;

    @SystemService
    public AccountManager mAccountManager;

    @StringRes(R.string.account_type)
    public String mAccountType;

    @AfterInject
    public void initViews() {
        Log.v(TAG, "initViews");
        Intent intent = getIntent();

        if(intent != null && ACTION_LOGIN.equals(intent.getAction())) {
            final Bundle bundle = intent.getExtras();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                .setView(getView())
                .setPositiveButton(
                    getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
    
                            AccountAuthenticatorResponse response = bundle.getParcelable(
                                KEY_ACCOUNT_AUTHENTICATOR_RESPONSE
                            );
                            createAccount(response);
                        }
                    }
                )
                .setNegativeButton(
                    getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }
                )
                .create()
                .show();
        } else {
            finish();
        }
    }

    public void createAccount(AccountAuthenticatorResponse response) {
        String username = toEditTextString(R.id.login_username);
        String password = toEditTextString(R.id.login_password);

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            Account account = new Account(username, mAccountType);

            boolean accountCreateSuccessful = mAccountManager.addAccountExplicitly(
                account,
                password,
                null
            );

            if (accountCreateSuccessful) {
                response.onResult(null);
            }
        }

        finish();
    }

    private View getView() {
        if (mLoginView == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            mLoginView = inflater.inflate(R.layout.user_registration, null);
        }

        return mLoginView;
    }

    private String toEditTextString(int resId) {
        String text = null;

        if (mLoginView != null) {
            text = ((EditText)mLoginView.findViewById(resId)).getText().toString();
        }

        return text;
    }
}