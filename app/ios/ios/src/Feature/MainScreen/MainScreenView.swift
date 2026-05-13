//
//  MainScreenView.swift
//  ios
//
//  Created by Dmitrii Semenov on 15.12.2024.
//

import ExchangeShared
import SwiftUI
import UIKit

struct MainScreenView: View {
    @Environment(\.theme) var theme
    @Environment(\.colorScheme) private var colorScheme

    private let viewModel: MainScreenViewModel
    @StateObject private var state: FlowWrapperObserver<MainScreenViewState>

    init(viewModel: MainScreenViewModel) {
        self.viewModel = viewModel
        self._state = StateObject(
            wrappedValue:
                FlowWrapperObserver<MainScreenViewState>(
                    stateFlow: viewModel.viewState
                )
        )
    }

    var body: some View {

        ZStack {
            theme.color.background
                .ignoresSafeArea()

            MainScreenBackgroundGradient(
                surfaceActionColor: theme.color.surfaceAction,
                isDark: colorScheme == .dark
            )
            .ignoresSafeArea()
            .allowsHitTesting(false)

            ScrollView(showsIndicators: false) {
                VStack(spacing: 0) {
                    HStack(alignment: .center, spacing: 8) {
                        HStack(alignment: .center, spacing: 8) {
                            Text("/agent_dev").font(theme.type.heading1)
                        }
                        .frame(height: 40)
                        .frame(maxWidth: .infinity, alignment: .leading)

                        Button(
                            action: {
                                viewModel.onUiEvent(
                                    event: MainScreenUiEventOnDebugPanelClick()
                                )
                            }
                        ) {
                            Image("ic_debug")
                                .renderingMode(.template)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(theme.color.iconsAction)
                                .frame(width: 24, height: 24)
                        }
                        .frame(width: 40, height: 40)
                        .offset(x: 12)

                        Button(
                            action: {
                                viewModel.onUiEvent(
                                    event: MainScreenUiEventOnSettingsClick()
                                )
                            }
                        ) {
                            Image("ic_settings")
                                .renderingMode(.template)
                                .resizable()
                                .aspectRatio(contentMode: .fit)
                                .foregroundColor(theme.color.iconsAction)
                                .frame(width: 24, height: 24)
                        }
                        .frame(width: 40, height: 40)
                        .offset(x: 8)
                    }
                    .padding(.horizontal, 16)

                    Spacer()
                        .frame(height: 12)
                }
                .frame(maxWidth: .infinity, alignment: .top)
                .padding(.bottom, 16)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

private struct MainScreenBackgroundGradient: View {
    let surfaceActionColor: Color
    let isDark: Bool

    var body: some View {
        if let alpha = mainScreenBackgroundGradientAlpha(isDark: isDark) {
            GeometryReader { proxy in
                RadialGradient(
                    gradient: Gradient(
                        colors: [
                            surfaceActionColor.opacity(alpha),
                            .clear,
                        ]
                    ),
                    center: .topTrailing,
                    startRadius: 0,
                    endRadius: max(proxy.size.width, proxy.size.height)
                    * CGFloat(0.5)
                )
                .frame(width: proxy.size.width, height: proxy.size.height)
            }
        }
    }
}

internal let mainScreenBackgroundGradientRadiusFactor: Double = 0.5

private let mainScreenBackgroundGradientAlphaDark: Double = 0.24

internal func mainScreenBackgroundGradientAlpha(
    isDark: Bool
) -> Double? {
    isDark ? mainScreenBackgroundGradientAlphaDark : nil
}
