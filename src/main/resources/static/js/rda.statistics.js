let statisticsViewToggle = () => {
	if (document.querySelector('.statistics-search-view')) {
		document.querySelector('.statistics-search-view').classList.add('none');
	}
	if (document.querySelector('.statistics-list-box')) {
		document.querySelector('.statistics-list-box').classList.remove('none');
	}
}

//OD start
let statisticsType = (_this) => {
	try {
		let originWrap = document.getElementById('origin');
		let destinationWrap = document.getElementById('destination');

		if(originWrap && destinationWrap){
			originWrap.textContent = '';
			destinationWrap.textContent = '';
		}

		const loading = new setLoading().start();

		Promise.all([
			rdaLocation('origin', 'gnNm', 2, 3, 'statistic-search-od'),
			rdaLocation('destination', 'gnNm', 2, 3, 'statistic-search-od')
		]).then(() => {
			loading.end();
		}).catch(error => {
			loading.end();
		});
	} catch(error) {
		consloe.log("에러 발생")
	}
}

let changeSearchData = () => {
	let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
	const queryString = new URLSearchParams(searchSurveyForm).toString();
	fetch(__contextPath__ + "/statistics/dashboard/sheet/search?" + queryString)
		.then((response) => response.json())
		.then((result) => {
			const sheetList = result.data.sheetList;

			let searchSrvyId = document.getElementById("searchSrvyId");
			let sheetHtml = `<option value="">${message.statistics.mccStatisticsDashboard_rank}</option>`;
			if (!isNull(sheetList)) {
				for (let item of sheetList) {
					sheetHtml += `<option value="${item.srvyId}">${item.srvyTitle}</option>`;
				}

			}
			searchSrvyId.innerHTML = sheetHtml;

			const years = result.data.years;
			let searchDate = document.getElementById("searchDate");
			let searchDateHtml = `<option value="">${message.statistics.mccStatisticsDashboard_rank}</option>`;
			if (!isNull(years)) {
				for (let item of years) {
					searchDateHtml += `<option value="${item}">${item}</option>`;
				}
			}
			searchDate.innerHTML = searchDateHtml;
		})
}

let generateOptions = (maxHour) => {
	let optionEl = `<option value=''>${message.common.select}</option>`;
	for (let i = 0; i <= maxHour; i++) {
		if (i < 10) {
			optionEl += `<option value="0${i}">0${i}${message.common.hour}</option>`;
		} else {
			optionEl += `<option value="${i}">${i}${message.common.hour}</option>`;
		}
	}
	return optionEl;
};

function getSiteIdList() {
	let data = null;
	let searchSurveyType = document.getElementById('searchSurveyType').value;
	let searchDateType = document.getElementById('searchDateType').value;
	let startDate = document.getElementsByName('startDate') != null ? document.getElementsByName('startDate') : null;
	let endDate = document.getElementsByName('endDate') != null ? document.getElementsByName('endDate') : null;
	
	//if time일때 Hour추가
	if(searchDateType == 'time'){
		let startHour = document.getElementsByName('startHour') != null?document.getElementsByName('startHour') : null;
		let endHour = document.getElementsByName('endHour') != null?document.getElementsByName('endHour') : null;
		
		 data = {
			searchSurveyType: searchSurveyType,
			searchDateType: searchDateType,
			startDate: startDate[0].value,
			endDate: endDate[0].value,
			startHour : startHour[0].value,
			endHour : endHour[0].value
		}
	} else {
		 data = {
			searchSurveyType: searchSurveyType,
			searchDateType: searchDateType,
			startDate: startDate[0].value,
			endDate: endDate[0].value
		}
	}

	let url = `${__contextPath__}/statistics/dashboard/period/list`;
	const loading = new setLoading().start('loading...');

	fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(data)
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then((result) => {
			let siteIdWrap = document.getElementById('select-siteId-wrap');
			let siteIdSelect = document.getElementById('select-siteId');

			siteIdSelect.innerHTML = '';

			let defaultOption = document.createElement('option');
			defaultOption.value = "";
			defaultOption.text = "선택";
			siteIdSelect.appendChild(defaultOption);

			if (result != null) {
				let siteIdDTO = result.data;
				if (siteIdDTO != null && siteIdDTO.length > 0) {
					siteIdDTO.forEach(function(item) {
						let option = document.createElement('option');
						option.value = item.instllcId;
						option.text = item.instllcNm;
						siteIdSelect.appendChild(option);
					});
					siteIdWrap.classList.remove('none');
					siteIdSelect.classList.add('validation-tag');

				} else {
					siteIdWrap.classList.add('none');
					siteIdSelect.classList.remove('validation-tag');
				}
			}
			siteIdWrap.classList.remove('none');
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
}

function surveyType(_this) {
	let searchDateType = document.getElementById('searchDateType');
	let siteIdWrap = document.getElementById('select-siteId-wrap');

	siteIdWrap.classList.add('none');

	searchDateType.value = '';

	let datePickerWrap = document.getElementById('date-picker-wrap');

	datePickerWrap.textContent = '';
}

function dateType(_this) {
	let dateType = _this.value;
	let appendWrap = document.getElementById('date-picker-wrap');

	let startInput = document.createElement('input');
	let endInput = document.createElement('input');

	let siteIdWrap = document.getElementById('select-siteId-wrap');

	siteIdWrap.classList.add("none");

	startInput.type = 'text';
	startInput.readOnly = true;
	// startInput.setAttribute('onchange','getSiteIdList()');
	startInput.onchange = getSiteIdList;
	endInput.type = 'text';
	endInput.readOnly = true;
	// endInput.setAttribute('onchange','getSiteIdList()');
	endInput.onchange = getSiteIdList;

	let startSelect = document.createElement('select');
	let endSelect = document.createElement('select');

	let startDateWrap = document.createElement('div');
	startDateWrap.className = 'statistics-search-item';

	let startTitleWrap = document.createElement('h4');
	startTitleWrap.className = 'statistics-search-title';
	startTitleWrap.textContent = message.statistics.statistics_js_start_date;

	let endDateWrap = document.createElement('div');
	endDateWrap.className = 'statistics-search-item';

	let endTitleWrap = document.createElement('h4');
	endTitleWrap.className = 'statistics-search-title';
	endTitleWrap.textContent = message.statistics.statistics_js_end_date;

	startDateWrap.append(startTitleWrap);
	endDateWrap.appendChild(endTitleWrap);

	appendWrap.textContent = '';

	switch (dateType) {
		case 'year':
			startSelect.className = 'dateYear scroll table-select-97 select-list-box validation-tag';
			startSelect.name = 'startDate';
			startSelect.setAttribute('data-validation', 'select');

			endSelect.className = 'dateYear scroll table-select-97 select-list-box validation-tag';
			endSelect.name = 'endDate';
			endSelect.setAttribute('data-validation', 'select');

			startDateWrap.appendChild(startSelect);
			endDateWrap.appendChild(endSelect);

			appendWrap.appendChild(startDateWrap);
			appendWrap.appendChild(endDateWrap);

			dateYearSetMinAndMax();
			break;
		case 'month':
			startInput.classList.add('input-table-text', 'monthStartPicker', 'validation-tag', 'month-start-input');
			startInput.name = "startDate";
			startInput.setAttribute('data-type', 'statistics');
			startInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_start_date);
			endInput.classList.add('input-table-text', 'monthEndPicker', 'validation-tag', 'month-end-input');
			endInput.name = "endDate";
			endInput.setAttribute('data-type', 'statistics');
			endInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_end_date);

			startDateWrap.appendChild(startInput);
			endDateWrap.appendChild(endInput);

			appendWrap.appendChild(startDateWrap);
			appendWrap.appendChild(endDateWrap);
			monthPicker();
			break;
		case 'day':
			startInput.classList.add('input-table-text', 'startPullPicker', 'validation-tag');
			startInput.name = "startDate";
			startInput.setAttribute('data-type', 'statistics');
			startInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_start_date);
			endInput.classList.add('input-table-text', 'endPullPicker', 'validation-tag');
			endInput.name = "endDate";
			endInput.setAttribute('data-type', 'statistics');
			endInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_end_date);

			startDateWrap.appendChild(startInput);
			endDateWrap.appendChild(endInput);

			appendWrap.appendChild(startDateWrap);
			appendWrap.appendChild(endDateWrap);

			datePickerSet();
			break;
		case 'time':
			startInput.classList.add('input-table-text', 'startPullPicker', 'mb16', 'validation-tag');
			startInput.name = "startDate";
			startInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_start_date);
			endInput.classList.add('input-table-text', 'endPullPicker', 'mb16', 'validation-tag');
			endInput.name = "endDate";
			endInput.setAttribute('placeholder', message.statistics.statistics_js_placeholder_end_date);

			startSelect.className = 'select-list-box table-select-97 validation-tag';
			startSelect.name = 'startHour';
			startSelect.setAttribute('data-validation', 'select');
			endSelect.className = 'select-list-box table-select-97 validation-tag';
			endSelect.name = 'endHour';
			endSelect.setAttribute('data-validation', 'select');

			startSelect.innerHTML = generateOptions(23);
			endSelect.innerHTML = generateOptions(23);

			let startTimeWrap = document.createElement('div');
			let startTimeTitleWrap = document.createElement('h6');
			startTimeTitleWrap.className = 'statistics-search-title';
			startTimeTitleWrap.textContent = message.statistics.statistics_js_start_time;

			let endTimeWrap = document.createElement('div');
			let endTimeTitleWrap = document.createElement('h6');
			endTimeTitleWrap.className = 'statistics-search-title';
			endTimeTitleWrap.textContent = message.statistics.statistics_js_end_time;

			startTimeWrap.appendChild(startTimeTitleWrap);
			startTimeWrap.appendChild(startSelect);
			endTimeWrap.appendChild(endTimeTitleWrap);
			endTimeWrap.appendChild(endSelect);

			startDateWrap.appendChild(startInput);
			startDateWrap.appendChild(startTimeWrap);
			endDateWrap.appendChild(endInput);
			endDateWrap.appendChild(endTimeWrap);

			appendWrap.appendChild(startDateWrap);
			appendWrap.appendChild(endDateWrap);

			datePickerSet();
			break;
	}

	///년도 선택시
	if (startSelect && endSelect) {
		startSelect.addEventListener('change', function() {
			if (startSelect.value !== '' && endSelect.value !== '') {
				getSiteIdList();
			}
		});

		endSelect.addEventListener('change', function() {
			if (startSelect.value !== '' && endSelect.value !== '') {
				getSiteIdList();
			}
		});
	}
}

