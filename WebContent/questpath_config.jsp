<!DOCTYPE HTML>
<!-- 
	Gamegogy Quest Path 1.0
    Copyright (C) 2012  David Thornton

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    
    Author: Jonathan Leftwich  Graduate Student at Jacksonville State University
-->
<%@page import="java.util.*"%>
<%@page import="blackboard.platform.plugin.PlugInUtil"%>
<%@page import="com.jsu.cs521.questpath.buildingblock.util.*"%>
<%@page import="com.jsu.cs521.questpath.buildingblock.object.*"%>
<%@page import="com.jsu.cs521.questpath.buildingblock.engine.*"%>
<%@page import="com.jsu.cs591.web.util.QPAttributes"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<bbNG:modulePage type="personalize" ctxId="ctx">
<%
		Processor proc = new Processor();
		proc.QPDriver(ctx);
		if (proc.isUserAnInstructor) {
		String cssPath1 = PlugInUtil.getUri("dt", "questpathblock12",	"css/questPath.css");
		StringBuilder ruleOptions = new StringBuilder("");
		StringBuilder rewardRuleOptions = new StringBuilder("");
		//String htcPath1 = PlugInUtil.getUri("dt", "questpathblock12",	"htc/PIE.htc");
%>
<bbNG:cssFile href="<%=cssPath1%>" />
<bbNG:cssFile href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<bbNG:pageHeader>
<bbNG:pageTitleBar title="Questpath Configuration"></bbNG:pageTitleBar>
</bbNG:pageHeader>
<bbNG:form action="questpath_save.jsp" method="post" isSecure="false">
	<div id="questpathBlockContainer" class="mainDiv">
	<%
		int j = 0;
		List<String> procQI = new ArrayList<String>();
		for (QuestPath qp : proc.qPaths) {
			for (QuestPathItem qpI : qp.getQuestPathItems()) {
				if (!procQI.contains(qpI.getExtContentId())) {
				QPAttributes qpAtt = new QPAttributes(qpI);
				if (qpI.isGradable()) {ruleOptions.append("<option value='" + qpI.getExtContentId() + "'>" + qpI.getName() + "</option>");}
				if (!qpI.isGradable()) {rewardRuleOptions.append("<option value='" + qpI.getExtContentId() + "'>" + qpI.getName() + "</option>");}
	%>
			<div id="<%=qpI.getExtContentId() %>"
				class="questItem locked"
				title="<%=qpAtt.getTitle()%>" <%if (!qpI.isLocked()) {%>
			<%}%>><%=qpI.getName()%></div>
		<%procQI.add(qpI.getExtContentId());}
			}
			j++;
		}
		for (QuestPathItem qpI : proc.nonQuestItems) {
				if (!procQI.contains(qpI.getExtContentId())) {
					if (qpI.isGradable()) {ruleOptions.append("<option value='" + qpI.getExtContentId() + "'>" + qpI.getName() + "</option>");}
					if (!qpI.isGradable()) {rewardRuleOptions.append("<option value='" + qpI.getExtContentId() + "'>" + qpI.getName() + "</option>");}
	%>
			<div id="<%=qpI.getExtContentId() %>"
				class="questItem nonQuestItem locked" title=""><%}%><%=qpI.getName()%>
			</div>
	<%procQI.add(qpI.getExtContentId());}%>
	
<bbNG:jsBlock>
<script type="text/javascript">
	<%String questString = proc.qpUtil.qpathsToJson(proc.qPaths);%>
	var quests = <%=questString%>;
	var questLayout = <%=proc.qLayout%>;
	var questTier = <%=proc.questTier%>;
	var questsLoaded = true;
	var questDraggable = true;
	var instructorView = false;//prevent quest stats when configuring block
</script>
<script type="text/javascript">
<jsp:include page="js/jquery.min.js" />
<%
String jqUIPath = PlugInUtil.getUri("dt", "questpathblock12",	"js/jquery-ui.min.2.js");
String jtPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/jquery.ui.touch-punch.min.js");
String jsPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/json2.js");
String jpPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/jquery.jsPlumb-1.3.16-all-min.js");
String hcPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/highcharts.js");
String qpPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/questPath.js");
String qiPath   = PlugInUtil.getUri("dt", "questpathblock12",	"js/qpInstructorView.js");
%>
jQuery.getScript('<%=jqUIPath%>', 
	function() {
		jQuery.getScript('<%=jtPath%>', function() { 
			jQuery.getScript('<%=jsPath%>' , function() { 
				jQuery.getScript('<%=jpPath%>', function() { 
					jQuery.getScript('<%=hcPath%>',function() { 
						jQuery.getScript('<%=qpPath%>', function(){ 
							jQuery.getScript('<%=qiPath%>', function(){
								waitForDependencies();buildDialog(); 
	});});});});});});});
</script>
</bbNG:jsBlock>
<div class="saveButton">
<!-- <button id='testButton' type="button" onclick='testGetConnectors();'>Test</button> -->
</div>
</div>
<button type="button" id='ruleButton'>Create a new Adaptive Release</button>
<div id='ruleDialog' title='Add a New Adaptive Release'>
From <select id='fromItem'><%=ruleOptions.toString()%></select><br />
To <select id='toItem'><%=ruleOptions.toString()%><%=rewardRuleOptions.toString() %></select>
<div id="ruleRadio">
    <input type="radio" id="radio1" name="ruleRadio" value='1' checked="checked" /><label for="radio1">Score Based</label>
    <input type="radio" id="radio2" name="ruleRadio" value= '2' /><label for="radio2">Percent Based</label>
</div>
Minimum Score or Percent: <input type='text' id='minValue' size='5'/>
<input type='hidden' id="ruleNumber" value='0' />
</div> 
	<bbNG:dataCollection>
		<bbNG:step title="QuestPath Configuration 1.1" >
			<input type="hidden" id="questLayout" name="questLayout" value='<%=proc.qLayout%>' />
			<input type="hidden" id="newRules" name="newRules" value='' />
			<input type="hidden" name="course_id" value="<%=request.getParameter("course_id")%>" />
		</bbNG:step>
		<bbNG:stepSubmit>
		<bbNG:stepSubmitButton onClick="setLocation();" label="Submit"/>
		</bbNG:stepSubmit>
	</bbNG:dataCollection>
</bbNG:form>
<%} 
else {%>
<h2>Personal Configuration for Students is not currently active, please check back later.</h2>
<%}%>
</bbNG:modulePage>