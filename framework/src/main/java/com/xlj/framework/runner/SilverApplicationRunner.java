package com.xlj.framework.runner;

import com.xlj.common.entity.AjaxResult;
import com.xlj.common.entity.DataResp;
import com.xlj.system.service.ISysConfigService;
import com.xlj.system.service.ISysDictTypeService;
import jakarta.annotation.PostConstruct;
import org.springdoc.core.converters.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
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
        // 增加自定义的返回类型 用以支持springdoc @link ResponseSupportConverter.resolve isResponseTypeWrapper(cls) org.springdoc.core.converters.ConverterUtils
        ConverterUtils.addResponseWrapperToIgnore(DataResp.class);
        ConverterUtils.addResponseWrapperToIgnore(AjaxResult.class);
        // 项目启动时，初始化参数到缓存
        sysConfigService.loadingConfigCache();
        // 项目启动时，初始化字典到缓存
        sysDictTypeService.loadingDictCache();
    }
}
