//
//  UtilityScreenInputListItemView.swift
//  ios
//

import ExchangeShared
import SwiftUI

struct UtilityScreenInputListItemView: View {
    let state: UtilityScreenViewStateScreenNodeLeafListItemInput

    var body: some View {
        if let textState = state as? UtilityScreenViewStateScreenNodeLeafListItemInputText {
            UtilityScreenTextInputView(state: textState)
        } else {
            EmptyView()
        }
    }
}

private struct UtilityScreenTextInputView: View {
    @Environment(\.theme) private var theme
    let state: UtilityScreenViewStateScreenNodeLeafListItemInputText
    @State private var value: String

    init(state: UtilityScreenViewStateScreenNodeLeafListItemInputText) {
        self.state = state
        _value = State(initialValue: state.initialValue)
    }

    var body: some View {
        UtilityScreenInputLayout(
            value: $value,
            label: state.label,
            keyboardType: .default,
            textAlignment: .leading
        ) {
            HStack {
                if state.allowCopy {
                    Image(systemName: "doc.on.doc")
                        .foregroundColor(theme.color.textAction)
                        .onTapGesture {
                            UIPasteboard.general.string = value
                        }
                }

                Image(systemName: "doc.on.clipboard")
                    .foregroundColor(theme.color.textAction)
                    .onTapGesture {
                        let pasted = UIPasteboard.general.string ?? ""
                        value = pasted
                    }

                Image(systemName: "xmark.circle")
                    .foregroundColor(theme.color.textAction)
                    .onTapGesture {
                        value = ""
                    }
            }
        }
        .onChange(of: value) { newValue in
            state.onValueChange(newValue)
        }
        .onChange(of: state.initialValue) { newValue in
            if value != newValue {
                value = newValue
            }
        }
    }
}

private struct UtilityScreenInputLayout<Trailing: View>: View {
    @Environment(\.theme) private var theme
    @Binding var value: String
    let label: String
    let keyboardType: UIKeyboardType
    let textAlignment: TextAlignment
    let trailingContent: () -> Trailing

    init(
        value: Binding<String>,
        label: String,
        keyboardType: UIKeyboardType = .default,
        textAlignment: TextAlignment = .leading,
        @ViewBuilder trailingContent: @escaping () -> Trailing = { EmptyView() as! Trailing }
    ) {
        self._value = value
        self.label = label
        self.keyboardType = keyboardType
        self.textAlignment = textAlignment
        self.trailingContent = trailingContent
    }

    var body: some View {
        HStack(alignment: .center) {
            Text(label)
                .font(theme.type.body3)
                .foregroundColor(theme.color.textSecondary)

            Spacer()
                .frame(width: 16)

            TextField("", text: $value)
                .font(theme.type.body2)
                .foregroundColor(theme.color.textPrimary)
                .multilineTextAlignment(textAlignment)
                .keyboardType(keyboardType)
                .frame(maxWidth: .infinity)

            trailingContent()
        }
    }
}
