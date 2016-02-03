var questStats = [{"attemptedCount":1,"passedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan"],"passedCount":2,"lockedStudents":[],"lockedCount":0,"attemptedStudents":["Student, JBL"],"externalContentId":"_20_1"},{"attemptedCount":0,"passedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan"],"passedCount":2,"lockedStudents":["Student, JBL"],"lockedCount":1,"attemptedStudents":[],"externalContentId":"_21_1"},{"attemptedCount":2,"passedStudents":[],"passedCount":0,"lockedStudents":["Student, JBL"],"lockedCount":1,"attemptedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan"],"externalContentId":"_50_1"},{"attemptedCount":0,"passedStudents":[],"passedCount":0,"lockedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan","Student, JBL"],"lockedCount":3,"attemptedStudents":[],"externalContentId":"_24_1"},{"attemptedCount":0,"passedStudents":[],"passedCount":0,"lockedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan","Student, JBL"],"lockedCount":3,"attemptedStudents":[],"externalContentId":"_104_1"},{"attemptedCount":0,"passedStudents":[],"passedCount":0,"lockedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan","Student, JBL"],"lockedCount":3,"attemptedStudents":[],"externalContentId":"_105_1"},{"attemptedCount":2,"passedStudents":["Leftwich - Student, Jonathan"],"passedCount":1,"lockedStudents":[],"lockedCount":0,"attemptedStudents":["Leftwich, Stephanie","Student, JBL"],"externalContentId":"_46_1"},{"attemptedCount":1,"passedStudents":[],"passedCount":0,"lockedStudents":["Leftwich, Stephanie","Student, JBL"],"lockedCount":2,"attemptedStudents":["Leftwich - Student, Jonathan"],"externalContentId":"_47_1"},{"attemptedCount":0,"passedStudents":[],"passedCount":0,"lockedStudents":["Leftwich, Stephanie","Leftwich - Student, Jonathan","Student, JBL"],"lockedCount":3,"attemptedStudents":[],"externalContentId":"_49_1"}];
var jQueryreporting;

var reportingFunction = function (dataSeries) {
	//console.log("what was sent " + dataSeries);
	jQueryReporting = jQuery('#reporting'); 
	jQueryReporting.html("Total Students: " + totalStudents); 
	chart = new Highcharts.Chart({
		chart: {
			renderTo: 'container',
			plotBackgroundColor: 'white',
			plotBorderWidth: null,
			plotShadow: true,
			backgroundColor: '#FFFFCC'
		},
		credits: {enabled : false},
		title: {text: 'Current Progress for ' + (jQuery('#' + dataSeries.externalContentId).html())},
		tooltip: {animation: false,enabled: false},
		exporting: {enabled : false},
		plotOptions: {
			pie: {
				allowPointSelect: true,
				cursor: 'pointer',
				dataLabels: {
					enabled: true
					,color: '#000000'
					,connectorColor: '#000000'
					,formatter: function() {
                    return '<b>'+ this.point.name +'</b>: '+ this.percentage.toFixed(2) +' %';
					}
				}	
			}
		},
		series: [{
			type: 'pie',
			data: [
			{
				name: 'Attempted',
				y: dataSeries.attemptedCount,
				list: dataSeries.attemptedStudents,
				color: '#FFFF00'
			}, {
				name: 'Passed',
				y: dataSeries.passedCount,
				list: dataSeries.passedStudents,
				color: '#00FF00'
			}, {
				name: 'Locked',
				y: dataSeries.lockedCount,
				list: dataSeries.lockedStudents,
				color: '#FF0000'
			}
			],
			point: {
				events: {
					unselect: function() {
						//jQueryReporting.html("");
						//console.log("Unselect");
						//console.log(event);
					},
					select: function() {
						//console.log("Select");
						//console.log(event);
						jQueryReporting.hide();
						_line = 'Total Students: ' + totalStudents + '    ' + this.name + ' Students: ' + this.y + '<ul>'; 
						//_names = "";
						jQuery(this.list).each(
								function() {
									_line += '<li>' + this + '</li>';
								}
						);
						_line += '</ul>';
						jQueryReporting.html(_line);
						jQueryReporting.show();
					}
				}
			}
		}]
});

};

jQuery(document).ready(function() {
	moveItems(); 
	jsPlumbDemo.init();
// 	jQuery('#chartDiv').mouseleave(function() {jQuery('#reporting').hide();});
    jQuery(function() {
  	    jQuery( "#chartDiv" ).dialog({
  	      autoOpen: false, modal: true, height:650, width:650,
  	      buttons: {Close: function() {jQuery( this ).dialog( "close" );}}
   		 });
    });
    setInstructorCSSClass(questStats);
    jQuery('.questItem').click(
	  function() {
			for (var i = 0; i < questStats.length; i++) {
				if (questStats[i].externalContentId === this.id) {
					//console.log(questStats[i]);
					reportingFunction(questStats[i]);
					jQuery('#chartDiv').dialog("open");
				}
			}
	  }
	);
});

function setInstructorCSSClass(questStats) {
	for (var i = 0; i < questStats.length; i++) {
		var hybridPassed = questStats[i].passedCount;
		var totalStudents = questStats[i].passedCount + questStats[i].attemptedCount + questStats[i].lockedCount;
		var percentPassed = Math.round(hybridPassed/totalStudents / .1) * 10;
		jQuery('#' + questStats[i].externalContentId).addClass('p' + percentPassed);
	}
}

totalStudents = 3;