package redlib.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redlib.backend.annotation.BackendModule;
import redlib.backend.annotation.Privilege;
import redlib.backend.dto.StudentsDTO;
import redlib.backend.dto.SubmissionDTO;
import redlib.backend.dto.query.ClassTaskQueryDTO;
import redlib.backend.dto.query.StudentsQueryDTO;
import redlib.backend.dto.query.SubmissionQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.service.SubmissionService;
import redlib.backend.vo.StudentsVO;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 提交管理后端服务模块
 *
 * @author 彭勃
 * @description
 * @date 2024/3/14
 */

@RestController
@RequestMapping("/api/submission")
@BackendModule({"page:页面", "update:修改", "add:创建", "delete:删除"})
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @PostMapping("listSubmissions")
    @Privilege("page")
    public Page<SubmissionDTO> listSubmissions(@RequestBody SubmissionQueryDTO queryDTO) {
        return submissionService.listSubmissionsByPage(queryDTO);
    }

    @PostMapping("getSubmission")
    @Privilege
    public SubmissionDTO getSubmission(@RequestBody SubmissionDTO submissionDTO) {
        return submissionService.getSubmission(submissionDTO);
    }

    @PostMapping("deleteSubmission")
    @Privilege("delete")
    public void deleteSubmission(@RequestBody List<Integer> ids) throws IOException {

        submissionService.deleteSubmission(ids);
    }

    @GetMapping("clear")
    @Privilege("delete")
    public void clear() {

        submissionService.clear();
    }

    @PostMapping("getSubmitted")
    @Privilege("update")
    public Page<StudentsVO> getSubmitted(@RequestBody StudentsVO vo) {
        return submissionService.getSubOrUnsub(vo.getCurrent(), vo.getPageSize(), vo.getClassId(), vo.getTaskId(), true);
    }

    @PostMapping("getUnSubmitted")
    @Privilege("update")
    public Page<StudentsVO> getUnSubmitted(@RequestBody StudentsVO vo) {
        return submissionService.getSubOrUnsub(vo.getCurrent(), vo.getPageSize(), vo.getClassId(), vo.getTaskId(), false);
    }

    @PostMapping("submit")
    @Privilege
    public void submit(@RequestBody SubmissionDTO submissionDTO) {
        submissionService.submit(submissionDTO);
    }

    @PostMapping("uploadFile")
    @Privilege
    public int uploadFile(
            @RequestParam("taskId") String taskId,
            @RequestParam("studentId") String studentId,
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws Exception {
        return submissionService.uploadFile(taskId, file.getOriginalFilename(), file.getInputStream(), studentId, name);
    }

    @PostMapping("downloadFile")
    @Privilege
    public void downloadFile(@RequestParam("taskId") String taskId,
                             @RequestParam("studentId") Integer studentId,
                             HttpServletResponse response) throws Exception {
        File f = submissionService.searchFile(taskId, Integer.toString(studentId));
        InputStream in = new FileInputStream(f);

        String[] strArray = f.getName().split("\\.");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd$HHmmss");
        response.addHeader("Content-Disposition", "attachment;filename=file" + sdf.format(new Date()) + "." + strArray[strArray.length-1]);
        OutputStream os = response.getOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while((len=in.read(buffer))>0) {
            os.write(buffer, 0, len);
        }
        os.close();
    }

    @PostMapping("getFileName")
    @Privilege
    public String getFileName(@RequestParam("taskId") String taskId,
                               @RequestParam("studentId") Integer studentId) throws Exception {
        File f = submissionService.searchFile(taskId, Integer.toString(studentId));
        if (f == null){return "";}
        return f.getName();
    }
}
