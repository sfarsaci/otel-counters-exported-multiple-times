import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.common.InstrumentType;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.view.AggregationConfiguration;
import io.opentelemetry.sdk.metrics.view.Aggregations;
import io.opentelemetry.sdk.metrics.view.InstrumentSelector;

public class MeterProviderFactory implements io.opentelemetry.spi.metrics.MeterProviderFactory {
    @Override
    public MeterProvider create() {
        InstrumentSelector instrumentSelector = InstrumentSelector.newBuilder()
                .instrumentType(InstrumentType.COUNTER)
                .build();

        AggregationConfiguration viewSpecification =
                AggregationConfiguration.create(Aggregations.sum(), MetricData.AggregationTemporality.DELTA);

        SdkMeterProvider sdkMeterProvider = SdkMeterProvider.builder().build();
        sdkMeterProvider.registerView(instrumentSelector, viewSpecification);

        return sdkMeterProvider;
    }
}
