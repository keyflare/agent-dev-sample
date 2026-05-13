import SwiftUI
import UIKit
import ExchangeShared

struct StackView<T: AnyObject, Content: View>: View {
    @StateValue
    var stackValue: ChildStack<AnyObject, T>

    var onBack: (_ toIndex: Int32) -> Void
    
    @ViewBuilder
    var childContent: (T) -> Content
    
    private var stack: [Child<AnyObject, T>] { stackValue.items }

    var body: some View {
        NavigationStack(
            path: Binding(
                get: { stack.dropFirst() },
                set: { updatedPath in onBack(Int32(updatedPath.count)) }
            )
        ) {
            let component = stack.first!.instance!
            
            childContent(component)
                .navigationDestination(for: Child<AnyObject, T>.self) {
                    childContent($0.instance!)
                }
        }
    }
}
