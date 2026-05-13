package com.keyflare.exchange.core.analytics

public object A {
    private var agent: AnalyticsAgent = NoOpAnalyticsAgent

    public fun initialize(agent: AnalyticsAgent) {
        this.agent = agent
    }

    public object MainScreen {
        public fun trackOpened() {
            reportEvent("mainScreen.opened")
        }

        public fun trackSettingsClicked() {
            reportEvent("mainScreen.settings.clicked")
        }

        public fun trackDebugPanelClicked() {
            reportEvent("mainScreen.debugPanel.clicked")
        }

        public fun trackConverterClicked() {
            reportEvent("mainScreen.converter.clicked")
        }

        public fun trackExchangeClicked() {
            reportEvent("mainScreen.exchange.clicked")
        }

        public fun trackWidgetsClicked() {
            reportEvent("mainScreen.widgets.clicked")
        }

        public fun trackHistoryClicked() {
            reportEvent("mainScreen.history.clicked")
        }

        public fun trackHistoryRecordClicked(recordId: String) {
            reportEvent(
                event = "mainScreen.historyRecord.clicked",
                data = mapOf("recordId" to recordId),
            )
        }

        public fun trackRatesBarOpened(index: Int?) {
            reportEvent(
                event = "mainScreen.ratesBar.opened",
                data = mapOf("index" to index),
            )
        }

        public fun trackRatesBarClosed() {
            reportEvent("mainScreen.ratesBar.closed")
        }

        public fun trackRatesBarEditItemClicked(index: Int) {
            reportEvent(
                event = "mainScreen.ratesBar.editItem.clicked",
                data = mapOf("index" to index),
            )
        }

        public fun trackRatesBarChooseCurrencyClicked(isBase: Boolean) {
            reportEvent(
                event = "mainScreen.ratesBar.chooseCurrency.clicked",
                data = mapOf("isBase" to isBase),
            )
        }

        public fun trackRatesBarSwapClicked() {
            reportEvent("mainScreen.ratesBar.swap.clicked")
        }
    }

    public object ComplexExchange {
        public fun trackOpened(mode: String) {
            reportEvent(
                event = "complexExchange.opened",
                data = mapOf("mode" to mode),
            )
        }

        public fun trackAddStepClicked(stepsCount: Int) {
            reportEvent(
                event = "complexExchange.addStep.clicked",
                data = mapOf("stepsCount" to stepsCount),
            )
        }

        public fun trackDraftFromCurrencyClicked() {
            reportEvent("complexExchange.draft.fromCurrency.clicked")
        }

        public fun trackDraftToCurrencyClicked() {
            reportEvent("complexExchange.draft.toCurrency.clicked")
        }

        public fun trackDraftFieldToggled(
            field: String,
            selected: Boolean,
            selectedFieldsCount: Int,
        ) {
            reportEvent(
                event = "complexExchange.draft.field.toggled",
                data = mapOf(
                    "field" to field,
                    "selected" to selected,
                    "selectedFieldsCount" to selectedFieldsCount,
                ),
            )
        }

        public fun trackDraftCancelClicked(stepsCount: Int) {
            reportEvent(
                event = "complexExchange.draft.cancel.clicked",
                data = mapOf("stepsCount" to stepsCount),
            )
        }

        public fun trackDraftApplied(
            stepIndex: Int,
            stepsCount: Int,
            mode: String,
            from: String,
            to: String,
        ) {
            reportEvent(
                event = "complexExchange.draft.applied",
                data = mapOf(
                    "stepIndex" to stepIndex,
                    "stepsCount" to stepsCount,
                    "mode" to mode,
                    "from" to from,
                    "to" to to,
                ),
            )
        }

        public fun trackStepDeleted(
            stepIndex: Int,
            stepsCount: Int,
        ) {
            reportEvent(
                event = "complexExchange.step.deleted",
                data = mapOf(
                    "stepIndex" to stepIndex,
                    "stepsCount" to stepsCount,
                ),
            )
        }

        public fun trackStepFromCurrencyClicked(stepIndex: Int) {
            reportEvent(
                event = "complexExchange.step.fromCurrency.clicked",
                data = mapOf("stepIndex" to stepIndex),
            )
        }

        public fun trackStepToCurrencyClicked(stepIndex: Int) {
            reportEvent(
                event = "complexExchange.step.toCurrency.clicked",
                data = mapOf("stepIndex" to stepIndex),
            )
        }

        public fun trackHistorySaved(
            stepsCount: Int,
            hasNote: Boolean,
            from: String,
            to: String,
            isGain: Boolean,
        ) {
            reportEvent(
                event = "complexExchange.history.saved",
                data = mapOf(
                    "stepsCount" to stepsCount,
                    "hasNote" to hasNote,
                    "from" to from,
                    "to" to to,
                    "isGain" to isGain,
                ),
            )
        }
    }

    public object ExchangeHistory {
        public fun trackOpened() {
            reportEvent("exchangeHistory.opened")
        }

        public fun trackBackClicked() {
            reportEvent("exchangeHistory.back.clicked")
        }

        public fun trackSearchUsed(recordsCount: Int) {
            reportEvent(
                event = "exchangeHistory.search.used",
                data = mapOf("recordsCount" to recordsCount),
            )
        }

