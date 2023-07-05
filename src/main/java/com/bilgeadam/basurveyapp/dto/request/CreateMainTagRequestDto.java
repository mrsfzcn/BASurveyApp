package com.bilgeadam.basurveyapp.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateMainTagRequestDto {
    List<String> tagClass;
    String tagName;
}
