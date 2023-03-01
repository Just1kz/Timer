package belykh.projects.Timer.service.impl;

import belykh.projects.Timer.dto.TimerDto;
import belykh.projects.Timer.out.rest.api.request.wrapper.archivist.ArchivistRequestService;
import belykh.projects.Timer.service.TimerService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Log4j
public class TimerServiceImpl implements TimerService {

    private final ArchivistRequestService archivistRequestService;

    private final Map<String, Long> data = new ConcurrentHashMap<>();
    private static final String THREAD_PATTERN_INFO = "ThreadInfo: name = %s, id = %s, state = %s";

    @Autowired
    public TimerServiceImpl(ArchivistRequestService archivistRequestService) {
        this.archivistRequestService = archivistRequestService;
    }

    @Override
    public synchronized void startTimer(String keyObject) {
        logThreadInfo(null);
        data.put(keyObject, System.currentTimeMillis());
    }

    @Override
    public synchronized String stopTimer(String keyObject) {
        logThreadInfo(null);
        Long end = System.currentTimeMillis();
        Long start = data.remove(keyObject);

        if (start == null) {
            String info = "Таймер по запрашиваемому ключу не запущен, keyObject = %s! Сначала запустите таймер!";
            throw new IllegalArgumentException(String.format(info, keyObject));
        }

        String rsl = calcTimeAndFormat(start, end);

        saveTimerToDb(keyObject, start, end);

        logThreadInfo(" ------- " + rsl);
        return rsl;
    }

    private synchronized void saveTimerToDb(String keyObject, Long start, Long end) {
        logThreadInfo(null);
        TimerDto dto = new TimerDto(keyObject, getTimeStringByDataAndPattern(start, "yyyy-MM-dd'T'HH:mm:ss"), end - start);

        String rsl = archivistRequestService.save(dto);
        log.debug(rsl);
    }

    @Override
    public synchronized Map<String, String> getActiveTimers() {
        logThreadInfo(null);

        long endTimeForAll = System.currentTimeMillis();

        Map<String, String> rslData = new HashMap<>();

        data.forEach((key, value) -> rslData.put(key, calcTimeAndFormat(value, endTimeForAll)));

        return rslData;
    }

    @Override
    public void resetAllTimers() {
       logThreadInfo(null);

       data.clear();
    }

    public static String getTimeStringByDataAndPattern(Long data, String pattern) {
        Date date = new Date(data);
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));

        return dateFormat.format(date);
    }

    private String calcTimeAndFormat(Long start, Long end) {
        long totalTime = end - start;
        int seconds = (int) (totalTime / 1000) % 60 ;
        int minutes = (int) ((totalTime / (1000*60)) % 60);
        int hours   = (int) ((totalTime / (1000*60*60)) % 24);
        String pattern = "%s часов, %s минут, %s секунд";
        return String.format(pattern, hours, minutes, seconds);
    }

    private void logThreadInfo(String dopInfo) {
        Thread currentThread = Thread.currentThread();
        if (dopInfo == null) {
            log.debug(String.format(THREAD_PATTERN_INFO, currentThread.getName(), currentThread.getId(), currentThread.getState()));
        } else {
            log.debug(String.format(THREAD_PATTERN_INFO, currentThread.getName(), currentThread.getId(), currentThread.getState()) + dopInfo);
        }
    }
}
