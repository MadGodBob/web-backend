package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * ����:class_task���ʵ����
 * @version
 * @author:  aaaaa
 * @����ʱ��: 2024-03-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassTaskQueryDTO extends PageQueryDTO{
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
     * ��ҵ����id
     */
    private String taskId;

    /**
     * ��ҵ����
     */
    private String taskDescription;

    /**
     * ״̬
     */
    private String taskState;

    private Integer studentId;

}