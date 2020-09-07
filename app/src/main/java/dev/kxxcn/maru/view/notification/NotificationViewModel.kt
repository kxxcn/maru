package dev.kxxcn.maru.view.notification

import androidx.lifecycle.MutableLiveData
import dev.kxxcn.maru.util.preference.PreferenceUtils
import dev.kxxcn.maru.view.base.BaseViewModel

class NotificationViewModel : BaseViewModel() {

    val noticeUse = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNotice
    }

    val noticeVibrate = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNoticeVibrate
    }

    val noticeSound = MutableLiveData<Boolean>().apply {
        value = PreferenceUtils.notifyNoticeSound
    }
}
