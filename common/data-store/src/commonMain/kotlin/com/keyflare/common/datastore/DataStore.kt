package com.keyflare.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

expect class DataStorePlatform

internal expect fun producePath(
    filename: String,
    platform: DataStorePlatform,
    useExternalFilesDir: Boolean,
): String

/**
 * Creates a [DataStore] with the given [filename] and [platform].
 * Inspired by https://developer.android.com/kotlin/multiplatform/datastore.
 */
fun createDataStore(
    platform: DataStorePlatform,
    filename: String,
    useExternalFilesDir: Boolean = false,
): DataStore<Preferences> = PreferenceDataStoreFactory
    .createWithPath(produceFile = { producePath(filename, platform, useExternalFilesDir).toPath() })
