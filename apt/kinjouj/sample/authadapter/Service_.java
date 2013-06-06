//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package kinjouj.sample.authadapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import kinjouj.sample.authadapter.R.string;

public final class Service_
    extends Service
{

    private Context context_;
    private static Service_ instance_;

    private Service_(Context context) {
        super(context);
        context_ = context;
        init_();
    }

    public void afterSetContentView_() {
        if (!(context_ instanceof Activity)) {
            return ;
        }
        ((OAuth_) oauth).afterSetContentView_();
    }

    /**
     * You should check that context is an activity before calling this method
     * 
     */
    public View findViewById(int id) {
        Activity activity_ = ((Activity) context_);
        return activity_.findViewById(id);
    }

    @SuppressWarnings("all")
    private void init_() {
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
        Resources resources_ = context_.getResources();
        restServerUrl = resources_.getString(string.rest_server_url);
        oauth = OAuth_.getInstance_(context_);
    }

    public static Service_ getInstance_(Context context) {
        if (instance_ == null) {
            instance_ = new Service_(context.getApplicationContext());
        }
        return instance_;
    }

    public void rebind(Context context) {
    }

}