        public fun trackRecordClicked(
            recordId: String,
            fromAsset: String,
            toAsset: String,
            stepsCount: Int,
        ) {
            reportEvent(
                event = "exchangeHistory.record.clicked",
                data = mapOf(
                    "recordId" to recordId,
                    "fromAsset" to fromAsset,
                    "toAsset" to toAsset,
                    "stepsCount" to stepsCount,
                ),
            )
        }

        public fun trackRecordDeleted(
            recordId: String,
            fromAsset: String,
            toAsset: String,
        ) {
            reportEvent(
                event = "exchangeHistory.record.deleted",
                data = mapOf(
                    "recordId" to recordId,
                    "fromAsset" to fromAsset,
                    "toAsset" to toAsset,
                ),
            )
        }

        public fun trackRecordDeletionCanceled(recordId: String) {
            reportEvent(
                event = "exchangeHistory.recordDeletion.canceled",
                data = mapOf("recordId" to recordId),
            )
        }
    }

    public object Converter {
        public fun trackOpened(
            fromRef: String,
            fromType: String,
            toRef: String,
            toType: String,
        ) {
            reportEvent(
                event = "converter.opened",
                data = pairData(
                    fromRef = fromRef,
                    fromType = fromType,
                    toRef = toRef,
                    toType = toType,
                ),
            )
        }

        public fun trackBackClicked() {
            reportEvent("converter.back.clicked")
        }

        public fun trackFromAssetClicked(
            fromRef: String,
            fromType: String,
            toRef: String,
            toType: String,
        ) {
            reportEvent(
                event = "converter.fromAsset.clicked",
                data = pairData(
                    fromRef = fromRef,
                    fromType = fromType,
                    toRef = toRef,
                    toType = toType,
                ),
            )
        }

        public fun trackToAssetClicked(
            fromRef: String,
            fromType: String,
            toRef: String,
            toType: String,
        ) {
            reportEvent(
                event = "converter.toAsset.clicked",
                data = pairData(
                    fromRef = fromRef,
                    fromType = fromType,
                    toRef = toRef,
                    toType = toType,
                ),
            )
        }

        public fun trackFromAssetChanged(
            oldRef: String,
            oldType: String,
            newRef: String,
            newType: String,
            toRef: String,
            toType: String,
        ) {
            reportEvent(
                event = "converter.fromAsset.changed",
                data = mapOf(
                    "oldRef" to oldRef,
                    "oldType" to oldType,
                    "newRef" to newRef,
                    "newType" to newType,
                    "toRef" to toRef,
                    "toType" to toType,
                ),
            )
        }

        public fun trackToAssetChanged(
            fromRef: String,
            fromType: String,
            oldRef: String,
            oldType: String,
            newRef: String,
            newType: String,
        ) {
            reportEvent(
                event = "converter.toAsset.changed",
                data = mapOf(
                    "fromRef" to fromRef,
                    "fromType" to fromType,
                    "oldRef" to oldRef,
                    "oldType" to oldType,
                    "newRef" to newRef,
                    "newType" to newType,
                ),
            )
        }

        public fun trackAssetsSwapped(
            fromRef: String,
            fromType: String,
            toRef: String,
            toType: String,
        ) {
            reportEvent(
                event = "converter.assets.swapped",
                data = pairData(
                    fromRef = fromRef,
                    fromType = fromType,
                    toRef = toRef,
                    toType = toType,
                ),
            )
        }

        public fun trackAmountInputStarted() {
            reportEvent("converter.amountInput.started")
        }

        public fun trackCalculatorEqualsClicked() {
            reportEvent("converter.calculator.equals.clicked")
        }

        private fun pairData(
            fromRef: String,
            fromType: String,
            toRef: String,
            toType: String,
        ): Map<String?, Any?> = mapOf(
            "fromRef" to fromRef,
            "fromType" to fromType,
            "toRef" to toRef,
            "toType" to toType,
        )
    }

    public object CurrencyChooser {
        public fun trackOpened(source: String) {
            reportEvent(
                event = "currencyChooser.opened",
                data = mapOf("source" to source),
            )
        }

        public fun trackSearchPerformed(
            source: String,
            queryLength: Int,
            fiatResultsCount: Int,
            cryptoResultsCount: Int,
            success: Boolean,
        ) {
            reportEvent(
                event = "currencyChooser.search.performed",
                data = mapOf(
                    "source" to source,
                    "queryLength" to queryLength,
                    "fiatResultsCount" to fiatResultsCount,
                    "cryptoResultsCount" to cryptoResultsCount,
                    "success" to success,
                ),
            )
        }

        public fun trackCurrencySelected(
            source: String,
            assetType: String,
            assetId: String,
            assetSymbol: String,
            wasAlreadySelected: Boolean,
            searchActive: Boolean,
        ) {
            reportEvent(
                event = "currencyChooser.currency.selected",
                data = mapOf(
                    "source" to source,
                    "assetType" to assetType,
                    "assetId" to assetId,
                    "assetSymbol" to assetSymbol,
                    "wasAlreadySelected" to wasAlreadySelected,
                    "searchActive" to searchActive,
                ),
            )
        }

        public fun trackBackClicked(source: String) {
            reportEvent(
                event = "currencyChooser.back.clicked",
                data = mapOf("source" to source),
            )
        }
    }

    public fun reportError(
        message: String,
        error: Throwable? = null,
    ) {
        agent.reportError(message, error)
    }

    private fun reportEvent(
        event: String,
        data: Map<String?, Any?>? = null,
    ) {
        agent.reportEvent(event, data)
    }
}
