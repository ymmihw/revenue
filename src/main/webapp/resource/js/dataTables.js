var language = {
	"sProcessing" : "处理中...",
	"sLengthMenu" : "显示 _MENU_ 项结果",
	"sZeroRecords" : "没有匹配结果",
	"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
	"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
	// "sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
	"sInfoFiltered" : "",
	"sInfoPostFix" : "",
	"sSearch" : "搜索:",
	"sUrl" : "",
	"sEmptyTable" : "表中数据为空",
	"sLoadingRecords" : "载入中...",
	"sInfoThousands" : ",",
	"oPaginate" : {
		"sFirst" : "首页",
		"sPrevious" : "上页",
		"sNext" : "下页",
		"sLast" : "末页"
	},
	"oAria" : {
		"sSortAscending" : ": 以升序排列此列",
		"sSortDescending" : ": 以降序排列此列"
	}
};

function defaultDataTable(obj) {
	var defaultFnDrawCallback = function(settings) {
		var api = new $.fn.dataTable.Api(settings);
		if (settings._iDisplayStart > 0 && api.data().length == 0) {
			setTimeout(function() {
				api.page('previous').draw(false);
			}, 0);
		}
	}

	var defaultPara = {
		columnDefs : [ {
			targets : '_all'
		} ],
		pagingType : "input",
		lengthMenu : [ [ 10, 25, 50, 100 ], [ 10, 25, 50, 100 ] ],
		bAutoWidth : true,
		bSort : false,
		bLengthChange : true,
		bFilter : false,
		destroy : true,
		serverSide : true,
		fnDrawCallback : defaultFnDrawCallback,
	}
	var defaultCallbackBuilder = {
		fnDrawCallback : function(f) {
			return function(settings) {
				f.apply(this, arguments);
				defaultFnDrawCallback.apply(this, arguments);
			}
		}
	}

	var copy = clone(defaultPara);

	for (var k in obj) {
		if (typeof obj[k] === 'function') {
			copy[k] = defaultCallbackBuilder[k](obj[k]);
		} else {
			copy[k] = obj[k];
		}

	}
	return copy;
}
function clone(obj) {
	if (null == obj || "object" != typeof obj)
		return obj;
	var copy = obj.constructor();
	for (var attr in obj) {
		if (obj.hasOwnProperty(attr))
			copy[attr] = obj[attr];
	}
	return copy;
}
$.fn.dataTable.render.ellipsis = function() {
	return function(data, type, row) {
		var maxLen = 16;
		return type === 'display' && data != null && data.length > maxLen ? data.substr(0, maxLen) + '…' : data;
	}
};
(function($) {
	function calcDisableClasses(oSettings) {
		var start = oSettings._iDisplayStart;
		var length = oSettings._iDisplayLength;
		var visibleRecords = oSettings.fnRecordsDisplay();
		var all = length === -1;

		// Gordey Doronin: Re-used this code from main jQuery.dataTables source
		// code. To be consistent.
		var page = all ? 0 : Math.ceil(start / length);
		var pages = all ? 1 : Math.ceil(visibleRecords / length);

		var disableFirstPrevClass = (page > 0 ? '' : oSettings.oClasses.sPageButtonDisabled);
		var disableNextLastClass = (page < pages - 1 ? '' : oSettings.oClasses.sPageButtonDisabled);

		return {
			'first' : disableFirstPrevClass,
			'previous' : disableFirstPrevClass,
			'next' : disableNextLastClass,
			'last' : disableNextLastClass
		};
	}

	function calcCurrentPage(oSettings) {
		return Math.ceil(oSettings._iDisplayStart / oSettings._iDisplayLength) + 1;
	}

	function calcPages(oSettings) {
		return Math.ceil(oSettings.fnRecordsDisplay() / oSettings._iDisplayLength);
	}

	var firstClassName = 'first';
	var previousClassName = 'previous';
	var nextClassName = 'next';
	var lastClassName = 'last';

	var paginateClassName = 'paginate';
	var paginateOfClassName = 'paginate_of';
	var paginatePageClassName = 'paginate_page';
	var paginateInputClassName = 'paginate_input';

	$.fn.dataTableExt.oPagination.input = {
		'fnInit' : function(oSettings, nPaging, fnCallbackDraw) {
			var nFirst = document.createElement('span');
			var nPrevious = document.createElement('span');
			var nNext = document.createElement('span');
			var nLast = document.createElement('span');
			var nInput = document.createElement('input');
			var nPage = document.createElement('span');
			var nOf = document.createElement('span');

			var language = oSettings.oLanguage.oPaginate;
			var classes = oSettings.oClasses;

			nFirst.innerHTML = language.sFirst;
			nPrevious.innerHTML = language.sPrevious;
			nNext.innerHTML = language.sNext;
			nLast.innerHTML = language.sLast;

			nFirst.className = firstClassName + ' ' + classes.sPageButton;
			nPrevious.className = previousClassName + ' ' + classes.sPageButton;
			nNext.className = nextClassName + ' ' + classes.sPageButton;
			nLast.className = lastClassName + ' ' + classes.sPageButton;

			nOf.className = paginateOfClassName;
			nPage.className = paginatePageClassName;
			nInput.className = paginateInputClassName;

			if (oSettings.sTableId !== '') {
				nPaging.setAttribute('id', oSettings.sTableId + '_' + paginateClassName);
				nFirst.setAttribute('id', oSettings.sTableId + '_' + firstClassName);
				nPrevious.setAttribute('id', oSettings.sTableId + '_' + previousClassName);
				nNext.setAttribute('id', oSettings.sTableId + '_' + nextClassName);
				nLast.setAttribute('id', oSettings.sTableId + '_' + lastClassName);
			}

			nInput.type = 'text';
			nPage.innerHTML = '页码 ';

			nPaging.appendChild(nFirst);
			nPaging.appendChild(nPrevious);
			nPaging.appendChild(nPage);
			nPaging.appendChild(nInput);
			nPaging.appendChild(nOf);
			nPaging.appendChild(nNext);
			nPaging.appendChild(nLast);

			$(nFirst).click(function() {
				var iCurrentPage = calcCurrentPage(oSettings);
				if (iCurrentPage !== 1) {
					oSettings.oApi._fnPageChange(oSettings, 'first');
					fnCallbackDraw(oSettings);
				}
			});

			$(nPrevious).click(function() {
				var iCurrentPage = calcCurrentPage(oSettings);
				if (iCurrentPage !== 1) {
					oSettings.oApi._fnPageChange(oSettings, 'previous');
					fnCallbackDraw(oSettings);
				}
			});

			$(nNext).click(function() {
				var iCurrentPage = calcCurrentPage(oSettings);
				if (iCurrentPage !== calcPages(oSettings)) {
					oSettings.oApi._fnPageChange(oSettings, 'next');
					fnCallbackDraw(oSettings);
				}
			});

			$(nLast).click(function() {
				var iCurrentPage = calcCurrentPage(oSettings);
				if (iCurrentPage !== calcPages(oSettings)) {
					oSettings.oApi._fnPageChange(oSettings, 'last');
					fnCallbackDraw(oSettings);
				}
			});


			$(nInput).keyup(function(e) {
				var _this = this;
				last = e.timeStamp;
				setTimeout(function() {
					if (last - e.timeStamp === 0) {
						// 38 = up arrow, 39 = right arrow
						if (e.which === 38 || e.which === 39) {
							_this.value++;
						}
						// 37 = left arrow, 40 = down arrow
						else if ((e.which === 37 || e.which === 40) && _this.value > 1) {
							_this.value--;
						}

						if (_this.value === '' || _this.value.match(/[^0-9]/)) {
							/* Nothing entered or non-numeric character */
							_this.value = _this.value.replace(/[^\d]/g, ''); // don't
							// even
							// allow
							// anything
							// but
							// digits
							return;
						}

						var iNewStart = oSettings._iDisplayLength * (_this.value - 1);
						if (iNewStart < 0) {
							iNewStart = 0;
						}
						if (iNewStart >= oSettings.fnRecordsDisplay()) {
							iNewStart = (Math.ceil((oSettings.fnRecordsDisplay()) / oSettings._iDisplayLength) - 1) * oSettings._iDisplayLength;
						}

						oSettings._iDisplayStart = iNewStart;
						fnCallbackDraw(oSettings);
					}
				}, 500);
			});

			// Take the brutal approach to cancelling text selection.
			$('span', nPaging).bind('mousedown', function() {
				return false;
			});
			$('span', nPaging).bind('selectstart', function() {
				return false;
			});

			// If we can't page anyway, might as well not show it.
			var iPages = calcPages(oSettings);
			if (iPages <= 1) {
				$(nPaging).hide();
			}
		},

		'fnUpdate' : function(oSettings) {
			if (!oSettings.aanFeatures.p) {
				return;
			}

			var iPages = calcPages(oSettings);
			var iCurrentPage = calcCurrentPage(oSettings);

			var an = oSettings.aanFeatures.p;
			if (iPages <= 1) // hide paging when we can't page
			{
				$(an).hide();
				return;
			}

			var disableClasses = calcDisableClasses(oSettings);

			$(an).show();

			// Enable/Disable `first` button.
			$(an).children('.' + firstClassName).removeClass(oSettings.oClasses.sPageButtonDisabled).addClass(disableClasses[firstClassName]);

			// Enable/Disable `prev` button.
			$(an).children('.' + previousClassName).removeClass(oSettings.oClasses.sPageButtonDisabled).addClass(disableClasses[previousClassName]);

			// Enable/Disable `next` button.
			$(an).children('.' + nextClassName).removeClass(oSettings.oClasses.sPageButtonDisabled).addClass(disableClasses[nextClassName]);

			// Enable/Disable `last` button.
			$(an).children('.' + lastClassName).removeClass(oSettings.oClasses.sPageButtonDisabled).addClass(disableClasses[lastClassName]);

			// Paginate of N pages text
			// $(an).children('.' + paginateOfClassName).html(' of ' + iPages);

			// Current page numer input value
			$(an).children('.' + paginateInputClassName).val(iCurrentPage);
		}
	};
})(jQuery);