//
//  UtilityScreenGeneralScreenView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenGeneralScreenView: View {
    let state: UtilityScreenViewStateScreenGeneral
    let onUiEvent: (UtilityScreenUiEvent) -> Void

    var body: some View {
        List {
            ForEach(state.nodes, id: \.id) { node in
                UtilityScreenNodeView(
                    state: node,
                    onUiEvent: onUiEvent
                )
            }
        }
        .navigationTitle(state.title)
    }
}
