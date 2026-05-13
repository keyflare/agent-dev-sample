package com.keyflare.exchange.core.icons

fun getIcon(icon: AppIcon) : IconName {
    return when (icon) {
        AppIcon.BACK -> IconName.System("chevron.left")
        AppIcon.ALERT -> IconName.System("exclamationmark.triangle")
        AppIcon.BUG -> IconName.System("ladybug")
        AppIcon.CHECK -> IconName.System("checkmark")
        AppIcon.CHEVRON_LEFT -> IconName.System("chevron.left")
        AppIcon.CHEVRON_RIGHT -> IconName.System("chevron.right")
        AppIcon.CLOSE -> IconName.System("xmark")
        AppIcon.SETTINGS -> IconName.System("gear")
        AppIcon.EXCHANGE -> IconName.Custom("ic_exchange")
        AppIcon.CARD_SETTINGS -> IconName.Custom("ic_card")
        AppIcon.WIDGETS -> IconName.Custom("ic_widgets")
        AppIcon.COPY -> IconName.System("doc.on.clipboard")
        AppIcon.DIVIDE -> IconName.System("divide")
        AppIcon.EQUAL -> IconName.System("equal")
        AppIcon.NETWORK_ALERT -> IconName.System("wifi.exclamationmark")
        AppIcon.MINUS -> IconName.System("minus")
        AppIcon.PASTE -> IconName.System("doc.on.doc")
        AppIcon.PLUS -> IconName.System("plus")
        AppIcon.RECEIPT_LONG -> IconName.System("list.bullet.rectangle.portrait")
        AppIcon.SWAP -> IconName.System("arrow.up.arrow.down")
        AppIcon.TRASH -> IconName.System("trash")
    }
}

interface IconName {
    val name: String

    data class System(override val name: String): IconName
    data class Custom(override val name: String): IconName
}
