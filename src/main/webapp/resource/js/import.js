$(document).ready(init);

function init() {
	var a = $.fn.fileinputLocales['zh'];
	a.uploadLabel = '导入';
	a.msgPlaceholder = '选择{files}...';
	$.fn.fileinputLocales['zh'] = a;

	$("#file").fileinput({
		language : "zh",
		showPreview : true,
		maxFileSize : 10000,
		allowedFileExtensions : [ "xlsx" ],
	});
}