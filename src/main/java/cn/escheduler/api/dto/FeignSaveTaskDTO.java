package cn.escheduler.api.dto;


import java.util.List;

/**
 * 包名称：com.win.dfas.dto
 * 类名称：FeignSaveTaskDTO
 * 类描述：调用第三方接口保存task
 * 创建人：@author wanglei
 * 创建时间：2019/7/16/10:19
 */
public class FeignSaveTaskDTO {
    private Long id;
    private String processDefinitionJson;
    private String name;
    private String desc;
    private String locations;
    private List connects;
    private String projectName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessDefinitionJson() {
        return processDefinitionJson;
    }

    public void setProcessDefinitionJson(String processDefinitionJson) {
        this.processDefinitionJson = processDefinitionJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public List getConnects() {
        return connects;
    }

    public void setConnects(List connects) {
        this.connects = connects;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
