import Foundation
import ExchangeShared

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

private func testUnselectedToggleIconUsesDefaultAccentForEveryField() {
    let fields: [StepField] = [.sent, .received, .rate]

    fields.forEach { field in
        assertEqual(
            complexExchangeDraftToggleIconAccent(field: field, isSelected: false),
            .default,
            "Unselected draft toggle icon should use the default accent"
        )
    }
}

private func testSelectedToggleIconAccentDependsOnField() {
    assertEqual(
        complexExchangeDraftToggleIconAccent(field: .sent, isSelected: true),
        .sent,
        "Selected sent draft toggle icon should use the sent accent"
    )
    assertEqual(
        complexExchangeDraftToggleIconAccent(field: .received, isSelected: true),
        .received,
        "Selected received draft toggle icon should use the received accent"
    )
    assertEqual(
        complexExchangeDraftToggleIconAccent(field: .rate, isSelected: true),
        .rate,
        "Selected rate draft toggle icon should use the rate accent"
    )
}

@main
struct ComplexExchangeDraftToggleStyleTestsRunner {
    static func main() {
        testUnselectedToggleIconUsesDefaultAccentForEveryField()
        testSelectedToggleIconAccentDependsOnField()
        print("ComplexExchangeDraftToggleStyle tests passed")
    }
}
