package com.horical.demofcm.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.horical.demofcm.extension.tag

/**
 * Created by Nhơn Võ on 5/26/18
 */
class FCMInstanceIDService : FirebaseInstanceIdService() {

    companion object {
        val TAG: String = FCMInstanceIDService::class.java.simpleName.tag()
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        // Get updated InstanceID token.
        val refreshToken: String? = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refresh token: $refreshToken")
        val token = FirebaseInstanceId.getInstance().token
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
    }
}