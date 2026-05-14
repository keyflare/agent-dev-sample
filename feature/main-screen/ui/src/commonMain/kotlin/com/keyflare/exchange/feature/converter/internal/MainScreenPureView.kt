@file:OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)

package com.keyflare.exchange.feature.converter.internal

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.keyflare.exchange.core.ds.common.CustomTheme
import com.keyflare.exchange.core.ds.component.DsTextField
import com.keyflare.exchange.core.icons.AppIcon
import com.keyflare.exchange.core.icons.compose.list.Bug
import com.keyflare.exchange.core.icons.compose.list.Settings
import com.keyflare.exchange.feature.converter.api.MainScreenViewState

@Composable
internal fun MainScreenPureView(
    state: State<MainScreenViewState>,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onSuggestionClick: (Int) -> Unit,
    onSettingsClick: () -> Unit,
    onDebugPanelClick: () -> Unit,
) {
    val scrollState = rememberScrollState()

    SharedTransitionLayout {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(
                modifier = Modifier
                    .statusBarsPadding()
                    .size(8.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "/agent_dev ",
                    style = CustomTheme.type.heading1Mono,
                    color = CustomTheme.colors.textPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onDebugPanelClick,
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .offset(x = 8.dp),
                ) {
                    Icon(
                        imageVector = AppIcon.Compose.Bug,
                        contentDescription = null,
                        tint = CustomTheme.colors.iconsAction,
                    )
                }
                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(40.dp, 40.dp)
                        .offset(x = 8.dp),
                ) {
                    Icon(
                        imageVector = AppIcon.Compose.Settings,
                        contentDescription = null,
                        tint = CustomTheme.colors.iconsAction,
                    )
                }
            }
            DsTextField(
                value = state.value.query,
                onValueChange = onQueryChange,
                placeholder = "Search hero",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Button(
                onClick = onSearchClick,
                enabled = state.value.query.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = CustomTheme.colors.surfaceAction,
                    contentColor = CustomTheme.colors.iconsSecondary,
                    disabledBackgroundColor = CustomTheme.colors.surfaceSecondaryVariant,
                    disabledContentColor = CustomTheme.colors.textAdditional,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Search",
                    style = CustomTheme.type.body1,
                )
            }
            state.value.suggestions.forEach { suggestion ->
                Text(
                    text = suggestion.name,
                    style = CustomTheme.type.body1,
                    color = CustomTheme.colors.textPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .clickable { onSuggestionClick(suggestion.id) }
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
            if (state.value.isSearching) {
                Text(
                    text = "Searching...",
                    style = CustomTheme.type.body2,
                    color = CustomTheme.colors.textAdditional,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            state.value.searchError?.let { error ->
                Text(
                    text = error,
                    style = CustomTheme.type.body2,
                    color = CustomTheme.colors.textAlert,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
