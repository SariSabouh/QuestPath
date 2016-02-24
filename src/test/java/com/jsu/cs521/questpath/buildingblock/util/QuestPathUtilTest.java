package com.jsu.cs521.questpath.buildingblock.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
	public void testUpdateQuestPathItem() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuildQuestRules() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetGradableQuestPathItemStatus() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetLockOrUnlocked() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetQuest() {
		fail("Not yet implemented");
	}

	@Test
	public void testQpathsToJson() {
		fail("Not yet implemented");
	}

	@Test
	public void testStatsToJson() {
		fail("Not yet implemented");
	}

}
