//
//  DsCard.swift
//  ios
//
//  Created by Codex on 22.04.2026.
//

import SwiftUI

struct DsCard<Content: View>: View {
    @Environment(\.theme) private var theme

    private let cornerRadius: CGFloat
    private let onClick: (() -> Void)?
    private let shadow: Bool
    private let border: Bool
    private let content: Content

    init(
        cornerRadius: CGFloat = 20,
        onClick: (() -> Void)? = nil,
        shadow: Bool = true,
        border: Bool = true,
        @ViewBuilder content: () -> Content
    ) {
        self.cornerRadius = cornerRadius
        self.onClick = onClick
        self.shadow = shadow
        self.border = border
        self.content = content()
    }

    var body: some View {
        dsCardBody(
            content: content,
            theme: theme,
            cornerRadius: cornerRadius,
            onClick: onClick,
            shadow: shadow,
            border: border
        )
    }
}
