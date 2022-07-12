package proxy;

import test.entity.User;

import java.util.List;

public interface UserMapper {
   @Sql("select * from user")
   List<User> query();
}
