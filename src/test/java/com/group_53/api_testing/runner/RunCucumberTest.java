package com.group_53.api_testing.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.group_53.api_testing.stepDefinitions"},
        plugin = {"pretty", "html:target/cucumber-reports","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}

)
public class  RunCucumberTest extends AbstractTestNGCucumberTests {
}