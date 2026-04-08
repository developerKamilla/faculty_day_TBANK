#74
SELECT id,
    CASE 
    WHEN has_internet THEN 'YES'
    ELSE 'NO'
END 
AS has_internet
FROM Rooms
#56
DELETE FROM Trip 
WHERE town_from = 'Moscow'
#114
SELECT DISTINCT name FROM Pilots 
JOIN Flights 
ON Pilots.pilot_id = Flights.second_pilot_id 
AND Flights.flight_date BETWEEN '2023-08-01' AND '2023-08-31'
AND Flights.destination = 'New York'
#19
SELECT DISTINCT fm.status
FROM FamilyMembers fm
JOIN Payments p ON fm.member_id = p.family_member
JOIN Goods g ON p.good = g.good_id
WHERE g.good_name = 'potato';
#21
SELECT DISTINCT good_name FROM Goods g JOIN Payments p ON g.good_id = p.good GROUP BY g.good_name
HAVING COUNT(p.payment_id) > 1;
#8
SELECT town_to, TIMEDIFF(time_in, time_out) AS flight_time
FROM Trip 
WHERE town_from = 'Paris'
