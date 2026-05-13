import CoreGraphics
import Foundation

private func assertEqual(
    _ actual: CGFloat,
    _ expected: CGFloat,
    _ message: String,
    file: StaticString = #filePath,
    line: UInt = #line
) {
    guard abs(actual - expected) < 0.001 else {
        fputs("Assertion failed: \(message). Expected \(expected), got \(actual)\n", stderr)
        exit(1)
    }
}

private func testCentersDialogInFullScreenWhenKeypadIsHidden() {
    let result = complexExchangeFeeDialogVerticalOffset(
        contentGlobalMinY: 100,
        containerHeight: 700,
        keypadSheetTopGlobalY: nil
    )

    assertEqual(
        result,
        -50,
        "Dialog should compensate navigation bar area when keypad is hidden"
    )
}

private func testCentersDialogAboveCurrentKeypadTop() {
    let result = complexExchangeFeeDialogVerticalOffset(
        contentGlobalMinY: 100,
        containerHeight: 700,
        keypadSheetTopGlobalY: 500
    )

    assertEqual(
        result,
        -200,
        "Dialog should center between global top and current keypad top"
    )
}

private func testClampsKeypadTopToVisibleContainerBottom() {
    let result = complexExchangeFeeDialogVerticalOffset(
        contentGlobalMinY: 100,
        containerHeight: 700,
        keypadSheetTopGlobalY: 900
    )

    assertEqual(
        result,
        -50,
        "Dialog should not move below its hidden-keypad center"
    )
}

@main
struct ComplexExchangeFeeDialogLayoutLogicTestsRunner {
    static func main() {
        testCentersDialogInFullScreenWhenKeypadIsHidden()
        testCentersDialogAboveCurrentKeypadTop()
        testClampsKeypadTopToVisibleContainerBottom()
        print("ComplexExchangeFeeDialogLayoutLogic tests passed")
    }
}
