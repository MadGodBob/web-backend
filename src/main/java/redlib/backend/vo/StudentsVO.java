package redlib.backend.vo;

import lombok.Data;

/**
 * 描述:
 * @version
 * @author:  彭勃
 * @创建时间: 2024-03-11
 */
@Data
public class StudentsVO {

    private String taskId;

    private String name;

    private Integer studentid;

    private String classId;

    private String className;

    private String answer;

    private int current;

    private int pageSize;

}