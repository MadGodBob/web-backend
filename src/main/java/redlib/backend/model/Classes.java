package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:classes表的实体类
 * @version
 * @author:  aaaaa
 * @创建时间: 2024-03-12
 */
@Data
public class Classes {
    /**
     * 
     */
    private Integer id;

    /**
     * 班级id
     */
    private String classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;
}