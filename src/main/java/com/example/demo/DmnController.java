package com.example.demo;

import com.example.demo.po.TripExpense;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.flowable.dmn.api.DmnRepositoryService;
import org.flowable.dmn.api.DmnRuleService;
import org.flowable.dmn.engine.DmnEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private DmnEngine dmnEngine;

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

        List<Map<String, Object>> decisionMap = dmnRuleService.createExecuteDecisionBuilder()
                .decisionKey("decision")
                .variables(ruleMap)//map类型传入
                .execute();
       // Map<String, Object> stringObjectMap = dmnRuleService.createExecuteDecisionBuilder().decisionKey("decision").variable("input1", "Stringtest").executeWithSingleResult();
        // List<Map<String, Object>> result = dmnRuleService.executeDecisionByKey("decision", processVariablesInput);
        System.out.println(decisionMap.get(0).get("output1"));
        //System.out.println(stringObjectMap.get("output1"));
    }
    @PostMapping("tripExpense")
    @ApiOperation(value = "打车报销",notes = "打车报销规则引擎")
    public String tripExpense(@RequestBody TripExpense tripExpense) {
        //部署规则
        //dmnRepositoryService.createDeployment().name("tripExpense").disableSchemaValidation()
        //        .addClasspathResource("dmn/tripExpense.dmn").tenantId("testTripExpense")
        //        .deploy();
        //根据规则引擎获取规则service
        DmnRuleService dmnRuleService = dmnEngine.getDmnRuleService();
        //Map<String, Object> processVariablesInput = new HashMap<>();
        //processVariablesInput.put("input1", "Stringtest");
        //根据规则ID,匹配规则的数据项
        List<Map<String, Object>> decisionMap = dmnRuleService.createExecuteDecisionBuilder().decisionKey("tripReimbursement")
                                                .variable("money",tripExpense.getMoney())//变量类型传入
                                                .variable("level",tripExpense.getLevel())
                                                .execute();
       // Map<String, Object> stringObjectMap = dmnRuleService.createExecuteDecisionBuilder().decisionKey("decision").variable("input1", "Stringtest").executeWithSingleResult();
        // List<Map<String, Object>> result = dmnRuleService.executeDecisionByKey("decision", processVariablesInput);
        System.out.println(decisionMap.get(0).get("approvalResults"));
        Boolean approvalResults = (Boolean)(decisionMap.get(0).get("approvalResults"));
        return approvalResults == true ? "审核通过" :"审核不通过";

        //System.out.println(stringObjectMap.get("output1"));
    }

}
