package com.bilgeadam.basurveyapp.exceptions;

import lombok.*;
import org.springframework.stereotype.Component;

/**
 * @author Eralp Nitelik
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class ExceptionResponse {
    private int exceptionCode;
    private String customMessage;
    private String exceptionMessage;
    private int httpStatus;
}
