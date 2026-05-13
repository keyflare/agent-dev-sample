package com.keyflare.exchange.core.platform

public interface EmailHelper {
    public fun writeEmail(
        contactEmail: String,
        subject: String,
        body: String? = "",
    ): Boolean
}

public object RatebenchEmailConfig {
    public const val CONTACT_EMAIL: String = "semenov.dm.a@ya.ru"
    public const val FEEDBACK_SUBJECT: String = "Ratebench: feedback or idea"
    public const val BUG_REPORT_SUBJECT: String = "Ratebench: bug report"
}
