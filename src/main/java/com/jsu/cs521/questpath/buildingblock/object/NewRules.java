package com.jsu.cs521.questpath.buildingblock.object;

import java.util.List;
import java.util.ArrayList;

public class NewRules {
	private String ruleNumber;
	private List<NewRule> rules = new ArrayList<NewRule>();
	public String getRuleNumber() {
		return ruleNumber;
	}
	public void setRuleNumber(String ruleNumber) {
		this.ruleNumber = ruleNumber;
	}
	public List<NewRule> getRules() {
		return rules;
	}
	public void setRules(List<NewRule> rules) {
		this.rules = rules;
	}
}
