//
//  DebugPanelScreenView.swift
//  ios
//
//  Created by Dmitry Semenov on 25.05.2025.
//

import ExchangeShared
import SwiftUI

struct DebugPanelScreenView: View {
    let viewModel: DebugPanelScreenBaseViewModel

    @StateObject private var state: FlowWrapperObserver<UtilityScreenViewStateScreen>
    
    init (viewModel: DebugPanelScreenBaseViewModel) {
        self.viewModel = viewModel
        self._state = StateObject(
            wrappedValue:
                FlowWrapperObserver<UtilityScreenViewStateScreen>(
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
