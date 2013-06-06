package kinjouj.sample.authadapter;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class OAuth {

    @StringRes(R.string.oauth_client_id)
    public String clientId;

    @StringRes(R.string.oauth_client_secret)
    public String clientSecret;

}