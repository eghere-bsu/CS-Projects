package cs321.create;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GeneBankCreateBTreeTest {
    private String[] args;
    private GeneBankCreateBTreeArguments expectedConfiguration;
    private GeneBankCreateBTreeArguments actualConfiguration;

    @Test
    public void parse4CorrectArgumentsTest() throws IOException {
        args = new String[4];
        args[0] = "--cache=0";
        args[1] = "--degree=20";
        args[2] = "--gbkfile=data/files_gbk/test0.gbk";
        args[3] = "--length=13";

        expectedConfiguration = new GeneBankCreateBTreeArguments(false, 20, "test0.gbk", 13, 0, 0);
        GeneBankCreateBTree test = new GeneBankCreateBTree();
        actualConfiguration = test.parseArgumentsAndHandleExceptions(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }

    @Test
    public void parse5CorrectArgumentsTest() throws Exception {
        args = new String[5];
        args[0] = "--cachesize=200";
        args[1] = "--length=13";
        args[2] = "--gbkfile=data/files_gbk/test0.gbk";
        args[3] = "--degree=20";
        args[4] = "--cache=0";

        expectedConfiguration = new GeneBankCreateBTreeArguments(false, 20, "test0.gbk", 13, 200, 0);
        GeneBankCreateBTree test = new GeneBankCreateBTree();
        actualConfiguration = test.parseArgumentsAndHandleExceptions(args);
        assertEquals(expectedConfiguration, actualConfiguration);
    }

    @Test
    public void parseInvalidDegreeTest() {
        args = new String[4];
        args[0] = "--cache=1";
        args[1] = "--degree=-4";
        args[2] = "--gbkfile=data/files_gbk/test0.gbk";
        args[3] = "--length=10";
        boolean passed = true;
        GeneBankCreateBTree test = new GeneBankCreateBTree();
        try {
            test.parseArguments(args);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(passed = true);
        }
    }

}
