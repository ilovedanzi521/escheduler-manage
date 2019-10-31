package cn.escheduler.api.controller.api;

import cn.escheduler.api.controller.BaseController;
import cn.escheduler.api.dto.FeignSaveTaskDTO;
import cn.escheduler.api.enums.Status;
import cn.escheduler.api.service.ProcessDefinitionService;
import cn.escheduler.api.service.ProjectService;
import cn.escheduler.api.utils.Constants;
import cn.escheduler.api.utils.Result;
import cn.escheduler.common.enums.LayoutType;
import cn.escheduler.common.enums.UserType;
import cn.escheduler.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.escheduler.api.enums.Status.CREATE_PROCESS_DEFINITION;

/**
 * 包名称：cn.escheduler.api.controller.api
 * 类名称：TaskFeignController
 * 类描述：新增接口
 * 创建人：@author wanglei
 * 创建时间：2019/7/16/11:08
 */
@RestController
@RequestMapping("/projects/winSchedule/process")
//projects/{projectName}/process
public class TaskFeignController extends BaseController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;
    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/feignsave")
    @CrossOrigin
//    @ResponseStatus(HttpStatus.CREATED)
    public Result createProcessDefinition(@RequestBody FeignSaveTaskDTO feignSaveTaskDTO) {
        String projectName = "winSchedule";
        String name = feignSaveTaskDTO.getName();
        String json = feignSaveTaskDTO.getProcessDefinitionJson();
        String locations = feignSaveTaskDTO.getLocations();
        String connects = feignSaveTaskDTO.getConnects() == null ? null : feignSaveTaskDTO.getConnects().toString();
        String desc = feignSaveTaskDTO.getDesc();
        User loginUser = new User();
        loginUser.setId(2);
        loginUser.setUserType(UserType.GENERAL_USER);
        try {
            Map<String, Object> rtn = projectService.createProject(loginUser, projectName, desc);
            Object status = rtn.get(Constants.STATUS);
            if (Status.SUCCESS.equals(status) || Status.PROJECT_ALREADY_EXISTS.equals(status)) {
                Map<String, Object> result = processDefinitionService.createProcessDefinition(loginUser, projectName, name, json,
                        desc, locations, connects, LayoutType.NOTSHOW);
                return returnDataList(result);
            } else {
                return returnDataList(rtn);
            }

        } catch (Exception e) {
            return error(CREATE_PROCESS_DEFINITION.getCode(), CREATE_PROCESS_DEFINITION.getMsg());
        }

    }
}
