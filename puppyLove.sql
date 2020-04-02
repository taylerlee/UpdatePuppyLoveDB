-- CREATE DATABASE PuppyLove COLLATE Chinese_PRC_CI_AS;
use PuppyLove;

create table area
(
    area_pkid int not null,
    area_name nvarchar(10) not null,
    primary key (area_pkid) 
)

/*

預備資料

*/

insert into area (area_pkid, area_name) 
values (2, N'臺北市'), 
       (3, N'新北市'),
       (4, N'基隆市'),
       (5, N'宜蘭縣'),
       (6, N'桃園縣'),
       (7, N'新竹縣'),
       (8, N'新竹市'),
       (9, N'苗栗縣'),
       (10, N'臺中市'),
       (11, N'彰化縣'),
       (12, N'南投縣'),
       (13, N'雲林縣'),
       (14, N'嘉義縣'),
       (15, N'嘉義市'),
       (16, N'臺南市'),
       (17, N'高雄市'),
       (18, N'屏東縣'),
       (19, N'花蓮縣'),
       (20, N'臺東縣'),
       (21, N'澎湖縣'),
       (22, N'金門縣'),
       (23, N'連江縣');

create table animal
(
	check_id nvarchar(50) not null,
	subid nvarchar(50) not null,
	shelter_id int not null,
	shelter_name nvarchar(50) not null,
	place nvarchar(50),
	area_id int not null,
	kind nvarchar(50) not null,
	sex nvarchar(50) not null,
	bodytype nvarchar(50) not null,
	colour nvarchar(50) not null,
	age nvarchar(50) not null,
	sterilization nvarchar(10),
	bacterin nvarchar(10),
	foundplace nvarchar(50),
	animal_status nvarchar(50),
	remark nvarchar(max),
	animal_opendate datetime,
	animal_closeddate datetime,
	animal_createdate datetime,
	animal_update datetime,
	album nvarchar(max),
	shelter_address nvarchar(max),
	shelter_tel nvarchar(max),
 	primary key (check_id)
)

create table hospital
(
	id int not null,
	name nvarchar(50),
	tel nvarchar(50),
	addr nvarchar(50)
)

create table Contact
(
	id int identity(1,1) NOT NULL,
	name nvarchar(10),
	address nvarchar(MAX),
	number nvarchar(20),
	message nvarchar(MAX),
	primary key (id)
)
go

create proc sp_insert_animal
@check_id nvarchar(50),
@subid nvarchar(50),
@shelter_id int,
@shelter_name nvarchar(50),
@place nvarchar(50),
@area_id int,
@kind nvarchar(50),
@sex nvarchar(50),
@bodytype nvarchar(50),
@colour nvarchar(50),
@age nvarchar(50),
@sterilization nvarchar(10),
@bacterin nvarchar(10),
@foundplace nvarchar(50),
@status nvarchar(50),
@remark nvarchar(max),
@opendate datetime,
@closeddate datetime,
@createdate datetime,
@update datetime,
@album nvarchar(max),
@shelter_address nvarchar(max),
@shelter_tel nvarchar(max)
as
declare @id varchar(20)
set @id = (select check_id from animal where check_id = @check_id);
if (@id is null)
begin
    insert into animal (check_id, subid, shelter_id, shelter_name, place, area_id, kind, sex, bodytype, colour, age, sterilization, 
						bacterin, foundplace, animal_status, remark, animal_opendate, animal_closeddate, animal_createdate, animal_update,
						album, shelter_address, shelter_tel)
	values (@check_id, @subid, @shelter_id, @shelter_name, @place, @area_id, @kind, @sex, @bodytype, @colour, @age, @sterilization, 
			@bacterin, @foundplace, @status, @remark, @opendate, @closeddate, @createdate, @update, @album, @shelter_address, @shelter_tel)
end
else
begin
    update animal
    set subid = @subid, shelter_id = @shelter_id, shelter_name = @shelter_name, place = @place, area_id = @area_id, kind = @kind, sex = @sex, 
        bodytype = @bodytype, colour = @colour, age = @age, sterilization = @sterilization, bacterin = @bacterin, foundplace = @foundplace, 
        animal_status = @status, remark = @remark, animal_opendate = @opendate, animal_closeddate = @closeddate, animal_createdate = @createdate, 
        animal_update = @update, album = @album, shelter_address = @shelter_address, shelter_tel = @shelter_tel
    where check_id = @check_id
end
go

create proc sp_select_animal_by_checkid
@check_id nvarchar(50)
as
select check_id, 
       subid, 
       shelter_id, 
       shelter_name, 
       place, 
       area.area_name as area, 
       kind, 
       sex,
       bodytype,
       colour, 
       age, 
       sterilization, 
       bacterin, 
       foundplace, 
       animal_status, 
       remark, 
       animal_opendate, 
       animal_closeddate, 
       animal_createdate, 
       animal_update, 
       album,
       shelter_address,
       shelter_tel
from animal
     join area on animal.area_id = area.area_pkid 
where check_id = @check_id
go

create proc sp_select_animal
@startIndex int,
@number int
as
select check_id, album 
from animal 
order by check_id offset @startIndex row fetch next @number rows only
go

create proc sp_select_animal_by_type
@type nvarchar(10) = 'all',
@startIndex int,
@number int
as
select *
from animal 
where (@type = 'all') or
      (@type = 'dog' and kind = '狗') or
      (@type = 'cat' and kind = '貓') or
      (@type = 'other' and kind not in ('狗','貓'))
order by check_id offset @startIndex row fetch next @number rows only
go

create proc sp_search_animal
@area nvarchar(max) = null,
@kind nvarchar(20) = null,
@sex nvarchar(20) = null,
@bodytype nvarchar(50) = null,
@age nvarchar(20) = null,
@colour nvarchar(50) = null,
@status nvarchar(50) = null,
@sterilization nvarchar(20) = null,
@bacterin nvarchar(20) = null,
@startIndex int,
@number int
as
select check_id, album
from animal 
     join area on animal.area_id = area.area_pkid 
where (@area is null or (@area like '%' + area_name + '%'))
  and (@kind is null or (@kind = '其他' and kind not in ('狗', '貓')) or (@kind like '%' + kind + '%'))
  and (@sex is null or (@sex = '其他' and sex not in ('公', '母')) or (@sex like '%' + sex + '%'))
  and (@bodytype is null or (@bodytype = '其他' and bodytype not in ('迷你', '小型', '中型', '大型')) or (@bodytype like '%' + bodytype + '%'))
  and (@age is null or (@age = '其他' and age not in ('幼年', '成年')) or (@age like '%' + age + '%'))
  and (@colour is null or (@colour = '其他' and '白色;黑色;棕色;黃色' not like '%' + colour + '%') or (@colour like '%' + colour + '%'))
  and (@status is null or (@status = '其他' and animal_status not in ('開放認養', '已認養')) or (@status like '%' + animal_status + '%'))
  and (@sterilization is null or sterilization = @sterilization)
  and (@bacterin is null or bacterin = @bacterin)
order by check_id offset @startIndex row fetch next @number rows only
go

create proc sp_select_hospital
@addr nvarchar(50)
as
select name, tel, addr
from hospital
where left(addr, 3) = @addr
GO