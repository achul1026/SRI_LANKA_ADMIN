const ChartType = {BAR : "bar", LINE : "line", DOUGHNUT : "doughnut"};
let rdaChart = function(type){
	
    let _self = this;
    
    let _parentDiv = null;

    let _element = null;

    let _data = {};

    let _labels = [];

    let _datasets = [];

    const _type = type;

    let _opt = {};

    let _typeOpts = {
        bar : {
            animation: {
                duration: 0
            },
            maintainAspectRatio :false,
            scales: {
                y: {
                    beginAtZero: true,
                    stacked:true,
                    ticks: {
                        stepSize:2,
                        color :'#000',
						font: {
							family:'Noto_Sans_KR_Thin',
						},
                        callback: function(dataLabel) {
                            let label = this.getLabelForValue(dataLabel);
                            return label.length > 10 ? label.substr(0, 8) + '...' : label;
                        }
                    },
                    grid: {
                        color:'#cdd3da',
                        tickColor:'transparent',
                        display:false
                    }
                },
                x: {
                    beginAtZero: true,
                    stacked:true,
                    ticks: {
                        color:'#000',
						font: {
							/*family:'Noto_Sans_KR_Thin',*/
						},
                        callback: function(dataLabel) {
                            let label = this.getLabelForValue(dataLabel);
                            return label.length > 10 ? label.substr(0, 8) + '...' : label;
                        },
                    },
                    grid: {
                        color:'gray',
                        tickColor:'transparent',
                        display:false
                    }
                },
            },     
            plugins: {
                legend: {
                    position:'bottom',
                    usePointStyle:true,
                    labels: {
                        color:"#323232",
                        padding:10,
						boxWidth:16,
						font: {
							family:'Noto_Sans_KR_Thin',
							size:12,
						}
                    },
                    
                },
            },
        },
        line : {
            animation: {
                duration: 0
            },
            maintainAspectRatio :false,
            elements: {
                point: {
                    radius: 2,
                },
            },
			options:{
				indexAxis:'y',
			},
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        stepSize:1,
                        color :'#323232',
						font: {
							family:'Noto_Sans_KR_Thin',
						},
                    },
                    grid: {
                        color:'rgba(255, 255, 255, 0.44)',
                        tickColor:'transparent'
                    }
                },
                x: {
                    beginAtZero: true,
                    ticks: {
                        color:'#000',
						font: {
							family:'Noto_Sans_KR_Thin',
						}
                    },
                    grid: {
                        color:'rgba(255, 255, 255, 0.44)',
                        tickColor:'transparent',
                        display:false
                    }
                },
            },
            interaction: {
				mode: 'nearest',
				intersect: false
			},
            plugins: {
                legend: {
                    position:'bottom',
                    labels: {
						padding:10,
                        color:"#000",
						boxWidth:16,
						font: {
							size:12,
						}
                    }
                },
                tooltip: {
					mode: 'nearest',
					intersect: false,
					callbacks: {
						label: function(tooltipItem) {
							return `${tooltipItem.raw}`
						}
					}
				}
            },
        },
		doughnut : {
            animation: {
                duration: 0
            },
			maintainAspectRatio :false,
            plugins: {
                legend: {
                    position:'bottom',
                    labels: {
						padding:10,
                        color:"#000",
						boxWidth:16,
						font: {
							size:12,
						}
                    }
                },
            },
		}
    }

    //id 값 받아오기
    _self.init = function(parentId) {
        _parentDiv = document.getElementById(parentId);
        
        _element = document.createElement('canvas');
        _element.classList.add('chart-data')
        
        _parentDiv.innerHTML = '';
        
        _parentDiv.appendChild(_element);

        _opt = _typeOpts[_type];

        return _self;
    }

    // 옵션 값 받아오기
    _self.setOption = function(opt) {
        _opt = $.extend(_opt, opt, {});
        return _self;
    }

    //데이터값 받아오기
    _self.setData = function(data) {
        _data = data;
        return _self;
    }

    //es6 분법  args 여러 매개변수를 받아온다
    _self.setDataSetLabel = function(...args) {
        _data = {};
        _labels = args;
		_data.labels = _labels;
        return _self;
    }

 	_self.setDataSetArrayLabel = function(array) {
        _data = {};
        _labels = array;
		_data.labels = array;
        return _self;
    }

    // 그리드컬러변경
    _self.setGridColor = function(color) {
        try{
            _opt.scales.y.grid.color = color;
        }catch(e) {console.error('option error')}
        return _self;
    }

	// tick color
	_self.setTickColorX = function(tickColorX) {
		try {
			_opt.scales.x.grid.tickColor = tickColorX
		}catch(e) {console.error('option error')}
		return _self;
	}
	_self.setTickColorY = function(tickColorY) {
		try {
			_opt.scales.y.grid.tickColor = tickColorY
		}catch(e) {console.error('option error')}
		return _self;
	}
	_self.setTickX = function(tickX) {
		try {
			_opt.scales.x.ticks.color = tickX
		}catch(e) {console.error('option error')}
		return _self;
	}
	_self.setTickY = function(tickY) {
		try {
			_opt.scales.y.ticks.color = tickY
		}catch(e) {console.error('option error')}
		return _self;
	}
	//y축 ticks display
	_self.setTickDisplayY = function(displayY) {
		try {
			_opt.scales.y.ticks.display = displayY
		}catch(e) {console.error('option error')}
		return _self;
	}

    // 라벨 온오프
    _self.setLabelDisplay = function(display) {
        try{
            _opt.plugins.legend.display = display;
        }catch(e) {console.error('option error')}
        return _self;
    }

	_self.setLabelPadding = function(padding) {
		try {
			_opt.plugins.legend.labels.padding = padding
		}catch(e) {console.error('option error')}
		return _self;
	}
	
	_self.SetMaxWidth = function(width) {
		try {
			_opt.maxBarThickness = width
		}catch(e) {console.error('option error')}
		return _self;
	}
    // Y축 stepSize
    _self.setTicksStep = function(stepSize) {
        try {
            _opt.scales.y.ticks.stepSize = stepSize
        } catch(e) {console.error('option error')}
        return _self;
    }

    // X축 stepSize
    _self.setTickStepX = function(stepSizeX) {
        try {
            _opt.scales.x.ticks.stepSize = stepSizeX
        } catch(e) {console.error('option error')}
        return _self;
    }

    // 축 Axis
    _self.setAxis = function(indexAxis) {
        try {
            _opt.indexAxis = indexAxis
        } catch(e) {console.error('option error')}
        return _self;
    }

    // X축 data 누적
    _self.setAxisStackedX = function(stackedX) {
        try {
            _opt.scales.x.stacked = stackedX
        } catch(e) {console.error('option error')}
        return _self;
    }

    // Y축 data 누적
    _self.setAxisStackedY = function(stackedY) {
        try {
            _opt.scales.y.stacked = stackedY
        } catch(e) {console.error('option error')}
        return _self;
    }
    
    // X축 데이터 균일?
    _self.setXLogarithmic = function() {
        try {
            _typeOpts.bar.scales.x.type = 'logarithmic'
        } catch(e) {console.error('option error')}
        return _self;
    }
    // Y축 데이터 균일?
    _self.setYLogarithmic = function() {
        try {
            _typeOpts.bar.scales.y.type = 'logarithmic'
        } catch(e) {console.error('option error')}
        return _self;
    }

    // grid bar Y축
    _self.setBarGridY = function(barGridY) {
        try {
            _typeOpts.bar.scales.y.grid.display = barGridY
        } catch(e) {console.error('option error')}
        return _self;
    }

    // grid bar X축
    _self.setBarGridX = function(barGridX) {
        try {
            _typeOpts.bar.scales.x.grid.display = barGridX
        } catch(e) {console.error('option error')}
        return _self;
    }

    // X축 display
    _self.setDisplayX = function(xDisplay) {
        try {
            _typeOpts.bar.scales.x.display = xDisplay
        } catch(e) {console.error('option error')}
        return _self;
    }
    
    // Y축 display
    _self.setDisplayY = function(yDisplay) {
        try {
            _typeOpts.bar.scales.y.display = yDisplay
        } catch(e) {console.error('option error')}
        return _self;
    }

	_self.setPoint = function(point) {
		try {
			_typeOpts.line.elements.point.radius = point
		} catch(e) {console.error('option error')}
		return _self;
	}

    //데이터 넣기
    _self.setDataSet = function(...args){
        _datasets = args;
        _data.datasets = _datasets;
        return _self;
    }

    _self.setDataArraySet = function(args){
        _datasets = args;
        _data.datasets = _datasets;
        return _self;
    }

	//lable name change
	_self.labelCallback = function(item) {
		try {
			_opt.scales.y.ticks.callback = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	
	// line chart grid Y축 color
	_self.setLineYGridColor = function(item) {
		try {
			_typeOpts.line.scales.y.grid.color = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	
	// line chart grid X축 color
	_self.setLineXGridColor = function(item) {
		try {
			_typeOpts.line.scales.x.grid.color = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	
	// line chart grid X축 display
	_self.setLineXGrid = function(item) {
		try {
			_typeOpts.line.scales.x.grid.display = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	
	// line chart grid Y축 display
	_self.setLineYGrid = function(item) {
		try {
			_typeOpts.line.scales.y.grid.display = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	
	// line chart grid Y축 tick color
	_self.setLineYGridTick = function(item) {
		try {
			_typeOpts.line.scales.y.grid.tickColor = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	// line chart grid X축 tick color
	_self.setLineXGridTick = function(item) {
		try {
			_typeOpts.line.scales.x.grid.tickColor = item 
		} catch(e) {console.error('option error')}
		return _self;
	}
	// line tick maxlength
	_self.setLineXTickAuto = function(item) {
		try {
			_typeOpts.line.scales.x.ticks.autoSkip = item 
		} catch(e) {console.error('option error')}
		return _self;
	}

    //차트 호출
    _self.draw = function(){
        if(typeof Chart === "undefined") {
            console.error("chart.js가 import 되어 있지 않습니다.");
            return;
        }
        
        new Chart(_element, {
            type:_type,
            data: _data,
            options: _opt
        });
    }
}