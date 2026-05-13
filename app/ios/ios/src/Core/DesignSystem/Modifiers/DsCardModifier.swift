//
//  DsCard.swift
//  ios
//
//  Created by Dmitrii Semenov on 10.02.2025.
//

import SwiftUI

@ViewBuilder
func dsCardBody<Content: View>(
    content: Content,
    theme: AppTheme,
    cornerRadius: CGFloat,
    onClick: (() -> Void)?,
    shadow: Bool,
    border: Bool
) -> some View {
    let shape = RoundedRectangle(cornerRadius: cornerRadius)

    content
        .background(theme.color.surface)
        .clipShape(shape)
        .addIf(border) {
            $0.overlay(
                shape.stroke(theme.color.border, lineWidth: 0.5)
            )
        }
        .addIf(shadow) {
            $0.shadow(
                color: Color.black.opacity(0.08),
                radius: 30,
                x: 0,
                y: 4
            )
        }
        .contentShape(shape)
        .addIf(onClick != nil) {
            $0.onTapGesture {
                onClick?()
            }
        }
}

private struct DsCardModifier: ViewModifier {
    @Environment(\.theme) var theme
    
    /// Corner radius for the card.
    let cornerRadius: CGFloat
    
    /// Optional click handler. If `nil`, the card is non-interactive.
    let onClick: (() -> Void)?
    
    /// Whether the card should render a drop shadow.
    let shadow: Bool
    
    /// Whether the card should render a border.
    let border: Bool
    
    func body(content: Content) -> some View {
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

extension View {
    func dsCard(
        cornerRadius: CGFloat = 20,
        onClick: (() -> Void)? = nil,
        shadow: Bool = true,
        border: Bool = true
    ) -> some View {
        modifier(
            DsCardModifier(
                cornerRadius: cornerRadius,
                onClick: onClick,
                shadow: shadow,
                border: border
            )
        )
    }
}

struct DsCardPreview : View {
    var body: some View {
        VStack {
            // This one must match text size
            ZStack {
                Text("text")
            }
            .dsCard()
            
            // This one must fill screen width
            ZStack {
                Text("text")
            }
            .frame(maxWidth: .infinity)
            .dsCard()
        }
    }
}
