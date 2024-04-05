package redlib.backend.dto;

import lombok.Data;

/**
 * 描述:
 * @version
 * @author:  彭勃
 * @创建时间: 2024-03-11
 */
@Data
public class StudentsDTO {

    private Integer id;

    private String name;

    private Integer studentid;

    private String classId;

    private String className;

    private int submitted;

    private int unSubmitted;
}