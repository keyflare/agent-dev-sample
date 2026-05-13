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

private func testFirstFocusedStepRequestsScroll() {
    assertEqual(
        shouldScrollToFocusedStep(
            currentScrolledStepId: nil,
            newlyFocusedStepId: 101
        ),
        true,
        "First focused step should request a scroll"
    )
}

private func testAnotherFieldInSameStepDoesNotRequestScroll() {
    assertEqual(
        shouldScrollToFocusedStep(
            currentScrolledStepId: 101,
            newlyFocusedStepId: 101
        ),
        false,
        "Changing focus inside the same step should not re-scroll"
    )
}

private func testDifferentFocusedStepRequestsScroll() {
    assertEqual(
        shouldScrollToFocusedStep(
            currentScrolledStepId: 101,
            newlyFocusedStepId: 202
        ),
        true,
        "Moving focus to a different step should request a scroll"
    )
}

private func testMissingFocusedStepDoesNotRequestScroll() {
    assertEqual(
        shouldScrollToFocusedStep(
            currentScrolledStepId: 101,
            newlyFocusedStepId: nil
        ),
        false,
        "Losing focus entirely should not request a scroll"
    )
}

private func testKeyboardAppearanceRetriesFocusedStepScroll() {
    assertEqual(
        focusedStepScrollMode(
            currentScrolledStepId: 101,
            newlyFocusedStepId: 101
        ),
        .pinToTop,
        "Focusing another field in the same step should pin the same card back to the top"
    )
}

private func testKeyboardChangeDoesNotRetryWithoutFocusedStep() {
    assertEqual(
        focusedStepScrollMode(
            currentScrolledStepId: 101,
            newlyFocusedStepId: 202
        ),
        .animateToTop,
        "Focusing a different step should animate that new card to the top"
    )
}

private func testKeyboardHeightUpdateDoesNotRetryWhenAlreadyVisible() {
    assertEqual(
        focusedStepScrollMode(
            currentScrolledStepId: 101,
            newlyFocusedStepId: nil
        ),
        nil,
        "Missing focus target should not produce any scroll mode"
    )
}

private func testReadOnlyStepHidesDeleteLabel() {
    assertEqual(
        deleteStepLabelOpacity(isReadOnly: true),
        0,
        "Read-only step should keep delete label fully transparent"
    )
}

private func testEditableStepShowsDeleteLabel() {
    assertEqual(
        deleteStepLabelOpacity(isReadOnly: false),
        1,
        "Editable step should keep delete label fully visible"
    )
}

@main
struct ComplexExchangeStepScrollLogicTestsRunner {
    static func main() {
        testFirstFocusedStepRequestsScroll()
        testAnotherFieldInSameStepDoesNotRequestScroll()
        testDifferentFocusedStepRequestsScroll()
        testMissingFocusedStepDoesNotRequestScroll()
        testKeyboardAppearanceRetriesFocusedStepScroll()
        testKeyboardChangeDoesNotRetryWithoutFocusedStep()
        testKeyboardHeightUpdateDoesNotRetryWhenAlreadyVisible()
        testReadOnlyStepHidesDeleteLabel()
        testEditableStepShowsDeleteLabel()
        print("ComplexExchangeStepScrollLogic tests passed")
    }
}
