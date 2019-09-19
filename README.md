# EisGroupTask
Program that takes currencies data from www.lb.lt

program can:
get one day data
get one day data for excat currency
get two diferent days data for excat currency
get two diferent days data and compare  difference currency values
get array of days, show all daya
gwt array of days, show excat currency


check if:
start day is after end date(return)
start day equals to end date(switch to "workWithOneDay" mode)
date is right format (if no - return)
date is later than 2014-09-30 (if no - return)
end date isnt in future(if so - set to current)
start date is current day (switch to "workWithOneDay" mode)
its first day of the year (return)
its christmas (return)
is weekend and "workWithOneDay" (return)

checks if:
url is valid
does not redirect
and more
