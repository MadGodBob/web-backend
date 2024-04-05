package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.dto.query.SubmissionQueryDTO;
import redlib.backend.model.Students;
import redlib.backend.model.Submission;

import java.util.List;

public interface SubmissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Submission record);

    Submission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeyWithBLOBs(Submission record);

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(SubmissionQueryDTO queryDTO);

    /**
     * 根据查询条件获取提交列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 学生列表
     */
    List<Submission> list(@Param("queryDTO") SubmissionQueryDTO queryDTO, @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 清空提交列表
     *
     */
    void clear();

}