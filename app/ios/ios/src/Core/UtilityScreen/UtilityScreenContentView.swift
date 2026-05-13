//
//  UtilityScreenContentView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenContentView: View {
    let state: UtilityScreenViewStateContent
    @Environment(\.theme) private var theme

    var body: some View {
        if let icon = state as? UtilityScreenViewStateContentIcon {
            DsIconView(icon: icon.icon, tint: theme.color.iconsAction)
                .frame(
                    width: icon.icon == AppIcon.check ? 28 : 20,
                    height: icon.icon == AppIcon.check ? 28 : 20
                )
        } else if state is UtilityScreenViewStateContentNone {
            EmptyView()
        }
    }
}
