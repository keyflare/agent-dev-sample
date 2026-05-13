import CoreGraphics
import Foundation

private func assertEqual(
    _ actual: CGFloat,
    _ expected: CGFloat,
    _ message: String,
    file: StaticString = #filePath,
    line: UInt = #line
) {
    guard actual == expected else {
        fputs("Assertion failed: \(message). Expected \(expected), got \(actual)\n", stderr)
        exit(1)
    }
}

private func testFooterUsesScreenCornerRadiusWhenAvailable() {
    let radius = resolveComplexExchangeFooterBottomCornerRadius(
        screenCornerRadius: 55,
        safeAreaBottomInset: 34
    )

    assertEqual(
        radius,
        53,
        "Footer bottom radius should match the physical screen radius minus the outer inset"
    )
}

private func testFooterFallsBackToBaseCornerRadiusWithoutRoundedScreenInset() {
    let radius = resolveComplexExchangeFooterBottomCornerRadius(
        screenCornerRadius: nil,
        safeAreaBottomInset: 0
    )

    assertEqual(
        radius,
        28,
        "Footer should keep its default bottom radius on flat-bottom devices"
    )
}

private func testFooterFallsBackToSafeAreaInsetWhenScreenRadiusIsUnavailable() {
    let radius = resolveComplexExchangeFooterBottomCornerRadius(
        screenCornerRadius: nil,
        safeAreaBottomInset: 34
    )

    assertEqual(
        radius,
        42,
        "Footer should preserve the existing safe-area heuristic when screen radius is unavailable"
    )
}

private func testFooterClampsTinyScreenRadiusToZero() {
    let radius = resolveComplexExchangeFooterBottomCornerRadius(
        screenCornerRadius: 1,
        safeAreaBottomInset: 34
    )

    assertEqual(
        radius,
        0,
        "Footer bottom radius should never become negative after subtracting the outer inset"
    )
}

@main
struct ComplexExchangeFooterGeometryTestsRunner {
    static func main() {
        testFooterUsesScreenCornerRadiusWhenAvailable()
        testFooterFallsBackToBaseCornerRadiusWithoutRoundedScreenInset()
        testFooterFallsBackToSafeAreaInsetWhenScreenRadiusIsUnavailable()
        testFooterClampsTinyScreenRadiusToZero()
        print("ComplexExchangeFooterGeometry tests passed")
    }
}
