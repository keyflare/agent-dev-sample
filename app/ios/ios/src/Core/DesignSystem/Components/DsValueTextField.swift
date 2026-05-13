import SwiftUI
import UIKit

internal let dsValueTextFieldVerticalPadding: CGFloat = 12
internal let dsValueTextFieldDefaultUIFont = UIFont.systemFont(ofSize: 16, weight: .medium)
internal let dsValueTextFieldDefaultHeight: CGFloat =
    ceil(dsValueTextFieldDefaultUIFont.lineHeight) + (dsValueTextFieldVerticalPadding * 2)

struct DsValueTextFieldContainer<Content: View>: View {
    @Environment(\.theme) private var theme

    let label: String
    let enabled: Bool
    let isError: Bool
    let suffix: String?
    let content: Content

    init(
        label: String,
        enabled: Bool = true,
        isError: Bool = false,
        suffix: String? = nil,
        @ViewBuilder content: () -> Content
    ) {
        self.label = label
        self.enabled = enabled
        self.isError = isError
        self.suffix = suffix
        self.content = content()
    }

    var body: some View {
        let accentColor = isError ? theme.color.textAlert : theme.color.textAdditional
        let borderColor = isError ? theme.color.textAlert : theme.color.surfaceSecondary

        HStack(alignment: .center, spacing: 8) {
            Text(label)
                .font(theme.type.body2)
                .foregroundColor(accentColor)
                .padding(.leading, 8)

            HStack(alignment: .center, spacing: 2) {
                content
                    .frame(maxWidth: .infinity, alignment: .trailing)

                if let suffix {
                    Text(suffix)
                        .font(theme.type.body1)
                        .foregroundColor(theme.color.textSecondary)
                        .lineLimit(1)
                }
            }
            .frame(maxWidth: .infinity, alignment: .trailing)
        }
        .padding(.trailing, 12)
        .padding(.vertical, dsValueTextFieldVerticalPadding)
        .background(enabled ? .clear : theme.color.surfaceSecondary)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(enabled ? borderColor : .clear, lineWidth: enabled ? 1 : 0)
        )
        .clipShape(RoundedRectangle(cornerRadius: 12))
    }
}

struct DsValueTextField: View {
    @Environment(\.theme) private var theme

    let label: String
    @Binding var text: String
    let placeholder: String
    let enabled: Bool
    let isError: Bool
    let suffix: String?
    let isFocused: Bool
    let usesSystemKeyboard: Bool
    let keyboardType: UIKeyboardType
    let returnKeyType: UIReturnKeyType
    let inputAccessoryButtonTitle: String?
    let inputFilter: DsValueTextFieldInputFilter
    let inputCommand: DsValueTextFieldInputCommand?
    let onFocusChanged: (Bool) -> Void
    let onSubmit: () -> DsValueTextFieldSubmitBehavior

    init(
        label: String,
        text: Binding<String>,
        placeholder: String,
        enabled: Bool = true,
        isError: Bool = false,
        suffix: String? = nil,
        isFocused: Bool = false,
        usesSystemKeyboard: Bool = true,
        keyboardType: UIKeyboardType = .default,
        returnKeyType: UIReturnKeyType = .done,
        inputAccessoryButtonTitle: String? = nil,
        inputFilter: DsValueTextFieldInputFilter = .plain,
        inputCommand: DsValueTextFieldInputCommand? = nil,
        onFocusChanged: @escaping (Bool) -> Void = { _ in },
        onSubmit: @escaping () -> DsValueTextFieldSubmitBehavior = { .resignFocus }
    ) {
        self.label = label
        self._text = text
        self.placeholder = placeholder
        self.enabled = enabled
        self.isError = isError
        self.suffix = suffix
        self.isFocused = isFocused
        self.usesSystemKeyboard = usesSystemKeyboard
        self.keyboardType = keyboardType
        self.returnKeyType = returnKeyType
        self.inputAccessoryButtonTitle = inputAccessoryButtonTitle
        self.inputFilter = inputFilter
        self.inputCommand = inputCommand
        self.onFocusChanged = onFocusChanged
        self.onSubmit = onSubmit
    }

