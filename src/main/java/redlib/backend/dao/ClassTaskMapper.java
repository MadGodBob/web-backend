package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.ClassTaskDTO;
import redlib.backend.dto.query.ClassTaskQueryDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.ClassTask;
import redlib.backend.model.Students;

import java.util.List;

public interface ClassTaskMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClassTask record);

    ClassTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(ClassTask record);

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(ClassTaskQueryDTO queryDTO);

    /**
     * 根据查询条件获取任务列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 任务列表
     */
    List<ClassTask> list(@Param("queryDTO") ClassTaskQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 改变任务状态ON或OFF
     *
     * @param id ID
     * @param state 状态
     * @return 命中数量
     */
    void updateState(@Param("ID") Integer id, @Param("state") String state);

    /**
     * 获取指定taskId任务的状态
     *
     * @return 状态
     */
    String getState(@Param("taskId") String taskId);
}