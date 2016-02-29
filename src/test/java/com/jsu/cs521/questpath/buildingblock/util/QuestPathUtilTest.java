package com.jsu.cs521.questpath.buildingblock.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.jsu.cs521.questpath.buildingblock.object.QuestPath;
import com.jsu.cs521.questpath.buildingblock.object.QuestPathItem;
import com.jsu.cs521.questpath.buildingblock.object.QuestRule;
import com.jsu.cs521.questpath.buildingblock.object.RuleCriteria;

import blackboard.data.content.Content;
import blackboard.data.content.avlrule.AvailabilityRule;
import blackboard.data.course.CourseMembership;
import blackboard.data.gradebook.Lineitem;
import blackboard.data.gradebook.Score;
import blackboard.persist.Id;

public class QuestPathUtilTest {

	QuestPathUtil qpUtil;
	
	@Before
	public void setUp() throws Exception {
		qpUtil = new QuestPathUtil();
	}

	@Test
	public void testFindNonAdaptiveReleaseContent() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> parnetContent = new ArrayList<String>();
		parnetContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		QuestPathItem secondItem = new QuestPathItem();
		secondItem.setGradable(true);
		secondItem.setPointsPossible(100);
		secondItem.setName("Test");
		secondItem.setExtContentId("Id2");
		secondItem.setParentContent(parnetContent);
		QuestPathItem thirdItem = new QuestPathItem();
		thirdItem.setGradable(true);
		thirdItem.setPointsPossible(100);
		thirdItem.setName("QUESTPATH");
		thirdItem.setExtContentId("Id3");
		items.add(firstItem);
		items.add(secondItem);
		items.add(thirdItem);
		items = qpUtil.findNonAdaptiveReleaseContent(items);
		assertEquals(1, items.size());
		assertEquals("PARENT1", items.get(0).getExtContentId());
	}

	@Test
	public void testRemoveNonAdaptiveReleaseContent() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> parnetContent = new ArrayList<String>();
		parnetContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		firstItem.setChildContent(parnetContent);
		QuestPathItem secondItem = new QuestPathItem();
		secondItem.setGradable(true);
		secondItem.setPointsPossible(100);
		secondItem.setName("Test");
		secondItem.setExtContentId("Id2");
		secondItem.setParentContent(parnetContent);
		QuestPathItem thirdItem = new QuestPathItem();
		thirdItem.setGradable(true);
		thirdItem.setPointsPossible(100);
		thirdItem.setName("QUESTPATH");
		thirdItem.setExtContentId("Id3");
		items.add(firstItem);
		items.add(secondItem);
		items.add(thirdItem);
		items = qpUtil.removeNonAdaptiveReleaseContent(items);
		assertEquals(2, items.size());
		assertEquals("Test1", items.get(0).getName());
		assertEquals("Test", items.get(1).getName());
	}

	@Test
	public void testSetInitialFinalWithChild() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> childContent = new ArrayList<String>();
		childContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		firstItem.setChildContent(childContent);
		items.add(firstItem);
		items = qpUtil.setInitialFinal(items);
		assertEquals(1, items.size());
		assertEquals("Test1", items.get(0).getName());
	}
	
	@Test
	public void testSetInitialFinalWithParent() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		firstItem.setParentContent(parentContent);
		items.add(firstItem);
		items = qpUtil.setInitialFinal(items);
		assertEquals(1, items.size());
		assertEquals("Test1", items.get(0).getName());
	}
	
	@Test
	public void testSetInitialFinalWithChildAndParent() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("PARENT1");
		List<String> childContent = new ArrayList<String>();
		childContent.add("CHILD1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		firstItem.setParentContent(parentContent);
		firstItem.setChildContent(childContent);
		items.add(firstItem);
		items = qpUtil.setInitialFinal(items);
		assertEquals(1, items.size());
		assertEquals("Test1", items.get(0).getName());
	}

	@Test
	public void testSetParentChildList() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test1");
		firstItem.setExtContentId("PARENT1");
		firstItem.setParentContent(parentContent);
		items.add(firstItem);
		List<QuestRule> allRules = new ArrayList<QuestRule>();
		QuestRule qRule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(80);
		criteria.setMaxScore(70);
		criteria.setParentContent("PARENT1");
		criterias.add(criteria);
		qRule.setCriterias(criterias);
		qRule.setExtContentId("PARENT1");
		qRule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		allRules.add(qRule);
		items = qpUtil.setParentChildList(items, allRules);
		assertEquals(" Item will be unlocked when the following Quest Path Items are completed: null."
				, items.get(0).getUnlockRule());
		
	}

	@Test //blackboard.platform.RuntimeBbServiceException errors
	@Ignore
	public void testBuildInitialList() {
		List<Content> contentItems = new ArrayList<Content>();
		List<Lineitem> lineitems = new ArrayList<Lineitem>();
		Content content = new Content();
		content.setId(Id.newId(Content.DATA_TYPE));
		content.setTitle("Content Test");
		contentItems.add(content);
		Lineitem lineitem = new Lineitem();
		lineitem.setType("Test");
		lineitem.setName("Line Name");
		lineitem.setPointsPossible(100);
		List<Score> scores = new ArrayList<Score>();
		Score score = new Score();
		score.setId(content.getId());
		score.setGrade("80");
		scores.add(score);
		lineitem.setScores(scores);
		qpUtil.buildInitialList(Id.newId(CourseMembership.DATA_TYPE), contentItems, lineitems, true);
		//
	}

	@Test
	@Ignore
	public void testUpdateQuestPathItem() {
//		qpUtil.updateQuestPathItem(items, lineitems, cm);
	}

	@Test
	@Ignore
	public void testBuildQuestRules() {
//		qpUtil.buildQuestRules(rules, avCriLoader, defLoad)
	}

	@Test
	public void testSetGradableQuestPathItemStatusLastQuestItemPassed() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setLastQuestItem(true);
		item.setPercentageEarned(81);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(true, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusLastQuestItemAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setLastQuestItem(true);
		item.setPointsEarned(70);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(true, items.get(0).isAttempted());
		assertEquals(" Quest Path Item will be complete when a score of 80% or higher is scored.", items.get(0).getCompleteRule());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusLastQuestItemNotAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setLastQuestItem(true);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusLastQuestItemNotGradable() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(false);
		item.setLastQuestItem(true);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradePercentagePassed() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		item.setPointsEarned(60);
		item.setPercentageEarned(60);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(true, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradePercentageAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		item.setPointsEarned(30);
		item.setPercentageEarned(30);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(true, items.get(0).isAttempted());
		assertEquals(" Rule 1 Quest Path Item will be complete when a score of 50.0% or higher is scored.", items.get(0).getCompleteRule());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradePercentageNotAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradePercent(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
		assertEquals(" Rule 1 Quest Path Item will be complete when a score of 50.0% or higher is scored.", items.get(0).getCompleteRule());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradeRangePassed() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		item.setPointsEarned(60);
		item.setPercentageEarned(60);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(true, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradeRangeAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		item.setPointsEarned(30);
		item.setPercentageEarned(30);
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(true, items.get(0).isAttempted());
		assertEquals(" Rule 1 Quest Path Item will be complete when a score of 50.0 or higher is scored.", items.get(0).getCompleteRule());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleCriteriaGradeRangeNotAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
		assertEquals(" Rule 1 Quest Path Item will be complete when a score of 50.0 or higher is scored.", items.get(0).getCompleteRule());
	}
	
	@Test
	public void testSetGradableQuestPathItemStatusRuleNotSameParentId() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("NotParent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		items.add(item);
		items = qpUtil.setGradableQuestPathItemStatus(items, rules);
		assertEquals(false, items.get(0).isPassed());
		assertEquals(false, items.get(0).isAttempted());
	}

	@Test
	public void testSetLockOrUnlockedWithNoParent() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("NotParent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		items.add(item);
		items = qpUtil.setLockOrUnlocked(items, rules);
		assertEquals(true, items.get(0).isUnLocked());
	}
	
	@Test
	public void testSetLockOrUnlockedPassed() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("Parent");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setExtContentId("Parent");
		item.setPassed(true);
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("Parent");
		item.setParentContent(parentContent);
		items.add(item);
		items = qpUtil.setLockOrUnlocked(items, rules);
		assertEquals(true, items.get(0).isUnLocked());
	}
	
	@Test
	public void testSetLockOrUnlockedAttempted() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("Parent");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setPassed(false);
		item.setExtContentId("Parent");
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("Parent");
		item.setParentContent(parentContent);
		items.add(item);
		items = qpUtil.setLockOrUnlocked(items, rules);
		assertEquals(false, items.get(0).isUnLocked());
		assertEquals(true, items.get(0).isLocked());
	}

	@Test
	public void testSetLockOrUnlockedNotSameId() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("Parent");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("RuleId");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setPassed(false);
		item.setExtContentId("Parent");
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("Parent");
		item.setParentContent(parentContent);
		items.add(item);
		items = qpUtil.setLockOrUnlocked(items, rules);
		assertEquals(false, items.get(0).isUnLocked());
		assertEquals(false, items.get(0).isLocked());
	}
	
	@Test
	public void testSetLockOrUnlockedNotSameCriteriaId() {
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		List<QuestRule> rules = new ArrayList<QuestRule>();
		QuestRule rule = new QuestRule();
		List<RuleCriteria> criterias = new ArrayList<RuleCriteria>();
		RuleCriteria criteria = new RuleCriteria();
		criteria.setGradeRange(true);
		criteria.setMaxScore(100);
		criteria.setMinScore(50);
		criteria.setParentContent("RuleId");
		criterias.add(criteria);
		rule.setCriterias(criterias);
		rule.setExtContentId("Parent");
		rule.setRuleId(Id.newId(AvailabilityRule.DATA_TYPE));
		rules.add(rule);
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setPassed(false);
		item.setExtContentId("Parent");
		List<String> parentContent = new ArrayList<String>();
		parentContent.add("Parent");
		item.setParentContent(parentContent);
		items.add(item);
		items = qpUtil.setLockOrUnlocked(items, rules);
		assertEquals(false, items.get(0).isUnLocked());
		assertEquals(false, items.get(0).isLocked());
	}

	@Test
	public void testSetQuestUnlockedPassed() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setPassed(true);
		item.setUnLocked(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getPassedQuests().get(0).intValue());
		assertEquals(1, questPath.getPassedQuests().size());
	}
	
	@Test
	public void testSetQuestUnlockedNotGradable() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getRewardItems().get(0).intValue());
		assertEquals(1, questPath.getRewardItems().size());
	}
	
	@Test
	public void testSetQuestUnlockedAttempted() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setGradable(true);
		item.setAttempted(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getAttemptedQuests().get(0).intValue());
		assertEquals(1, questPath.getAttemptedQuests().size());
	}
	
	@Test
	public void testSetQuestUnlockedOnly() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setGradable(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getUnlockedQuests().get(0).intValue());
		assertEquals(1, questPath.getUnlockedQuests().size());
	}
	
	@Test
	public void testSetQuestLockedGradable() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setLocked(true);
		item.setGradable(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getLockedQuests().get(0).intValue());
		assertEquals(1, questPath.getLockedQuests().size());
	}
	
	@Test
	public void testSetQuestLocked() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setLocked(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getLockedItems().get(0).intValue());
		assertEquals(1, questPath.getLockedItems().size());
	}
	
	@Test
	public void testSetQuestLockedAttempted() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setLocked(true);
		item.setAttempted(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getLockedItems().get(0).intValue());
		assertEquals(1, questPath.getLockedItems().size());
	}
	
	@Test
	public void testSetQuestGradable() {
		QuestPath questPath = new QuestPath();
		List<QuestPathItem> items = new ArrayList<QuestPathItem>();
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		items.add(item);
		questPath.setQuestPathItems(items);
		questPath = qpUtil.setQuest(questPath);
		assertEquals(0, questPath.getLockedItems().get(0).intValue());
		assertEquals(1, questPath.getLockedItems().size());
	}

}
