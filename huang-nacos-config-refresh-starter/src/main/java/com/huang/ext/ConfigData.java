package com.huang.ext;

import lombok.Data;

/**
 * @Time 2023-03-30 9:46
 * Created by Huang
 * className: ConfigData
 * Description:
 */
@Data
public class ConfigData {
    private String dataId;
    private String group;
    private String content;

    public ConfigData(String dataId, String group, String content) {
        this.dataId = dataId;
        this.group = group;
        this.content = content;
    }
}