    var body: some View {
        DsValueTextFieldContainer(
            label: label,
            enabled: enabled,
            isError: isError,
            suffix: suffix
        ) {
            DsUIKitValueTextField(
                text: $text,
                placeholder: placeholder,
                isEditable: enabled,
                isFocused: isFocused,
                usesSystemKeyboard: usesSystemKeyboard,
                keyboardType: keyboardType,
                returnKeyType: returnKeyType,
                inputAccessoryButtonTitle: inputAccessoryButtonTitle,
                inputFilter: inputFilter,
                inputCommand: inputCommand,
                style: .init(
                    textColor: UIColor(theme.color.textSecondary),
                    placeholderColor: UIColor(theme.color.textAdditional),
                    cursorColor: UIColor(theme.color.textAction),
                    font: dsValueTextFieldDefaultUIFont
                ),
                onFocusChanged: onFocusChanged,
                onSubmit: onSubmit
            )
        }
    }
}

internal enum DsValueTextFieldSubmitBehavior: Equatable {
    case keepFocus
    case resignFocus
}

internal struct DsValueTextFieldInputFilterResult: Equatable {
    let text: String
    let caretOffset: Int
}

internal struct DsValueTextFieldInputCommand {
    let id: UInt64
    let replacementText: (String, NSRange) -> DsValueTextFieldInputFilterResult
}

internal struct DsValueTextFieldInputFilter {
    let displayText: (String) -> String
    let replacementText: (String, NSRange, String) -> DsValueTextFieldInputFilterResult
    let rawOffset: (Int, String, String) -> Int

    init(
        displayText: @escaping (String) -> String = { $0 },
        replacementText: @escaping (String, NSRange, String) -> DsValueTextFieldInputFilterResult,
        rawOffset: @escaping (Int, String, String) -> Int = { displayedOffset, _, rawText in
            max(0, min(displayedOffset, rawText.count))
        }
    ) {
        self.displayText = displayText
        self.replacementText = replacementText
        self.rawOffset = rawOffset
    }

    static let plain = DsValueTextFieldInputFilter { currentText, replacementRange, replacement in
        guard let textRange = Range(replacementRange, in: currentText) else {
            return .init(
                text: currentText,
                caretOffset: min(replacementRange.location, currentText.count)
            )
        }

        let updatedText = currentText.replacingCharacters(in: textRange, with: replacement)
        return .init(
            text: updatedText,
            caretOffset: replacementRange.location + replacement.count
        )
    }

    static let decimal = DsValueTextFieldInputFilter { currentText, replacementRange, replacement in
        guard let textRange = Range(replacementRange, in: currentText) else {
            return .init(
                text: currentText,
                caretOffset: min(replacementRange.location, currentText.count)
            )
        }

        let textWithoutReplacement = currentText.replacingCharacters(
            in: textRange,
            with: ""
        )
        var hasDecimalSeparator = textWithoutReplacement.contains(where: isDsDecimalSeparator)
        var filteredReplacement = ""

        for character in replacement {
            if character.wholeNumberValue != nil {
                filteredReplacement.append(character)
            } else if isDsDecimalSeparator(character) && !hasDecimalSeparator {
                filteredReplacement.append(character)
                hasDecimalSeparator = true
            }
        }

        let updatedText = currentText.replacingCharacters(
            in: textRange,
            with: filteredReplacement
        )

        return .init(
            text: updatedText,
            caretOffset: replacementRange.location + filteredReplacement.count
        )
    }
}

private struct DsUIKitValueTextField: UIViewRepresentable {
    @Binding var text: String

    let placeholder: String
    let isEditable: Bool
    let isFocused: Bool
    let usesSystemKeyboard: Bool
    let keyboardType: UIKeyboardType
    let returnKeyType: UIReturnKeyType
    let inputAccessoryButtonTitle: String?
    let inputFilter: DsValueTextFieldInputFilter
    let inputCommand: DsValueTextFieldInputCommand?
    let style: Style
    let onFocusChanged: (Bool) -> Void
    let onSubmit: () -> DsValueTextFieldSubmitBehavior

