package com.keyflare.exchange.core.platform

import android.app.Activity

public interface ActivityProvider {
    public fun getCurrentActivity(): Activity?
}
