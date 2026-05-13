package com.keyflare.exchange.feature.settings.api

import android.content.Context
import android.content.ContextWrapper
import com.keyflare.common.datastore.DataStorePlatform
import kotlinx.coroutines.runBlocking
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppThemeStoreAndroidTest {

    private var filesDir: File? = null

    @AfterTest
    fun tearDown() {
        filesDir?.deleteRecursively()
        filesDir = null
    }

    @Test
    fun real_store_defaults_to_system_and_updates_selected_theme() = runBlocking {
        val platform = DataStorePlatform(context = TestContext(createFilesDir()))

        val store = AppThemeStore.create(dataStorePlatform = platform)

        assertEquals(AppThemeMode.System, store.themeMode.value)

        store.setThemeMode(AppThemeMode.Dark)
        assertEquals(AppThemeMode.Dark, store.themeMode.value)
    }

    private fun createFilesDir(): File {
        return Files.createTempDirectory("app-theme-store-test").toFile()
            .also { filesDir = it }
    }

    private class TestContext(
        private val filesDir: File,
    ) : ContextWrapper(null as Context?) {

        override fun getFilesDir(): File = filesDir
    }
}
