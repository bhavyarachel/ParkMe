package com.example.parkme.dbflow.TableHelpers;


import com.example.parkme.dbflow.models.User;
import com.example.parkme.dbflow.models.User_Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class UserTableHelper {
    /**
     * Return current logged in user
     * @return
     */
    public static User getCurrentUser() {
        return SQLite.select().
                from(User.class).where(User_Table.is_logged_in.eq(true)).querySingle();
    }

    /**
     * Delete user info when logging out
     */
    public static void deleteCurrentUserInfo(){
        Delete.table(User.class);
    }
}
