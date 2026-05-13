import Foundation

private func assertEqual<T: Equatable>(
    _ actual: T,
    _ expected: T,
    _ message: String,
    file: StaticString = #filePath,
    line: UInt = #line
) {
    guard actual == expected else {
        fputs("Assertion failed: \(message). Expected \(expected), got \(actual)\n", stderr)
        exit(1)
    }
}

private func testDigitInsertsAtCaret() {
    let result = applyComplexExchangeKeypadInputAction(
        currentText: "12",
        selectedRange: NSRange(location: 2, length: 0),
        action: .digit(3)
    )

    assertEqual(result.text, "123", "Digit action should insert the digit")
    assertEqual(result.caretOffset, 3, "Caret should move after inserted digit")
}

private func testDecimalSeparatorIsFilteredByAmountRules() {
    let result = applyComplexExchangeKeypadInputAction(
        currentText: "1.2",
        selectedRange: NSRange(location: 3, length: 0),
        action: .decimalSeparator
    )

    assertEqual(result.text, "1.2", "Second decimal separator should be ignored")
    assertEqual(result.caretOffset, 3, "Caret should stay at the rejected separator position")
}

private func testBackspaceDeletesPreviousCharacter() {
    let result = applyComplexExchangeKeypadInputAction(
        currentText: "123",
        selectedRange: NSRange(location: 2, length: 0),
        action: .backspace
    )

    assertEqual(result.text, "13", "Backspace should delete the character before the caret")
    assertEqual(result.caretOffset, 1, "Caret should move to the deleted character position")
}

private func testBackspaceDeletesSelection() {
    let result = applyComplexExchangeKeypadInputAction(
        currentText: "1234",
        selectedRange: NSRange(location: 1, length: 2),
        action: .backspace
    )

    assertEqual(result.text, "14", "Backspace should delete the selected range")
    assertEqual(result.caretOffset, 1, "Caret should move to the selection start")
}

private func testClearRemovesAllText() {
    let result = applyComplexExchangeKeypadInputAction(
        currentText: "123.45",
        selectedRange: NSRange(location: 6, length: 0),
        action: .clear
    )

    assertEqual(result.text, "", "Clear should remove all text")
    assertEqual(result.caretOffset, 0, "Caret should move to the beginning after clear")
}

@main
struct ComplexExchangeKeypadInputLogicTestsRunner {
    static func main() {
        testDigitInsertsAtCaret()
        testDecimalSeparatorIsFilteredByAmountRules()
        testBackspaceDeletesPreviousCharacter()
        testBackspaceDeletesSelection()
        testClearRemovesAllText()
        print("ComplexExchangeKeypadInputLogic tests passed")
    }
}
