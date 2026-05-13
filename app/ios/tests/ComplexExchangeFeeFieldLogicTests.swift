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

private func testMinMaxFieldsAreEditableOnlyForEnabledPercentBounds() {
    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .minimum,
            isPercentMode: true,
            isMinimumEnabled: true
        ),
        true,
        "Minimum field should be editable for enabled percent minimum"
    )

    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .minimum,
            isPercentMode: true,
            isMinimumEnabled: false
        ),
        false,
        "Minimum field should not be editable when toggle is disabled"
    )

    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .minimum,
            isPercentMode: false,
            isMinimumEnabled: true
        ),
        false,
        "Minimum field should not be editable for fixed fee"
    )

    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .maximum,
            isPercentMode: true,
            isMinimumEnabled: true
        ),
        true,
        "Maximum field should be editable for enabled percent bounds"
    )

    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .maximum,
            isPercentMode: true,
            isMinimumEnabled: false
        ),
        false,
        "Maximum field should not be editable when toggle is disabled"
    )

    assertEqual(
        isComplexExchangeFeeFieldEditable(
            field: .maximum,
            isPercentMode: false,
            isMinimumEnabled: true
        ),
        false,
        "Maximum field should not be editable for fixed fee"
    )
}

private func testValueFieldMovesThroughMinimumAndMaximumWhenBoundsAreEditable() {
    assertEqual(
        nextComplexExchangeFeeField(
            currentField: .value,
            isPercentMode: true,
            isMinimumEnabled: true
        ),
        .minimum,
        "Value field should move to minimum when it is editable"
    )

    assertEqual(
        nextComplexExchangeFeeField(
            currentField: .minimum,
            isPercentMode: true,
            isMinimumEnabled: true
        ),
        .maximum,
        "Minimum field should move to maximum when bounds are editable"
    )

    assertEqual(
        nextComplexExchangeFeeField(
            currentField: .value,
            isPercentMode: true,
            isMinimumEnabled: false
        ),
        nil,
        "Value field should finish when minimum is disabled"
    )

    assertEqual(
        nextComplexExchangeFeeField(
            currentField: .value,
            isPercentMode: false,
            isMinimumEnabled: true
        ),
        nil,
        "Value field should finish for fixed fee"
    )
}

private func testMaximumFieldAlwaysFinishesInput() {
    assertEqual(
        nextComplexExchangeFeeField(
            currentField: .maximum,
            isPercentMode: true,
            isMinimumEnabled: true
        ),
        nil,
        "Maximum field should not move focus further"
    )
}

@main
struct ComplexExchangeFeeFieldLogicTestsRunner {
    static func main() {
        testMinMaxFieldsAreEditableOnlyForEnabledPercentBounds()
        testValueFieldMovesThroughMinimumAndMaximumWhenBoundsAreEditable()
        testMaximumFieldAlwaysFinishesInput()
        print("ComplexExchangeFeeFieldLogic tests passed")
    }
}
