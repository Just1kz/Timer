package belykh.projects.Timer.exceptio_handling;

import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j
public class GlobalExceptionHandler {

    /**
     * Обработчик исключений - когда потребитель пытается остановить не запущенный таймер
     * @param exception исключение
     * @return обработанный ответ исключения отдаем как результат запроса
     */
    @ExceptionHandler
    public ResponseEntity<IncorrectData> handleException(Exception exception) {
        IncorrectData incorrectData = new IncorrectData();
        incorrectData.setInfo(exception.getMessage());
        log.error(exception.getMessage());
        return new ResponseEntity<>(incorrectData, HttpStatus.BAD_REQUEST);
    }
}
