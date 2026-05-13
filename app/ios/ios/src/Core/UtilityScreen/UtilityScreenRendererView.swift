//
//  UtilityScreenRendererView.swift
//  ios
//

import ExchangeShared
import SwiftUI

@ViewBuilder
func UtilityScreenRendererView(
    state: UtilityScreenViewStateScreen,
    onUiEvent: @escaping (UtilityScreenUiEvent) -> Void
) -> some View {
    switch state {
    case is UtilityScreenViewStateScreenGeneral:
        UtilityScreenGeneralScreenView(
            state: state as! UtilityScreenViewStateScreenGeneral,
            onUiEvent: onUiEvent
        )

    case is UtilityScreenViewStateScreenCustom:
        Text((state as! UtilityScreenViewStateScreenCustom).title)

    default:
        Text("Custom screen: \(String(describing: state))")
    }
}
