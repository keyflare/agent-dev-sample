//
//  DsAutoSizeText.swift
//  ios
//
//  Created by Codex on 22.04.2026.
//

import SwiftUI

struct DsAutoSizeText: View {
    private let text: Text
    private let font: Font
    private let color: Color
    private let minimumScaleFactor: CGFloat

    init(
        _ text: String,
        font: Font,
        color: Color = .primary,
        minimumScaleFactor: CGFloat = 0.5
    ) {
        self.text = Text(text)
        self.font = font
        self.color = color
        self.minimumScaleFactor = minimumScaleFactor
    }

    init(
        _ text: AttributedString,
        font: Font,
        color: Color = .primary,
        minimumScaleFactor: CGFloat = 0.5
    ) {
        self.text = Text(text)
        self.font = font
        self.color = color
        self.minimumScaleFactor = minimumScaleFactor
    }

    var body: some View {
        text
            .font(font)
            .foregroundColor(color)
            .lineLimit(1)
            .minimumScaleFactor(minimumScaleFactor)
            .allowsTightening(true)
    }
}
