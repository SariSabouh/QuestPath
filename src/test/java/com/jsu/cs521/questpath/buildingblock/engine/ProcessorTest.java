package com.jsu.cs521.questpath.buildingblock.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jsu.cs521.questpath.buildingblock.object.GraphTier;
import com.jsu.cs521.questpath.buildingblock.object.QuestPath;
import com.jsu.cs521.questpath.buildingblock.object.QuestPathItem;
import com.jsu.cs521.questpath.buildingblock.object.QuestStats;

import blackboard.persist.PersistenceException;

public class ProcessorTest {

	Processor process;
	List<QuestPathItem> items;
	
	@Before
	public void setUp() throws Exception {
		process = new Processor();
		items = new ArrayList<QuestPathItem>();
		List<String> parnetContent = new ArrayList<String>();
		parnetContent.add("PARENT1");
		QuestPathItem firstItem = new QuestPathItem();
		firstItem.setFirstQuestItem(true);
		firstItem.setGradable(true);
		firstItem.setPointsPossible(100);
		firstItem.setName("Test");
		firstItem.setExtContentId("PARENT1");
		firstItem.setParentContent(parnetContent);
		QuestPathItem secondItem = new QuestPathItem();
		secondItem.setGradable(true);
		secondItem.setPointsPossible(100);
		secondItem.setName("Test");
		secondItem.setExtContentId("Id2");
		secondItem.setParentContent(parnetContent);
		items.add(firstItem);
		items.add(secondItem);
	}

	@Test
	public void testBuildQuests() {
		List<QuestPath> qps = process.buildQuests(items);
		assertEquals(1, qps.size());
		assertEquals("PARENT1", qps.get(0).getQuestItemNames().get(0));
		assertEquals("Id2", qps.get(0).getQuestItemNames().get(1));
	}

	@Test
	public void testBuildGraphTier() {
		List<GraphTier> graphs = process.buildGraphTier(items);
		assertEquals(2, graphs.size());
		assertEquals("[PARENT1]", graphs.get(0).getTier().toString());
		assertEquals("[Id2]", graphs.get(1).getTier().toString());
	}

	@Test
	public void testBuildInitQS() {
		List<QuestPath> quests = new ArrayList<QuestPath>();
		QuestPath quest = new QuestPath();
		quest.setQuestPathItems(items);
		quests.add(quest);
		List<QuestStats> stats = process.buildInitQS(quests);
		assertEquals(2, stats.size());
		assertEquals("PARENT1", stats.get(0).getExternalContentId());
		assertEquals("Id2", stats.get(1).getExternalContentId());
	}

}
