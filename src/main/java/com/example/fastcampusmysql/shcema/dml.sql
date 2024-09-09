select * from Member;
select * from MemberNickNameHistory;
select * from Follow;
select * from Post;

update Post set `createdDate`= date_format('2024-9-8', '%Y-%m-%d') where id = 1 or id = 2

truncate Member;
truncate MemberNickNameHistory;
truncate Follow;
truncate Post;

describe Member;
describe MemberNickNameHistory;
describe Follow;
describe Post;