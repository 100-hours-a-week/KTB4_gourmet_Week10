package KTB4_gourmet_Week10.Assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResultDto {

    private LoginResponseDto response;

    private String accessToken;

    private String refreshToken;
}