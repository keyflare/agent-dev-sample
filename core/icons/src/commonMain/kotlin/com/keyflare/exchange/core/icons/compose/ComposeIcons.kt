package com.keyflare.exchange.core.icons.compose

import androidx.compose.ui.graphics.vector.ImageVector
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.list.Alert
import com.keyflare.exchange.core.icons.compose.list.Back
import com.keyflare.exchange.core.icons.compose.list.Bug
import com.keyflare.exchange.core.icons.compose.list.CardSettings
import com.keyflare.exchange.core.icons.compose.list.Check
import com.keyflare.exchange.core.icons.compose.list.ChevronLeft
import com.keyflare.exchange.core.icons.compose.list.ChevronRight
import com.keyflare.exchange.core.icons.compose.list.Close
import com.keyflare.exchange.core.icons.compose.list.Copy
import com.keyflare.exchange.core.icons.compose.list.Divide
import com.keyflare.exchange.core.icons.compose.list.Equal
import com.keyflare.exchange.core.icons.compose.list.Exchange
import com.keyflare.exchange.core.icons.compose.list.Minus
import com.keyflare.exchange.core.icons.compose.list.NetworkAlert
import com.keyflare.exchange.core.icons.compose.list.Paste
import com.keyflare.exchange.core.icons.compose.list.Plus
import com.keyflare.exchange.core.icons.compose.list.ReceiptLong
import com.keyflare.exchange.core.icons.compose.list.Settings
import com.keyflare.exchange.core.icons.compose.list.Swap
import com.keyflare.exchange.core.icons.compose.list.Trash
import com.keyflare.exchange.core.icons.compose.list.Widgets

val AppIcon.Compose.AllIcons: List<ImageVector>
    get() {
        if (all != null) {
            return all!!
        }
        all = listOf(
            // add all the icons below
            Alert,
            Back,
            Bug,
            CardSettings,
            Check,
            ChevronLeft,
            ChevronRight,
            Close,
            Copy,
            Divide,
            Equal,
            Exchange,
            Minus,
            NetworkAlert,
            Paste,
            Plus,
            ReceiptLong,
            Settings,
            Swap,
            Trash,
            Widgets,
        )
        return all!!
    }

fun AppIcon.imageVector(): ImageVector? =
    AppIcon.Compose.AllIcons.firstOrNull { it.name == this.name }

fun AppIcon.imageVector(default: ImageVector): ImageVector =
    imageVector() ?: default

private var all: List<ImageVector>? = null
