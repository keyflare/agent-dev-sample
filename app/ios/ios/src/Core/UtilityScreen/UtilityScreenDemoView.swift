//
//  UtilityScreenDemoView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenDemoView: View {
    var body: some View {
        UtilityScreenRendererView(
            state: UtilityScreenDemoStateFactory.shared.createGeneralScreen(),
            onUiEvent: { _ in }
        )
    }
}
