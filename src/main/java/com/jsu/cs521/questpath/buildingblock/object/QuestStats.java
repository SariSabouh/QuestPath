package com.jsu.cs521.questpath.buildingblock.object;

import java.util.ArrayList;
import java.util.List;

public class QuestStats {

	private String externalContentId;
	private int passedCount;
	private int attemptedCount;
	private int lockedCount;
	private List<String> passedStudents = new ArrayList<String>();
	private List<String> attemptedStudents = new ArrayList<String>();
	private List<String> lockedStudents = new ArrayList<String>();
	
	public QuestStats() {
		this.passedCount = 0;
		this.attemptedCount = 0;
		this.lockedCount = 0;
	}

	public String getExternalContentId() {
		return externalContentId;
	}

	public void setExternalContentId(String externalContentId) {
		this.externalContentId = externalContentId;
	}

	public int getPassedCount() {
		return passedCount;
	}

	public void setPassedCount(int passedCount) {
		this.passedCount = passedCount;
	}

	public int getAttemptedCount() {
		return attemptedCount;
	}

	public void setAttemptedCount(int attemptedCount) {
		this.attemptedCount = attemptedCount;
	}

	public int getLockedCount() {
		return lockedCount;
	}


	public void incrementPassedCount() {
		this.passedCount ++;
	}
	public void incrementAttemptedCount() {
		this.attemptedCount ++;
	}
	public void incrementLockedCount() {
		this.lockedCount ++;
	}

	
	public void setLockedCount(int lockedCount) {
		this.lockedCount = lockedCount;
	}

	public List<String> getPassedStudents() {
		return passedStudents;
	}

	public void setPassedStudents(List<String> passedStudents) {
		this.passedStudents = passedStudents;
	}

	public List<String> getAttemptedStudents() {
		return attemptedStudents;
	}

	public void setAttemptedStudents(List<String> attemptedStudents) {
		this.attemptedStudents = attemptedStudents;
	}

	public List<String> getLockedStudents() {
		return lockedStudents;
	}

	public void setLockedStudents(List<String> lockedStudents) {
		this.lockedStudents = lockedStudents;
	}
	
}