function searchPeroidStatistics() {
	const statisticsListBox = document.querySelector(".statistics-list-box");
	const statisticsSearchBox = document.querySelector('.statistics-search-view');
	const siteId = document.getElementById('select-siteId');

	if (validation('#searchSurveyForm')) {
		const loading = new setLoading().start();
		fetch(`${__contextPath__}/statistics/dashboard/period/stats`, {
			method: "GET",
			headers: {
				'Accept': 'text/html'
			}
		}).then((response) => {
				if (!response.ok) {
					throw new Error('Network response was not ok ' + response.statusText);
				}
				return response.text();
		}).then((html) => {
				statisticsListBox.textContent = '';
				statisticsListBox.innerHTML = html;
	
				let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
				const params = new URLSearchParams(searchSurveyForm);
				const queryString = params.toString();
				//const queryString = "searchSurveyType=METROCOUNT_FIXED&searchDateType=month&startDate=2024-02&endDate=2024-08&searchContent=4";
				fetch(`${__contextPath__}/statistics/dashboard/period/chart?${queryString}`, {
					method: "GET"
				}).then((response) => {
					if (!response.ok) {
						throw new Error('Network response was not ok ' + response.statusText);
					}
					return response.json();
				}).then((result) => {
				if (result.data != null) {
					let chartData = result.data.chartData;
					if (chartData != null && chartData.length > 0) {
						const searchDateType = params.get('searchDateType');

						let directionList = result.data.directionList;
						let trfvlmSelect = document.getElementById('trfvlmDrctCd');
						let avgSpdSelect = document.getElementById('avgSpdDrctCd');

						directionList.forEach(direction => {
							let optionElement = document.createElement('option');
							optionElement.value = direction.cd;
							optionElement.text = direction.cdNm;

							trfvlmSelect.appendChild(optionElement.cloneNode(true));
							avgSpdSelect.appendChild(optionElement);
						});
						let groupedData = {};
						chartData.forEach(data => {
							let key = `${data.instllcNm}-${data.cdNm}`;
							let statsDate = searchDateType != 'day' ?formatStatsDate(data.statsDate, searchDateType) : message.date.dayNamesMin[data.dayOfTheWeek];

							if (!groupedData[key]) {
								groupedData[key] = {
									instllcNm: data.instllcNm,
									drctCd: data.drctCd,
									cdNm : data.cdNm,
									dataByDate: {}
								};
							}
							groupedData[key].dataByDate[statsDate] = {
								trfvlm: data.trfvlm,
								avgSpeed: data.avgSpeed
							};
						});

						let labels = null;

						if(searchDateType == 'day'){
							labels = [...new Set(chartData.map(data => message.date.dayNamesMin[data.dayOfTheWeek]))];
						} else {
							labels =  [...new Set(chartData.map(data => formatStatsDate(data.statsDate, searchDateType).toUpperCase()))];
						}
						let createDataset = (dataKey, color) => {
							let datasets = [];
							for (let key in groupedData) {
								let group = groupedData[key];
								datasets.push({
									label: `${group.instllcNm} - ${group.cdNm}`,
									data: labels.map(label => (group.dataByDate[label] ? group.dataByDate[label][dataKey] : 0)),
									backgroundColor: color,
									borderRadius: 1,
									borderWidth: 1,
									fill: false
								});
							}
							return datasets;
						};

						let trfvlmDatasets = createDataset('trfvlm', '#FF6384');
						let avgSpeedDatasets = createDataset('avgSpeed', '#36A2EB');

						['trfvlm-chart', 'avg-speed-chart'].forEach((id, index) => {
							new rdaChart(ChartType.LINE).init(id)
								.setDataSetArrayLabel(labels)
								.setDataArraySet(index === 0 ? trfvlmDatasets : avgSpeedDatasets)
								.setTicksStep(0)
								.setBarGridY(false)
								.setBarGridX(true)
								.draw();
						});

						let trfvlmTableWrap = document.getElementById('trfvlm-table');
						let avgSpeedWrap = document.getElementById('avg-speed-table');

						trfvlmTableWrap.textContent = '';
						avgSpeedWrap.textContent = '';

						let createTable = (wrapElement, dataKey) => {
							let table = document.createElement('table');
							table.classList.add('list-table', 'mt8');

							let thead = document.createElement('thead');
							let headerRow = document.createElement('tr');
							headerRow.appendChild(document.createElement('th')).innerText = '지점';
							labels.forEach(label => {
								let th = document.createElement('th');
								th.innerText = label;
								headerRow.appendChild(th);
							});
							thead.appendChild(headerRow);
							table.appendChild(thead);

							let tbody = document.createElement('tbody');
							for (let key in groupedData) {
								let group = groupedData[key];
								let row = document.createElement('tr');
								let tdCdNm = document.createElement('td');
								tdCdNm.innerText = `${group.instllcNm} - ${group.cdNm}`;
								row.appendChild(tdCdNm);

								labels.forEach(label => {
									let td = document.createElement('td');
									td.innerText = group.dataByDate[label] ? group.dataByDate[label][dataKey] : '0';
									row.appendChild(td);
								});
								tbody.appendChild(row);
							}
							table.appendChild(tbody);

							wrapElement.appendChild(table);
						};

					createTable(trfvlmTableWrap, 'trfvlm');
					createTable(avgSpeedWrap, 'avgSpeed');
					} else {
						
						statisticsListBox.innerHTML = `
											<div class="is-box filter-no-data">
												  <div class="no-chart-list">
													    <img src="/images/no_filter_search.png" alt="noChart"/>
													    <div class="no-chart-text filter-no-text">${message.statistics.statistics_js_search_not_exist}</div>
												  </div>
											 </div>
												`;
							//TODO 차트 데이터가 없을때
						}
						statisticsListBox.classList.remove('none');
						statisticsSearchBox.classList.add('none');
					}
				});
			})
			.catch((error) => {
				console.error('Error:', error);
			}).finally(() => {
				loading.end();
			});
	 }
}

function formatStatsDate(statsDate, type) {
	if (!statsDate) {
		return statsDate;
	}

	let year = statsDate.substring(0, 4);
	let month = statsDate.length >= 6 ? statsDate.substring(4, 6) : '';
	let day = statsDate.length >= 8 ? statsDate.substring(6, 8) : '';

	switch(type) {
		case 'year':
			return `${year} ${message.schedule.year}`;
		case 'month':
			return month ? `${year}-${month}${message.schedule.month}` : year;
		case 'day':
			return day ? `${year}-${month}-${day}` : `${year}-${month}`;
		case 'time':
			return statsDate+message.common.hour;
		default:
			return statsDate;
	}
}

function statisticsExcelDownLoad(excelType, queryString) {
	const loading = new setLoading().start();

	switch (excelType) {
		case 'od':
			window.location.href = `${__contextPath__}/statistics/dashboard/${excelType}/excelDownload?${queryString}`;
			break;
		case 'roadside':
			window.location.href = `${__contextPath__}/statistics/dashboard/${excelType}/excelDownload?${queryString}`;
			break;
		default:
			alert('타입이 잘못되었습니다.')
			break;
	}

	loading.end();
}

let ODStatisticsInit = () => {
	const statisticsListBox = document.querySelector(".statistics-list-box");
	const statisticsSearchBox = document.querySelector('.statistics-search-view');

	if (validation('#searchSurveyForm')) {
		let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
		let originLocationInfo = getLocatinInfo('origin');
		searchSurveyForm.append('exmnLc', originLocationInfo.exmnLc);
		searchSurveyForm.append('dsdCd', originLocationInfo.dsdCd);
		searchSurveyForm.append('gnCd', originLocationInfo.gnCd);

		let destinationLocationInfo = getLocatinInfo('destination');
		searchSurveyForm.append('destinationExmnLc', destinationLocationInfo.exmnLc);
		searchSurveyForm.append('destinationDsdCd', destinationLocationInfo.dsdCd);
		searchSurveyForm.append('destinationGnCd', destinationLocationInfo.gnCd);

		const queryString = new URLSearchParams(searchSurveyForm).toString();
		const exmnType = document.getElementById('exmnTypeCd').value;

		let type = exmnType === 'ROADSIDE' ? 'roadside' : 'od';

		let htmlUrl = `${__contextPath__}/statistics/dashboard/${type}/search`;

		const loading = new setLoading().start();

		fetch(htmlUrl, {
			method: "GET",
			headers: {
				'Accept': 'text/html'
			}
		})
			.then((response) => {
				if (!response.ok) {
					throw new Error('Network response was not ok ' + response.statusText);
				}
				return response.text();
			})
			.then((html) => {
				statisticsListBox.textContent = '';
				statisticsListBox.innerHTML = html;

				const excelBtn = document.getElementById('excelDownLoadBtn');
				if (excelBtn) {
					excelBtn.removeEventListener('click', excelBtn._listener);
					excelBtn._listener = () => statisticsExcelDownLoad(type, queryString);
					excelBtn.addEventListener('click', excelBtn._listener);
				}
			})
			.catch((error) => {
				console.error('Error:', error);
			});

		statisticsListBox.classList.remove('none');
		statisticsSearchBox.classList.add('none');

		let dataUrl = `${__contextPath__}/statistics/dashboard/${type}/searchData?${queryString}`;

		fetch(dataUrl)
			.then((response) => response.json())
			.then((result) => {
				const statisticsChartList = result.data != null ? result.data.statisticsChartList : null;
				if (!isNull(statisticsChartList)) {
					statisticsChartList.forEach(processChartItem);
				}
			})
			.catch((error) => {
				console.error('Error:', error);
			}).finally(() => {
				loading.end();
			});
	} else {
		statisticsListBox.classList.add('none');
		statisticsSearchBox.classList.remove('none');
	}
}

