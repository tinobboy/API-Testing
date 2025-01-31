package listeners;

import io.qameta.allure.Attachment;
import io.qameta.allure.listener.StepLifecycleListener;

import java.util.logging.Logger;

public class StepReporterEventListener implements StepLifecycleListener
{
    private static final Logger LOGGER = Logger.getLogger(String.valueOf(StepReporterEventListener.class));

    @Attachment(value = "{name}", type = "text/plain")
    public static String attachFile(String name, String data) {
        return data;
    }
}
