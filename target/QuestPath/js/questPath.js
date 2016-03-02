/**
 * Main Script for Quest Path Building Block
 * Author: Jonathan Leftwich  Graduate Student at Jacksonville State University
 */
/*
 * Initial function to draw connectors after quest items have been put in place
 */
(function() {
	window.jsPlumbDemo = {
			init : function() {			
				var color = "black";
				jsPlumb.importDefaults({
					Connector : [ "Bezier", { curviness:40 } ],
					DragOptions : { cursor: "pointer", zIndex:2000 },
					PaintStyle : { strokeStyle:color, lineWidth:3 },
					EndpointStyle : { radius:3, fillStyle:color },
					HoverPaintStyle : {strokeStyle:"black" },
					EndpointHoverStyle : {fillStyle:"#ec9f2e" },			
					Anchors :  [ "AutoDefault", "AutoDefault" ]
				});
				var arrowCommon = { foldback:1.0, fillStyle:color, width:14 };
				overlays = [[ "Arrow", { location:0.5 }, arrowCommon ]];
				var procQuests = new Array();
				for (var i = 0; i < quests.length; i++) {
					for (var j = 0; j < quests[i].questPathItems.length; j++) {
						if (jQueryAlias.inArray(quests[i].questPathItems[j].extContentId, procQuests) < 0) {
							for (var k = 0; k < quests[i].questPathItems[j].childContent.length; k++) {
								jsPlumb.connect({source:quests[i].questPathItems[j].extContentId,  
									target:quests[i].questPathItems[j].childContent[k], overlays:overlays});
							}
						}
						procQuests.push(quests[i].questPathItems[j].extContentId);
					}
				}
				
				if (questDraggable) {
					jsPlumb.draggable(jsPlumb.getSelector(".questItem"),{containment:"parent"});
				}
			}
	};

})();

/*
 * Move quest items based on saved layout
 */
function moveItems() {
	if (questLayout != null) {
		var initWidth = questLayout.width;
		var currentWidth = document.getElementById('questpathBlockContainer').offsetWidth;
		var widthRatio = currentWidth/initWidth;
		try {
			for (var i = 0; i < questLayout.qItemLayout.length; i++) {
				if (document.getElementById(questLayout.qItemLayout[i].extContentId)) {
					var x = document.getElementById(questLayout.qItemLayout[i].extContentId);
					x.style.top = (parseInt(questLayout.qItemLayout[i].top) * 1) + "px";
					x.style.left = (parseInt(questLayout.qItemLayout[i].left) * widthRatio) + "px";
				}
			}
		} catch(exception) {if (questTier != null) {initLayout();}}//default to init layout if unable to build layout
	} else {
		if (questTier != null) {initLayout();}
	}
};

/*
 * Pause for other script files to be loaded
 */
function waitForDependencies() {
		jsPlumb.bind("ready", function() {
			if (questTier != null) {initLayout();} 
			moveItems(); 
			positionNonQuestItems();
			setTimeout(jsPlumbDemo.init, 0200); 
			jQuery(function() {
		  	    jQuery( "#chartDiv" ).dialog({
		  	      autoOpen: false, modal: true, height:650, width:650,
		  	      buttons: {Ok: function() {jQuery( this ).dialog( "close" );}}
		   		 });
		    });
			jQuery(function() {
			 jQuery( "#questpathBlockContainer").tooltip();
			});
			if(instructorView) {
				setInstructorCSSClass(questStats);
				jQuery('.questItem').click(
		    		function() {
						for (var i = 0; i < questStats.length; i++) {
							if (questStats[i].externalContentId === this.id) {
								//console.log(questStats);
								reportingFunction(questStats[i]);
								jQuery('#chartDiv').dialog("open");
							}
						}
		    		});
			}
		});
}

function openAssignment(link) {
	urlLoc = window.location;
	if (urlLoc.toString().indexOf('detach_module') !== -1) {
		window.location.href = '/webapps/' + link;
	}
	else {
		window.location.href = '/webapps/' + link;
	}

}

/*
 * Set initial location, this is used by config page
 */
