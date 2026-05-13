//
//  UtilityScreenSwitcherListItemView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenSwitcherListItemView: View {
    let state: UtilityScreenViewStateScreenNodeLeafListItemSwitcher
    @Environment(\.theme) private var theme

    var body: some View {
        Toggle(
            state.label,
            isOn: Binding(
                get: { state.value },
                set: { value in state.onValueChange(toKotlinBoolean(value)) }
            )
        )
        .tint(theme.color.surfaceAction)
    }
}

private func toKotlinBoolean(_ value: Bool) -> KotlinBoolean {
    KotlinBoolean(bool: value)
}
