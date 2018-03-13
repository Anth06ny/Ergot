package ergot.anthony.com.ergot.model.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import ergot.anthony.com.ergot.utils.SharedPreferenceUtils;

/**
 * Created by Utilisateur on 13/03/2018.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferenceUtils.saveFireBaseToken(refreshedToken);
    }
}
