package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

/**
 * 描述:
 * @version
 * @author:  彭勃
 * @创建时间: 2024-03-11
 */
@Data
public class ClassesVO {

    private Integer id;

    private String classId;

    private String className;

    private String description;

    private Date createdAt;

    private String createdBy;

    private Integer sum;

}