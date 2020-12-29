import io.opentelemetry.api.common.Labels;
import io.opentelemetry.api.metrics.GlobalMetricsProvider;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.exporter.logging.LoggingMetricExporter;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.IntervalMetricReader;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static java.util.stream.IntStream.range;

public class Main {


    public static void main(String[] args) throws InterruptedException {
        final IntervalMetricReader intervalMetricReader = setupMetricsReader();

        range(0, 10).forEach((i) -> {
            final LongCounter recorder = GlobalMetricsProvider.get().get("test")
                    .longCounterBuilder("counter-name")
                    .build();

            final Labels labels = Labels.builder()
                    .put("spanId", randomUUID().toString())
                    .build();

            recorder.add(1, labels);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        });

        Thread.sleep(20000);
        intervalMetricReader.shutdown();
    }

    private static IntervalMetricReader setupMetricsReader() {
        final SdkMeterProvider sdkMeterProvider = (SdkMeterProvider) GlobalMetricsProvider.get();
        return IntervalMetricReader.builder()
                .setExportIntervalMillis(500)
                .setMetricProducers(singletonList(sdkMeterProvider.getMetricProducer()))
                .setMetricExporter(new LoggingMetricExporter()).build();
    }
}
