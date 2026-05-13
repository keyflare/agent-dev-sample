import Foundation
import CoreGraphics

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

private func testOperationButtonsUseSymbolIcons() {
    assertEqual(
        converterButtonIcon(for: "/"),
        .divide,
        "Divide operation should use an icon"
    )
    assertEqual(
        converterButtonIcon(for: "*"),
        .multiply,
        "Multiply operation should use the close icon"
    )
    assertEqual(
        converterButtonIcon(for: "-"),
        .minus,
        "Minus operation should use an icon"
    )
    assertEqual(
        converterButtonIcon(for: "+"),
        .plus,
        "Plus operation should use an icon"
    )
    assertEqual(
        converterButtonIcon(for: "="),
        .equal,
        "Equals operation should use an icon"
    )
}

private func testSwapCurrenciesButtonUsesSymbolIcon() {
    assertEqual(
        converterButtonIcon(for: "⇄"),
        .swapCurrencies,
        "Swap currencies action should use an icon"
    )
}

private func testButtonIconsUseStableSymbolNames() {
    assertEqual(
        ConverterButtonIcon.swapCurrencies.systemImageName,
        "arrow.up.arrow.down",
        "Swap currencies action should use the vertical swap SF Symbol"
    )
    assertEqual(
        ConverterButtonIcon.divide.systemImageName,
        "divide",
        "Divide operation should use the divide SF Symbol"
    )
    assertEqual(
        ConverterButtonIcon.multiply.systemImageName,
        "xmark",
        "Multiply operation should use the close SF Symbol"
    )
    assertEqual(
        ConverterButtonIcon.minus.systemImageName,
        "minus",
        "Minus operation should use the minus SF Symbol"
    )
    assertEqual(
        ConverterButtonIcon.plus.systemImageName,
        "plus",
        "Plus operation should use the plus SF Symbol"
    )
    assertEqual(
        ConverterButtonIcon.equal.systemImageName,
        "equal",
        "Equals operation should use the equal SF Symbol"
    )
}

private func testButtonIconsUseOpticalPointSizes() {
    assertEqual(
        ConverterButtonIcon.divide.pointSize,
        34,
        "Divide icon should keep the baseline operation icon size"
    )
    assertEqual(
        ConverterButtonIcon.multiply.pointSize,
        30,
        "Multiply icon should be optically smaller than plus and divide"
    )
    assertEqual(
        ConverterButtonIcon.minus.pointSize,
        34,
        "Minus icon should keep the baseline operation icon size"
    )
    assertEqual(
        ConverterButtonIcon.plus.pointSize,
        34,
        "Plus icon should keep the baseline operation icon size"
    )
    assertEqual(
        ConverterButtonIcon.equal.pointSize,
        34,
        "Equals icon should keep the baseline operation icon size"
    )
    assertEqual(
        ConverterButtonIcon.swapCurrencies.pointSize,
        28,
        "Swap currencies icon should be optically smaller than operation icons"
    )
}

private func testButtonIconsUseMediumWeight() {
    assertEqual(
        ConverterButtonIcon.divide.weight,
        .medium,
        "Button icons should render with a denser SF Symbol weight"
    )
    assertEqual(
        ConverterButtonIcon.multiply.weight,
        .medium,
        "Button icons should render with a denser SF Symbol weight"
    )
    assertEqual(
        ConverterButtonIcon.swapCurrencies.weight,
        .medium,
        "Button icons should render with a denser SF Symbol weight"
    )
}

private func testNonOperationButtonsKeepText() {
    assertEqual(
        converterButtonIcon(for: "7"),
        nil,
        "Digit buttons should keep text"
    )
    assertEqual(
        converterButtonIcon(for: "C"),
        nil,
        "Action buttons should keep text"
    )
}

@main
struct ConverterScreenLogicTestsRunner {
    static func main() {
        testOperationButtonsUseSymbolIcons()
        testSwapCurrenciesButtonUsesSymbolIcon()
        testButtonIconsUseStableSymbolNames()
        testButtonIconsUseOpticalPointSizes()
        testButtonIconsUseMediumWeight()
        testNonOperationButtonsKeepText()
        print("ConverterScreenLogic tests passed")
    }
}
