package redlib.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.ClassTaskDTO;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.query.ClassTaskQueryDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.ClassTaskService;
import redlib.backend.vo.ClassTaskVO;

import java.util.List;

/**
 * 学生管理后端服务模块
 *
 * @author 彭勃
 * @description
 * @date 2024/3/13
 */

@RestController
@RequestMapping("/api/ClassTask")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除", "modify:开启/关闭任务"})
public class ClassTaskController {
    @Autowired
    private ClassTaskService classTaskService;

    @PostMapping("listTasks")
    @Privilege
    public Page<ClassTaskDTO> listTasks(@RequestBody ClassTaskQueryDTO queryDTO) {
        return classTaskService.listTasksByPage(queryDTO);
    }

    @PostMapping("listTasksForStudent")
    @Privilege
    public Page<ClassTaskVO> listTasksForStudent(@RequestBody ClassTaskQueryDTO queryDTO) {
        return classTaskService.listTasksForStudent(queryDTO);
    }

    @PostMapping("deleteTask")
    @Privilege("delete")
    @Transactional
    public void deleteTasks(@RequestBody List<String> taskIds) {
        for (String taskId: taskIds){
            classTaskService.deleteTask(taskId);
        }
    }

    @PostMapping("addTask")
    @Privilege("add")
    public void addTask(@RequestBody ClassTaskDTO classTaskDTO) {
        classTaskService.addTask(classTaskDTO);
    }

    @PostMapping("updateTask")
    @Privilege("update")
    public void updateTask(@RequestBody ClassTaskDTO classTaskDTO) {
        classTaskService.updateTask(classTaskDTO);
    }

    @PostMapping("enableTask")
    @Privilege("modify")
    public void enableTask(@RequestBody ClassTaskDTO classTaskDTO) {
        classTaskService.enableTask(classTaskDTO);
    }

    @PostMapping("disableTask")
    @Privilege("modify")
    public void disableTask(@RequestBody ClassTaskDTO classTaskDTO) {
        classTaskService.disableTask(classTaskDTO);
    }

}