    func makeCoordinator() -> Coordinator {
        Coordinator(parent: self)
    }

    func makeUIView(context: Context) -> UITextField {
        let textField = UIKitTextField(frame: .zero)
        textField.delegate = context.coordinator
        textField.borderStyle = .none
        textField.textAlignment = .right
        textField.adjustsFontSizeToFitWidth = true
        textField.minimumFontSize = 11
        textField.autocorrectionType = .no
        textField.spellCheckingType = .no
        textField.smartInsertDeleteType = .no
        textField.smartQuotesType = .no
        textField.smartDashesType = .no
        textField.textContentType = .none
        if !usesSystemKeyboard {
            textField.inputView = UIView(frame: .zero)
        }
        textField.keyboardType = keyboardType
        textField.returnKeyType = returnKeyType
        textField.inputAccessoryView = context.coordinator.makeInputAccessoryView(
            title: inputAccessoryButtonTitle
        )
        textField.setContentCompressionResistancePriority(.defaultLow, for: .horizontal)
        textField.setContentHuggingPriority(.defaultLow, for: .horizontal)
        context.coordinator.apply(parent: self, to: textField)
        return textField
    }

    func updateUIView(_ uiView: UITextField, context: Context) {
        context.coordinator.parent = self
        context.coordinator.apply(parent: self, to: uiView)

        if isEditable && isFocused && !uiView.isFirstResponder {
            context.coordinator.requestFocus(for: uiView)
        } else if context.coordinator.shouldResign(
            isEditable: isEditable,
            isFocused: isFocused,
            isFirstResponder: uiView.isFirstResponder
        ) {
            uiView.resignFirstResponder()
        }
    }

    struct Style {
        let textColor: UIColor
        let placeholderColor: UIColor
        let cursorColor: UIColor
        let font: UIFont
    }

    private final class UIKitTextField: UITextField {
        override var intrinsicContentSize: CGSize {
            let textHeight = ceil(font?.lineHeight ?? 0)
            return CGSize(width: UIView.noIntrinsicMetric, height: textHeight)
        }

        override func textRect(forBounds bounds: CGRect) -> CGRect {
            bounds
        }

        override func editingRect(forBounds bounds: CGRect) -> CGRect {
            bounds
        }

        override func placeholderRect(forBounds bounds: CGRect) -> CGRect {
            bounds
        }
    }

    final class Coordinator: NSObject, UITextFieldDelegate {
        var parent: DsUIKitValueTextField

        private var isApplyingUpdate = false
        private weak var pendingFocusTextField: UITextField?
        private weak var pendingCaretToEndTextField: UITextField?
        private weak var activeTextField: UITextField?
        private var pendingSubmitBehavior: DsValueTextFieldSubmitBehavior = .resignFocus
        private var currentInputAccessoryButtonTitle: String?
        private var lastAppliedInputCommandId: UInt64?

        init(parent: DsUIKitValueTextField) {
            self.parent = parent
        }

        func requestFocus(for textField: UITextField) {
            guard pendingFocusTextField !== textField else { return }

            pendingFocusTextField = textField

            DispatchQueue.main.async { [weak self, weak textField] in
                guard let self, let textField else { return }
                self.pendingFocusTextField = nil

                guard self.parent.isEditable,
                      self.parent.isFocused,
                      !textField.isFirstResponder else {
                    return
                }

                guard textField.becomeFirstResponder() else { return }
                self.requestCaretToEndAfterSystemSelection(for: textField)
            }
        }

        func shouldResign(
            isEditable: Bool,
            isFocused: Bool,
            isFirstResponder: Bool
        ) -> Bool {
            guard isFirstResponder else {
                return false
            }

            guard isEditable else {
                return true
            }

            guard !isFocused else {
                return false
            }

            return pendingSubmitBehavior != .keepFocus
        }

