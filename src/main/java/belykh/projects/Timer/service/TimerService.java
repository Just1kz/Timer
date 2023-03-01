package belykh.projects.Timer.service;

import java.util.Map;

public interface TimerService {

    void startTimer(String keyObject);

    String stopTimer(String keyObject);

    Map<String, String> getActiveTimers();

    void resetAllTimers();
}
