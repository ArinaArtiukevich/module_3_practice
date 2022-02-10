package com.esm.epam.extractor;

import com.esm.epam.entity.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.esm.epam.util.ParameterAttribute.USER_ID;
import static com.esm.epam.util.ParameterAttribute.USER_LOGIN;

public class UserExtractor implements ResultSetExtractor<List<User>> {
    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Long idUser;
        List<User> users = new ArrayList<>();
        if (rs.isBeforeFirst()) {
            rs.next();
            while (!rs.isAfterLast()) {
                User user = new User();
                idUser = rs.getLong(USER_ID);
                user.setId(idUser);
                user.setLogin(rs.getString(USER_LOGIN));
                users.add(user);
                rs.next();
            }
        }
        return users;
    }
}