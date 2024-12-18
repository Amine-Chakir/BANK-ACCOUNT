package tgpr.bank.model;

import org.apache.commons.lang3.NotImplementedException;
import tgpr.framework.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Test extends Model {
    public Test() {
    }

    public Test(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void mapper(ResultSet rs) throws SQLException {
        throw new NotImplementedException();
    }

    @Override
    public void reload() {
        throw new NotImplementedException();
    }

    public static List<Test> getAll() {
        return List.of(
                new Test("test1"),
                new Test("test2"),
                new Test("test3"),
                new Test("test4"),
                new Test("test5")
        );
    }
}








