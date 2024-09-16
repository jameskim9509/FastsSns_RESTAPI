select * from Member;
select * from MemberNickNameHistory;
select * from Follow;
select * from Post;
explain select * from Post where memberId = 1;

update Post set `createdDate`= date_format('2024-9-8', '%Y-%m-%d') where id = 1 or id = 2

select * from Post where memberId=1 limit 3 offset 0 order by id desc;

delete from Member;
delete from MemberNickNameHistory;
delete from Follow;
delete from Post;

describe Member;
describe MemberNickNameHistory;
describe Follow;
describe Post;