        func requestCaretToEndAfterSystemSelection(for textField: UITextField) {
            guard pendingCaretToEndTextField !== textField else { return }

            pendingCaretToEndTextField = textField

            DispatchQueue.main.async { [weak self, weak textField] in
                guard let self, let textField else { return }
                self.pendingCaretToEndTextField = nil

                guard textField.isFirstResponder else {
                    return
                }

                self.moveCaretToEnd(in: textField)
            }
        }

        func apply(parent: DsUIKitValueTextField, to textField: UITextField) {
            isApplyingUpdate = true
            textField.textColor = parent.style.textColor
            textField.tintColor = parent.style.cursorColor
            textField.font = parent.style.font
            textField.invalidateIntrinsicContentSize()
            textField.isEnabled = parent.isEditable
            textField.inputView = parent.usesSystemKeyboard ? nil : UIView(frame: .zero)
            textField.keyboardType = parent.keyboardType
            if textField.returnKeyType != parent.returnKeyType ||
                currentInputAccessoryButtonTitle != parent.inputAccessoryButtonTitle {
                textField.returnKeyType = parent.returnKeyType
                textField.inputAccessoryView = makeInputAccessoryView(
                    title: parent.inputAccessoryButtonTitle
                )
                currentInputAccessoryButtonTitle = parent.inputAccessoryButtonTitle
                if textField.isFirstResponder {
                    textField.reloadInputViews()
                }
            }
            textField.attributedPlaceholder = NSAttributedString(
                string: parent.placeholder,
                attributes: [
                    .foregroundColor: parent.style.placeholderColor,
                    .font: parent.style.font
                ]
            )

            let displayedText = parent.inputFilter.displayText(parent.text)

            if textField.text != displayedText {
                textField.text = displayedText
            }

            if !textField.isFirstResponder {
                moveCaretToEnd(in: textField)
            }

            applyInputCommandIfNeeded(parent: parent, to: textField)
            isApplyingUpdate = false
        }

        func textFieldDidBeginEditing(_ textField: UITextField) {
            activeTextField = textField
            pendingSubmitBehavior = .resignFocus
            requestCaretToEndAfterSystemSelection(for: textField)
            parent.onFocusChanged(true)
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            if activeTextField === textField {
                activeTextField = nil
            }
            pendingSubmitBehavior = .resignFocus
            moveCaretToEnd(in: textField)
            parent.onFocusChanged(false)
        }

        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            submitValueField(textField) == .resignFocus
        }

        func makeInputAccessoryView(title: String?) -> UIView? {
            guard let title else {
                return nil
            }

            let toolbar = UIToolbar(
                frame: CGRect(
                    x: 0,
                    y: 0,
                    width: UIScreen.main.bounds.width,
                    height: 44
                )
            )
            toolbar.items = [
                UIBarButtonItem(
                    barButtonSystemItem: .flexibleSpace,
                    target: nil,
                    action: nil
                ),
                UIBarButtonItem(
                    title: title,
                    style: .done,
                    target: self,
                    action: #selector(handleAccessoryButtonTap)
                )
            ]
            toolbar.sizeToFit()
            return toolbar
        }

        @objc private func handleAccessoryButtonTap(_ sender: UIBarButtonItem) {
            submitValueField(activeTextField)
        }

        @discardableResult
        private func submitValueField(_ textField: UITextField?) -> DsValueTextFieldSubmitBehavior {
            let submitBehavior = parent.onSubmit()
            pendingSubmitBehavior = submitBehavior

            if submitBehavior == .resignFocus {
                textField?.resignFirstResponder()
            }

            return submitBehavior
        }

