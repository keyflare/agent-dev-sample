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

private func testMainScreenNetworkBannerIsHiddenWhenMissing() {
    assertEqual(
        shouldShowMainScreenNetworkBanner(networkBanner: Optional<Int>.none),
        false,
        "Main screen should hide network banner when state has no banner"
    )
}

private func testMainScreenNetworkBannerIsShownWhenPresent() {
    assertEqual(
        shouldShowMainScreenNetworkBanner(networkBanner: Optional.some(1)),
        true,
        "Main screen should show network banner when state has a banner"
    )
}

private func testHiddenNetworkBannerKeepsOriginalGap() {
    assertEqual(
        mainScreenNetworkBannerTopSpacing(isBannerVisible: false),
        20,
        "Hidden network banner should keep the original gap between rates bar and cards"
    )
    assertEqual(
        mainScreenNetworkBannerBottomSpacing(isBannerVisible: false),
        0,
        "Hidden network banner should not add extra bottom spacing"
    )
}

private func testVisibleNetworkBannerUsesBannerSpacing() {
    assertEqual(
        mainScreenNetworkBannerTopSpacing(isBannerVisible: true),
        16,
        "Visible network banner should keep 16pt top spacing"
    )
    assertEqual(
        mainScreenNetworkBannerBottomSpacing(isBannerVisible: true),
        16,
        "Visible network banner should keep 16pt bottom spacing"
    )
}

@main
struct MainScreenLogicTestsRunner {
    static func main() {
        testMainScreenNetworkBannerIsHiddenWhenMissing()
        testMainScreenNetworkBannerIsShownWhenPresent()
        testHiddenNetworkBannerKeepsOriginalGap()
        testVisibleNetworkBannerUsesBannerSpacing()
        print("MainScreenLogic tests passed")
    }
}
