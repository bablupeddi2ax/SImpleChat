package com.example.simplechat

import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal

class FirebaseInstanceId: FirebaseInstanceIdInternal {
    override fun getId(): String {
        TODO("Not yet implemented")
    }

    override fun getToken(): String? {
        TODO("Not yet implemented")
    }

    override fun getTokenTask(): Task<String> {
        TODO("Not yet implemented")
    }

    override fun deleteToken(p0: String, p1: String) {
        TODO("Not yet implemented")
    }

    override fun addNewTokenListener(p0: FirebaseInstanceIdInternal.NewTokenListener?) {
        TODO("Not yet implemented")
    }
}