let moreTableData = (_this) => {
	let dataType = _this.dataset.value;
	const tableDivList = document.querySelectorAll('.statistics-table-view');

	tableDivList.forEach((tableDiv) => {
		let tableDivType = tableDiv.dataset.value;
		let hasClassCondition = tableDiv.classList.contains('statistics-table-show');

		if (dataType == tableDivType && !hasClassCondition) {
			tableDiv.classList.add('statistics-table-show');
		} else {
			tableDiv.classList.remove('statistics-table-show');
		}
	});
}

let searchChartData = (_this, type, exmnType) => {
	let searchSurveyForm = new FormData($("#cc")[0]);

	let searchContent = _this.value;
	searchSurveyForm.append('searchContent', searchContent);

	let originLocationInfo = getLocatinInfo('origin');
	searchSurveyForm.append('exmnLc', originLocationInfo.exmnLc);
	searchSurveyForm.append('dsdCd', originLocationInfo.dsdCd);
	searchSurveyForm.append('gnCd', originLocationInfo.gnCd);

	let destinationLocationInfo = getLocatinInfo('destination');
	searchSurveyForm.append('destinationExmnLc', destinationLocationInfo.exmnLc);
	searchSurveyForm.append('destinationDsdCd', destinationLocationInfo.dsdCd);
	searchSurveyForm.append('destinationGnCd', destinationLocationInfo.gnCd);

	let formDataObject = {};
	searchSurveyForm.forEach((value, key) => {
		formDataObject[key] = value;
	});

	let urlType = exmnType === 'roadside' ? 'roadside' : 'od';
	let url = `${__contextPath__}/statistics/dashboard/${urlType}/search/chart`;

	if (exmnType === 'roadside') {
		url += `?roadSideChartType=${type}`;
	} else {
		url += `?personalTransportationChartType=${type}`;
	}


	let chartDivId = _this.getAttribute('data-chart-div');
	const loading = new setEachLoading().start(`#${chartDivId}`);

	fetch(url, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(formDataObject)
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then((result) => {
			if (!isNull(result.data)) {
				processChartItem(result.data);  // 결과를 처리하는 함수
			}
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
};

function selectBoxInit(item) {
	if (item != null && item.selectBox) {
		const selectBox = document.getElementById(item.enumType);
		let selectHtml = `<option value="">${message.common.select}</option>`;

		if (item.searchList != null && item.searchList.length > 0) {
			selectBox.classList.remove('none');
			for (const selectItem of item.searchList) {
				selectHtml += `<option value="${selectItem}">${selectItem}</option>`;
			}
			selectBox.innerHTML = selectHtml;
		} else {
			selectBox.classList.add('none');
		}
	}
}

function processChartItem(item) {
	if (!isNull(item) && item.statisticsList != null && item.statisticsList.length > 0) {
		const dataLabels = item.statisticsList.filter(x => x.value != 0).map(x => x.name);
		const datas = item.statisticsList.filter(x => x.value != 0).map(x => x.value);
		const colorArrays = item.colorArrays;
		if (item.chartType === 'PIE') {
			new rdaChart(ChartType.DOUGHNUT).init(item.appendId)
				.setData({
					labels: dataLabels,
					datasets: [{
						data: datas,
						backgroundColor: colorArrays
					}]
				})
				.draw();
		} else if (item.chartType === 'LINE') {
			if (item.enumType == 'DEPARTURE_TIME' || item.enumType == 'TRAVEL_DEPARTURE_TIME' || item.enumType == 'TRAVEL_DESTINATION_TIME') {
				const hoursArray = new Array(24).fill(0);
				const labels = message.date.hours;

				item.statisticsList.forEach(stat => {
					const hour = parseInt(stat.name, 10);
					hoursArray[hour] = stat.value;
				});

				new rdaChart(ChartType.LINE).init(item.appendId)
					.setLabelDisplay(false)
					.setData({
						labels: labels,
						datasets: [{
							data: hoursArray,
							backgroundColor: colorArrays
						}]
					})
					.draw();
			} else {
				new rdaChart(ChartType.LINE).init(item.appendId)
					.setLabelDisplay(false)
					.setData({
						labels: dataLabels,
						datasets: [{
							data: datas,
							backgroundColor: colorArrays
						}]
					})
					.draw();
			}
		} else if (item.chartType === 'BAR') {
			new rdaChart(ChartType.BAR).init(item.appendId)
				.setLabelDisplay(false)
				.setData({
					labels: dataLabels,
					datasets: [{
						data: datas,
						backgroundColor: colorArrays
					}]
				})
				.draw();
		} else if (!isNull(item) && item.chartType == 'TABLE') {
			let appendDiv = document.getElementById(item.appendId);
			if (item.enumType == 'PERSONAL_STATUS_BY_GENDER') {
				let appendHTML = '';
				item.statisticsList.forEach(stat => {
					let name = stat.name;
					let value = stat.value;

					appendHTML += `<li class="od-statistics-chart-table-list">
									<span>${name}</span>
									<span class="statistics-total">${value}%</span>
								</li>`
				});
				appendDiv.innerHTML = appendHTML;
			} else if (item.enumType == 'PERSONAL_STATUS_BY_EDUCATION' || item.enumType == 'PERSONAL_STATUS_BY_OCCUPATION' || item.enumType == 'PERSONAL_STATUS_BY_WORKINGDAY') {
				let appendHTML = '';
				item.statisticsList.forEach((stat, index) => {
					let name = stat.name;
					let value = stat.value;
					appendHTML += `<tr>
									<td>${index + 1}</td>
									<td>${name}</td>
									<td>${value}%</td>
								</tr>`
				});
				appendDiv.innerHTML = appendHTML;
			}
		}
		selectBoxInit(item);
	} else if (!isNull(item) && item.passengerStatisticsList != null && item.passengerStatisticsList.length > 0) {
		let passengerStatisticsList = item.passengerStatisticsList;

		let totalOne = 0, totalTwo = 0, totalThree = 0, totalFour = 0, totalFive = 0; totalSixMore = 0;

		passengerStatisticsList.forEach(stats => {
			totalOne += stats.passengerOne;
			totalTwo += stats.passengerTwo;
			totalThree += stats.passengerThree;
			totalFour += stats.passengerFour;
			totalFive += stats.passengerFive;
			totalSixMore += stats.passengerSixMore;
		});
		let labels = message.statistics.statistics_js_legend_people;
		let datas = [totalOne, totalTwo, totalThree, totalFour, totalFive, totalSixMore];
		const colorArrays = item.colorArrays;
		new rdaChart(ChartType.DOUGHNUT).init(item.appendId)
			.setData({
				labels: labels,
				datasets: [{
					data: datas,
					backgroundColor: colorArrays
				}]
			})
			.draw();

		selectBoxInit(item);
	} else if (!isNull(item) && item.chartType == 'AVG') {
		let appendDiv = document.getElementById(item.appendId);
		let avgNumber = item.avgNumber;

		appendDiv.innerHTML = avgNumber;

		selectBoxInit(item);
	} else if (!isNull(item) && item.purposeStatisticsList != null && item.purposeStatisticsList.length > 0) {
		let purposeStatisticsList = item.purposeStatisticsList;

		let totalOne = 0, minGroup0To15 = 0, minGroup16To30 = 0, minGroup31To45 = 0, minGroup46To60 = 0,
			minGroup61To90 = 0, minGroup91To120 = 0, minGroup121To150 = 0, minGroup151To180 = 0, minGroup180Above = 0;

		purposeStatisticsList.forEach(stats => {
			minGroup0To15 += stats.minGroup0To15;
			minGroup16To30 += stats.minGroup16To30;
			minGroup31To45 += stats.minGroup31To45;
			minGroup46To60 += stats.minGroup46To60;
			minGroup61To90 += stats.minGroup61To90;
			minGroup91To120 += stats.minGroup91To120;
			minGroup121To150 += stats.minGroup121To150;
			minGroup151To180 += stats.minGroup151To180;
			minGroup180Above += stats.minGroup180Above;
		});
		let labels = ['0~15', '16~30', '31~45', '46~60', '61~90', '91~120', '121~150', '151~180', '3시간 이상'];
		let datas = [minGroup0To15, minGroup16To30, minGroup31To45, minGroup46To60, minGroup61To90, minGroup91To120, minGroup121To150, minGroup151To180, minGroup180Above];
		const colorArrays = item.colorArrays;

		new rdaChart(ChartType.BAR).init(item.appendId)
			.setLabelDisplay(false)
			.setData({
				labels: labels,
				datasets: [{
					data: datas,
					backgroundColor: colorArrays
				}]
			})
			.draw();
		selectBoxInit(item);
	} else {
		let appendDiv = document.getElementById(item.appendId);
		if(appendDiv){
			appendDiv.innerHTML = `
							  <div class="no-chart-list">
							    <img src="/images/no_data.png" alt="noChart"/>
							    <div class="no-chart-text">${message.statistics.statistics_js_data_not_exist}</div>
							  </div>
								`;
			selectBoxInit(item);
		}
	}
}
//OD end


//MCC start
let searchMccStatistics = (startlcNm = null, endlcNm = null) => {
	if (validation('#searchTrfvlForm')) {
		const loading = new setLoading().start();

		const locationSelectList = document.querySelectorAll(".location-select");
		let searchTrfvlForm = new FormData($("#searchTrfvlForm")[0]);
		let dsdCd = null;
		let gnCd = null;
		let code = '';
		let exmnLc = '';
		locationSelectList.forEach((locationSelectItem, idx) => {
			const dataType = locationSelectItem.dataset.type;
			const selectedOption = locationSelectItem.querySelector('option:checked');
			if (!isNull(selectedOption.value)) {
				if (idx === 0) {
					exmnLc = `${selectedOption.value}`;
				} else {
					exmnLc += `, ${selectedOption.value}`;
				}

				//dsd/gn
				code += `${selectedOption.dataset.cd}`;
				if (dataType === 'dsdNm') {
					dsdCd = code;
				} else if (dataType === 'gnNm') {
					gnCd = code;
					dsdCd = null;
				}
			}
		});

		searchTrfvlForm.append('exmnLc', exmnLc);
		searchTrfvlForm.append('dsdCd', dsdCd);
		searchTrfvlForm.append('gnCd', gnCd);
		searchTrfvlForm.append('startlcNm', startlcNm);
		searchTrfvlForm.append('endlcNm', endlcNm);
		const queryString = new URLSearchParams(searchTrfvlForm).toString();
		fetch(__contextPath__ + "/statistics/dashboard/mcc/search?" + queryString)
			.then((response) => response.json())
			.then((result) => {
				const statisticsList = result.data.statisticsList;
				const colorArrays = result.data.colorArrays;

				const statisticsListBox = document.querySelector(".statistics-list-box");
				const statisticsSearchBox = document.querySelector('.statistics-search-view');
				const appendBox = document.querySelector('.statistics-append-box');
				if (!isNull(statisticsList)) {
					const searchDTO = result.data.searchDTO;
					const directionList = result.data.directionList;
					const html = mccAppendContentHTML(statisticsList, directionList, searchDTO);
					const statisticsAppendBox = document.querySelector('.statistics-content');
					statisticsListBox.classList.remove('none');
					statisticsSearchBox.classList.add('none');
					if (appendBox) appendBox.remove();
					statisticsAppendBox.insertAdjacentHTML('beforeend', html);

					let dataLabels = statisticsList.filter(x => x.value != 0).map(x => x.name);
					let datas = statisticsList.filter(x => x.value != 0).map(x => x.value);

					new rdaChart(ChartType.DOUGHNUT).init("mccChart")
						.setData({
							labels: dataLabels,
							datasets: [{
								data: datas,
								backgroundColor: colorArrays
							}]
						})
						.draw();

					let downloadTitle = document.getElementById('downloadTitle');
					let searchRoadCd = document.querySelector('#searchRoadCd option:checked').innerText;
					let searchExmnDistance = document.getElementById('searchExmnDistance').value;
					let searchDate = document.getElementById('searchDate').value;
					downloadTitle.innerText = 'MCC / ' + exmnLc + ' / ' + searchRoadCd + ' / ' + searchExmnDistance + 'km / ' + searchDate;

				} else {
					statisticsListBox.classList.add('none');
					statisticsSearchBox.classList.remove('none');
				}
			})
			.catch((error) => {
				console.error('Error:', error);
			})
			.finally(() => {
				loading.end();
			});

	}
}

let mccExcelDownload = () => {
	const locationSelectList = document.querySelectorAll(".location-select");
	let searchTrfvlForm = new FormData($("#searchTrfvlForm")[0]);
	let dsdCd = null;
	let gnCd = null;
	let code = '';
	let exmnLc = '';
	locationSelectList.forEach((locationSelectItem, idx) => {
		const dataType = locationSelectItem.dataset.type;
		const selectedOption = locationSelectItem.querySelector('option:checked');
		if (!isNull(selectedOption.value)) {
			if (idx === 0) {
				exmnLc = `${selectedOption.value}`;
			} else {
				exmnLc += `, ${selectedOption.value}`;
			}

			//dsd/gn
			code += `${selectedOption.dataset.cd}`;
			if (dataType === 'dsdNm') {
				dsdCd = code;
			} else if (dataType === 'gnNm') {
				gnCd = code;
				dsdCd = null;
			}
		}
	});
	let roadNm = document.querySelector('#searchRoadCd option:checked').innerText;

	searchTrfvlForm.append('roadNm', roadNm);
	searchTrfvlForm.append('exmnLc', exmnLc);
	searchTrfvlForm.append('dsdCd', dsdCd);
	searchTrfvlForm.append('gnCd', gnCd);
	const queryString = new URLSearchParams(searchTrfvlForm).toString();
	window.location.href = __contextPath__ + "/statistics/dashboard/mcc/excel/download?" + queryString;
}
let mccAppendContentHTML = (data, directionList, searchDTO) => {
	const searchStartlcNm = searchDTO.startlcNm;
	const searchEndlcNm = searchDTO.endlcNm;

	let result = `
					<div class="cm-one-chart">
						<div class="statistics-append-box">
							<div class="statistics-content-item-box">
								<div class="content-chart-box">
									<div class="select-div-box">
										<select class="select-list-box" onchange="changeDirection(this)">
											<option value="" startlc-nm="" data-endlc-nm="">${message.common.select}</option>`;
	for (const item of directionList) {
		let attrSelected = "";
		if (`${item.endlcNm}` === searchEndlcNm && `${item.startlcNm}` === searchStartlcNm) attrSelected = "selected";
		result += `<option data-startlc-nm="${item.startlcNm}" data-endlc-nm="${item.endlcNm}" ${attrSelected}>${item.startlcNm} -> ${item.endlcNm}</option>`;
	}
	result += `</select>
									</div>
									<div class="content-chart">
										<div class="pie-chart" id="mccChart">
											
										</div>
								</div>
							</div>
							<div class="content-table-box">
								<div class="scroll statistics-mcc">
									<table class="list-table">
										<thead>
										<tr>
											<th>${message.statistics.mccStatisticsDashboard_rank}</th>
											<th>${message.statistics.mccStatisticsDashboard_transportation}</th>
											<th>${message.statistics.mccStatisticsDashboard_number_of_vehicles}</th>
											<th>${message.statistics.mccStatisticsDashboard_ratio}</th>
										</tr>
										</thead>
										<tbody>`;
	for (const [index, item] of data.entries()) {
		result += `<tr>
											<td>${index + 1}</td>
											<td>${item.name}</td>
											<td>${item.value}</td>
											<td>${item.rate}%</td>
										</tr>`;

	}
	result += `</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
					`
	return result;
}

let changeDirection = (_this) => {
	const startlcNm = _this.querySelector('option:checked').dataset.startlcNm;
	const endlcNm = _this.querySelector('option:checked').dataset.endlcNm;
	searchMccStatistics(startlcNm, endlcNm)
}
//MCC end

// AXLELOAD start
// AXLELOAD Statistics find additional infomation(site, year)
$(document).on('change', '#axlSiteId', function() {
	searchAxlAddtInfo('year');
})

let searchAxlAddtInfo = (type) => {

	let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
	const queryString = new URLSearchParams(searchSurveyForm).toString();

	let selectHtml = `<option value="">${message.common.select}</option>`;

	fetch(__contextPath__ + "/statistics/dashboard/axleload/" + type + "/search?" + queryString)
		.then(response => response.json())
		.then(result => {
			const dataList = result.data.dataList;
			$("#searchDate option").remove();

			if (!isNull(dataList) && dataList.length > 0) {
				for (const item of dataList) {
					selectHtml += `<option value="${item}">${item}</option>`;
				}
			}
			$('#searchDate').append(selectHtml);
		})
}

let axleFacilityType = (_this) => {
	const button = document.getElementById('facilitySearchChange');
	const appnedParent = document.getElementById('faciliySearch');

	getAxleFcltsInfo(_this);
}

let axleMetroSearchHTML = () => {
	const html = `
				<div id="Metro">
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_site_id}</h4>
						<div class="select-div-box">
							<select class="select-list-box table-select-97" id="siteId" name="siteId">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>					
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_survey_year}</h4>
						<div class="select-div-box">
							<select class="dateYear scroll table-select-97 select-list-box validation-tag" id="searchDate" name="searchDate" data-validation="oneDateSelect">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>					
				</div>	
				`
	return html
}

let axleloadAppendContentHTML = (trfStatisticsList, axleStatisticsList) => {
	let result = `
					<div class="statistics-append-box">
						<div class="od-statistics-content-box mt16">
							<div class="od-statistics-content-item" style="border-right:0;">
								<h4 class="cm-stat-sub-title">차량종류</h4>
								<div class="pie-chart" id="vehicleChart">
									chart
								</div>
							</div>
						<div class="od-statistics-content-item">
							<div class="scroll">
								<table class="list-table">
									<thead>
										<tr>
											<th>${message.statistics.trfStatisticsDashboard_vehicle_type}</th>
											<th>${message.statistics.trfStatisticsDashboard_traffic_volume}</th>
											<th>${message.statistics.mccStatisticsDashboard_ratio}</th>
										</tr>
									</thead>
									<tbody>`;
	for (const [index, item] of trfStatisticsList.entries()) {
		result += `
										<tr>
											<td>${item.vhclClsf}</td>
											<td>${numberComma(item.trfvlm)}</td>
											<td>${item.rate}%</td>
										</tr>`;
	}
	result += `
									</tbody>
								</table>
							</div>	
						</div>
					</div>					
					<div class="od-statistics-content-box">
						<div class="od-statistics-content-item">
								<div class="scroll mt16">
									<h4 class="cm-stat-sub-title">${message.statistics.axleloadStatisticsDashboard_axleload_type}</h5>
									<table class="list-table mt8">
										<thead>
											<tr>
												<th>${message.statistics.axleloadStatisticsDashboard_axleload_type}</th>
												<th>${message.statistics.mccStatisticsDashboard_ratio}</th>
											</tr>
										</thead>
										<tbody>
						`;
	for (const [index, item] of axleStatisticsList.entries()) {
		result += `	
										<tr>
											<td>${item.axleType}</td>
											<td>${item.rate}%</td>
										</tr>`;
	}
	result += `		</tbody>
									</table>
								</div>	
							</div>
						</div>					
					</div>`;

	return result;
}

let searchAXLStatistics = () => {
	if (validation('#searchSurveyForm')) {
		const loading = new setLoading().start();
		let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
		const queryString = new URLSearchParams(searchSurveyForm).toString();

		fetch(__contextPath__ + "/statistics/dashboard/axleload/search?" + queryString)
			.then((response) => response.json())
			.then((result) => {
				const trfStatisticsList = result.data.trfStatisticsList;
				const axleStatisticsList = result.data.axleStatisticsList;
				const colorArrays = result.data.colorArrays;

				const statisticsListBox = document.querySelector(".statistics-list-box");
				const statisticsSearchBox = document.querySelector('.statistics-search-view');
				const appendBox = document.querySelector('.statistics-append-box');

				if (!isNull(trfStatisticsList) && !isNull(axleStatisticsList)) {
					const statisticsAppendBox = document.querySelector('.statistics-content');
					const html = axleloadAppendContentHTML(trfStatisticsList, axleStatisticsList);
					statisticsListBox.classList.remove('none');
					statisticsSearchBox.classList.add('none');
					if (appendBox) appendBox.remove();
					// 데이터 추가
					statisticsAppendBox.insertAdjacentHTML('beforeend', html);

					let dataLabels = trfStatisticsList.filter(x => x.trfvlm != 0).map(x => x.vhclClsf);
					let datas = trfStatisticsList.filter(x => x.trfvlm != 0).map(x => x.trfvlm);

					new rdaChart(ChartType.DOUGHNUT).init("vehicleChart")
						.setData({
							labels: dataLabels,
							datasets: [{
								data: datas,
								backgroundColor: colorArrays
							}]
						})
						.draw();
				} else {
					statisticsListBox.classList.add('none');
					statisticsSearchBox.classList.remove('none');
				}

				const excelBtn = document.getElementById('excelDownLoadBtn');
				excelBtn.removeEventListener('click', excelBtn._listener);
				excelBtn._listener = () => axleloadStatisticsExcelDownLoad(queryString);
				excelBtn.addEventListener('click', excelBtn._listener);
				
				let downloadTitle = document.getElementById('downloadTitle');
				let surveyType = document.querySelector('#surveyType option:checked').innerText;
				let roadName = document.querySelector('#axlSiteId option:checked').innerText;
				let searchDate = document.getElementById('searchDate').value;
				downloadTitle.innerText = 'AXLELOAD / ' + surveyType + ' / ' + roadName + ' / ' + searchDate ;
			})
			.catch((error) => {
				console.error('Error:', error);
			})
			.finally(() => {
				loading.end();
			});
	}
}

let getAxleFcltsInfo = (_this) => {
	if (!isNull(_this.value)) {

		fetch(__contextPath__ + "/statistics/dashboard/facilityType?fcltsType=" + _this.value)
			.then((response) => response.json())
			.then((result) => {
				const fcltsList = result.data.fcltsList;

				let siteBox = document.getElementById("axlSiteId");
				let yearBox = document.getElementById("searchDate");

				let fcltsHtml = `<option value="">${message.common.select}</option>`;

				yearBox.innerHTML = fcltsHtml;

				if (!isNull(fcltsList)) {
					for (let item of fcltsList) {
						fcltsHtml += `<option value="${item.instllcId}">${item.instllcId} (${item.instllcNm} / ${item.instllcDescr})</option>`;
					}
				}
				siteBox.innerHTML = fcltsHtml;
			});
	}
}

function axleloadStatisticsExcelDownLoad(queryString) {
	const loading = new setLoading().start();
	window.location.href = `${__contextPath__}/statistics/dashboard/axleload/excelDownload?${queryString}`;
	loading.end();
}


// AXLELOAD end

//traffic start
// traffic Statistics find additional infomation(site, year)
$(document).on('change', '#fcltsBox', function() {
	searchTrfAddtInfo('site');
});

$(document).on('change', '#siteId', function() {
	searchTrfAddtInfo('year');
})

let searchTrfAddtInfo = (type) => {

	let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
	let fcltsBox = document.getElementById("fcltsBox");
	let fclts = fcltsBox.value;

	searchSurveyForm.append('facltId', fclts);
	const queryString = new URLSearchParams(searchSurveyForm).toString();

	let selectHtml = `<option value="">${message.common.select}</option>`;

	fetch(__contextPath__ + "/statistics/dashboard/traffic/" + type + "/search?" + queryString)
		.then(response => response.json())
		.then(result => {
			const dataList = result.data.dataList;

			if (!isNull(dataList) && dataList.length > 0) {
				if (type === 'site') {
					$("#searchDate option").remove();
					$('#searchDate').append(selectHtml);

					$("#siteId option").remove();
					for (const item of dataList) {
						selectHtml += `<option value="${item.value}">${item.value} (${item.name} / ${item.roadDescr})</option>`;
					}
					$('#siteId').append(selectHtml);

				} else if (type === 'year') {
					$("#searchDate option").remove();
					for (const item of dataList) {
						selectHtml += `<option value="${item.value}">${item.value}</option>`;
					}
					$('#searchDate').append(selectHtml);
				}
			} else {
				if (type === 'site') {
					$("#siteId option").remove();
					$('#siteId').append(selectHtml);
					$("#searchDate option").remove();
					$('#searchDate').append(selectHtml);
				} else if (type === 'year') {
					$("#searchDate option").remove();
					$('#searchDate').append(selectHtml);
				}
			}
		})
}

let facilityType = (_this) => {
	const button = document.getElementById('facilitySearchChange');
	const appnedParent = document.getElementById('faciliySearch');
	if (_this.value == 'VDS') {
		button.setAttribute('onclick', 'searchTrfStatistics();');
		document.getElementById('Metro').remove();
		appnedParent.insertAdjacentHTML('beforeend', vdsSearchHTML());
		rdaLocation('VDSLocationSearch');
		getFcltsInfo(_this);
	} else if (_this.value == 'METROCOUNT_FIXED') {
		button.setAttribute('onclick', 'searchTrfStatistics();');
		if (document.getElementById('VDS')) {
			document.getElementById('VDS').remove();
		} else if (document.getElementById('Metro')) {
			document.getElementById('Metro').remove();
		}
		appnedParent.insertAdjacentHTML('beforeend', metroSearchHTML());
		getFcltsInfo(_this);
	} else if (_this.value == 'METROCOUNT_MOVED') {
		button.setAttribute('onclick', 'searchTrfStatistics();');
		if (document.getElementById('VDS')) {
			document.getElementById('VDS').remove();
		} else if (document.getElementById('Metro')) {
			document.getElementById('Metro').remove();
		}
		appnedParent.insertAdjacentHTML('beforeend', metroSearchHTML());
		getFcltsInfo(_this);
	}
}

let vdsSearchHTML = () => {
	const html = `
				<div id="VDS">		
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_site_id}</h4>
						<div class="select-div-box">
							<select class="select-list-box table-select-97" id="siteId" name="siteId">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>				
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_survey_year}</h4>
						<div class="select-div-box">
							<select class="dateYear scroll table-select-97 select-list-box validation-tag" id="searchDate" name="searchDate" data-validation="oneDateSelect">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>		
				</div>
				`
	return html
}

let metroSearchHTML = () => {
	const html = `
				<div id="Metro">
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_site_id}</h4>
						<div class="select-div-box">
							<select class="select-list-box table-select-97" id="siteId" name="siteId">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>					
					<div class="statistics-search-item">
						<h4 class="statistics-search-title">${message.statistics.trfStatisticsDashboard_survey_year}</h4>
						<div class="select-div-box">
							<select class="dateYear scroll table-select-97 select-list-box validation-tag" id="searchDate" name="searchDate" data-validation="oneDateSelect">
								<option value="">${message.common.select}</option>
							</select>
						</div>
					</div>					
				</div>	
				`
	return html
}

let searchTrfStatistics = () => {
	if (validation('#searchSurveyForm')) {
		const loading = new setLoading().start();
		let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
		let fcltsBox = document.getElementById("fcltsBox");
		let fclts = fcltsBox.value;

		searchSurveyForm.append('facltId', fclts);
		const queryString = new URLSearchParams(searchSurveyForm).toString();

		fetch(__contextPath__ + "/statistics/dashboard/traffic/search?" + queryString)
			.then((response) => response.json())
			.then((result) => {
				const trfStatisticsList = result.data.trfStatisticsList;
				const spdStatisticsList = result.data.spdStatisticsList;
				const totTrfvlm = result.data.totTrfvlm;
				const totAvgSpd = result.data.totAvgSpd;
				const colorArrays = result.data.colorArrays;

				const statisticsListBox = document.querySelector(".statistics-list-box");
				const statisticsSearchBox = document.querySelector('.statistics-search-view');
				const appendBox = document.querySelector('.statistics-append-box');

				if (!isNull(trfStatisticsList) && !isNull(spdStatisticsList)) {
					const statisticsAppendBox = document.querySelector('.statistics-content');
					const html = vdsAppendContentHTML(trfStatisticsList, spdStatisticsList, totTrfvlm, totAvgSpd);
					statisticsListBox.classList.remove('none');
					statisticsSearchBox.classList.add('none');
					if (appendBox) appendBox.remove();
					// 데이터 추가
					statisticsAppendBox.insertAdjacentHTML('beforeend', html);

					let dataLabels = trfStatisticsList.filter(x => x.trfvlm != 0).map(x => x.vhclClsf);
					let datas = trfStatisticsList.filter(x => x.trfvlm != 0).map(x => x.trfvlm);

					new rdaChart(ChartType.DOUGHNUT).init("trfChart")
						.setData({
							labels: dataLabels,
							datasets: [{
								data: datas,
								backgroundColor: colorArrays
							}]
						})
						.draw();
				} else {
					statisticsListBox.classList.add('none');
					statisticsSearchBox.classList.remove('none');
				}

				const excelBtn = document.getElementById('excelDownLoadBtn');
				excelBtn.removeEventListener('click', excelBtn._listener);
				excelBtn._listener = () => trafficStatisticsExcelDownLoad(queryString);
				excelBtn.addEventListener('click', excelBtn._listener);
				
				let downloadTitle = document.getElementById('downloadTitle');
				let surveyTypeName = document.querySelector('#surveyType option:checked').innerText;
				
				let surveyType = document.getElementById('surveyType').value;
				
				let facltId = '';
				if(surveyType === 'VDS'){
					facltId = document.querySelector('#fcltsBox option:checked').innerText;					
				}
				
				let roadName = document.querySelector('#siteId option:checked').innerText;
				let searchDate = document.getElementById('searchDate').value;
				
				if(surveyType === 'VDS'){
					downloadTitle.innerText = 'TRAFFIC / ' + surveyTypeName + ' / ' + facltId + ' / ' + roadName + ' / ' + searchDate ;					
				}else {
					downloadTitle.innerText = 'TRAFFIC / ' + surveyTypeName + ' / ' + roadName + ' / ' + searchDate ;										
				}
			})
			.catch((error) => {
				console.error('Error:', error);
			})
			.finally(() => {
				loading.end();
			});
	}
}

let vdsAppendContentHTML = (trfStatisticsList, spdStatisticsList, totTrfvlm, totAvgSpd) => {
	let result = `
					<div class="statistics-append-box">
						<div class="od-statistics-content-box">
							<div class="od-statistics-content-item" style="border-right:0;">
								<h4>${message.statistics.trfStatisticsDashboard_traffic_volume}</h4>
								<div class="pie-chart" id="trfChart">
									chart
								</div>
							</div>
						<div class="od-statistics-content-item">
							<div class="od-statistics-chart-table-list"><span>${message.statistics.trfStatisticsDashboard_total_traffic_volume}</span><span class="statistics-total">${numberComma(totTrfvlm)}</span></div>
							<div class="scroll mt16">
								<h5>${message.statistics.trfStatisticsDashboard_traffic_volume_by_vehicle_type}</h5>
								<table class="list-table mt8">
									<thead>
										<tr>
											<th>${message.statistics.mccStatisticsDashboard_rank}</th>
											<th>${message.statistics.trfStatisticsDashboard_vehicle_type}</th>
											<th>${message.statistics.trfStatisticsDashboard_traffic_volume}</th>
											<th>${message.statistics.mccStatisticsDashboard_ratio}</th>
										</tr>
									</thead>
									<tbody>`;
	for (const [index, item] of trfStatisticsList.entries()) {
		result += `
										<tr>
											<td>${index + 1}</td>
											<td>${item.vhclClsf}</td>
											<td>${numberComma(item.trfvlm)}</td>
											<td>${item.rate}%</td>
										</tr>`;
	}
	result += `
									</tbody>
								</table>
							</div>	
						</div>
					</div>					
					<div class="od-statistics-content-box">
						<div class="od-statistics-content-item">
							<div class="od-statistics-chart-table-list">
								<span>${message.statistics.trfStatisticsDashboard_average_speed_all_vehicles}</span>
								<span class="statistics-total">${totAvgSpd}km</span>
							</div>
								<div class="scroll mt16">
									<h5>${message.statistics.trfStatisticsDashboard_average_speed_vehicle_type}</h5>
									<table class="list-table mt8">
										<thead>
											<tr>
												<th>${message.statistics.mccStatisticsDashboard_rank}</th>
												<th>${message.statistics.trfStatisticsDashboard_vehicle_type}</th>
												<th>${message.statistics.trfStatisticsDashboard_average_speed}</th>
											</tr>
										</thead>
										<tbody>
						`;
	for (const [index, item] of spdStatisticsList.entries()) {
		result += `	
										<tr>
											<td>${index + 1}</td>
											<td>${item.vhclClsf}</td>
											<td>${item.avgspeed}km/h</td>
										</tr>`;
	}
	result += `		</tbody>
									</table>
								</div>	
							</div>
						</div>					
					</div>`;

	return result;
}

// 장비 정보 조회
let getFcltsInfo = (_this) => {
	if (!isNull(_this.value)) {

		if (_this.value === 'VDS') {
			document.getElementById('facilityIdDiv').classList.remove('none');
		} else {
			document.getElementById('facilityIdDiv').classList.add('none');
		}

		fetch(__contextPath__ + "/statistics/dashboard/facilityType?fcltsType=" + _this.value)
			.then((response) => response.json())
			.then((result) => {
				const fcltsList = result.data.fcltsList;

				let fcltsBox = document.getElementById("fcltsBox");
				let siteBox = document.getElementById("siteId");

				let fcltsHtml = `<option value="">${message.common.select}</option>`;
				if (!isNull(fcltsList)) {
					for (let item of fcltsList) {
						if (_this.value === 'VDS') {
							fcltsHtml += `<option value="${item.cameraId}">${item.cameraId}</option>`;
						} else {
							fcltsHtml += `<option value="${item.instllcId}">${item.instllcId} (${item.instllcNm} / ${item.instllcDescr})</option>`;
						}
					}
				}

				if (_this.value === 'VDS') {
					fcltsBox.innerHTML = fcltsHtml;
				} else {
					siteBox.innerHTML = fcltsHtml;
				}
			});
	}
}

function trafficStatisticsExcelDownLoad(queryString) {
	const loading = new setLoading().start();
	window.location.href = `${__contextPath__}/statistics/dashboard/traffic/excelDownload?${queryString}`;
	loading.end();
}
//traffic end

//인구 통계데이터 차트 start

// 중복 제거 및 매핑 유지를 위해 매핑 객체 생성
function removeDuplicatesWithMapping(arr, arrCd) {
	let uniqueArr = [];
	let uniqueArrCd = [];
	let seen = new Set();

	for (let i = 0; i < arr.length; i++) {
		if (!seen.has(arr[i])) {
			uniqueArr.push(arr[i]);
			uniqueArrCd.push(arrCd[i]);
			seen.add(arr[i]);
		}
	}

	return { uniqueArr, uniqueArrCd };
}
/**
 * 지역정보조회
 */
let popStatsRegion = (_this) => {
	let formData = new FormData(document.getElementById('searchOption'));

	const urlParams = new URLSearchParams(formData);

	const loading = new setLoading().start();

	fetch(`${__contextPath__}/statistics/dashboard/area/region?${urlParams.toString()}`)
		.then((response) => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.json();
		})
		.then((result) => {
			if (result.data != null) {
				let regionDTO = result.data;
				let provinceDiv = document.getElementById('provinceSearch');
				let districtDiv = document.getElementById('districtSearch');
				let dsdDiv = document.getElementById('dsdSearch');
				let gnDiv = document.getElementById('gnSearch');

				let provinceSelect = document.getElementById('provinceSelect');
				let districtSelect = document.getElementById('districtSelect');
				let dsdSelect = document.getElementById('dsDivisionSelect');
				let gnSelect = document.getElementById('gnDivisionSelect');

				provinceDiv.classList.add('none');
				districtDiv.classList.add('none');
				dsdDiv.classList.add('none');
				gnDiv.classList.add('none');

				const newProvinceSelect = provinceSelect.cloneNode(true);
				provinceSelect.parentNode.replaceChild(newProvinceSelect, provinceSelect);
				provinceSelect = newProvinceSelect;

				const newDistrictSelect = districtSelect.cloneNode(true);
				districtSelect.parentNode.replaceChild(newDistrictSelect, districtSelect);
				districtSelect = newDistrictSelect;

				if (regionDTO.provinceJSON != null && regionDTO.provinceCdJSON != null &&
					regionDTO.districtCdJSON != null && regionDTO.dsdJSON != null && regionDTO.dsdCdJSON != null) {
					let provinceArr = JSON.parse(regionDTO.provinceJSON);
					let provinceCdArr = JSON.parse(regionDTO.provinceCdJSON);
					let districtArr = JSON.parse(regionDTO.districtJSON);
					let districtCdArr = JSON.parse(regionDTO.districtCdJSON);
					let dsdArr = JSON.parse(regionDTO.dsdJSON);
					let dsdCdArr = JSON.parse(regionDTO.dsdCdJSON);

					provinceSelect.innerHTML = '';

					let dataMap = {};

					for (let index = 0; index < provinceArr.length; index++) {
						const provinceCd = provinceCdArr[index];
						const provinceName = provinceArr[index];
						const districtName = districtArr[index];
						const districtCd = districtCdArr[index];
						const dsdName = dsdArr[index];
						const dsdCd = dsdCdArr[index];

						if (!dataMap[provinceCd]) {
							dataMap[provinceCd] = {
								name: provinceName,
								districts: {}
							};
						}

						if (!dataMap[provinceCd].districts[districtCd]) {
							dataMap[provinceCd].districts[districtCd] = {
								name: districtName,
								dsds: []
							};
						}

						dataMap[provinceCd].districts[districtCd].dsds.push({ dsdCd, dsdName });
					}

					Object.keys(dataMap).forEach(provinceCd => {
						const option = document.createElement('option');
						option.value = provinceCd;
						option.textContent = dataMap[provinceCd].name;
						provinceSelect.appendChild(option);
					});

					provinceDiv.classList.remove('none');

					function updateDistrictSelect() {
						districtSelect.innerHTML = '';

						const selectedProvinceCd = provinceSelect.value;

						if (selectedProvinceCd && dataMap[selectedProvinceCd]) {
							const districts = dataMap[selectedProvinceCd].districts;
							Object.keys(districts).forEach(districtCd => {
								const option = document.createElement('option');
								option.value = districtCd;
								option.textContent = districts[districtCd].name;
								districtSelect.appendChild(option);
							});
						}
						districtDiv.classList.remove('none');
						updateDsdSelect();
					}

					function updateDsdSelect() {
						dsdSelect.innerHTML = '';

						const selectedProvinceCd = provinceSelect.value;
						const selectedDistrictCd = districtSelect.value;

						if (selectedProvinceCd && selectedDistrictCd &&
							dataMap[selectedProvinceCd] && dataMap[selectedProvinceCd].districts[selectedDistrictCd]) {
							const dsds = dataMap[selectedProvinceCd].districts[selectedDistrictCd].dsds;
							dsds.forEach(dsd => {
								const option = document.createElement('option');
								option.value = dsd.dsdCd;
								option.textContent = dsd.dsdName;
								dsdSelect.appendChild(option);
							});
						}
						dsdDiv.classList.remove('none');
					}
					provinceSelect.addEventListener('change', updateDistrictSelect);
					districtSelect.addEventListener('change', updateDsdSelect);

					if (provinceSelect.options.length > 0) {
						provinceSelect.selectedIndex = 0;
						updateDistrictSelect();
					}
				} else if (regionDTO.provinceCdJSON != null && regionDTO.districtCdJSON != null) {
					let provinceArr = JSON.parse(regionDTO.provinceJSON);
					let provinceCdArr = JSON.parse(regionDTO.provinceCdJSON);
					let districtArr = JSON.parse(regionDTO.districtJSON);
					let districtCdArr = JSON.parse(regionDTO.districtCdJSON);

					provinceSelect.innerHTML = '';

					let provinceMap = {};

					for (let index = 0; index < provinceArr.length; index++) {
						const provinceCd = provinceCdArr[index];
						const provinceName = provinceArr[index];
						const districtName = districtArr[index];
						const districtCd = districtCdArr[index];

						if (!provinceMap[provinceCd]) {
							provinceMap[provinceCd] = {
								name: provinceName,
								districts: []
							};
						}

						provinceMap[provinceCd].districts.push({ districtCd, districtName });
					}
					Object.keys(provinceMap).forEach(provinceCd => {
						const option = document.createElement('option');
						option.value = provinceCd;
						option.textContent = provinceMap[provinceCd].name;
						provinceSelect.appendChild(option);
					});

					provinceDiv.classList.remove('none');

					function updateDistrictSelect() {
						districtSelect.innerHTML = '';

						const selectedProvinceCd = provinceSelect.value;

						if (selectedProvinceCd && provinceMap[selectedProvinceCd]) {
							provinceMap[selectedProvinceCd].districts.forEach(district => {
								const option = document.createElement('option');
								option.value = district.districtCd;
								option.textContent = district.districtName;
								districtSelect.appendChild(option);
							});
						}
						districtDiv.classList.remove('none');
					}

					//provinceSelect.removeEventListener('change', updateDistrictSelect);
					provinceSelect.addEventListener('change', updateDistrictSelect);

					if (provinceSelect.options.length > 0) {
						provinceSelect.selectedIndex = 0;
						updateDistrictSelect();
					}
				} else if (regionDTO.provinceJSON != null && regionDTO.provinceCdJSON != null) {
					let provinceArr = JSON.parse(regionDTO.provinceJSON);
					let provinceCdArr = JSON.parse(regionDTO.provinceCdJSON);

					provinceSelect.innerHTML = '';

					for (let i = 0; i < provinceArr.length; i++) {
						const option = document.createElement('option');
						option.value = provinceCdArr[i];
						option.textContent = provinceArr[i];
						provinceSelect.appendChild(option);
					}
					provinceDiv.classList.remove('none');
				} else if (regionDTO.provinceJSON != null) {
					let provinceArr = JSON.parse(regionDTO.provinceJSON);
					provinceSelect.innerHTML = '';
					provinceArr.forEach(province => {
						const option = document.createElement('option');
						option.value = province;
						option.textContent = province;
						provinceSelect.appendChild(option);
					});
					provinceDiv.classList.remove('none');
				}
				//province끝

				//district시작
				if (regionDTO.provinceJSON == null && regionDTO.districtCdJSON != null && regionDTO.districtJSON != null
					&& regionDTO.dsdCdJSON != null && regionDTO.dsdJSON != null) {
					let districtArr = JSON.parse(regionDTO.districtJSON);
					let districtCdArr = JSON.parse(regionDTO.districtCdJSON);
					let dsdArr = JSON.parse(regionDTO.dsdJSON);
					let dsdCdArr = JSON.parse(regionDTO.dsdCdJSON);

					districtSelect.innerHTML = '';

					let districtMap = {};

					for (let i = 0; i < districtArr.length; i++) {
						const districtCd = districtCdArr[i];
						const districtName = districtArr[i];
						const dsdName = dsdArr[i];
						const dsdCd = dsdCdArr[i];

						if (!districtMap[districtCd]) {
							districtMap[districtCd] = {
								name: districtName,
								dsds: []
							};
						}

						districtMap[districtCd].dsds.push({ dsdCd, dsdName });
					}

					Object.keys(districtMap).forEach(districtCd => {
						const option = document.createElement('option');
						option.value = districtCd;
						option.textContent = districtMap[districtCd].name;
						districtSelect.appendChild(option);
					});

					districtDiv.classList.remove('none');

					function updateDsdSelect() {
						dsdSelect.innerHTML = '';

						const selectedDistrictCd = districtSelect.value;

						if (selectedDistrictCd && districtMap[selectedDistrictCd]) {
							districtMap[selectedDistrictCd].dsds.forEach(dsd => {
								const option = document.createElement('option');
								option.value = dsd.dsdCd;
								option.textContent = dsd.dsdName;
								dsdSelect.appendChild(option);
							});
						}
						dsdDiv.classList.remove('none');
					}

					districtSelect.addEventListener('change', updateDsdSelect);

					if (districtSelect.options.length > 0) {
						districtSelect.selectedIndex = 0;
						updateDsdSelect();
					}
				} else if (regionDTO.provinceJSON == null && regionDTO.districtCdJSON != null && regionDTO.districtJSON != null) {
					let districtArr = JSON.parse(regionDTO.districtJSON);
					let districtCdArr = JSON.parse(regionDTO.districtCdJSON);

					districtSelect.innerHTML = '';
					for (let i = 0; i < districtArr.length; i++) {
						const option = document.createElement('option');
						option.value = districtCdArr[i];
						option.textContent = districtArr[i];
						districtSelect.appendChild(option);
					}

					districtDiv.classList.remove('none');
				} else if (regionDTO.provinceJSON == null && regionDTO.districtJSON != null) {
					let districtArr = JSON.parse(regionDTO.districtJSON);

					districtSelect.innerHTML = '';

					districtArr.forEach(district => {
						const option = document.createElement('option');
						option.value = district;
						option.textContent = district;
						districtSelect.appendChild(option);
					});
					districtDiv.classList.remove('none');
				}

				if (regionDTO.provinceJSON == null && regionDTO.districtCdJSON == null && regionDTO.dsdJSON != null) {
					let dsdArr = JSON.parse(regionDTO.dsdJSON);
					dsdSelect.innerHTML = '';
					dsdArr.forEach(dsd => {
						const option = document.createElement('option');
						option.value = dsd;
						option.textContent = dsd;
						dsdSelect.appendChild(option);
					});
					dsdDiv.classList.remove('none');
				}

				if (regionDTO.gnJSON != null) {
					let gnArr = JSON.parse(regionDTO.gnJSON);
					gnSelect.innerHTML = '';
					gnArr.forEach(gn => {
						const option = document.createElement('option');
						option.value = gn;
						option.textContent = gn;
						gnSelect.appendChild(option);
					});
					gnDiv.classList.remove('none');
				}

				document.getElementById('areaSearch').classList.remove('none');
			} else {
				document.getElementById('areaSearch').classList.add('none');
			}
			document.querySelector('.statistics-content').innerHTML = '';
		}).catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
}

let populationStatisticsExcelDownLoad = (formData) => {
	const queryString = new URLSearchParams(formData).toString();

	location.href = `${__contextPath__}/statistics/dashboard/popstats/excelDownload?${queryString}`;
}

/**
 * 체크박스로 차트조회
 */
let areaCheckBoxChartDraw = (_this) => {
	let popmngIdArr = new Array();
	let popmngIdCheckBox = document.querySelectorAll('input[name="popmngId"][type="checkbox"]');

	popmngIdCheckBox.forEach(checkbox => {
		if (checkbox.checked) {
			popmngIdArr.push(checkbox.value);
			checkbox.closest('.division-title').classList.add('on')
		} else {
			checkbox.closest('.division-title').classList.remove('on')
		}
	});

	const loading = new setLoading().start();

	let formData = new FormData(document.getElementById('searchOption'));
	formData.append("popmngIdArr", popmngIdArr);

	fetch(`${__contextPath__}/statistics/dashboard/area/popstats`, {
		method: "POST",
		body: formData
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.json();
		})
		.then((result) => {
			//		let statisticsList = [
			//		    {"name":"COLOMBO","values":[321234,302580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KADUWELA","values":[251234,252580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KOLONNAWA","values":[191234,192580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]}
			//		];

			const excelBtn = document.getElementById('excelDownLoadBtn');
			excelBtn.removeEventListener('click', excelBtn._listener);
			excelBtn._listener = () => populationStatisticsExcelDownLoad(formData);
			excelBtn.addEventListener('click', excelBtn._listener);

			const resultWrap = document.getElementById('chartResultWrap');

			resultWrap.classList.remove('none');

			let chartDTO = result.data;

			if (chartDTO.labelArray != null && chartDTO.labelArray.length > 1) {
				let labels = chartDTO.labelArray.map(label => label.toUpperCase());

				let datasets = chartDTO.statisticsList[0].labels.map((label, index) => {
					return {
						label: label,
						data: chartDTO.statisticsList.map(stat => stat.values[index]),
						backgroundColor: chartDTO.colorArray[index % chartDTO.colorArray.length],
						borderRadius: 1,
						borderWidth: 1,
						fill: false
					};
				});

				new rdaChart(ChartType.BAR).init("pop_chart")
					.setDataSetArrayLabel(labels)
					.setDataArraySet(datasets)
					.setTicksStep(0)
					.setBarGridY(false)
					.setBarGridX(true)
					.draw();

				document.getElementById('pop_chart').classList.remove('none');
				document.getElementById('noneData').classList.add('none');
			} else {
				document.getElementById('pop_chart').classList.add('none');
				document.getElementById('noneData').classList.remove('none');
			}
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
}

/**
 * 체크박스로 차트조회
 */
let areaChartDrawCheckAll = (_this) => {
	let popmngIdCheckBox = document.querySelectorAll('input[name="popmngId"][type="checkbox"]');

	if (_this.checked) {
		popmngIdCheckBox.forEach(checkbox => {
			checkbox.checked = true;
			checkbox.closest('.division-title').classList.add('on')
		});
	} else {
		popmngIdCheckBox.forEach(checkbox => {
			checkbox.checked = false;
			checkbox.closest('.division-title').classList.remove('on')
		});
	}

	areaCheckBoxChartDraw();
};

/**
 * 라디오로 차트조회
 */
let areaRadioChartDraw = (_this) => {
	let popmngIdArr = [];
	let popmngIdRadio = document.querySelectorAll('input[name="popmngId"][type="radio"]');

	popmngIdRadio.forEach(radio => {
		if (radio.checked) {
			popmngIdArr.push(radio.value);
			radio.closest('.division-title').classList.add('on');
		} else {
			radio.closest('.division-title').classList.remove('on');
		}
	});

	const loading = new setLoading().start();

	let formData = new FormData(document.getElementById('searchOption'));
	formData.append("popmngIdArr", popmngIdArr);

	fetch(`${__contextPath__}/statistics/dashboard/area/popstats`, {
		method: "POST",
		body: formData
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.json();
		})
		.then((result) => {
			//		let statisticsList = [
			//		    {"name":"COLOMBO","values":[321234,302580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KADUWELA","values":[251234,252580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KOLONNAWA","values":[191234,192580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]}
			//		];

			const excelBtn = document.getElementById('excelDownLoadBtn');
			excelBtn.removeEventListener('click', excelBtn._listener);
			excelBtn._listener = () => populationStatisticsExcelDownLoad(formData);
			excelBtn.addEventListener('click', excelBtn._listener);

			const resultWrap = document.getElementById('chartResultWrap');

			resultWrap.classList.remove('none');

			let chartDTO = result.data;

			if (chartDTO.statisticsList != null && chartDTO.statisticsList.length > 0) {
				let labels = chartDTO.labelArray;

				let datasets = chartDTO.statisticsList.map((stat, index) => {
					return {
						label: stat.title,
						data: stat.countArray,
						backgroundColor: chartDTO.colorArray[index % chartDTO.colorArray.length],
						borderRadius: 1,
						borderWidth: 1,
						fill: false
					};
				});

				new rdaChart(ChartType.BAR).init("pop_chart")
					.setDataSetArrayLabel(labels)
					.setDataArraySet(datasets)
					.setTicksStep(1000)
					.setBarGridY(false)
					.setBarGridX(true)
					.draw();

				document.getElementById('pop_chart').classList.remove('none');
				document.getElementById('noneData').classList.add('none');
			} else {
				document.getElementById('pop_chart').classList.add('none');
				document.getElementById('noneData').classList.remove('none');
			}
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
}

let areaChartAndTableDraw = (_this) => {
	let popmngIdArr = [];
	let popmngIdRadio = document.querySelectorAll('input[name="popmngId"][type="radio"]');

	popmngIdRadio.forEach(radio => {
		if (radio.checked) {
			popmngIdArr.push(radio.value);
			radio.closest('.division-title').classList.add('on');
		} else {
			radio.closest('.division-title').classList.remove('on');
		}
	});

	const loading = new setLoading().start();

	let formData = new FormData(document.getElementById('searchOption'));
	formData.append("popmngIdArr", popmngIdArr);

	fetch(`${__contextPath__}/statistics/dashboard/area/popstats`, {
		method: "POST",
		body: formData
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.json();
		})
		.then((result) => {
			//		let statisticsList = [
			//		    {"name":"COLOMBO","values":[321234,302580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KADUWELA","values":[251234,252580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]},
			//		    {"name":"KOLONNAWA","values":[191234,192580],"labels":["2022년 1분기 지역별 인구수 통계","지역별통계데이터 테스트"]}
			//		];

			const excelBtn = document.getElementById('excelDownLoadBtn');
			excelBtn.removeEventListener('click', excelBtn._listener);
			excelBtn._listener = () => populationStatisticsExcelDownLoad(formData);
			excelBtn.addEventListener('click', excelBtn._listener);

			const resultWrap = document.getElementById('chartResultWrap');

			resultWrap.classList.remove('none');

			let chartDTO = result.data;

			if (chartDTO.statisticsList != null && chartDTO.statisticsList.length > 0) {
				let labels = chartDTO.labelArray;

				let datasets = chartDTO.statisticsList.map((stat, index) => {
					return {
						label: '',
						data: stat.countArray,
						backgroundColor: chartDTO.colorArray[index % chartDTO.colorArray.length],
						borderRadius: 1,
						borderWidth: 1,
						fill: false
					};
				});

				new rdaChart(ChartType.BAR).init("pop_chart")
					.setDataSetArrayLabel(labels)
					.setDataArraySet(datasets)
					.setTicksStep(100)
					.setBarGridY(false)
					.setBarGridX(true)
					.setAxis('y')
					.setLabelDisplay(false)
					.draw();

				let tableDTO = chartDTO.tableData;

				let popStatTypeCdSelect = document.getElementById('popStatTypeCd');
				let popStatTypeCd = popStatTypeCdSelect.options[popStatTypeCdSelect.selectedIndex].getAttribute('data-value');

				const tbody = document.getElementById('appendTBody');
				let keyArr = new Array();
				switch (popStatTypeCd) {
					case 'PST006':
						keyArr = ['gnId', 'ownedByHouseholdMember', 'rentLeasePrivatelyOwned', 'rentLeaseGovernmentOwned', 'rentFree', 'encroached', 'otherOccupied'];
						break;
				}
				for (const rowData of tableDTO) {
					const tr = document.createElement('tr');

					for (const key of keyArr) {
						const td = document.createElement('td');
						td.textContent = rowData[key];
						tr.appendChild(td);
					}
					tbody.appendChild(tr);
				}
				document.getElementById('pop_chart').classList.remove('none');
				document.getElementById('pop_table').classList.remove('none');
				document.getElementById('noneData').classList.add('none');
			} else {
				document.getElementById('pop_chart').classList.add('none');
				document.getElementById('pop_table').classList.add('none');
				document.getElementById('noneData').classList.remove('none');
			}
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
}

let areaAppendContentHTML = () => {
	
	
	statisticsViewToggle();
	const appnedParent = document.querySelector('.statistics-content');
	let formData = new FormData(document.getElementById('searchOption'));

	const urlParams = new URLSearchParams(formData);

	
	if (validation('#searchOption')) {
		const loading = new setLoading().start();
		fetch(`${__contextPath__}/statistics/dashboard/area/search?${urlParams.toString()}`, {
		method: "GET"
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error('Network response was not ok ' + response.statusText);
			}
			return response.text();
		})
		.then((html) => {
			appnedParent.innerHTML = html;
		})
		.catch((error) => {
			console.error('Error:', error);
		}).finally(() => {
			loading.end();
		});
	}
	
}
//인구 통계데이터 차트 end

let getLocatinInfo = (elementId) => {
	const locationSelectList = document.querySelectorAll("#" + elementId + " .location-select");
	let result = new Object();

	let dsdCd = null;
	let gnCd = null;
	let code = '';
	let exmnLc = '';
	locationSelectList.forEach((locationSelectItem, idx) => {
		const dataType = locationSelectItem.dataset.type;
		const selectedOption = locationSelectItem.querySelector('option:checked');
		if (!isNull(selectedOption.value)) {
			if (idx === 0) {
				exmnLc = `${selectedOption.value}`;
			} else {
				exmnLc += `, ${selectedOption.value}`;
			}

			//dsd/gn
			code += `${selectedOption.dataset.cd}`;
			if (dataType === 'dsdNm') {
				dsdCd = code;
			} else if (dataType === 'gnNm') {
				gnCd = code;
				dsdCd = null;
			}
		}
	});

	result.dsdCd = dsdCd;
	result.gnCd = gnCd;
	result.exmnLc = exmnLc;

	return result;
}

document.addEventListener('change', function(e) {
	if (e.target.classList.contains('statistic-search-od')) {
		e.preventDefault();

		if (statisticValidation('#searchSurveyForm')) {
			let searchSurveyForm = new FormData($("#searchSurveyForm")[0]);
			let originLocationInfo = getLocatinInfo('origin');
			searchSurveyForm.append('exmnLc', originLocationInfo.exmnLc);
			searchSurveyForm.append('dsdCd', originLocationInfo.dsdCd);
			searchSurveyForm.append('gnCd', originLocationInfo.gnCd);

			let destinationLocationInfo = getLocatinInfo('destination');
			searchSurveyForm.append('destinationExmnLc', destinationLocationInfo.exmnLc);
			searchSurveyForm.append('destinationDsdCd', destinationLocationInfo.dsdCd);
			searchSurveyForm.append('destinationGnCd', destinationLocationInfo.gnCd);

			const queryString = new URLSearchParams(searchSurveyForm).toString();
			fetch(__contextPath__ + "/statistics/dashboard/od/search/year?" + queryString)
				.then((response) => response.json())
				.then((result) => {
					const years = result.data.years;
					let searchDate = document.getElementById("searchDate");
					let searchDateHtml = `<option value="">${message.common.select}</option>`;
					if (!isNull(years)) {
						for (let item of years) {
							searchDateHtml += `<option value="${item}">${item}</option>`;
						}
					}
					searchDate.innerHTML = searchDateHtml;
				})
		}
	}

	if (e.target.classList.contains('statistic-search-mcc')) {
		e.preventDefault();

		let searchType = e.target.dataset.searchType;
		let appendLocationId = e.target.dataset.appendLocation;
		getSearchInit(searchType);

		let searchTrfvlForm = new FormData($("#searchTrfvlForm")[0]);
		let surveyLocationSearch = getLocatinInfo('surveyLocationSearch');
		searchTrfvlForm.append('dsdCd', surveyLocationSearch.dsdCd);
		searchTrfvlForm.append('gnCd', surveyLocationSearch.gnCd);

		const queryString = new URLSearchParams(searchTrfvlForm).toString();
		fetch(__contextPath__ + "/statistics/dashboard/mcc/" + searchType + "/search/?" + queryString)
			.then((response) => response.json())
			.then((result) => {
				const data = result.data;
				let searchDate = document.getElementById(appendLocationId);
				let searchDateHtml = `<option value="">${message.common.select}</option>`;
				if (!isNull(data)) {
					for (let item of data) {
						searchDateHtml += `<option value="${item.value}">${item.name}</option>`;
					}
				}
				searchDate.innerHTML = searchDateHtml;
			})

	}
}, false);

function getSearchInit(searchType) {
	let searchDate = document.getElementById('searchDate');
	let searchRoadCd = document.getElementById('searchRoadCd');
	let searchExmnDistance = document.getElementById('searchExmnDistance');
	searchDate.value = '';
	removeSelectOption(searchDate);
	switch (searchType) {
		case "searchRegion":
			searchRoadCd.value = '';
			removeSelectOption(searchRoadCd);
			searchExmnDistance.value = '';
			removeSelectOption(searchExmnDistance);
		case "searchRoadCd":
			searchExmnDistance.value = '';
			removeSelectOption(searchExmnDistance);
		default:
			break;
	}
}

function removeSelectOption(ele) {
	while (ele.options.length > 1) {
		ele.remove(1); // 항상 1번째 인덱스를 제거
	}
}