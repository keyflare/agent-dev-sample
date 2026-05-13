//
//  AppDelegate.swift
//  ios
//
//  Created by Dmitrii Semenov on 15.12.2024.
//

import Foundation
import UIKit
import ExchangeShared
import AppMetricaCore
import AppMetricaCrashes

class AppDelegate: NSObject, UIApplicationDelegate {
    let exchangeApp: ExchangeApp
    let rootComponent: ExchangeRootComponent

    override init() {
        let analyticsAgent = Self.createAnalyticsAgent()

        exchangeApp = ExchangeApp(
            platformDependencies: ExchangePlatformDependencies(
                dataStorePlatform: Data_storeDataStorePlatform(),
                buildType: Self.appBuildType,
                analyticsAgent: analyticsAgent,
                platformServices: PlatformServices(
                    shareHelper: ShareHelperIos(),
                    hapticSystemSettingsProvider: HapticSystemSettingsProviderIos(),
                    hapticDelegate: HapticDelegateIos(),
                    urlOpener: UrlOpenerIos(),
                    emailHelper: EmailHelperIos()
                ),
                openNetworkLogs: nil
            )
        )
        rootComponent = exchangeApp.onPlatformCreate(
            componentContext: DefaultComponentContext(
                lifecycle: ApplicationLifecycle()
            )
        )
    }

    func application(
        _ app: UIApplication,
        open url: URL,
        options: [UIApplication.OpenURLOptionsKey : Any] = [:]
    ) -> Bool {
        return handleDebugStageURL(url)
    }

    func handleDebugStageURL(_ url: URL) -> Bool {
        #if DEBUG
        guard url.scheme == "exchange-debug" else { return false }
        guard url.host == "network" else { return false }
        guard url.path == "/stage" else { return false }

        guard
            let components = URLComponents(url: url, resolvingAgainstBaseURL: false),
            let host = components.queryItems?.first(where: { $0.name == "host" })?.value,
            let portString = components.queryItems?.first(where: { $0.name == "port" })?.value,
            let port = Int32(portString)
        else {
            return false
        }

        return true
        #else
        return false
        #endif
    }

    private static func createAnalyticsAgent() -> AnalyticsAgent {
        guard
            let apiKey = Bundle.main.object(forInfoDictionaryKey: "AppMetricaApiKey") as? String,
            !apiKey.isEmpty,
            !apiKey.hasPrefix("$("),
            let configuration = AppMetricaConfiguration(apiKey: apiKey)
        else {
            return NoOpIosAnalyticsAgent()
        }

        #if DEBUG
        configuration.areLogsEnabled = true
        #endif

        AppMetrica.activate(with: configuration)
        return IosAppMetricaAnalyticsAgent()
    }

    private static var appBuildType: UtilsAppBuildType {
        #if DEBUG
        return UtilsAppBuildType.debug
        #else
        return UtilsAppBuildType.production
        #endif
    }
}

private final class NoOpIosAnalyticsAgent: AnalyticsAgent {
    func reportEvent(event: String, data: [AnyHashable : Any]?) {}

    func reportError(message: String, error: KotlinThrowable?) {}
}

private final class IosAppMetricaAnalyticsAgent: AnalyticsAgent {
    func reportEvent(event: String, data: [AnyHashable : Any]?) {
        AppMetrica.reportEvent(name: event, parameters: data)
    }

    func reportError(message: String, error: KotlinThrowable?) {
        let nsError = NSError(
            domain: "kotlin.native.exception",
            code: 1,
            userInfo: [
                NSLocalizedDescriptionKey: message,
                "KotlinThrowable": error?.description() ?? "nil",
            ],
        )
        AppMetricaCrashes.crashes().report(nserror: nsError)
    }
}
