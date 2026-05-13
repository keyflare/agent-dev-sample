//
//  DsIconView.swift
//  ios
//
//  Created by Dmitry Semenov on 03.06.2025.
//

import SwiftUI
import ExchangeShared

struct DsIconView : View {
    let icon: AppIcon
    let tint: Color?
    
    private let iconName: IconName
    
    init(icon: AppIcon, tint: Color? = nil) {
        self.icon = icon
        self.iconName = IosIconsKt.getIcon(icon: icon)
        self.tint = tint
    }
    
    var body: some View {
        if let system = iconName as? IconNameSystem {
            Image(systemName: system.name)
                .resizable()
                .aspectRatio(contentMode: .fill)
                .padding(6)
                .addIf(tint != nil) { $0.foregroundColor(tint!) }
            
        } else if let custom = iconName as? IconNameCustom {
            Image(custom.name)
                .renderingMode(.template)
                .resizable()
                .aspectRatio(contentMode: .fill)
                .addIf(tint != nil) { $0.foregroundColor(tint!) }
        }
    }
}
