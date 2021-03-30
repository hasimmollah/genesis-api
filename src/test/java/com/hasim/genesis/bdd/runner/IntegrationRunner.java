package com.hasim.genesis.bdd.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		glue = {"com.hasim.genesis.bdd.stepdefs"},
		format = {
				"pretty",
				"json:target/surefire-reports/IntegrationTestReport.json",
		},
		tags ={"@Dev"},
		features = {"src/test/integration/features/"})
public class IntegrationRunner {
    @BeforeClass
	public static void setup() throws Exception {
		System.setProperty("appenv", "Sandbox");
		
	}

	@AfterClass
	public static void teardown() throws Exception {
		
	}
}
