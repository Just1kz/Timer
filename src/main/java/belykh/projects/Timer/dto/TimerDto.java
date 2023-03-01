package belykh.projects.Timer.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Data
@Builder
public class TimerDto implements Serializable {

    static final long serialVersionUID = 1L;

    private String typeObject;

    private String startDate;

    private Long totalTime;
}
