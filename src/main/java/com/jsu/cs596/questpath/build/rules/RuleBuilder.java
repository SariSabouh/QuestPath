package com.jsu.cs596.questpath.build.rules;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.jsu.cs521.questpath.buildingblock.object.NewRule;
import com.jsu.cs521.questpath.buildingblock.object.NewRules;

import blackboard.data.content.Content;
import blackboard.data.content.avlrule.AvailabilityCriteria;
import blackboard.data.content.avlrule.AvailabilityRule;
import blackboard.data.content.avlrule.GradeRangeCriteria;
import blackboard.data.gradebook.Lineitem;
import blackboard.persist.BbPersistenceManager;
import blackboard.persist.content.avlrule.AvailabilityCriteriaDbPersister;
import blackboard.persist.content.avlrule.AvailabilityRuleDbPersister;
import blackboard.persist.gradebook.LineitemDbLoader;
import blackboard.platform.context.Context;
import blackboard.platform.persistence.PersistenceServiceFactory;

public class RuleBuilder {

	public String debugString = "";

	public void buildRule(Context ctx, List<Content> children, JSONArray newRules) throws Exception {
		List<NewRules> newRulesArray = buildRules(newRules);
		AvailabilityRuleDbPersister arP = AvailabilityRuleDbPersister.Default.getInstance();
		AvailabilityCriteriaDbPersister acP = AvailabilityCriteriaDbPersister.Default.getInstance();
		BbPersistenceManager bbPm = PersistenceServiceFactory.getInstance().getDbPersistenceManager();
		for (NewRules nrS : newRulesArray) {
			AvailabilityRule ar = new AvailabilityRule();
			ar.setContentId(bbPm.generateId(Content.DATA_TYPE, nrS.getRules().get(0).getToId()));
			ar.setTitle("New Rule " + nrS.getRuleNumber());
 			ar.setType(AvailabilityRule.Type.USER);
			arP.persist(ar);
			List<AvailabilityCriteria> acS = new ArrayList<AvailabilityCriteria>();
			for (NewRule nr : nrS.getRules()) {
				String contentName = "";
				for (Content c : children) {
					if (c.getId().getExternalString().equals(nr.getFromId())) {
						contentName = c.getTitle();
					}
				}
				GradeRangeCriteria ac = new GradeRangeCriteria();
				ac.setRuleId(ar.getId());
				LineitemDbLoader lineItemDbLoader = LineitemDbLoader.Default.getInstance();
				List<Lineitem> lineitems = lineItemDbLoader.loadByCourseId(ctx.getCourseId());
				for (Lineitem lineItem : lineitems) {
					if (lineItem.getName().trim().equals(contentName)) {
						ac.setOutcomeDefinitionId(lineItem.getOutcomeDefinition().getId());
					}
				}
				ac.setMinScore(nr.getMinScore());
				if (nr.getTypeRule().equals("1")) {
					ac.setPercentRange(false);
				} else {
					ac.setPercentRange(true);
				}
				acP.persist(ac);
				acS.add(ac);
			}
			ar.setAvailabilityCriteria(acS);
			arP.persist(ar);
		}
	}
	
	private List<NewRules> buildRules(JSONArray newRules) throws JSONException {
		List<NewRules> newRulesArray = new ArrayList<NewRules>();
		for (int i = 0; i < newRules.length(); i++) {
			NewRule nr = new NewRule();
			nr.setFromId(newRules.getJSONObject(i).getString("fromId"));
			nr.setToId(newRules.getJSONObject(i).getString("toId"));
			nr.setMinScore(Float.valueOf(newRules.getJSONObject(i).getString("minValue")));
			nr.setTypeRule(newRules.getJSONObject(i).getString("typeRule"));
			boolean createNewRule = true;
			for (NewRules nrS : newRulesArray) {
				if (nrS.getRuleNumber().equals(newRules.getJSONObject(i).getString("ruleNumber"))) {
					nrS.getRules().add(nr);
					createNewRule = false;
				}
			}
			if (createNewRule) {
				NewRules nrS = new NewRules();
				nrS.setRuleNumber(newRules.getJSONObject(i).getString("ruleNumber"));
				nrS.getRules().add(nr);
				newRulesArray.add(nrS);
			}
		}
		return newRulesArray;
	}

/*http://code.lamsfoundation.org/fisheye/browse/~raw,r=1.6/lams/lams_bb_integration/web/modules/start_lesson_proc.jsp*/
} 	
