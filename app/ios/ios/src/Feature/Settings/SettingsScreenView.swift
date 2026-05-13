//
//  SettingsScreenView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct SettingsScreenView: View {
    let viewModel: SettingsViewModel

    @StateObject private var state: FlowWrapperObserver<UtilityScreenViewStateScreen>

    init(viewModel: SettingsViewModel) {
        self.viewModel = viewModel
        self._state = StateObject(
            wrappedValue: FlowWrapperObserver<UtilityScreenViewStateScreen>(
                stateFlow: viewModel.viewState
            )
        )
    }

    var body: some View {
        UtilityScreenRendererView(
            state: state.value,
            onUiEvent: { event in viewModel.onUiEvent(event: event) }
        )
    }
}
