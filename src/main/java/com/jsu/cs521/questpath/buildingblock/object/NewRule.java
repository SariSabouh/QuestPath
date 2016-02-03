package com.jsu.cs521.questpath.buildingblock.object;

public class NewRule {
	private float minScore;
	private String toId;
	private String fromId;
	private String typeRule;
	
	public NewRule() {
		minScore = 0.0f;
		toId = "";
		fromId = "";
		typeRule = "";
	}

	public float getMinScore() {
		return minScore;
	}

	public void setMinScore(float minScore) {
		this.minScore = minScore;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

	public String getTypeRule() {
		return typeRule;
	}

	public void setTypeRule(String typeRule) {
		this.typeRule = typeRule;
	}
}
