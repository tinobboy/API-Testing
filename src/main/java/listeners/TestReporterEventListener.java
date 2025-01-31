package listeners;

import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.util.ResultsUtils;

public class TestReporterEventListener implements TestLifecycleListener {

    @Override
    public void beforeTestStop(TestResult result) {
        result.getLabels().forEach(label -> {
            if (ResultsUtils.TAG_LABEL_NAME.equals(label.getName()) && "servicio no disponible: 503".equals(label.getValue())){
                result.setStatus(null);
            }
        });
    }
}
