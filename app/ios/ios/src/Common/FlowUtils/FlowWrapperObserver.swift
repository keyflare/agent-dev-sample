//
//  FlowWrapperObserver.swift
//  ios
//
//  Created by Dmitrii Semenov on 15.12.2024.
//

import Foundation
import ExchangeShared

class FlowWrapperObserver<T: AnyObject>: ObservableObject {
    @Published var value: T

    private var flowWrapper: FlowWrapper<T>

    init(stateFlow: Kotlinx_coroutines_coreStateFlow) {
        self.flowWrapper = FlowWrapper(stateFlow: stateFlow)
        self.value = flowWrapper.value
        observe()
    }

    deinit {
        cancel()
    }

    private func observe() {
        flowWrapper.observe(
            onEach: { [weak self] newValue in
                DispatchQueue.main.async {
                    self?.value = newValue
                }
            },
            onComplete: { cause in
                if let error = cause {
                    NSLog("FlowWrapper observation completed with error: \(error)")
                } else {
                    NSLog("FlowWrapper observation completed successfully.")
                }
            }
        )
    }

    func cancel() {
        flowWrapper.cancel()
    }
}
