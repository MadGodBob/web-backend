package redlib.backend.service;

import org.springframework.web.multipart.MultipartFile;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.SubmissionDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.dto.query.SubmissionQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Submission;
import redlib.backend.vo.StudentsVO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.List;

/**
 * 提交模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/13
 */

public interface SubmissionService {

    /**
     * 列出符合条件的提交
     *
     * @return 提交列表
     */
    Page<SubmissionDTO> listSubmissionsByPage(SubmissionQueryDTO queryDTO);

    /**
     * 获取指定的提交
     *
     * @return 提交
     */
    SubmissionDTO getSubmission(SubmissionDTO submissionDTO);

    /**
     * 删除提交
     *
     */
    void deleteSubmission(List<Integer> ids) throws IOException;

    /**
     * 清空提交列表
     *
     */
    void clear();

    /**
     * 获取指定taskId的任务状态
     *
     */
    String getState(String taskId);

    /**
     * 获取提交或未提交的学生列表
     *
     */
    Page<StudentsVO> getSubOrUnsub(Integer current, Integer pagesize, String classId, String taskId, Boolean ifSubmit);

    /**
     * 学生提交作业
     *
     */
    void submit(SubmissionDTO submissionDTO);

    int uploadFile(String taskId, String fileName, InputStream stream, String studentId, String name) throws IOException;

    File searchFile(String taskId, String studentId) throws IOException;
}
