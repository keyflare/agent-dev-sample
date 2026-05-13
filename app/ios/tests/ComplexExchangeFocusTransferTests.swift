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

private func assertFalse(
    _ value: Bool,
    _ message: String,
    file: StaticString = #filePath,
    line: UInt = #line
) {
    guard !value else {
        fputs("Assertion failed: \(message)\n", stderr)
        exit(1)
    }
}

private func assertTrue(
    _ value: Bool,
    _ message: String,
    file: StaticString = #filePath,
    line: UInt = #line
) {
    guard value else {
        fputs("Assertion failed: \(message)\n", stderr)
        exit(1)
    }
}

private func testNextSubmitUsesFocusTransferWithoutSystemDismiss() {
    assertEqual(
        complexExchangeSubmitStrategy(hasNextField: true),
        .focusTransfer,
        "Next submit should request a focus transfer"
    )
    assertFalse(
        shouldSystemHandleValueFieldSubmit(strategy: .focusTransfer),
        "System return handling should stay disabled during focus transfer"
    )
}

private func testDoneSubmitAllowsSystemDismiss() {
    assertEqual(
        complexExchangeSubmitStrategy(hasNextField: false),
        .dismissKeyboard,
        "Last field submit should dismiss the keyboard"
    )
    assertTrue(
        shouldSystemHandleValueFieldSubmit(strategy: .dismissKeyboard),
        "System return handling should stay enabled for the last field"
    )
}

private func testFocusedFieldDoesNotResignDuringTransfer() {
    assertFalse(
        shouldResignComplexExchangeValueField(
            isEditable: true,
            isFocused: false,
            isFirstResponder: true,
            submitStrategy: .focusTransfer
        ),
        "Focused field should not resign while the next field is taking focus"
    )
}

private func testFieldResignsWhenTransferIsNotPending() {
    assertTrue(
        shouldResignComplexExchangeValueField(
            isEditable: true,
            isFocused: false,
            isFirstResponder: true,
            submitStrategy: .dismissKeyboard
        ),
        "Field should resign when focus transfer is not pending"
    )
}

@main
struct ComplexExchangeFocusTransferTestsRunner {
    static func main() {
        testNextSubmitUsesFocusTransferWithoutSystemDismiss()
        testDoneSubmitAllowsSystemDismiss()
        testFocusedFieldDoesNotResignDuringTransfer()
        testFieldResignsWhenTransferIsNotPending()
        print("ComplexExchangeFocusTransfer tests passed")
    }
}
