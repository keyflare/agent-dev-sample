package com.keyflare.common.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> dataStoreSyncProperty(
    dataStore: DataStore<Preferences>,
    key: Preferences.Key<T>,
    defaultValue: T,
    enableCache: Boolean = false,
): DataStoreSyncPropertyDelegate<T> {
    return DataStoreSyncPropertyDelegate(dataStore, key, defaultValue, enableCache)
}

class DataStoreSyncPropertyDelegate<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
    private val enableCache: Boolean,
) : ReadWriteProperty<Any, T> {

    private var cache: T = runBlocking { dataStore.data.map { it[key] }.first() ?: defaultValue }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return if (enableCache) {
             cache
        } else {
            runBlocking { dataStore.data.map { it[key] }.first() ?: defaultValue }
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        runBlocking {
            cache = value
            dataStore.edit { it[key] = value }
        }
    }
}
