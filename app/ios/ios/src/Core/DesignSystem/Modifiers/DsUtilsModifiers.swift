//
//  DsUtilsModifiers.swift
//  ios
//
//  Created by Dmitry Semenov on 01.06.2025.
//

import SwiftUI

extension View {
    @ViewBuilder
    func addIf<Content: View>(
        _ condition: Bool,
        _ modifier: (Self) -> Content
    ) -> some View {
        if condition {
            modifier(self)
        } else {
            self
        }
    }
}
