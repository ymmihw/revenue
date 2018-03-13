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
		'<td class="left">请求时间:</td>' +
		'<td>' + d.requestOn + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">请求数据:</td>' +
		'<td>' + d.requestPayload + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">响应时间:</td>' +
		'<td>' + d.responseOn + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">响应数据:</td>' +
		'<td>' + d.responsePayload + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">请求方式:</td>' +
		'<td>' + d.method + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">请求路径:</td>' +
		'<td>' + d.uri + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">请求人:&nbsp;&nbsp;&nbsp;</td>' +
		'<td>' + d.user + '</td>' +
		'</tr>' +
		'<tr>' +
		'<td class="left">是否成功:</td>' +
		success +
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
					data : "idCardNo"
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
					d.idCardNo = $('#idCardNo').val();
					return JSON.stringify(d);
				}
			}),
			language : language
		}));
}