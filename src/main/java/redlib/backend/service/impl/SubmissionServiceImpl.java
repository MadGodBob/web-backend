package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.dao.ClassTaskMapper;
import redlib.backend.dao.StudentsMapper;
import redlib.backend.dao.SubmissionMapper;
import redlib.backend.dto.SubmissionDTO;
import redlib.backend.dto.query.SubmissionQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Students;
import redlib.backend.model.Submission;
import redlib.backend.service.SubmissionService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.StudentsVO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 提交模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/13
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private ClassTaskMapper classTaskMapper;

    @Autowired
    private StudentsMapper studentsMapper;

    /**
     * 列出符合条件的提交
     *
     * @return 提交列表
     */
    @Override
    public Page<SubmissionDTO> listSubmissionsByPage(SubmissionQueryDTO queryDTO){

        if (queryDTO == null) {
            queryDTO = new SubmissionQueryDTO();
        }
        queryDTO.setName(FormatUtils.makeFuzzySearchTerm(queryDTO.getName()));
        queryDTO.setClassId(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassId()));
        queryDTO.setClassName(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassName()));
        queryDTO.setTaskId(FormatUtils.makeFuzzySearchTerm(queryDTO.getTaskId()));
        Integer size = submissionMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Submission> list = submissionMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<SubmissionDTO> dtoList = new ArrayList<>();
        for (Submission submission : list) {
            SubmissionDTO dto = new SubmissionDTO();
            BeanUtils.copyProperties(submission, dto);
            dtoList.add(dto);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), dtoList);

    }

    /**
     * 获取指定的提交
     *
     * @return 提交
     */
    @Override
    public SubmissionDTO getSubmission(SubmissionDTO submissionDTO){
        SubmissionQueryDTO queryDTO = new SubmissionQueryDTO();
        BeanUtils.copyProperties(submissionDTO, queryDTO);
        queryDTO.setCurrent(1);
        queryDTO.setPageSize(10);
        Page<SubmissionDTO> p = listSubmissionsByPage(queryDTO);
        if (p.getList().size() != 1){
            //匹配到多个就返回null
            return null;
        }
        return p.getList().getFirst();

    }

    /**
     * 删除提交
     *
     */
    @Override
    @Transactional
    public void deleteSubmission(List<Integer> ids) throws IOException {
        for (Integer id: ids){
            //删除提交的文件
            Submission submission = submissionMapper.selectByPrimaryKey(id);
            File f = searchFile(submission.getTaskId(), Integer.toString(submission.getStudentid()));
            if (f != null){f.delete();}

            submissionMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 清空提交列表
     *
     */
    @Override
    public void clear(){
        submissionMapper.clear();

        //清空文件
        File homework = new File("homework\\");
        File[] files = homework.listFiles();
        for (File f : files){
            boolean a = f.delete();
            }
    }

    /**
     * 获取指定taskId的任务状态
     *
     */
    @Override
    public String getState(String taskId){
        return classTaskMapper.getState(taskId);
    }


    /**
     * 获取提交或未提交的学生列表
     *
     */
    @Override
    public Page<StudentsVO> getSubOrUnsub(Integer current, Integer pagesize, String classId, String taskId, Boolean ifSubmit){
        int size = 0;
        if (ifSubmit){
            size = studentsMapper.countSubmitted(classId, taskId);
        }
        else {
            size = studentsMapper.countUnSubmitted(classId, taskId);
        }

        PageUtils pageUtils = new PageUtils(current, pagesize, size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        List<Students> list = new ArrayList<>();
        if (ifSubmit){
            list = studentsMapper.getSubmitted(classId, taskId);
        }
        else {
            list = studentsMapper.getUnSubmitted(classId, taskId);
        }

        List<StudentsVO> voList = new ArrayList<>();
        for (Students students : list) {
            StudentsVO vo = new StudentsVO();
            SubmissionDTO sDTO = new SubmissionDTO();
            sDTO.setTaskId(taskId);
            sDTO.setStudentid(students.getStudentid());
            sDTO = getSubmission(sDTO);
            BeanUtils.copyProperties(students, vo);
            if (sDTO != null){
                vo.setAnswer(sDTO.getAnswer());
            }
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);

    }

    /**
     * 学生提交作业
     *
     */
    @Override
    public void submit(SubmissionDTO submissionDTO) {

        Assert.notNull(submissionDTO.getName(), "提交失败：姓名不能为空");
        Assert.notNull(submissionDTO.getStudentid(), "提交失败：学号不能为空");
        Assert.notNull(submissionDTO.getClassId(), "提交失败：班级ID不能为空");
        Assert.notNull(submissionDTO.getClassName(), "提交失败：班级名称不能为空");
        Assert.notNull(submissionDTO.getTaskId(), "提交失败：任务ID不能为空");
        Assert.notNull(submissionDTO.getAnswer(), "提交失败：答案不能为空");
        Assert.isTrue(Objects.equals(getState(submissionDTO.getTaskId()), "ON"), "提交失败：作业未开启或不存在该作业");

        SubmissionDTO dto = new SubmissionDTO();
        dto.setTaskId(submissionDTO.getTaskId());
        dto.setStudentid(submissionDTO.getStudentid());
        Assert.isTrue(getSubmission(dto) == null, "已提交");

        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionDTO, submission);
        submission.setSubmitAt(new Date());

        submissionMapper.insert(submission);

    }

    /**
     * 学生提交文件
     *
     */
    @Override
    public int uploadFile(String taskId, String fileName, InputStream stream, String studentId, String name) throws IOException {
        File file = new File("homework\\" + "[" + taskId + "]" + "(" + name + studentId + ")" + fileName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, file);
        if (!file.exists()){
            file.createNewFile();
            return 1;
        }
        return 0;
    }

    /**
     * 查询提交文件
     *
     */
    @Override
    public File searchFile(String taskId, String studentId) throws IOException {
        File homework = new File("homework\\");
        File[] files = homework.listFiles();
        for (File f : files){
            if(f.getName().contains(taskId) && f.getName().contains(studentId)){

                return f;
            }
        }
        return null;
    }
}
