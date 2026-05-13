//
//  CustomTheme.swift
//  ios
//
//  Created by Dmitrii Semenov on 08.02.2025.
//

import SwiftUI

struct AppTheme {
    let color: Colors
    let type: Types

    static let defaultTheme = AppTheme(
        color: Colors(
            textPrimary: Color("textPrimary"),
            textPrimaryVariant: Color("textPrimaryVariant"),
            textSecondary: Color("textSecondary"),
            textAdditional: Color("textAdditional"),
            textAction: Color("textAction"),
            textAlert: Color("textAlert"),
            textSuccess: Color("textSuccess"),
            background: Color("background"),
            surface: Color("surface"),
            surfaceSecondary: Color("surfaceSecondary"),
            surfaceSecondaryVariant: Color("surfaceSecondaryVariant"),
            keyboardButton: Color("keyboardButton"),
            surfaceAction: Color("surfaceAction"),
            surfaceAccent: Color("surfaceAccent"),
            tint: Color("tint"),
            iconsPrimary: Color("iconsPrimary"),
            iconsSecondary: Color("iconsSecondary"),
            iconsAction: Color("iconsAction"),
            border: Color("border"),
            divider: Color("divider")
        ),
        type: Types(
            digitL: Font.system(size: 40, weight: .medium),
            digitLMinimized: Font.system(size: 1, weight: .medium),
            digitM: Font.system(size: 36, weight: .medium),
            digitS: Font.system(size: 24, weight: .medium),
            heading1: Font.system(size: 20, weight: .semibold),
            heading2: Font.system(size: 16, weight: .bold),
            heading3: Font.system(size: 16, weight: .semibold),
            heading4: Font.system(size: 14, weight: .semibold),
            body1: Font.system(size: 16, weight: .medium),
            body2: Font.system(size: 14, weight: .medium),
            body3: Font.system(size: 12, weight: .medium),
            body4: Font.system(size: 8, weight: .medium),
            digitLMono: Font.system(size: 40, weight: .medium, design: .monospaced),
            digitLMinimizedMono: Font.system(size: 1, weight: .medium, design: .monospaced),
            digitMMono: Font.system(size: 36, weight: .medium, design: .monospaced),
            digitSMono: Font.system(size: 24, weight: .medium, design: .monospaced),
            heading1Mono: Font.system(size: 20, weight: .semibold, design: .monospaced),
            heading2Mono: Font.system(size: 16, weight: .bold, design: .monospaced),
            heading3Mono: Font.system(size: 16, weight: .semibold, design: .monospaced),
            heading4Mono: Font.system(size: 14, weight: .semibold, design: .monospaced),
            body1Mono: Font.system(size: 16, weight: .medium, design: .monospaced),
            body2Mono: Font.system(size: 14, weight: .medium, design: .monospaced),
            body3Mono: Font.system(size: 12, weight: .medium, design: .monospaced)
        )
    )

    struct Colors {
        let textPrimary: Color
        let textPrimaryVariant: Color
        let textSecondary: Color
        let textAdditional: Color
        let textAction: Color
        let textAlert: Color
        let textSuccess: Color
        let background: Color
        let surface: Color
        let surfaceSecondary: Color
        let surfaceSecondaryVariant: Color
        let keyboardButton: Color
        let surfaceAction: Color
        let surfaceAccent: Color
        let tint: Color
        let iconsPrimary: Color
        let iconsSecondary: Color
        let iconsAction: Color
        let border: Color
        let divider: Color
    }

    struct Types {
        let digitL: Font
        let digitLMinimized: Font
        let digitM: Font
        let digitS: Font
        let heading1: Font
        let heading2: Font
        let heading3: Font
        let heading4: Font
        let body1: Font
        let body2: Font
        let body3: Font
        let body4: Font
        let digitLMono: Font
        let digitLMinimizedMono: Font
        let digitMMono: Font
        let digitSMono: Font
        let heading1Mono: Font
        let heading2Mono: Font
        let heading3Mono: Font
        let heading4Mono: Font
        let body1Mono: Font
        let body2Mono: Font
        let body3Mono: Font
    }
}

private struct ThemeKey: EnvironmentKey {
    static let defaultValue = AppTheme.defaultTheme
}

extension EnvironmentValues {
    var theme: AppTheme {
        get { self[ThemeKey.self] }
        set { self[ThemeKey.self] = newValue }
    }
}
