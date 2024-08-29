const excelForm = {
	"popStatsForm" : {
		"PST000" : {
			"headerSize" : 3	
		},
		"PST001" : {
			"headerSize" : 22
		},
		"PST002" : {
			"headerSize" : 6	
		},
		"PST003" : {
			"headerSize" : 8	
		},
		"PST004" : {
			"headerSize" : 12	
		},
		"PST005" : {
			"headerSize" : 10
		},
		"PST006" : {
			"headerSize" : 9	
		},
		"PST007" : {
			"headerSize" : 15	
		},
		"PST008" : {
			"headerSize" : 12	
		},
		"PST009" : {
			"headerSize" : 7	
		},
		"PST010" : {
			"headerSize" : 10	
		},
		"PST011" : {
			"headerSize" : 9	
		},
		"PST012" : {
			"headerSize" : 8	
		},
		"PST013" : {
			"headerSize" : 5	
		},
		"PST014" : {
			"headerSize" : 11	
		},
		"PST015" : {
			"headerSize" : 7	
		},
		"PST016" : {
			"headerSize" : 9	
		},
		"PST017" : {
			"headerSize" : 4	
		},
		"PST018" : {
			"headerSize" : 7
		},
		"PST019" : {
			"headerSize" : 8	
		},
		"PST020" : {
			"headerSize" : 6
		},
		"PST021" : {
			"headerSize" : 13	
		}
	}
}

class ExcelAnalyzer {
    constructor(file) {
        this.file = file;
    }

    async loadFile() {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = (e) => {
                const data = new Uint8Array(e.target.result);
                const workbook = XLSX.read(data, { type: 'array' });
                resolve(workbook);
            };
            reader.onerror = (error) => reject(error);
            reader.readAsArrayBuffer(this.file);
        });
    }

    getRowAndColumnLengths(sheet) {
        const range = XLSX.utils.decode_range(sheet['!ref']);
        const numRows = range.e.r - range.s.r + 1;  // 행 개수 (헤더 포함)
        const numCols = range.e.c - range.s.c + 1;  // 열 개수
        return { numRows, numCols };
    }

    async analyze() {
        const workbook = await this.loadFile();
        const result = {};
        workbook.SheetNames.forEach(sheetName => {
            const sheet = workbook.Sheets[sheetName];
            result['rowAndCols'] = this.getRowAndColumnLengths(sheet);
        });
        return result;
    }
}

window.getMaxRowsLength = async function(file,type = '') {
    if (file) {
        const analyzer = new ExcelAnalyzer(file);
        const analysisResult = await analyzer.analyze();
        analysisResult['headerSize'] = excelForm.popStatsForm[type].headerSize;
        
        return analysisResult;
    } else {
		console.log("File is not found");
        return null;
    }
};