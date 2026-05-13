//
//  iosApp.swift
//  ios
//
//  Created by Dmitrii Semenov on 29.11.2024.
//

import SwiftUI
import ExchangeShared

let previewModeEnabled = false

@main
struct iosApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self)
    var appDelegate: AppDelegate
    
    var body: some Scene {
        WindowGroup {
            Group {
                if (previewModeEnabled) {
                    PreviewScreenView()
                        .environment(\.theme, AppTheme.defaultTheme)
                } else {
                    ExchangeAppView(rootComponent: appDelegate.rootComponent)
                        .environment(\.theme, AppTheme.defaultTheme)
                }
            }
            .onOpenURL { url in
                _ = appDelegate.handleDebugStageURL(url)
            }
        }
    }
}
