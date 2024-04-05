package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.ClassTaskMapper;
import redlib.backend.dto.ClassTaskDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.SubmissionDTO;
import redlib.backend.dto.query.ClassTaskQueryDTO;
import redlib.backend.model.ClassTask;
import redlib.backend.model.Page;
import redlib.backend.service.ClassTaskService;
import redlib.backend.service.SubmissionService;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.ClassTaskVO;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 班级任务模块服务接口
 *
 * @author 彭勃
 * @description
 * @date 2024/3/13
 */
@Service
public class ClassTaskServiceImpl implements ClassTaskService {

    @Autowired
    private ClassTaskMapper classTaskMapper;

    @Autowired
    private SubmissionService submissionService;

    @Override
    public List<Integer> getTaskAmount(String classId, Integer studentId){
        ClassTaskQueryDTO queryDTO = new ClassTaskQueryDTO();
        queryDTO.setCurrent(1);
        queryDTO.setPageSize(100);
        queryDTO.setClassId(classId);
        Page<ClassTaskDTO> pdto = listTasksByPage(queryDTO);

        int submitted = 0;
        int unSubmitted = 0;
        for (ClassTaskDTO dto : pdto.getList()){
            // 未开启的任务不计入
            if(Objects.equals(dto.getTaskState(), "OFF")){
                continue;
            }
            // 检查是否提交
            SubmissionDTO submissionDTO = new SubmissionDTO();
            submissionDTO.setStudentid(studentId);
            submissionDTO.setTaskId(dto.getTaskId());
            SubmissionDTO sdto = submissionService.getSubmission(submissionDTO);
            if(sdto != null){submitted++;}
            else {unSubmitted++;}
        }

        return Arrays.asList(submitted, unSubmitted);
    }

    @Override
    public Page<ClassTaskVO> listTasksForStudent(ClassTaskQueryDTO queryDTO){
        Page<ClassTaskDTO> pdto = listTasksByPage(queryDTO);
        Page<ClassTaskVO> pvo = new Page<>();
        BeanUtils.copyProperties(pdto, pvo);
        List<ClassTaskVO> classTaskVOList = new ArrayList<>();
        for (ClassTaskDTO dto : pdto.getList()){
            //取出dto中的每一个，检测学生是否已经提交作业
            ClassTaskVO vo = new ClassTaskVO();
            BeanUtils.copyProperties(dto, vo);
            SubmissionDTO submissionDTO = new SubmissionDTO();
            submissionDTO.setStudentid(queryDTO.getStudentId());
            submissionDTO.setTaskId(dto.getTaskId());
            SubmissionDTO sdto = submissionService.getSubmission(submissionDTO);
            if (sdto != null){
                vo.setSubmitted(true);
            }
            else{vo.setSubmitted(false);}
            classTaskVOList.add(vo);
        }
        pvo.setList(classTaskVOList);
        return pvo;
    }

    /**
     * 列出符合条件的任务
     *
     * @return 任务列表
     */
    @Override
    public Page<ClassTaskDTO> listTasksByPage(ClassTaskQueryDTO queryDTO){
        if (queryDTO == null) {
            queryDTO = new ClassTaskQueryDTO();
        }
        queryDTO.setTaskId(FormatUtils.makeFuzzySearchTerm(queryDTO.getTaskId()));
        queryDTO.setClassId(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassId()));
        queryDTO.setClassName(FormatUtils.makeFuzzySearchTerm(queryDTO.getClassName()));
        Integer size = classTaskMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<ClassTask> list = classTaskMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());
        List<ClassTaskDTO> dtoList = new ArrayList<>();
        for (ClassTask classTask : list) {
            ClassTaskDTO dto = new ClassTaskDTO();
            BeanUtils.copyProperties(classTask, dto);
            dtoList.add(dto);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), dtoList);

    }

    /**
     * 获取指定的任务
     *
     * @return 任务
     */
    @Override
    public ClassTaskDTO getTask(ClassTaskDTO classTaskDTO){
        ClassTaskQueryDTO queryDTO = new ClassTaskQueryDTO();
        BeanUtils.copyProperties(classTaskDTO, queryDTO);
        queryDTO.setCurrent(1);
        queryDTO.setPageSize(1);
        Page<ClassTaskDTO> p = listTasksByPage(queryDTO);
        if (p.getList().size() != 1){
            return null;
        }
        return p.getList().getFirst();

    }

    /**
     * 删除指定的任务
     *
     * @return 任务
     */
    @Override
    public void deleteTask(String taskId){
        ClassTaskDTO classTaskDTO = new ClassTaskDTO();
        classTaskDTO.setTaskId(taskId);
        ClassTaskDTO dto = getTask(classTaskDTO);
        Assert.notNull(dto, "删除失败：不存在该任务或匹配结果大于1");
        classTaskMapper.deleteByPrimaryKey(dto.getId());
    }

    /**
     * 添加任务
     *
     * @return
     */
    @Override
    public void addTask(ClassTaskDTO classTaskDTO){
        //检测任务ID是否符合标准
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(classTaskDTO.getTaskId());
        Assert.isTrue(matcher.matches(), "班级ID不符合格式：6位数");

        Assert.notNull(classTaskDTO, "任务数据不能为空");
        Assert.notNull(classTaskDTO.getClassId(), "任务所属班级ID不能为空");
        Assert.notNull(classTaskDTO.getClassName(), "任务所属班级名称不能为空");
        Assert.notNull(classTaskDTO.getTaskId(), "任务ID不能为空");
        Assert.notNull(classTaskDTO.getTaskDescription(), "任务描述不能为空");
        classTaskDTO.setTaskState("OFF");
        ClassTask classTask = new ClassTask();
        BeanUtils.copyProperties(classTaskDTO, classTask);
        classTask.setCreatedAt(new Date());
        classTaskMapper.insert(classTask);
    }

    /**
     * 更新任务
     *
     * @return
     */
    @Override
    public void updateTask(ClassTaskDTO classTaskDTO){
        //检测任务ID是否符合标准
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(classTaskDTO.getTaskId());
        Assert.isTrue(matcher.matches(), "班级ID不符合格式：6位数");

        ClassTask classTask = new ClassTask();
        BeanUtils.copyProperties(classTaskDTO, classTask);
        classTaskMapper.updateByPrimaryKey(classTask);
    }

    /**
     * 激活任务
     *
     * @return
     */
    @Override
    public void enableTask(ClassTaskDTO classTaskDTO){
        ClassTaskDTO dto = getTask(classTaskDTO);
        Assert.notNull(dto, "激活任务失败：该任务不存在或匹配结果大于1");
        classTaskMapper.updateState(dto.getId(), "ON");
    }

    /**
     * 关闭任务
     *
     * @return
     */
    @Override
    public void disableTask(ClassTaskDTO classTaskDTO){
        ClassTaskDTO dto = getTask(classTaskDTO);
        Assert.notNull(dto, "关闭任务失败：该任务不存在或匹配结果大于1");
        classTaskMapper.updateState(dto.getId(), "OFF");
    }
}
