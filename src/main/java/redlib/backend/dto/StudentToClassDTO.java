package redlib.backend.dto;

import lombok.Data;

import java.util.List;

/**
 * @author 彭勃
 * @description
 * @date 2024/3/10
 */
@Data
public class StudentToClassDTO {
    private List<Integer> ids;

    private ClassesDTO classesDTO;
}
