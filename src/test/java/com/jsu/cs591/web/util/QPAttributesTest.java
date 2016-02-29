package com.jsu.cs591.web.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jsu.cs521.questpath.buildingblock.object.QuestPathItem;

public class QPAttributesTest {

	@Test
	public void testQPAttributes() {
		QPAttributes qpAttr = new QPAttributes();
		assertEquals("" ,qpAttr.getTitle());
		assertEquals("" ,qpAttr.getStatusClassName());
	}

	@Test
	public void testQPAttributesQuestPathItemUnlockedPassed() {
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setPassed(true);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Assignment - Test" ,qpAttr.getTitle());
		assertEquals("passed" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathItemUnlockedNotGradable() {
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Reward - Test" ,qpAttr.getTitle());
		assertEquals("passed" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathItemUnlockedAttempted() {
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setGradable(true);
		item.setAttempted(true);
		item.setPointsEarned(50);
		item.setPointsPossible(150);
		item.setPercentageEarned(30);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Test Score - 50.0/150.0 ( 30.00%) " ,qpAttr.getTitle());
		assertEquals("failed" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathItemUnlockedGradable() {
		QuestPathItem item = new QuestPathItem();
		item.setUnLocked(true);
		item.setGradable(true);
		item.setPointsEarned(50);
		item.setPointsPossible(150);
		item.setPercentageEarned(30);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Test Score - 50.0/150.0 ( 30.00%) " ,qpAttr.getTitle());
		assertEquals("unlocked" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathItemLockedGradable() {
		QuestPathItem item = new QuestPathItem();
		item.setLocked(true);
		item.setGradable(true);
		item.setPointsEarned(50);
		item.setPointsPossible(150);
		item.setPercentageEarned(30);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Test Score - 50.0/150.0 ( 30.00%) " ,qpAttr.getTitle());
		assertEquals("locked" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathGradable() {
		QuestPathItem item = new QuestPathItem();
		item.setGradable(true);
		item.setPointsEarned(50);
		item.setPointsPossible(150);
		item.setPercentageEarned(30);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Reward - Test" ,qpAttr.getTitle());
		assertEquals("locked" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathItemLockedAttempted() {
		QuestPathItem item = new QuestPathItem();
		item.setLocked(true);
		item.setAttempted(true);
		item.setPointsEarned(50);
		item.setPointsPossible(150);
		item.setPercentageEarned(30);
		item.setName("Test");
		QPAttributes qpAttr = new QPAttributes(item);
		assertEquals("Reward - Test" ,qpAttr.getTitle());
		assertEquals("locked" ,qpAttr.getStatusClassName());
	}
	
	@Test
	public void testQPAttributesQuestPathSetTitleClassName() {
		QPAttributes qpAttr = new QPAttributes();
		qpAttr.setStatusClassName("Test");
		qpAttr.setTitle("Test");
		assertEquals("Test" ,qpAttr.getTitle());
		assertEquals("Test" ,qpAttr.getStatusClassName());
	}
	
}
