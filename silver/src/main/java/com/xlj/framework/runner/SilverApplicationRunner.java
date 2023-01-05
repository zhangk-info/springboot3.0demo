package com.xlj.framework.runner;

import com.xlj.system.service.ISysConfigService;
import com.xlj.system.service.ISysDictTypeService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class SilverApplicationRunner implements ApplicationRunner {

    @Autowired
    private ISysConfigService sysConfigService;
    @Autowired
    private ISysDictTypeService sysDictTypeService;

    /**
     * 项目启动后做一些事情
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 项目启动时，初始化参数到缓存
        sysConfigService.loadingConfigCache();
        // 项目启动时，初始化字典到缓存
        sysDictTypeService.loadingDictCache();
    }
}
