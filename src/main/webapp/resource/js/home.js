var table;
$(document).ready(initDataTable);
$(document).ready(function() {
	$('#searchButton').on('click', function() {
		table.draw();
	});

});

$('#dataTable tbody').on('click', 'td.details-control', function() {
	var tr = $(this).closest('tr');
	var row = table.row(tr);
	if (row.child.isShown()) {
		// This row is already open - close it
		row.child.hide();
		tr.removeClass('shown');
	} else {
		// Open this row
		row.child(format(row.data())).show();
		tr.addClass('shown');
	}
});

function format(d) {
	// `d` is the original data object for the row
	var success;
	if (d.success) {
		success = '<td class="successful">成功</td>';
	} else {
		success = '<td class="failure">失败</td>';
	}
	return '<table class="details">' +
		'<tr>' +
		'<td class="left">权利人:</td>' +
		'<td>' + d.owner + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">证件号码:</td>' +
		'<td>' + d.ownerId + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">不动产单元号:</td>' +
		'<td>' + d.estateId + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">坐落:</td>' +
		'<td>' + d.location + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">面积:</td>' +
		'<td>' + d.area + '</td>' +
		'</tr>' +
		'</table>';
}

function redrawTable() {
	table.draw(false);
}

function initDataTable() {
	table = $('#dataTable').DataTable(
		defaultDataTable({
			columns : [
				{
					data : "owner"
				},
				{
					data : "ownerId"
				},
				{
					data : "estateId"
				},
				{
					data : "area"
				},
				{
					sClass : 'details-control center',
					orderable : false,
					data : null,
					width : "36px",
					defaultContent : ''
				} ],
			ajax : getPostJson({
				url : "../doSearch",
				data : function(d) {
					d.draw = undefined;
					d.columns = undefined;
					d.order = undefined;
					d.search = undefined;
					d.ownerId = $('#ownerId').val();
					return JSON.stringify(d);
				}
			}),
			language : language
		}));
}