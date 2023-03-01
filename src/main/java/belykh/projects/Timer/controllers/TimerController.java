package belykh.projects.Timer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import belykh.projects.Timer.service.TimerService;

import java.util.Map;

/**
 * Обрабатывает запросы для работы с таймером по разным ключам
 * @author Belykh.A.S.
 * @version 1.0.
 */
@RestController
@RequestMapping("/timer")
public class TimerController {

    private final TimerService timerService;

    @Autowired
    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping("/start")
    public String startTimer(@RequestBody String keyObject) {
        timerService.startTimer(keyObject);
        return "Timer for " + keyObject + " started!";
    }

    @PostMapping("/stop")
    public String stopTimer(@RequestBody String keyObject) {
        return timerService.stopTimer(keyObject);
    }

    @GetMapping("/activeTimers")
    public Map<String, String> getActiveTimers() {
        return timerService.getActiveTimers();
    }

    @DeleteMapping("/resetAll")
    public String resetAllTimers() {
        timerService.resetAllTimers();
        return "All Timers are reset";
    }
}
