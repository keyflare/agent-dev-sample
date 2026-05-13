import UIKit

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

private func testValueFieldDoesNotUseSystemKeyboard() {
    assertEqual(
        complexExchangeValueFieldUsesSystemKeyboard(),
        false,
        "Value field should use the custom keypad sheet instead of a system keyboard"
    )
}

private func testNextReturnKeyUsesNextKeypadTitle() {
    assertEqual(
        complexExchangeValueFieldSubmitButtonTitle(returnKeyType: .next),
        "next",
        "Next return key should show a next keypad button"
    )
}

private func testDoneReturnKeyUsesDoneKeypadTitle() {
    assertEqual(
        complexExchangeValueFieldSubmitButtonTitle(returnKeyType: .done),
        "done",
        "Done return key should show a done keypad button"
    )
}

@main
struct ComplexExchangeValueFieldInputLogicTestsRunner {
    static func main() {
        testValueFieldDoesNotUseSystemKeyboard()
        testNextReturnKeyUsesNextKeypadTitle()
        testDoneReturnKeyUsesDoneKeypadTitle()
        print("ComplexExchangeValueFieldInputLogic tests passed")
    }
}
