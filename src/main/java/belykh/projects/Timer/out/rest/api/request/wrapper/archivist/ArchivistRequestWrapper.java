package belykh.projects.Timer.out.rest.api.request.wrapper.archivist;

import belykh.projects.Timer.dto.TimerDto;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Log4j
public class ArchivistRequestWrapper implements ArchivistRequestService {
    @Value("${archivist.rest.api.url}")
    private String archivistApiMainUrl;
    private static final String RESPONSE_THEN_ERROR_REQUEST = "При сохранении данных в БД возникла ошибка = %s";

    @Override
    public String save(TimerDto timerDto) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        String errorMessage = StringUtils.EMPTY;

        try {
            HttpEntity<TimerDto> requestBody = new HttpEntity<>(timerDto);
            responseEntity = restTemplate.postForEntity(archivistApiMainUrl + ArchivistApiEndPoints.SAVE.getCommandPath(), requestBody, String.class);
            log.debug("Запрос сохранения сущности с entity: " + requestBody + "rsl = " + responseEntity.getBody());
        } catch (RestClientException ex) {
            log.debug(RESPONSE_THEN_ERROR_REQUEST);
            ex.printStackTrace();
            errorMessage = ex.getMessage();
        }

        if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
            return String.format(RESPONSE_THEN_ERROR_REQUEST, errorMessage);
        }

        return responseEntity.getBody();
    }
}
