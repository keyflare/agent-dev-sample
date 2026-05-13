//
//  DsAppBar.swift
//  ios
//
//  Created by Codex on 22.04.2026.
//

import SwiftUI

struct DsAppBar: View {
    @Environment(\.theme) private var theme
    @State private var topInset: CGFloat = 0

    let title: String
    let subtitle: String?
    let caption: String?
    let dividerVisible: Bool
    let background: Color?
    let onBackClick: () -> Void

    init(
        title: String,
        subtitle: String? = nil,
        caption: String? = nil,
        dividerVisible: Bool = false,
        background: Color? = nil,
        onBackClick: @escaping () -> Void
    ) {
        self.title = title
        self.subtitle = subtitle
        self.caption = caption
        self.dividerVisible = dividerVisible
        self.background = background
        self.onBackClick = onBackClick
    }

    var body: some View {
        let backgroundColor = background ?? theme.color.background

        VStack(spacing: 0) {
            Color.clear
                .frame(height: topInset)

            HStack(alignment: .center, spacing: 8) {
                Button(action: onBackClick) {
                    Image(systemName: "chevron.left")
                        .font(.system(size: 17, weight: .semibold))
                        .frame(width: 24, height: 24)
                        .foregroundColor(theme.color.textPrimary)
                        .frame(width: 40, height: 40)
                }
                .buttonStyle(.plain)

                VStack(alignment: .leading, spacing: 2) {
                    Text(title)
                        .font(theme.type.heading1)
                        .foregroundColor(theme.color.textPrimary)
                        .lineLimit(1)

                    if let subtitle {
                        Text(subtitle)
                            .font(theme.type.body2)
                            .foregroundColor(theme.color.textSecondary)
                            .lineLimit(1)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                if let caption {
                    Text(caption)
                        .font(theme.type.body2)
                        .foregroundColor(theme.color.textPrimaryVariant)
                        .lineLimit(1)
                        .padding(.top, 4)
                }
            }
            .frame(height: 56)
            .padding(.leading, 8)
            .padding(.trailing, 12)

            if dividerVisible {
                Rectangle()
                    .fill(theme.color.divider)
                    .frame(height: 0.7)
            }
        }
        .frame(maxWidth: .infinity, alignment: .top)
        .background(backgroundColor.ignoresSafeArea(edges: .top))
        .background(
            GeometryReader { proxy in
                Color.clear.preference(
                    key: TopSafeAreaInsetPreferenceKey.self,
                    value: proxy.safeAreaInsets.top
                )
            }
        )
        .onPreferenceChange(TopSafeAreaInsetPreferenceKey.self) { topInset in
            self.topInset = topInset
        }
    }
}

private struct TopSafeAreaInsetPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0

    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = nextValue()
    }
}
