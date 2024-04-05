package redlib.backend.service;

import redlib.backend.dto.ClassTaskDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.ClassTaskQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.ClassTaskVO;

import java.util.List;

/**
 * 班级任务模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/13
 */

public interface ClassTaskService {

    List<Integer> getTaskAmount(String classId, Integer studentId);

    Page<ClassTaskVO> listTasksForStudent(ClassTaskQueryDTO queryDTO);

    /**
     * 列出符合条件的任务
     *
     * @return 任务列表
     */
    Page<ClassTaskDTO> listTasksByPage(ClassTaskQueryDTO queryDTO);

    /**
     * 获取指定的任务
     *
     * @return 任务
     */
    ClassTaskDTO getTask(ClassTaskDTO classTaskDTO);

    /**
     * 删除指定的任务
     *
     * @return 任务
     */
    void deleteTask(String taskId);

    /**
     * 添加任务
     *
     * @return
     */
    void addTask(ClassTaskDTO classTaskDTO);

    /**
     * 更新任务
     *
     * @return
     */
    void updateTask(ClassTaskDTO classTaskDTO);

    /**
     * 激活任务
     *
     * @return
     */
    void enableTask(ClassTaskDTO classTaskDTO);

    /**
     * 关闭任务
     *
     * @return
     */
    void disableTask(ClassTaskDTO classTaskDTO);
}
