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
<%@page import="blackboard.data.course.CourseMembership"%>
<%@page import="blackboard.platform.plugin.PlugInUtil"%>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.jsu.cs521.questpath.buildingblock.util.*" %>
<%@page import="com.jsu.cs521.questpath.buildingblock.object.*" %>
<%@page import="com.jsu.cs521.questpath.buildingblock.engine.*" %>
<%@page import="com.jsu.cs591.web.util.QPAttributes"%>
<%@ taglib uri="/bbData" prefix="bbData"%>
<%@ taglib uri="/bbNG" prefix="bbNG"%>
<!-- for tags -->
<%-- <bbData:context id="ctx">  --%>
<bbNG:includedPage ctxId="ctx">
	<!-- to allow access to the session variables -->
	<%
			String cssPath1 = PlugInUtil.getUri("dt", "questpathblock",	"css/questPath.css");
			String htcPath1 = PlugInUtil.getUri("dt", "questpathblock",	"htc/PIE.htc");
			Processor proc = new Processor();
			proc.QPDriver(ctx);
	%>
<bbNG:cssFile href="<%=cssPath1%>"/>
<bbNG:cssFile href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<bbNG:cssBlock>
<style>
.questItem {
	behavior: url(<%=htcPath1 %>);
	}
</style>
</bbNG:cssBlock>
<body>
<div id="questpathBlockContainer" class="mainDiv">
<%
	int j = 0;
	List<String> procQI = new ArrayList<String>();
	for (QuestPath qp : proc.qPaths) { 
		for (QuestPathItem qpI : qp.getQuestPathItems()) {
			if (!procQI.contains(qpI.getExtContentId())) {
			QPAttributes qpAtt = new QPAttributes(qpI);
%>
			<div id="<%=qpI.getExtContentId()%>" class="questItem <%=qpAtt.getStatusClassName() %>"
			title="<%=qpAtt.getTitle()%>" 
			<% if (!qpI.isLocked() && !proc.isUserAnInstructor) { %>
			ondblclick="openAssignment('assignment/uploadAssignment?content_id=<%=qpI.getExtContentId()%>&course_id=<%=ctx.getCourseId().toExternalString()%>&assign_group_id=&mode=view');"
			<%} %>><%=qpI.getName()%></div>
		<%
			procQI.add(qpI.getExtContentId());
			}	
		}
		j++;
	}
%>
<bbNG:jsBlock>
<script type="text/javascript">
<%String questString = proc.qpUtil.qpathsToJson(proc.qPaths);%>
var quests = <%=questString%>;
var questLayout = <%=proc.qLayout%>;
var questTier = <%=proc.questTier%>;
var questsLoaded = true;
var questDraggable = false;
var instructorView = <%=proc.isUserAnInstructor%>;
<%
String statString = "jQuery.noop";
if (proc.isUserAnInstructor) {
	statString = proc.qpUtil.statsToJson(proc.courseStats);
}
%>
var questStats = <%=statString %>;
</script>
<script type="text/javascript">
<jsp:include page="js/jquery.min.js" />
<%
String jqUIPath = PlugInUtil.getUri("dt", "questpathblock",	"js/jquery-ui.min.2.js");
String jtPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/jquery.ui.touch-punch.min.js");
String jsPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/json2.js");
String jpPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/jquery.jsPlumb-1.3.16-all-min.js");
String hcPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/highcharts.js");
String qpPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/questPath.js");
String qiPath   = PlugInUtil.getUri("dt", "questpathblock",	"js/qpInstructorView.js");
%>
jQuery.getScript('<%=jqUIPath%>', 
	function() {
		jQuery.getScript('<%=jtPath%>', function() { 
			jQuery.getScript('<%=jsPath%>' , function() { 
				jQuery.getScript('<%=jpPath%>', function() { 
					jQuery.getScript('<%=hcPath%>',function() { 
						jQuery.getScript('<%=qpPath%>', function(){ 
							jQuery.getScript('<%=qiPath%>', function(){
								waitForDependencies();});						
	});});});});});});
</script>
</bbNG:jsBlock>
<% if (proc.isUserAnInstructor) {%>
<div class="legend"><h5>LEGEND - Task Completeness</h5>
<div class="legendColor p100">100%</div>
<div class="legendColor p90">90%</div>
<div class="legendColor p80">80%</div>
<div class="legendColor p70">70%</div>
<div class="legendColor p60">60%</div>
<div class="legendColor p50">50%</div>
<div class="legendColor p40">40%</div>
<div class="legendColor p30">30%</div>
<div class="legendColor p20">20%</div>
<div class="legendColor p10">10%</div>
<div class="legendColor p0">0%</div>
</div>
<%} else { %>
<div class="legend"><h5>LEGEND</h5>
<div class="legendColor passed">Passed</div>
<div class="legendColor unlockedLegend">Unlocked</div>
<div class="legendColor locked">Locked</div>
</div>
<%} %>
</div>
<div id='chartDiv' title="Quest Item" class='chartDiv'>
	<div id="container" style="height: 400px; width: 600px"></div>
	<div id="reporting"></div>
</div>
<!--%=//statString %-->
<%=proc.debugString %>
</body>
</bbNG:includedPage>