        func textField(
            _ textField: UITextField,
            shouldChangeCharactersIn range: NSRange,
            replacementString string: String
        ) -> Bool {
            guard !isApplyingUpdate else {
                return false
            }

            guard parent.isEditable else {
                return false
            }

            let currentRawText = parent.text
            let currentDisplayedText = parent.inputFilter.displayText(currentRawText)
            let rawRange = rawRange(
                for: range,
                displayedText: currentDisplayedText
            )
            let filterResult = parent.inputFilter.replacementText(
                currentRawText,
                rawRange,
                string
            )
            let displayedCaretOffset = displayedOffset(
                forRawOffset: filterResult.caretOffset,
                rawText: filterResult.text
            )

            parent.text = filterResult.text
            isApplyingUpdate = true
            textField.text = parent.inputFilter.displayText(filterResult.text)
            setCaret(in: textField, displayedOffset: displayedCaretOffset)
            isApplyingUpdate = false
            return false
        }

        private func applyInputCommandIfNeeded(
            parent: DsUIKitValueTextField,
            to textField: UITextField
        ) {
            guard let command = parent.inputCommand,
                  lastAppliedInputCommandId != command.id,
                  parent.isEditable,
                  parent.isFocused else {
                return
            }

            lastAppliedInputCommandId = command.id

            DispatchQueue.main.async { [weak self, weak textField] in
                guard let self, let textField else { return }

                self.applyInputCommand(command, to: textField)
            }
        }

        private func applyInputCommand(
            _ command: DsValueTextFieldInputCommand,
            to textField: UITextField
        ) {
            guard parent.isEditable,
                  parent.isFocused else {
                return
            }

            let currentRawText = parent.text
            let currentDisplayedText = parent.inputFilter.displayText(currentRawText)
            let rawSelection = rawRange(
                for: selectedRange(in: textField),
                displayedText: currentDisplayedText
            )
            let filterResult = command.replacementText(currentRawText, rawSelection)
            let displayedCaretOffset = displayedOffset(
                forRawOffset: filterResult.caretOffset,
                rawText: filterResult.text
            )

            parent.text = filterResult.text
            textField.text = parent.inputFilter.displayText(filterResult.text)
            setCaret(in: textField, displayedOffset: displayedCaretOffset)
        }

        private func selectedRange(in textField: UITextField) -> NSRange {
            guard let selectedTextRange = textField.selectedTextRange else {
                return NSRange(location: textField.text?.count ?? 0, length: 0)
            }

            let location = textField.offset(
                from: textField.beginningOfDocument,
                to: selectedTextRange.start
            )
            let end = textField.offset(
                from: textField.beginningOfDocument,
                to: selectedTextRange.end
            )

            return NSRange(location: location, length: max(0, end - location))
        }

        func moveCaretToEnd(in textField: UITextField) {
            setCaret(in: textField, displayedOffset: textField.text?.count ?? 0)
        }

        private func setCaret(in textField: UITextField, displayedOffset: Int) {
            let boundedOffset = max(0, min(displayedOffset, textField.text?.count ?? 0))
            let start = textField.beginningOfDocument

            guard let caret = textField.position(from: start, offset: boundedOffset) else {
                return
            }

            textField.selectedTextRange = textField.textRange(from: caret, to: caret)
        }

        private func rawRange(
            for displayedRange: NSRange,
            displayedText: String
        ) -> NSRange {
            let startOffset = rawOffset(
                forDisplayedOffset: displayedRange.location,
                displayedText: displayedText
            )
            let endOffset = rawOffset(
                forDisplayedOffset: displayedRange.location + displayedRange.length,
                displayedText: displayedText
            )

            return NSRange(location: startOffset, length: max(0, endOffset - startOffset))
        }

        private func rawOffset(
            forDisplayedOffset displayedOffset: Int,
            displayedText: String
        ) -> Int {
            let boundedOffset = max(0, min(displayedOffset, displayedText.count))
            return parent.inputFilter.rawOffset(
                boundedOffset,
                displayedText,
                parent.text
            )
        }

        private func displayedOffset(
            forRawOffset rawOffset: Int,
            rawText: String
        ) -> Int {
            let boundedOffset = max(0, min(rawOffset, rawText.count))
            let prefix = String(rawText.prefix(boundedOffset))
            return parent.inputFilter.displayText(prefix).count
        }
    }
}

private func isDsDecimalSeparator(_ character: Character) -> Bool {
    character == "." || character == ","
}
