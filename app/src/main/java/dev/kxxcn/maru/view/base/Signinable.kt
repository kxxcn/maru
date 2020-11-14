package dev.kxxcn.maru.view.base

import android.content.Intent

interface Signinable {

    fun onSuccess(data: Intent?)

    fun onFailure()
}
