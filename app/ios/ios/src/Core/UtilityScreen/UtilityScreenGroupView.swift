//
//  UtilityScreenGroupView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenGroupView: View {
    let state: UtilityScreenViewStateScreenNodeGroup
    let onUiEvent: (UtilityScreenUiEvent) -> Void

    var body: some View {
        Section(state.title) {
            ForEach(state.nodes, id: \.id) { node in
                UtilityScreenNodeView(
                    state: node,
                    onUiEvent: onUiEvent
                )
            }
        }
    }
}
