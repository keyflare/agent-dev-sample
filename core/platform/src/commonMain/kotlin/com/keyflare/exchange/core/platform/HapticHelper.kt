package com.keyflare.exchange.core.platform

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
public class HapticHelper(
    hapticEnabledFlow: Flow<Boolean>,
    private val hapticDelegate: HapticDelegate,
    coroutineScope: CoroutineScope,
) {
    init {
        hapticEnabledFlow
            .onEach { hapticDelegate.setEnabled(it) }
            .launchIn(coroutineScope)
    }

    public fun levelComplete() {
        hapticDelegate.levelComplete()
    }

    public fun keyPress() {
        hapticDelegate.keyPress()
    }

    public fun tilePlaced() {
        hapticDelegate.tilePlaced()
    }
}

public interface HapticDelegate {
    public fun setEnabled(enabled: Boolean)
    public fun levelComplete()
    public fun keyPress()
    public fun tilePlaced()
}

public val LocalHapticHelper: ProvidableCompositionLocal<HapticHelper?> =
    staticCompositionLocalOf { null }
