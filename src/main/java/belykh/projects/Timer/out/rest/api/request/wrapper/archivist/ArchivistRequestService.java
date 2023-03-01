package belykh.projects.Timer.out.rest.api.request.wrapper.archivist;

import belykh.projects.Timer.dto.TimerDto;

public interface ArchivistRequestService {
    String save(TimerDto timerDto);
}
