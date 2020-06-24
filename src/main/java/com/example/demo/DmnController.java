package com.example.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.DmnRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: DmnController
 * @Package: com.example.demo
 * @Description:规则引擎测试类
 * @Author: 孙涛
 * @Date: 2020/6/21 10:14
 **/
@RestController
@RequestMapping("flowable--DMN")
@Api(tags="规则引擎测试")
public class DmnController {

    @Autowired
    private DmnRuleService dmnRuleService;

    @Autowired
    private DmnRepositoryService dmnRepositoryService;
    /**
     * @MethodName: redPacket
     * @param
     * @Description: 规则获取对应的值
     * @Author: 孙涛
     * @Return: void
     * @Date: 2020/6/24 14:08
     **/

    @PostMapping("getValue")
    @ApiOperation(value = "根据规则获取返回值",notes = "根据规则获取返回值")
    public void redPacket(@RequestBody Map<String, Object> ruleMap) {//{"input1":"Stringtest"}
        dmnRepositoryService.createDeployment().name("secondDeployment").disableSchemaValidation()
                .addClasspathResource("dmn/strings_1.dmn").tenantId("testTenant")
                .deploy();
        //Map<String, Object> processVariablesInput = new HashMap<>();
        //processVariablesInput.put("input1", "Stringtest");

        List<Map<String, Object>> decisionMap = dmnRuleService.createExecuteDecisionBuilder().decisionKey("decision").variables(ruleMap).execute();
       // Map<String, Object> stringObjectMap = dmnRuleService.createExecuteDecisionBuilder().decisionKey("decision").variable("input1", "Stringtest").executeWithSingleResult();
        // List<Map<String, Object>> result = dmnRuleService.executeDecisionByKey("decision", processVariablesInput);
        System.out.println(decisionMap.get(0).get("output1"));
        //System.out.println(stringObjectMap.get("output1"));
    }

}
