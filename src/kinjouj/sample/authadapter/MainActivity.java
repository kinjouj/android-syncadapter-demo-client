package kinjouj.sample.authadapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.res.StringRes;

import static kinjouj.sample.authadapter.SampleContentProvider.AUTHORITY;

@EActivity(R.layout.main)
@OptionsMenu(R.menu.main)
public class MainActivity extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MainActivity.class.getName();
    private static final int LOADER_ID = 0;

    @SystemService
    public AccountManager mAccountManager;

    @StringRes(R.string.account_type)
    public String mAccountType;

    private SimpleCursorAdapter mAdapter;

    @AfterViews
    public void initViews() {
        Log.v(TAG, "initViews");
        mAdapter = new SimpleCursorAdapter(
            this,
            android.R.layout.simple_expandable_list_item_2,
            null,
            new String[] { "_id", "name" },
            new int[] { android.R.id.text1, android.R.id.text2 },
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        );

        ((ListView)findViewById(R.id.listView)).setAdapter(mAdapter);
    }

    @OptionsItem(R.id.menu_item_setting)
    public void onMenuSettingClick() {
        Log.v(TAG, "onMenuSettingClick");

        Intent intent = new Intent(Settings.ACTION_SYNC_SETTINGS);
        intent.putExtra(Settings.EXTRA_AUTHORITIES, new String[] { AUTHORITY });

        startActivity(intent);
    }

    @OptionsItem(R.id.menu_item_reload)
    public void onMenuReloadClick() {
        Log.v(TAG, "onMenuReloadClick");
        Account account = findAccount();

        if (ContentResolver.isSyncActive(account, AUTHORITY)) {
            ContentResolver.requestSync(account, AUTHORITY, new Bundle());
        }
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();

        LoaderManager loaderManager = getSupportLoaderManager();

        if (!hasAccount()) {
            if (loaderManager.hasRunningLoaders()) {
                loaderManager.destroyLoader(LOADER_ID);
            }

            mAdapter.swapCursor(null);
            mAdapter.notifyDataSetChanged();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                .setTitle(getString(R.string.account_missing_dialog_title))
                .setMessage(getString(R.string.account_missing_dialog_summary))
                .setPositiveButton(
                    android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
                        }
                    }
                )
                .setNegativeButton(
                    android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }
                )
                .setOnKeyListener(new BackPressKeyDialogListener())
                .create()
                .show();
        } else {
            loaderManager.initLoader(LOADER_ID,  null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(TAG, "onCreateLoader");
        return new CursorLoader(
            this,
            Uri.parse("content://" + AUTHORITY + "/samples"),
            null, null, null ,null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Log.v(TAG, "onLoadFinished");
        mAdapter.swapCursor(c);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> c) {
        Log.v(TAG, "onLoaderReset");
        mAdapter.swapCursor(null);
    }

    private Account findAccount() {
        Account[] accounts = mAccountManager.getAccountsByType(mAccountType);
        return accounts.length > 0 ? accounts[0] : null;
    }

    private boolean hasAccount() {
        return findAccount() != null ? true : false;
    }
}