function setLocation() {
	var qLayout = new Object();
	qLayout.height = document.getElementById('questpathBlockContainer').offsetHeight;
	qLayout.width = document.getElementById('questpathBlockContainer').offsetWidth;
	qLayout.qItemLayout = new Array();
	var k = 0;
	for (var i = 0; i < quests.length; i++) {
		for (var j = 0; j < quests[i].questPathItems.length; j++) {
			var qItem = new Object();
			qItem.extContentId = quests[i].questPathItems[j].extContentId;
			qItem.top = document.getElementById(quests[i].questPathItems[j].extContentId).style.top;
			qItem.left = document.getElementById(quests[i].questPathItems[j].extContentId).style.left;
			qLayout.qItemLayout[k] = qItem;
			k++;
		}
	}

	var newQuestList = jQuery('.newQuestItem');
	newQuestList.each(function() {
		if (connectionExist(jQuery(this).attr('id'))) {
			var qItem = new Object();
			qItem.extContentId = jQuery(this).attr('id');
			qItem.top = document.getElementById(qItem.extContentId).style.top;
			qItem.left = document.getElementById(qItem.extContentId).style.left;
			qLayout.qItemLayout[k] = qItem;
			k++;
		}
	});
	var nonQuestList = jQuery('.nonQuestItem');
	nonQuestList.each(function() {
		if (connectionExist(jQuery(this).attr('id'))) {
			var qItem = new Object();
			qItem.extContentId = jQuery(this).attr('id');
			qItem.top = document.getElementById(qItem.extContentId).style.top;
			qItem.left = document.getElementById(qItem.extContentId).style.left;
			qLayout.qItemLayout[k] = qItem;
			k++;
		}
	});
	document.getElementById("questLayout").value = JSON.stringify(qLayout);
	document.getElementById("newRules").value = JSON.stringify(newRules);
}

/*
 * Redraw as page is resized
 */
window.onresize=function(){moveItems(); jsPlumbDemo.init(); positionNonQuestItems();};

/*
 * Build initial layout graph
 */
function initLayout() {
	init_height = document.getElementById('questpathBlockContainer').offsetHeight;
	init_width = document.getElementById('questpathBlockContainer').offsetWidth;
	pathWidth = init_width/questTier.length;
	for (var i = 0; i < questTier.length; i++) {
		for (var j = 0; j < questTier[i].length; j++) {
			tierWidth = (pathWidth/questTier[i][j].tier.length);
			tierWidthCenter = tierWidth/2;
			for (var k = 0; k < questTier[i][j].tier.length; k++) {
				var x = document.getElementById(questTier[i][j].tier[k]);
				x.style.left = pathWidth * i  + (tierWidth * (k +1)) - tierWidthCenter  - (init_width/20) + "px";
				x.style.top =  init_height/questTier[i].length * j + "px";
			}
		}
	}
}

var totalStudents = 0;

function setInstructorCSSClass(questStats) {
	for (var i = 0; i < questStats.length; i++) {
		var hybridPassed = questStats[i].passedCount;
		totalStudents = questStats[i].passedCount + questStats[i].attemptedCount + questStats[i].lockedCount;
		var percentPassed = Math.round(hybridPassed/totalStudents / .1) * 10;
		if (totalStudents === 0) {
			percentPassed = 0;
		}
		jQuery('#' + questStats[i].externalContentId).addClass('p' + percentPassed);
	}
}

var newRules = Array();

function buildDialog() {
	jQuery( "#ruleDialog" ).dialog({
	      autoOpen: false,
	      height: 300,
	      width: 500,
	      modal: true, 
	      buttons: {
	    	  "Save": function() {
	    		  if (validateRules()) {
	    			  jQuery( this ).dialog( "close" );
	    		  }
	           },
	         "Save and Add Another Criteria": function() {
	    		  if (validateRules()) {
	    			  initDialog(true);
	    		  };
	         },
	         "Cancel": function() {
	        	 jQuery( this ).dialog( "close" );
	         }
	      }
	});
	jQuery('#ruleButton').on('click', function() {
		jQuery('#ruleNumber').val((new Date).getTime());
		initDialog(false);
		jQuery('#ruleDialog').dialog("open");
	});
}

function initDialog(disableTo) {

	if (disableTo) {
		jQuery('#toItem').prop('disabled', true);
	}else  {
		jQuery('#toItem').prop('disabled', false);
		//This logic only allows items without adaptive release to be in the TO drop down
		//jQuery('#toItem').empty();
		//var nonQuestList = jQuery('.nonQuestItem');
		//nonQuestList.each(function() {
		//	jQuery('#toItem')
	    //   .append(jQuery("<option></option>")
	    //    .attr("value",jQuery(this).attr('id'))
	    //    .text(jQuery(this).html())); 
		//});
	}
	jQuery('#minValue').val("" );
}

