package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ����:classes���ʵ����
 * @version
 * @author:  aaaaa
 * @����ʱ��: 2024-03-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassesQueryDTO extends PageQueryDTO{
    /**
     * 
     */
    private Integer id;

    /**
     * �༶id
     */
    private String classId;

    /**
     * �༶����
     */
    private String className;

    /**
     * ����
     */
    private String description;

}