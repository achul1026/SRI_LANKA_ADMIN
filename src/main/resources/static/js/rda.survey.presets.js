const SURVEY_QTYPE = {
	SUBJECTIVE : "SUBJECTIVE",
	GPS : "GPS",
	SELECTBOX : "SELECTBOX",
	RADIO : "RADIO",
	CHECKBOX : "CHECKBOX",
	DATE : "DATE",
	TIME : "TIME",
	LOCATION : "LOCATION"
}
const SURVEY_PRESETS = {
	SYC003 : [ // AXLELOAD
	],
	SYC002 : [ // ROADSIDE
		{
			title: message.survey.personal_trip_behavior_survey,
			code: "STC004",
			question_box: [
				{
					question : message.survey.car_type_of_survey ?? 'not set',
					metadata : 'SMD065',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car,Jeep',
						'VAN',
						'Medium Bus',
						'Large Bus',
						'Pick up truck',
						'Medium Truck1(Gross weight should be less than 8.5 Tons) ',
						'Medium Truck2(Gross weight should be grater than 8.5 Tons) ',
						'Heavy Truck',
						'Articulated vehicles(3axles)',
						'Articulated vehicles(4axles)',
						'Articulated vehicles(5axles)',
						'Articulated vehicles(6axles)',
						'Tractor'
					]
				},
				{
					question : message.survey.purpose_of_trip ?? 'not set',
					metadata : 'SMD050',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Work',
						'School',
						'Business',
						'Shopping',
						'Leisure',
						'Going home',
						'Other'
					]
				},
				{
					question : message.survey.departure_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD021',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.departure_address_taz ?? 'not set',
					metadata : 'SMD055',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.departure_type ?? 'not set',
					metadata : 'SMD022',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Home',
						'Workplace',
						'School',
						'Other',
					]
				},
				{
					question : message.survey.departure_time ?? 'not set',
					metadata : 'SMD023',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.mode_of_transportation_at_departure ?? 'not set',
					metadata : 'SMD024',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers ?? 'not set',
					metadata : 'SMD026',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*First Transfer*/
				/*{
					question : message.survey.first_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD027',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.first_transfer_address_taz ?? 'not set',
					metadata : 'SMD056',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.first_transfer_arrival_time ?? 'not set',
					metadata : 'SMD028',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.first_transfer_mode ?? 'not set',
					metadata : 'SMD029',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers_in_first_transfer ?? 'not set',
					metadata : 'SMD030',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/!*Second Transfer*!/
				{
					question : message.survey.second_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD031',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.second_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD057',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.second_transfer_arrival_time ?? 'not set',
					metadata : 'SMD032',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.second_transfer_mode ?? 'not set',
					metadata : 'SMD033',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers_in_second_transfer ?? 'not set',
					metadata : 'SMD034',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/!*Third Transfer*!/
				{
					question : message.survey.third_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD035',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.third_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD058',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.third_transfer_arrival_time ?? 'not set',
					metadata : 'SMD036',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.third_transfer_mode ?? 'not set',
					metadata : 'SMD037',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers_in_third_transfer ?? 'not set',
					metadata : 'SMD038',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/!*Fourth Transfer*!/
				{
					question : message.survey.fourth_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD039',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.fourth_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD059',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.fourth_transfer_arrival_time ?? 'not set',
					metadata : 'SMD040',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.fourth_transfer_mode ?? 'not set',
					metadata : 'SMD041',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers_in_fourth_transfer ?? 'not set',
					metadata : 'SMD042',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/!*Fifth Transfer*!/
				{
					question : message.survey.fifth_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD043',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.fifth_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD060',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.fifth_transfer_arrival_time ?? 'not set',
					metadata : 'SMD044',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.fifth_transfer_mode ?? 'not set',
					metadata : 'SMD045',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},
				{
					question : message.survey.number_of_passengers_in_fifth_transfer ?? 'not set',
					metadata : 'SMD046',
					type : SURVEY_QTYPE.SUBJECTIVE
				},*/
				/*Final*/
				{
					question : message.survey.final_destination_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD047',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.final_destination_address_TAZ ?? 'not set',
					metadata : 'SMD061',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.final_destination_arrival_time ?? 'not set',
					metadata : 'SMD049',
					type : SURVEY_QTYPE.TIME
				},
				/*{
					question : message.survey.final_destination_mode ?? 'not set',
					metadata : 'SMD063',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Pick-up',
						'Medium Truck',
						'Large Truck',
						'Trailer'
					]
				},*/
				{
					question : message.survey.final_destination_type ?? 'not set',
					metadata : 'SMD048',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Home',
						'Workplace',
						'School',
						'Other',
					]
				},
				{
					question : message.survey.passenger_count ?? 'not set',
					metadata : 'SMD064',
					type : SURVEY_QTYPE.SUBJECTIVE
				}
			]
		}
	],
	SYC001 : [ // OD
		{
			title : message.survey.household_title,
			code : "STC001",
			question_box : [
				{
					question : message.survey.name_of_householder ?? 'not set',
					metadata : 'SMD001',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.phone_of_householder ?? 'not set',
					metadata : 'SMD002',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.residential_address_gps ?? '',
					metadata : 'SMD003',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.residential_address_TAZ ?? '',
					metadata : 'SMD054',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.number_of_householder ?? 'not set',
					metadata : 'SMD004',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_preschool_children ?? '',
					metadata : 'SMD005',
					type : SURVEY_QTYPE.SUBJECTIVE
				}
				,
				{
					question : message.survey.average_monthly_income ?? '',
					metadata : 'SMD006',
					type : SURVEY_QTYPE.RADIO,
					hasOptionEtc : false,
					options : [
						'below 40,000 Rs',
						'40,000 ~ 59,999 Rs',
						'60,000 ~ 79,999 Rs',
						'80,000 ~ 99,999 Rs',
						'100,000 ~ 119,999 Rs',
						'120,000 ~ 139,999 Rs',
						'140,000 ~ 159,999 Rs',
						'160,000 ~ 199,999 Rs',
						'200,000 ~ 299,999 Rs',
						'300,000 ~ 399,999 Rs',
						'over 400,000 Rs'
					]
				},
				{
					question : message.survey.type_of_residence ?? '',
					metadata : 'SMD007',
					type : SURVEY_QTYPE.RADIO,
					hasOptionEtc : false,
					options : [
						'Owned by a household',
						'Rent-lease government',
						'Rent-lease privately'
					]
				},
				{
					question : message.survey.number_of_rooms ?? '',
					metadata : 'SMD008',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_motorcycles ?? '',
					metadata : 'SMD009',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_three_wheel_motorcycles ?? '',
					metadata : 'SMD010',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_passenger_cars ?? '',
					metadata : 'SMD011',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_vans ?? '',
					metadata : 'SMD012',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.number_of_other_vehicles ?? '',
					metadata : 'SMD013',
					type : SURVEY_QTYPE.SUBJECTIVE
				}
			]
		},
		{
			title: message.survey.personal_characteristics_title,
			code: "STC002",
			question_box: [
				{
					question : message.survey.respondent_name ?? 'not set',
					metadata : 'SMD014',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.respondent_phone_number ?? 'not set',
					metadata : 'SMD015',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				{
					question : message.survey.gender ?? 'not set',
					metadata : 'SMD016',
					type : SURVEY_QTYPE.RADIO,
					hasOptionEtc : false,
					options : [
						'Male',
						'Female'
					]
				},
				{
					question : message.survey.age_group ?? 'not set',
					metadata : 'SMD017',
					type : SURVEY_QTYPE.RADIO,
					hasOptionEtc : false,
					options : [
						'5-9',
						'10-19',
						'20-29',
						'30-39',
						'40-49',
						'50-59',
						'60-69',
						'over 70',
					]
				},
				{
					question : message.survey.occupation ?? 'not set',
					metadata : 'SMD018',
					type : SURVEY_QTYPE.RADIO,
					hasOptionEtc : true,
					etcValue : "Other",
					options : [
						'Managers, Senior Officials and  Legislators',
						'Professionals, Lecturers, Teachers',
						'Researchers, Engineers, Medical Doctors, etc.',
						'Government Employees, Technicians, etc.',
						'Brokers, Sale/Business service agents',
						'Office clerks, Customer Service clerks',
						'Travel attendants, Guides, Chefs, etc.',
						'Sale persons at shop, stall, at market',
						'Policemen, Fire fighters, Security guards',
						'Farmers, Fishermen, Forestry workers',
						'Workers in construction, textile, etc.',
						'Drivers, Operators of vehicle & machinery',
						'Housemaids, Cleaners, Helpers',
						'Laborers',
						'Street vendors, Elementary occupations',
						'Armed Forces',
						//'Other'
					]
				},
				{
					question : message.survey.average_working_days_per_week ?? 'not set',
					metadata : 'SMD019',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'1~2 days a week',
						'3~4 days a week',
						'5 days a week',
						'more than 6 days',
						'etc'
					]
				},
				{
					question : message.survey.education_level ?? 'not set',
					metadata : 'SMD020',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'No Schooling',
						'Primary school(Up to Grade 5)',
						'Secondary school (Passed Grade 6 – 9)',
						'Secondary school (Passed G.C.E O/L 10-11)',
						'Secondary school (Passed G.C.E A/L 12-13)',
						'Undergraduate & Bachelor’s Degree',
						'Master’s Degree',
						'Ph.D',
						//'Passed Degree & Above',
						'Special Education'
					]
				},
			]
		},
		{
			title: message.survey.personal_trip_behavior_survey,
			code: "STC003",
			question_box: [
				{
					question : message.survey.purpose_of_trip ?? 'not set',
					metadata : 'SMD050',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Work',
						'School',
						'Business',
						'Shopping',
						'Leisure',
						'Going home',
						'Other'
					]
				},
				{
					question : message.survey.departure_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD021',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.departure_address_taz ?? 'not set',
					metadata : 'SMD055',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.departure_type ?? 'not set',
					metadata : 'SMD022',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Home',
						'Workplace',
						'School',
						'Other',
					]
				},
				{
					question : message.survey.departure_time ?? 'not set',
					metadata : 'SMD023',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.mode_of_transportation_at_departure ?? 'not set',
					metadata : 'SMD024',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.highway_usage ?? 'not set',
					metadata : 'SMD025',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Yes',
						'No'
					]
				},
				{
					question : message.survey.number_of_passengers ?? 'not set',
					metadata : 'SMD026',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*First Transfer*/
				{
					question : message.survey.first_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD027',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.first_transfer_address_taz ?? 'not set',
					metadata : 'SMD056',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.first_transfer_arrival_time ?? 'not set',
					metadata : 'SMD028',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.first_transfer_mode ?? 'not set',
					metadata : 'SMD029',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.number_of_passengers_in_first_transfer ?? 'not set',
					metadata : 'SMD030',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*Second Transfer*/
				{
					question : message.survey.second_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD031',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.second_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD057',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.second_transfer_arrival_time ?? 'not set',
					metadata : 'SMD032',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.second_transfer_mode ?? 'not set',
					metadata : 'SMD033',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.number_of_passengers_in_second_transfer ?? 'not set',
					metadata : 'SMD034',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*Third Transfer*/
				{
					question : message.survey.third_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD035',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.third_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD058',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.third_transfer_arrival_time ?? 'not set',
					metadata : 'SMD036',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.third_transfer_mode ?? 'not set',
					metadata : 'SMD037',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.number_of_passengers_in_third_transfer ?? 'not set',
					metadata : 'SMD038',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*Fourth Transfer*/
				{
					question : message.survey.fourth_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD039',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.fourth_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD059',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.fourth_transfer_arrival_time ?? 'not set',
					metadata : 'SMD040',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.fourth_transfer_mode ?? 'not set',
					metadata : 'SMD041',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.number_of_passengers_in_fourth_transfer ?? 'not set',
					metadata : 'SMD042',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*Fifth Transfer*/
				{
					question : message.survey.fifth_transfer_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD043',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.fifth_transfer_address_TAZ ?? 'not set',
					metadata : 'SMD060',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.fifth_transfer_arrival_time ?? 'not set',
					metadata : 'SMD044',
					type : SURVEY_QTYPE.TIME
				},
				{
					question : message.survey.fifth_transfer_mode ?? 'not set',
					metadata : 'SMD045',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},
				{
					question : message.survey.number_of_passengers_in_fifth_transfer ?? 'not set',
					metadata : 'SMD046',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
				/*Final*/
				{
					question : message.survey.final_destination_address_latitude_and_longitude ?? 'not set',
					metadata : 'SMD047',
					type : SURVEY_QTYPE.GPS
				},
				{
					question : message.survey.final_destination_address_TAZ ?? 'not set',
					metadata : 'SMD061',
					type : SURVEY_QTYPE.LOCATION
				},
				{
					question : message.survey.final_destination_arrival_time ?? 'not set',
					metadata : 'SMD049',
					type : SURVEY_QTYPE.TIME
				},
				/*{
					question : message.survey.final_destination_mode ?? 'not set',
					metadata : 'SMD063',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Motorcycle',
						'Three Wheeler(Tuk Tuk)',
						'Car',
						'Bus',
						'Taxi',
						'Train',
						'Walk',
						'Others'
					]
				},*/
				{
					question : message.survey.final_destination_type ?? 'not set',
					metadata : 'SMD048',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Home',
						'Workplace',
						'School',
						'Other',
					]
				},
				{
					question : message.survey.passenger_count ?? 'not set',
					metadata : 'SMD064',
					type : SURVEY_QTYPE.SUBJECTIVE
				},
/*				{
					question : message.survey.fare_type ?? 'not set',
					metadata : 'SMD051',
					type : SURVEY_QTYPE.RADIO,
					options : [
						'Public Transportation',
						'Road Toll',
						'Other'
					]
				},
				{
					question : message.survey.total_fare ?? 'not set',
					metadata : 'SMD052',
					type : SURVEY_QTYPE.SUBJECTIVE
				},*/
			]
		}
	]
}