function validateRules() {
	  var errorFree = true;
	  var fromId = jQuery('#fromItem').val();
	  var toId = jQuery('#toItem').val();
	  var typeRule = jQuery( "input:radio[name=ruleRadio]:checked" ).val();
	  var minValue = jQuery('#minValue').val();
	  if (fromId.length === 0) {
		  alert("From Quest Item Required");
		  errorFree = false;
	  }
	  if (toId.length === 0) {
		  alert("To Quest Item Required");
		  errorFree = false;
	  }
	  if (toId === fromId) {
		  alert("From and To Must Be Different Items");
		  errorFree = false;
	  }
	  if (typeRule.length == 0) {
		  alert("Adaptive Release Type Rule Required");
		  errorFree = false;
	  }
	  if (isNaN(minValue) || minValue.length === 0 || minValue === 0) {
		  alert("Minimum Score Must Be Numeric and Greater Than 0");
		  errorFree = false;
	  }
	  if (errorFree) {
		  var rule = new Object();
		  rule.fromId = fromId;
		  rule.toId = toId;
		  console.log("From " + fromId + " to " + toId);
		  if(createsLoop(fromId, toId)){
			  alert("Connection Will Create a Loop. Please Check Your Graph.")
			  return;
		  }
		  rule.typeRule = typeRule;
		  rule.minValue = minValue;
		  rule.ruleNumber = jQuery('#ruleNumber').val();
		  console.log(rule);
		  newRules.push(rule);
		  buildNewConnection(fromId, toId);
	  };
	  return errorFree;
}

function buildNewConnection(fromId, toId) {
	jQuery('#' + toId).addClass("questItem").addClass("newQuestItem");
	var color = "red";
	jsPlumb.importDefaults({
		Connector : [ "Bezier", { curviness:40 } ],
		DragOptions : { cursor: "pointer", zIndex:2000 },
		PaintStyle : { strokeStyle:color, lineWidth:3 },
		EndpointStyle : { radius:3, fillStyle:color },
		HoverPaintStyle : {strokeStyle:"red" },
		EndpointHoverStyle : {fillStyle:"#ec9f2e" },			
		Anchors :  [ "AutoDefault", "AutoDefault" ]
	});
	var arrowCommon = { foldback:1.0, fillStyle:color, width:14 };
	var overlays = [[ "Arrow", { location:0.5 }, arrowCommon ]];
	//TODO what gets returned here?
	jsPlumb.connect({source:fromId, target:toId, overlays:overlays}).bind("dblclick", function(connection, originalEvent)  {removeNewConnection(connection);});
	if (questDraggable) {
		jsPlumb.draggable(jsPlumb.getSelector(".questItem"),{containment:"parent"});
	}
}

function removeNewConnection(connection) {
	var c = confirm("Confirm Deletion!");
	var len = newRules.length;
	if (c === true) {
		while (len--) {
			if (newRules[len].toId === connection.targetId && newRules[len].fromId === connection.sourceId) {
				newRules.splice(len,1);
			}
		}
		jQuery('#' + connection.targetId).addClass("questItem").removeClass("newQuestItem");
		jQuery('#' + connection.targetId).addClass("questItem").addClass("nonQuestItem");
		jsPlumb.detach(connection);
	}
}

function positionNonQuestItems() {
	nonQuestList = jQuery('.nonQuestItem');
	init_height = document.getElementById('questpathBlockContainer').offsetHeight;
	init_width = document.getElementById('questpathBlockContainer').offsetWidth;
	var newTop = init_height - init_height/15;
	var newLeft = 0;
	var count = 0;
	nonQuestList.each(function() {
		document.getElementById(jQuery( this ).attr('id')).style.top = newTop + "px";
		document.getElementById(jQuery( this ).attr('id')).style.left = newLeft + "px";
		count++;
		newLeft += init_width/10;
		if (count === 9) {
			newLeft = 0;
			newTop = newTop - init_height/15;
			count = 0;
		}
	});
}

//TODO modify to allow source to be newQuestItem

function connectionExist(id) {
	var targetArray = new Array();
	targetArray.push(id);
	var elementList = jsPlumb.selectEndpoints({
		element: targetArray 
	});
	return (elementList.length > 0) ? "true" : "false";
}

function createsLoop(fromId, toId){
	var allRules = Array();
	var items = Array();
	var children = Array();
	for (var i = 0; i < quests.length; i++) {
		for (var j = 0; j < quests[i].questPathItems.length; j++) {
			for(var l = 0; l < quests[i].questPathItems[j].childContent.length; l++){
				var rule = new Object();
				rule.fromId = quests[i].questPathItems[j].extContentId;
				rule.toId = quests[i].questPathItems[j].childContent[l];
				allRules.push(rule);
			}
			
		}
	}
	for(var i = 0; i < newRules.length; i++){
		var rule = new Object();
		rule.fromId = newRules[i].fromId;
		rule.toId = newRules[i].toId;
		allRules.push(rule); 
	}
	var rule = new Object();
	rule.fromId = fromId;
	rule.toId = toId;
	return loopExists(allRules, rule, fromId);
}

function loopExists(allRules, rule, start){
	for(var i = 0; i < allRules.length; i++){
		if(allRules[i].fromId == rule.toId){
			if(allRules[i].toId == start){
				return true;
			}
			else{
				var newRule = new Object();
				newRule.fromId = allRules[i].fromId;
				newRule.toId = allRules[i].toId;
				if(loopExists(allRules, newRule, start)){
					return true;
				}
			}
		}
	}
	return false;
}