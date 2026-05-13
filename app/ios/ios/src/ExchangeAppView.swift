//
//  ExchangeAppView.swift
//  ios
//
//  Created by Dmitrii Semenov on 15.12.2024.
//

import SwiftUI
import ExchangeShared

struct ExchangeAppView: View {
    let rootComponent: ExchangeRootComponent

    @StateObject private var themeMode: FlowWrapperObserver<ApiAppThemeMode>

    init(rootComponent: ExchangeRootComponent) {
        self.rootComponent = rootComponent
        self._themeMode = StateObject(
            wrappedValue: FlowWrapperObserver<ApiAppThemeMode>(
                stateFlow: rootComponent.themeMode
            )
        )
    }

    var body: some View {
        StackView(
            stackValue: StateValue(rootComponent.stack),
            onBack: { toIndex in rootComponent.onBack(toIndex: toIndex) },
            childContent: {
                childContent(
                    component: $0,
                    viewModel: $0.viewModel as AnyObject
                )
            }
        )
        .preferredColorScheme(rootColorScheme(for: themeMode.value))
    }
}

@ViewBuilder
func childContent<C, V>(component: C, viewModel: V) -> some View {
    
    if let vm = viewModel as? MainScreenViewModel {
        MainScreenView(viewModel: vm)
    }
    
    else if let vm = viewModel as? DebugPanelScreenBaseViewModel {
        DebugPanelScreenView(viewModel: vm)
    }

    else if let vm = viewModel as? SettingsViewModel {
        SettingsScreenView(viewModel: vm)
    }
}

private func rootColorScheme(for themeMode: ApiAppThemeMode) -> ColorScheme? {
    if themeMode == ApiAppThemeMode.system {
        return nil
    }

    if themeMode == ApiAppThemeMode.light {
        return .light
    }

    if themeMode == ApiAppThemeMode.dark {
        return .dark
    }

    return nil
}
