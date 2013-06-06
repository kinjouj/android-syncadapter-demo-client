package kinjouj.sample.authadapter;

import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.os.Bundle;

public class AuthenticatorPreferenceActivity extends PreferenceActivity {
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.pref);

        findPreference("user_nickname").setOnPreferenceChangeListener(
            new OnPreferenceChangeListener() {
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    return true;
                }
            }
        );
    }
}