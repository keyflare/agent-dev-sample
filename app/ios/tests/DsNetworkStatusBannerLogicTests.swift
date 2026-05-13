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

private func testNetworkIssueBannerUsesNetworkSymbol() {
    assertEqual(
        dsNetworkStatusBannerSymbolName(for: .networkIssue),
        "wifi.exclamationmark",
        "Network issue banner should use network alert symbol"
    )
}

private func testAlertBannerUsesAlertSymbol() {
    assertEqual(
        dsNetworkStatusBannerSymbolName(for: .alert),
        "exclamationmark.triangle.fill",
        "Alert banner should use alert symbol"
    )
}

private func testInitialDisplayedBannerUsesIncomingBanner() {
    assertEqual(
        initialDisplayedNetworkStatusBanner(banner: Optional.some(1)),
        Optional.some(1),
        "Initial render should display an already available banner"
    )
}

private func testExpandedBannerContainerUsesNaturalHeight() {
    assertEqual(
        networkStatusBannerContainerHeight(isExpanded: true, naturalHeight: 72),
        72,
        "Expanded banner container should use measured natural height"
    )
}

private func testCollapsedBannerContainerUsesZeroHeight() {
    assertEqual(
        networkStatusBannerContainerHeight(isExpanded: false, naturalHeight: 72),
        0,
        "Collapsed banner container should animate toward zero height"
    )
}

private func testMeasuredHeightAnimatesWhileExpanded() {
    assertEqual(
        shouldAnimateNetworkStatusBannerMeasuredHeight(
            isExpanded: true,
            currentHeight: 0,
            newHeight: 72
        ),
        true,
        "First measured height should animate while banner is expanding"
    )
}

private func testMeasuredHeightDoesNotAnimateWhenUnchanged() {
    assertEqual(
        shouldAnimateNetworkStatusBannerMeasuredHeight(
            isExpanded: true,
            currentHeight: 72,
            newHeight: 72
        ),
        false,
        "Unchanged measured height should not start a new animation"
    )
}

@main
struct DsNetworkStatusBannerLogicTestsRunner {
    static func main() {
        testNetworkIssueBannerUsesNetworkSymbol()
        testAlertBannerUsesAlertSymbol()
        testInitialDisplayedBannerUsesIncomingBanner()
        testExpandedBannerContainerUsesNaturalHeight()
        testCollapsedBannerContainerUsesZeroHeight()
        testMeasuredHeightAnimatesWhileExpanded()
        testMeasuredHeightDoesNotAnimateWhenUnchanged()
        print("DsNetworkStatusBannerLogic tests passed")
    }
}
