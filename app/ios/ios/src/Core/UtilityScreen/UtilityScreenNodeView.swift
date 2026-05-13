//
//  UtilityScreenNodeView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenNodeView: View {
    let state: UtilityScreenViewStateScreenNode
    let onUiEvent: (UtilityScreenUiEvent) -> Void

    var body: some View {
        if let simple = state as? UtilityScreenViewStateScreenNodeLeafListItemSimple {
            UtilityScreenSimpleListItemView(
                state: simple,
                onClick: onUiEvent
            )
        } else if let switcher = state as? UtilityScreenViewStateScreenNodeLeafListItemSwitcher {
            UtilityScreenSwitcherListItemView(state: switcher)
        } else if let input = state as? UtilityScreenViewStateScreenNodeLeafListItemInput {
            UtilityScreenInputListItemView(state: input)
        } else if let group = state as? UtilityScreenViewStateScreenNodeGroup {
            UtilityScreenGroupView(
                state: group,
                onUiEvent: onUiEvent
            )
        } else {
            Text("Unknown node")
        }
    }
}
