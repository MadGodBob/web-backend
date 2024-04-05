package redlib.backend.dto;

import lombok.Data;

/**
 * @author 彭勃
 * @description
 * @date 2024/3/20 9:20
 */

@Data
public class PasswordDTO {

    private String oldPassword;

    private String newPassword;
}
