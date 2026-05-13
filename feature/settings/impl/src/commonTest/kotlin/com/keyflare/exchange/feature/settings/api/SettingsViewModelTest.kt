package com.keyflare.exchange.feature.settings.api

import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.platform.EmailHelper
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenUiEvent
import com.keyflare.exchange.core.utilityscreen.api.UtilityScreenViewState
import com.keyflare.exchange.feature.settings.internal.SettingsScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun selected_theme_is_marked_with_check_icon() {
        val viewModel = createViewModel(initialTheme = AppThemeMode.Dark)
        dispatcher.scheduler.runCurrent()

        val viewState = viewModel.viewState.value as UtilityScreenViewState.Screen.General

        assertEquals(
            UtilityScreenViewState.Content.Icon(AppIcon.CHECK),
            viewState.listItem(SettingsScreenState.DARK_THEME_NODE_ID).additional,
        )
        assertEquals(
            UtilityScreenViewState.Content.None,
            viewState.listItem(SettingsScreenState.LIGHT_THEME_NODE_ID).additional,
        )
        assertEquals(
            UtilityScreenViewState.Content.None,
            viewState.listItem(SettingsScreenState.SYSTEM_THEME_NODE_ID).additional,
        )
    }

    @Test
    fun selecting_a_theme_updates_the_store_and_view_state() {
        val store = FakeAppThemeStore(initialTheme = AppThemeMode.System)
        val viewModel = createViewModel(store = store)
        dispatcher.scheduler.runCurrent()

        viewModel.onUiEvent(
            UtilityScreenUiEvent.OnNodeClick(SettingsScreenState.LIGHT_THEME_NODE_ID)
        )
        dispatcher.scheduler.runCurrent()

        val viewState = viewModel.viewState.value as UtilityScreenViewState.Screen.General

        assertEquals(listOf(AppThemeMode.Light), store.savedThemes)
        assertEquals(
            UtilityScreenViewState.Content.Icon(AppIcon.CHECK),
            viewState.listItem(SettingsScreenState.LIGHT_THEME_NODE_ID).additional,
        )
        assertEquals(
            UtilityScreenViewState.Content.None,
            viewState.listItem(SettingsScreenState.SYSTEM_THEME_NODE_ID).additional,
        )

        viewModel.onUiEvent(
            UtilityScreenUiEvent.OnNodeClick(SettingsScreenState.LIGHT_THEME_NODE_ID)
        )
        dispatcher.scheduler.runCurrent()

        assertEquals(listOf(AppThemeMode.Light), store.savedThemes)
    }

    @Test
    fun contact_us_opens_email_with_feedback_subject() {
        val emailHelper = FakeEmailHelper()
        val viewModel = createViewModel(emailHelper = emailHelper)
        dispatcher.scheduler.runCurrent()

        viewModel.onUiEvent(UtilityScreenUiEvent.OnNodeClick(SettingsScreenState.CONTACT_US_NODE_ID))

        assertEquals(
            listOf(
                FakeEmailHelper.EmailDraft(
                    contactEmail = "semenov.dm.a@ya.ru",
                    subject = "Ratebench: feedback or idea",
                    body = null,
                ),
            ),
            emailHelper.emailDrafts,
        )
    }

    @Test
    fun report_a_bug_opens_email_with_bug_report_subject() {
        val emailHelper = FakeEmailHelper()
        val viewModel = createViewModel(emailHelper = emailHelper)
        dispatcher.scheduler.runCurrent()

        viewModel.onUiEvent(UtilityScreenUiEvent.OnNodeClick(SettingsScreenState.REPORT_A_BUG_NODE_ID))

        assertEquals(
            listOf(
                FakeEmailHelper.EmailDraft(
                    contactEmail = "semenov.dm.a@ya.ru",
                    subject = "Ratebench: bug report",
                    body = null,
                ),
            ),
            emailHelper.emailDrafts,
        )
    }

    @Test
    fun back_event_navigates_back() {
        val navigator = FakeSettingsNavigator()
        val viewModel = createViewModel(navigator = navigator)

        viewModel.onUiEvent(UtilityScreenUiEvent.OnBack)

        assertEquals(1, navigator.backCalls)
    }

    @Test
    fun settings_di_exposes_injected_theme_store_for_reuse() {
        val store = FakeAppThemeStore(initialTheme = AppThemeMode.System)

        val di = SettingsDi(
            themeStore = store,
            ioDispatcher = dispatcher,
        )

        val reusedStore: AppThemeStore = di.themeStore

        assertSame(store, reusedStore)
        assertSame(store, di.themeStore)
    }

    private fun createViewModel(
        initialTheme: AppThemeMode = AppThemeMode.System,
        store: FakeAppThemeStore = FakeAppThemeStore(initialTheme),
        navigator: SettingsNavigator = FakeSettingsNavigator(),
        emailHelper: EmailHelper = FakeEmailHelper(),
    ): SettingsViewModel {
        return SettingsViewModel(
            navigator = navigator,
            di = SettingsDi(
                themeStore = store,
                ioDispatcher = dispatcher,
                emailHelper = emailHelper,
            ),
        )
    }

    private class FakeAppThemeStore(
        initialTheme: AppThemeMode,
    ) : AppThemeStore {

        private val _theme = MutableStateFlow(initialTheme)
        val savedThemes = mutableListOf<AppThemeMode>()

        override val themeMode: StateFlow<AppThemeMode> = _theme

        override suspend fun setThemeMode(themeMode: AppThemeMode) {
            savedThemes += themeMode
            _theme.value = themeMode
        }
    }

    private class FakeSettingsNavigator : SettingsNavigator {
        var backCalls: Int = 0

        override fun navigateBack() {
            backCalls++
        }
    }

    private class FakeEmailHelper : EmailHelper {
        val emailDrafts = mutableListOf<EmailDraft>()

        override fun writeEmail(
            contactEmail: String,
            subject: String,
            body: String?,
        ): Boolean {
            emailDrafts += EmailDraft(
                contactEmail = contactEmail,
                subject = subject,
                body = body,
            )
            return true
        }

        data class EmailDraft(
            val contactEmail: String,
            val subject: String,
            val body: String?,
        )
    }
}

private fun UtilityScreenViewState.Screen.General.listItem(
    nodeId: String,
): UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple {
    return nodes
        .filterIsInstance<UtilityScreenViewState.ScreenNode.Group>()
        .flatMap { it.nodes }
        .filterIsInstance<UtilityScreenViewState.ScreenNode.Leaf.ListItem.Simple>()
        .first { it.id == nodeId }
}

private fun UtilityScreenViewState.Screen.General.group(
    title: String,
): UtilityScreenViewState.ScreenNode.Group {
    return nodes
        .filterIsInstance<UtilityScreenViewState.ScreenNode.Group>()
        .first { it.title == title }
}
