//
//  UtilityScreenSimpleListItemView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenSimpleListItemView: View {
    let state: UtilityScreenViewStateScreenNodeLeafListItemSimple
    let onClick: (UtilityScreenUiEvent) -> Void

    var body: some View {
        HStack {
            Text(state.label)
            Spacer()
            UtilityScreenContentView(state: state.additional)
        }
        .frame(minHeight: 28)
        .contentShape(Rectangle())
        .onTapGesture {
            onClick(state.onClick)
        }
    }
}
