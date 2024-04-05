package redlib.backend.model;

import lombok.Data;

/**
 * 描述:students表的实体类
 * @version
 * @author:  aaaaa
 * @创建时间: 2024-03-12
 */
@Data
public class Students {
    /**
     * 
     */
    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 学号
     */
    private Integer studentid;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 班级名称
     */
    private String className;
}