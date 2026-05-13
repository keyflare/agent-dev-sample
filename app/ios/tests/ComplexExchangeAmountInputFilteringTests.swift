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

private func testKeepsDigits() {
    let result = filteredAmountFieldReplacement(
        currentText: "12",
        replacementRange: NSRange(location: 2, length: 0),
        replacement: "3"
    )

    assertEqual(result.text, "123", "Digits should be accepted")
    assertEqual(result.caretOffset, 3, "Caret should move after the accepted digit")
}

private func testDropsLettersAndSpacesFromPastedText() {
    let result = filteredAmountFieldReplacement(
        currentText: "",
        replacementRange: NSRange(location: 0, length: 0),
        replacement: "12a 3x"
    )

    assertEqual(result.text, "123", "Invalid pasted characters should be dropped")
    assertEqual(result.caretOffset, 3, "Caret should move after the filtered paste")
}

private func testKeepsFirstDecimalSeparatorFromPastedText() {
    let result = filteredAmountFieldReplacement(
        currentText: "",
        replacementRange: NSRange(location: 0, length: 0),
        replacement: "12,3.4"
    )

    assertEqual(result.text, "12,34", "Only the first decimal separator should be accepted")
    assertEqual(result.caretOffset, 5, "Caret should account for the dropped separator")
}

private func testIgnoresSecondDecimalSeparatorWhenValueAlreadyHasOne() {
    let result = filteredAmountFieldReplacement(
        currentText: "1,2",
        replacementRange: NSRange(location: 1, length: 0),
        replacement: "."
    )

    assertEqual(result.text, "1,2", "A second decimal separator should be ignored")
    assertEqual(result.caretOffset, 1, "Caret should stay where the rejected separator was typed")
}

private func testAllowsReplacingExistingDecimalSeparatorWithAnotherOne() {
    let result = filteredAmountFieldReplacement(
        currentText: "1,2",
        replacementRange: NSRange(location: 1, length: 1),
        replacement: "."
    )

    assertEqual(result.text, "1.2", "Selected decimal separator should be replaceable")
    assertEqual(result.caretOffset, 2, "Caret should move after the replacement separator")
}

@main
struct ComplexExchangeAmountInputFilteringTestsRunner {
    static func main() {
        testKeepsDigits()
        testDropsLettersAndSpacesFromPastedText()
        testKeepsFirstDecimalSeparatorFromPastedText()
        testIgnoresSecondDecimalSeparatorWhenValueAlreadyHasOne()
        testAllowsReplacingExistingDecimalSeparatorWithAnotherOne()
        print("ComplexExchangeAmountInputFiltering tests passed")
    }
}
